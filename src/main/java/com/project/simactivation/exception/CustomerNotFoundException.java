package com.project.simactivation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends ResourceNotFoundException {
    public CustomerNotFoundException(Long id) {
        super("Customer not found with ID: " + id);
    }
    public CustomerNotFoundException(String email) {
        super("Customer not found with email: " + email);
    }
}
