package com.project.simactivation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * JPA Entity representing a telecom offer/plan.
 * Offers are filtered by eligibility criteria (e.g., SIM type, validity).
 */
@Entity
@Table(name = "offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Long offerId;

    @Column(name = "offer_name", nullable = false, length = 100)
    private String offerName;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** Validity in days */
    @Column(name = "validity_days", nullable = false)
    private Integer validityDays;

    /** Data in GB */
    @Column(name = "data_gb", precision = 5, scale = 2)
    private BigDecimal dataGb;

    @Column(name = "calling_minutes")
    private Integer callingMinutes;

    @Column(name = "sms_count")
    private Integer smsCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "sim_type_eligible")
    private Sim.SimType simTypeEligible;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
