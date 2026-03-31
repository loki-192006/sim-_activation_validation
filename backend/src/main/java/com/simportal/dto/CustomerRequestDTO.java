package com.simportal.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequestDTO {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100)
    private String lastName;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Date of birth is required")
    @Past(message = "DOB must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 500)
    private String address;

    @NotBlank(message = "ID proof is required")
    private String idProof;
}
