package com.montran.server.model;

import java.util.ArrayList;

public class MontranResponse {
    private boolean success;
    private Object value;
    private ArrayList<MontranAPIError> errors;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success){
        this.success = success;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value){
        this.value = value;
    }

    public ArrayList<MontranAPIError> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<MontranAPIError> errors){
        this.errors = errors;
    }

}
