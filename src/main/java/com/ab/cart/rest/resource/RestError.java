package com.ab.cart.rest.resource;

public class RestError {

    private int status;
    private String message;

    public RestError(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
