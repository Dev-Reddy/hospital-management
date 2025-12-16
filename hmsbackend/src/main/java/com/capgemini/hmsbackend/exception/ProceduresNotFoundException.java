package com.capgemini.hmsbackend.exception;

public class ProceduresNotFoundException extends RuntimeException{

    public  ProceduresNotFoundException(int id){
        super("Procedure Not Found with id" + id);
    }
}
