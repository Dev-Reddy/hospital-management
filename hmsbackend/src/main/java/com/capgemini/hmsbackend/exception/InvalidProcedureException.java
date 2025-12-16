package com.capgemini.hmsbackend.exception;



public class InvalidProcedureException extends RuntimeException {
    public InvalidProcedureException(String message) {
        super(message);
    }
}