package com.capgemini.hmsbackend.exception;


public class EmployeeIdNotFoundException extends RuntimeException {
    public EmployeeIdNotFoundException(String message) {
        super(message);
    }
}
