package com.montran.server.model;

import java.util.ArrayList;

public class MontranResponse {
    private boolean success;
    private Object values;
    private ArrayList<MontranAPIError> errors;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success){
        this.success = success;
    }

    public Object getValues() {
        return values;
    }

    public void setValues(Object values){
        this.values = values;
    }

    public ArrayList<MontranAPIError> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<MontranAPIError> errors){
        this.errors = errors;
    }

}
