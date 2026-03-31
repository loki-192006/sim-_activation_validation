package com.project.simactivation.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO for POST /activation/start
 * Bundles the customer ID, SIM ICCID, mobile number, and optional plan selection.
 */
@Data
public class ActivationRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "SIM ICCID is required")
    @Size(min = 19, max = 22, message = "SIM ICCID must be between 19 and 22 characters")
    private String simIccid;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Please provide a valid 10-digit Indian mobile number")
    private String mobileNumber;

    /** Optional — plan/offer the customer has chosen */
    private String planSelected;
}
