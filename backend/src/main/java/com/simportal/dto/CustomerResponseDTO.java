package com.simportal.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDTO {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private String address;
    private String idProof;
}
