package com.montran.server.controller;

import com.montran.server.helper.Utils;
import com.montran.server.model.Customer;
import com.montran.server.model.MontranAPIError;
import com.montran.server.model.MontranResponse;
import com.montran.server.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping(path = "/user")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Customer> getAllUsers() {
        return customerRepository.findAll();
    }

    @GetMapping(path = "/create")
    public @ResponseBody
    MontranResponse addNewUser(@RequestParam String email, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String password) {
        MontranResponse response = new MontranResponse();

        if(!Utils.isEmailValid(email)){
            return createSingleError(response, MontranAPIError.ErrorType.DATA_ERROR_FORMAT, "Email format is incorrect.");
        } else if (customerRepository.existsByEmail(email)) {
            return createSingleError(response, MontranAPIError.ErrorType.DUPLICATE_RECORD, "Email is Duplicated.");
        } else{
            Customer user = new Customer(firstName, lastName, email, password);
            customerRepository.save(user);
            response.setSuccess(true);
            response.setValues(user);
            return response;
        }
    }

    private MontranResponse createSingleError(MontranResponse response, MontranAPIError.ErrorType errorType, String errorMessage){
        ArrayList<MontranAPIError> errors = new ArrayList<>();
        response.setSuccess(false);
        MontranAPIError error = new MontranAPIError();
        error.setStatus(errorType.toString());
        error.setMessage(errorMessage);
        errors.add(error);
        response.setErrors(errors);
        return response;
    }

}
