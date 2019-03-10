package com.montran.server.controller;

import com.montran.server.helper.Utils;
import com.montran.server.model.*;
import com.montran.server.repository.CustomerRepository;
import com.montran.server.security.MontranJWTService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@RestController
@RequestMapping(path = "/user")
public class CustomerController {
    private String sessionTokenTag = "session-token";

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping(path = "/transaction")
    public @ResponseBody
    Mono<ResponseEntity<MontranResponse>> makeTransaction(@RequestHeader String authorization, @RequestBody MontranTransactionBody body) {
        Mono<ResponseEntity<MontranResponse>> result;
        ResponseEntity<MontranResponse> entity;

        MontranResponse response = new MontranResponse();
        HttpHeaders responseHeaders = new HttpHeaders();
        MontranJWTService service = MontranJWTService.getInstance();

        String transactionID = body.getTransactionId();

        Function<MontranResponse, ResponseEntity<MontranResponse>> entityFunction = res -> ResponseEntity.ok().headers(responseHeaders).body(response);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            response.setSuccess(false);
            response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.DATA_ERROR_FORMAT, "Transaction must need a session token."));
            return Mono.just(response).map(entityFunction);
        } else if (transactionID == null) {
            response.setSuccess(false);
            response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.DATA_ERROR_FORMAT, "Transaction must need a transaction ID."));
            return Mono.just(response).map(entityFunction);
        }
        String sessionToken = authorization.replace("Bearer ", "");
        Claims claims = service.getJWTClaimerInfo(sessionToken);

        if (claims == null || (claims.get(service.getEmailTag()) != null && !(claims.get(service.getEmailTag()) instanceof String))) {
            response.setSuccess(false);
            response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.UNEXPECTED_ERROR, "Invalid session token."));
            return Mono.just(response).map(entityFunction);
        } else {
            WebClient webClient = service.getWebClientBuilder().build();
            return webClient.get().uri(service.getTransactionURL()).retrieve().bodyToMono(MontranTransactionResponse.class)
                    .map(res -> {
                        if(!res.isSuccess()){
                            response.setSuccess(false);
                            response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.DATA_ERROR_FORMAT, "Transaction failed."));
                            return ResponseEntity.ok().headers(responseHeaders).body(response);
                        } else{
                            response.setSuccess(true);
                            String email = (String) claims.get(service.getEmailTag());
                            Customer customer = customerRepository.findCustomerByEmail(email);
                            if(res.getCurrency().equals(Customer.CurrencyType.HKD.toString())){
                                customer.setBalanceHKD(customer.getBalanceHKD().add(res.getAmount()));
                                customerRepository.save(customer);
                            } else if(res.getCurrency().equals(Customer.CurrencyType.USD.toString())){
                                customer.setBalanceHKD(customer.getBalanceUSD().add(res.getAmount()));
                                customerRepository.save(customer);
                            }
                            response.setValues(customer);
                            return ResponseEntity.ok().headers(responseHeaders).body(response);
                        }
                    });
        }
    }


    @GetMapping(path = "/me")
    public @ResponseBody
    ResponseEntity<MontranResponse> getMyProfile(@RequestHeader String authorization) {
        MontranResponse response = new MontranResponse();
        HttpHeaders responseHeaders = new HttpHeaders();
        MontranJWTService service = MontranJWTService.getInstance();

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            response.setSuccess(false);
            response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.DATA_ERROR_FORMAT, "Profile must need a session token."));
        } else {
            String sessionToken = authorization.replace("Bearer ", "");
            if (!service.verifyJWT(sessionToken)) {
                response.setSuccess(false);
                response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.DATA_ERROR_FORMAT, "Invalid session token."));
            } else {
                Claims claims = service.getJWTClaimerInfo(sessionToken);

                if (claims == null || (claims.get(service.getEmailTag()) != null && !(claims.get(service.getEmailTag()) instanceof String))) {
                    response.setSuccess(false);
                    response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.UNEXPECTED_ERROR, "Invalid session token."));
                } else {
                    String email = (String) claims.get(service.getEmailTag());
                    Customer customer = customerRepository.findCustomerByEmail(email);

                    if(customer == null){
                        response.setSuccess(false);
                        response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.UNEXPECTED_ERROR, "User email record did not exist."));
                    } else {
                        response.setSuccess(true);
                        response.setValues(customer);
                        responseHeaders.add(sessionTokenTag, MontranJWTService.getInstance().generatorJWT(email));
                    }
                }
            }
        }
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(response);
    }

    @PostMapping(path = "/login")
    public @ResponseBody
    ResponseEntity<MontranResponse> login(@RequestBody MontranLoginBody body) {
        String email = body.getEmail();
        String password = body.getPassword();

        MontranResponse response = new MontranResponse();
        HttpHeaders responseHeaders = new HttpHeaders();

        if (email == null) {
            response.setSuccess(false);
            response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.DATA_ERROR_FORMAT, "Registration must need an email"));
        } else if (password == null) {
            response.setSuccess(false);
            response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.DATA_ERROR_FORMAT, "Registration must need an password"));
        }

        Customer customer = customerRepository.findCustomerByEmail(email);

        if (customer == null) {
            response.setSuccess(false);
            response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.RECORD_NOT_FOUND, "User email is not registered."));
        } else {
            if (!customer.getPassword().equals(MontranJWTService.getInstance().hashPassword(password))) {
                response.setSuccess(false);
                response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.INCORRECT_DATA, "Password is incorrect."));
            } else {
                response.setSuccess(true);
                response.setValues(customer);
                responseHeaders.add(sessionTokenTag, MontranJWTService.getInstance().generatorJWT(email));
            }
        }
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(response);
    }

    @PostMapping(path = "/register")
    public @ResponseBody
    ResponseEntity<MontranResponse> register(@RequestBody MontranRegisterBody body) {
        HttpHeaders responseHeaders = new HttpHeaders();
        MontranResponse response = new MontranResponse();

        String email = body.getEmail();
        String firstName = body.getFirstName();
        String lastName = body.getLastName();
        String password = body.getPassword();

        if (email == null) {
            response.setSuccess(false);
            response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.DATA_ERROR_FORMAT, "Registration must need an email"));
        } else if (password == null) {
            response.setSuccess(false);
            response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.DATA_ERROR_FORMAT, "Registration must need an password"));
        } else if (!Utils.isEmailValid(email)) {
            response.setSuccess(false);
            response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.DATA_ERROR_FORMAT, "Email format is incorrect."));
        } else if (customerRepository.existsByEmail(email)) {
            response.setSuccess(false);
            response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.DUPLICATE_RECORD, "Email is Duplicated."));
        } else {
            Customer user = new Customer(firstName, lastName, email, MontranJWTService.getInstance().hashPassword(password));
            customerRepository.save(user);
            response.setSuccess(true);
            response.setValues(user);
            responseHeaders.add(sessionTokenTag, MontranJWTService.getInstance().generatorJWT(email));
        }
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(response);
    }


}
