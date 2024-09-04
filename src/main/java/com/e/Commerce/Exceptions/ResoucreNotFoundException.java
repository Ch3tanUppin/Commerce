package com.e.Commerce.Exceptions;

public class ResoucreNotFoundException extends RuntimeException{
    String resourceName;
    String field;
    String fieldName;
    long fieldId;

    //constructor
    public ResoucreNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("%s not found %s:%s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }
    public ResoucreNotFoundException(String resourceName, String field, long fieldId) {
        super(String.format("%s not found %s:%d", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }

    

}

