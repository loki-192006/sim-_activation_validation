package com.project.simactivation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * JPA Entity representing a SIM activation record.
 * Links a Customer to a SIM card and tracks the activation lifecycle.
 *
 * Relationships:
 *  - ManyToOne → Customer (one customer can activate multiple SIMs over time)
 *  - ManyToOne → Sim     (one SIM has one active activation record)
 */
@Entity
@Table(name = "activations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activation_id")
    private Long activationId;

    /** The customer requesting activation */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /** The SIM card being activated */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sim_id", nullable = false)
    private Sim sim;

    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status", nullable = false)
    @Builder.Default
    private ActivationStatus activationStatus = ActivationStatus.PENDING;

    /** Plan/offer selected at activation time */
    @Column(name = "plan_selected", length = 100)
    private String planSelected;

    /** Remarks from system or operator */
    @Column(name = "remarks", length = 255)
    private String remarks;

    @Column(name = "activated_at")
    private LocalDateTime activatedAt;

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

    public enum ActivationStatus {
        PENDING, SUCCESS, FAILED, CANCELLED
    }
}
