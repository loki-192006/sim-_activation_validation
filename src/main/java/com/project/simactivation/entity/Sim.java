package com.project.simactivation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * JPA Entity representing a SIM card in the telecom system.
 * Each SIM has a unique SIM ID and mobile number.
 */
@Entity
@Table(name = "sim_cards", uniqueConstraints = {
        @UniqueConstraint(columnNames = "mobile_number"),
        @UniqueConstraint(columnNames = "sim_iccid")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sim_id")
    private Long simId;

    /**
     * ICCID — Integrated Circuit Card Identifier (19–20 digit unique SIM ID)
     */
    @Column(name = "sim_iccid", nullable = false, unique = true, length = 22)
    private String simIccid;

    @Column(name = "mobile_number", nullable = false, unique = true, length = 15)
    private String mobileNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private SimStatus status = SimStatus.INACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "sim_type", nullable = false)
    @Builder.Default
    private SimType simType = SimType.PREPAID;

    @Column(name = "operator_code", length = 10)
    private String operatorCode;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum SimStatus {
        INACTIVE, ACTIVE, BLOCKED, EXPIRED
    }

    public enum SimType {
        PREPAID, POSTPAID
    }
}
