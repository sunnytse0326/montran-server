package com.montran.server.model;

import java.math.BigDecimal;

public class MontranTransactionResponse {
    private boolean success;
    private String transactionId;
    private String currency;
    private BigDecimal amount;

    public boolean isSuccess(){
        return success;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

