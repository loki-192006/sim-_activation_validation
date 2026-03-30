package com.project.simactivation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.simactivation.entity.Offer;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO returned for offer-related responses.
 */
@Data
@Builder
public class OfferResponse {

    private Long offerId;
    private String offerName;
    private String description;
    private BigDecimal price;
    private Integer validityDays;
    private BigDecimal dataGb;
    private Integer callingMinutes;
    private Integer smsCount;
    private String simTypeEligible;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate validFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate validTo;

    public static OfferResponse fromEntity(Offer offer) {
        return OfferResponse.builder()
                .offerId(offer.getOfferId())
                .offerName(offer.getOfferName())
                .description(offer.getDescription())
                .price(offer.getPrice())
                .validityDays(offer.getValidityDays())
                .dataGb(offer.getDataGb())
                .callingMinutes(offer.getCallingMinutes())
                .smsCount(offer.getSmsCount())
                .simTypeEligible(offer.getSimTypeEligible() != null ? offer.getSimTypeEligible().name() : "ALL")
                .validFrom(offer.getValidFrom())
                .validTo(offer.getValidTo())
                .build();
    }
}
