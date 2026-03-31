package com.simportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimActivationRequestDTO {

    @NotBlank(message = "SIM number is required")
    private String simNumber;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Offer ID is required")
    private Long offerId;
}
