package com.montran.server.model;

import java.math.BigDecimal;

public class Customer {
    private final long id;
    private final String firstName;

    public Customer(long id, String firstName, String lastName, BigDecimal balance) {
        this.id = id;
        this.firstName = firstName;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }
}
