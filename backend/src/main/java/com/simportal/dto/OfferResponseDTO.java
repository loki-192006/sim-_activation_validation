package com.simportal.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferResponseDTO {
    private Long offerId;
    private String planName;
    private BigDecimal price;
    private String validity;
    private String description;
    private String dataLimit;
    private String calls;
    private String sms;
    private boolean popular;
}
