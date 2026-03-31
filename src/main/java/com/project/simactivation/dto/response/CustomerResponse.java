package com.project.simactivation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.simactivation.entity.Customer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO returned to the client for customer-related responses.
 * Never exposes sensitive fields like raw passwords or internal IDs beyond what's needed.
 */
@Data
@Builder
public class CustomerResponse {

    private Long customerId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    private String address;
    private String phoneNumber;
    private String kycStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /** Convenience factory — map entity → DTO */
    public static CustomerResponse fromEntity(Customer customer) {
        return CustomerResponse.builder()
                .customerId(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .fullName(customer.getFirstName() + " " + customer.getLastName())
                .email(customer.getEmail())
                .dob(customer.getDob())
                .address(customer.getAddress())
                .phoneNumber(customer.getPhoneNumber())
                .kycStatus(customer.getKycStatus().name())
                .createdAt(customer.getCreatedAt())
                .build();
    }
}
