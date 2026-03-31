package com.project.simactivation.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO for PUT /customers/address
 */
@Data
public class AddressUpdateRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "New address is required")
    @Size(min = 10, max = 255, message = "Address must be between 10 and 255 characters")
    private String newAddress;
}
