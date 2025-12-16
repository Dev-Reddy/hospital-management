package com.capgemini.hmsbackend.exception;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String message) {

        super(message);
    }
}