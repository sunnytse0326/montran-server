package com.montran.server.helper;

import com.montran.server.model.MontranAPIError;
import com.montran.server.model.MontranResponse;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Utils {
    public static boolean isEmailValid(String email) {
        return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,8})$"
        ).matcher(email).matches();
    }

    public static ArrayList<MontranAPIError> createSingleError(MontranAPIError.ErrorType errorType, String errorMessage){
        ArrayList<MontranAPIError> errors = new ArrayList<>();
        MontranAPIError error = new MontranAPIError();
        error.setStatus(errorType.toString());
        error.setMessage(errorMessage);
        errors.add(error);
        return errors;
    }
}
