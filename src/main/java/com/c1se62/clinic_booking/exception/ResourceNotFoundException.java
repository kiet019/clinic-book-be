package com.c1se62.clinic_booking.exception;

public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String fieldName;
    String fieldValue;
    public ResourceNotFoundException(String resourceName, String fieldName, int fieldValue){
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = String.valueOf(fieldValue);
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
