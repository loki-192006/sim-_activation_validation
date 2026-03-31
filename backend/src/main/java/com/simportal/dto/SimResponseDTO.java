package com.simportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimResponseDTO {
    private Long simId;
    private String simNumber;
    private String status;
    private Long customerId;
}
