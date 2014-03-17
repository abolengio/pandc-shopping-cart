package com.ab.cart.rest.resource;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends RestError {

    private final List<RestFieldError> errors  = new ArrayList<>();

    public ValidationError(int status, String message, Errors errors) {
        super(status, message);
        for(FieldError objectError: errors.getFieldErrors()) {
            this.errors.add(new RestFieldError(objectError.getField(), objectError.getCode()));
        }
    }

    public List<RestFieldError> getErrors() {
        return errors;
    }

    public static class RestFieldError {

        private String field;
        private String message;

        private RestFieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}
