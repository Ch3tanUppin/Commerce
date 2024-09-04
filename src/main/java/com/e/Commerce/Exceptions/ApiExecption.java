package com.e.Commerce.Exceptions;

public class ApiExecption extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ApiExecption(){
    }

    public ApiExecption(String message){
        super(message); 
    }
    
}
