package com.rentalgeek.android.backend;

import java.util.List;

public class ErrorArray {


    public List<Error> errors;


    public class Error {
        public String code;
        public String attribute;
        public String message;
    }

}
