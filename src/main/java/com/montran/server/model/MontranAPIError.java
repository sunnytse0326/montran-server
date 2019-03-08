package com.montran.server.model;

import java.util.Date;

public class MontranAPIError {
    public enum ErrorType {
        DUPLICATE_RECORD, RECORD_NOT_FOUND, UNEXPECTED_ERROR, DATA_ERROR_FORMAT
    }

    private String status;

    private String message;

    private Date createAt;

    public MontranAPIError() {
        createAt = new Date();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt){
        this.createAt = createAt;
    }
}
