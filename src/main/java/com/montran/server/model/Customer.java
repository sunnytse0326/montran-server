package com.montran.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "User")
public class Customer {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "balanceHKD")
    private BigDecimal balanceHKD;
    @Column(name = "balanceUSD")
    private BigDecimal balanceUSD;
    @Column(name = "password")
    @JsonIgnore
    private String password;
    @Column(name = "create_at")
    private Date createAt;

    public enum CurrencyType { HKD, USD }

    public Customer(){}

    public Customer(String firstName, String lastName, String email, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.balanceHKD = BigDecimal.ZERO;
        this.balanceUSD = BigDecimal.ZERO;
        this.createAt = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public BigDecimal getBalanceUSD() {
        return balanceUSD;
    }

    public void setBalanceUSD(BigDecimal balanceUSD) {
        this.balanceUSD = balanceUSD;
    }

    public BigDecimal getBalanceHKD() {
        return balanceHKD;
    }

    public void setBalanceHKD(BigDecimal balance){
        this.balanceHKD = balance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt){
        this.createAt = createAt;
    }
}
