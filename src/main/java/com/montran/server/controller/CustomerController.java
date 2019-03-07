package com.montran.server.controller;

import com.montran.server.model.Customer;
import com.montran.server.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    String addNewUser(@RequestParam String email, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String password) {
        Customer user = new Customer();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCreateAt(new Date());
        customerRepository.save(user);
        return "";
    }

}
