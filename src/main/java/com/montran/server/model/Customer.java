package com.montran.server.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final long id;
    private final String firstName;
    private final String lastName;
    private final BigDecimal balance;
    private final String password;
    private final Date create_at;

    public Customer(long id, String firstName, String lastName, BigDecimal balance, String password, Date create_at) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.password = password;
        this.create_at = create_at;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getPassword() {
        return password;
    }

    public Date getCreate_at() {
        return create_at;
    }
}
