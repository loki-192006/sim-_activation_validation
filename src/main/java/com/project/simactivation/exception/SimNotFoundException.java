package com.project.simactivation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SimNotFoundException extends ResourceNotFoundException {
    public SimNotFoundException(String detail) {
        super("SIM not found: " + detail);
    }
}
