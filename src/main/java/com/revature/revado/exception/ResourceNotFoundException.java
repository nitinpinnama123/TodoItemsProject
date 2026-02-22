package com.revature.revado.exception;

/**
 * @author $ {USER}
 **/
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
