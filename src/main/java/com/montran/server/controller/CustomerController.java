package com.montran.server.controller;

import com.montran.server.helper.Utils;
import com.montran.server.model.Customer;
import com.montran.server.model.MontranAPIError;
import com.montran.server.model.MontranResponse;
import com.montran.server.repository.CustomerRepository;
import com.montran.server.security.MontranJWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Customer> getAllUsers() {
        //String data = MontranJWTService.getInstance().generatorJWT();
        //System.out.println(MontranJWTService.getInstance().verifyJWT(data));

        return customerRepository.findAll();
    }

    @GetMapping(path = "/login")
    public @ResponseBody
    MontranResponse login(@RequestParam String email, @RequestParam String password) {
        MontranResponse response = new MontranResponse();

        return response;
    }

    @GetMapping(path = "/register")
    public @ResponseBody
    MontranResponse register(@RequestParam String email, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String password) {
        MontranResponse response = new MontranResponse();

        if(!Utils.isEmailValid(email)){
            response.setSuccess(false);
            response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.DATA_ERROR_FORMAT, "Email format is incorrect."));
            return response;
        } else if (customerRepository.existsByEmail(email)) {
            response.setSuccess(false);
            response.setErrors(Utils.createSingleError(MontranAPIError.ErrorType.DUPLICATE_RECORD, "Email is Duplicated."));
            return response;
        } else{
            Customer user = new Customer(firstName, lastName, email, password);
            customerRepository.save(user);
            response.setSuccess(true);
            response.setValues(user);
            return response;
        }
    }


}
