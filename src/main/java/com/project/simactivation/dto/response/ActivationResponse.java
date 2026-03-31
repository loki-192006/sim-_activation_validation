package com.project.simactivation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.simactivation.entity.Activation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO returned for activation-related responses.
 */
@Data
@Builder
public class ActivationResponse {

    private Long activationId;
    private Long customerId;
    private String customerName;
    private Long simId;
    private String mobileNumber;
    private String activationStatus;
    private String planSelected;
    private String remarks;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime activatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static ActivationResponse fromEntity(Activation activation) {
        return ActivationResponse.builder()
                .activationId(activation.getActivationId())
                .customerId(activation.getCustomer().getId())
                .customerName(activation.getCustomer().getFirstName() + " " + activation.getCustomer().getLastName())
                .simId(activation.getSim().getSimId())
                .mobileNumber(activation.getSim().getMobileNumber())
                .activationStatus(activation.getActivationStatus().name())
                .planSelected(activation.getPlanSelected())
                .remarks(activation.getRemarks())
                .activatedAt(activation.getActivatedAt())
                .createdAt(activation.getCreatedAt())
                .build();
    }
}
