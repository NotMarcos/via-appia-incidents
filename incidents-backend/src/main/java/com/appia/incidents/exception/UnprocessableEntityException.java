package com.appia.incidents.exception;

public class UnprocessableEntityException extends RuntimeException {
    public UnprocessableEntityException(String msg) {
        super(msg);
    }
}
