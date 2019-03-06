package com.montran.server.controller;

import com.montran.server.model.Customer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CustomerController {
    @RequestMapping("/getBalance")
    public Customer greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Customer(0, "", "", BigDecimal.ZERO);
    }
}
