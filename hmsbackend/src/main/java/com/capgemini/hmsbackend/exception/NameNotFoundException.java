package com.capgemini.hmsbackend.exception;


public class NameNotFoundException extends RuntimeException {
    public NameNotFoundException(String message) {
        super(message);
    }
}
