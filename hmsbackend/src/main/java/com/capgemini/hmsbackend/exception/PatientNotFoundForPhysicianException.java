package com.capgemini.hmsbackend.exception;


public class PatientNotFoundForPhysicianException extends RuntimeException {
    public PatientNotFoundForPhysicianException(String message) {
        super(message);
    }
}
