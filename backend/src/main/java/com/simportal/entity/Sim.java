package com.simportal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sim")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long simId;

    @Column(nullable = false, unique = true, length = 20)
    private String simNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SimStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public enum SimStatus {
        AVAILABLE, ACTIVE, INACTIVE, BLOCKED
    }
}
