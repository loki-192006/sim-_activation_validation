package com.project.simactivation.repository;

import com.project.simactivation.entity.Activation;
import com.project.simactivation.entity.Activation.ActivationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Activation entity.
 */
@Repository
public interface ActivationRepository extends JpaRepository<Activation, Long> {

    /**
     * Fetch all activations for a specific customer.
     */
    List<Activation> findByCustomer_Id(Long customerId);

    /**
     * Check if a SIM is already successfully activated.
     * Prevents duplicate activation attempts.
     */
    boolean existsBySim_SimIdAndActivationStatus(Long simId, ActivationStatus status);

    /**
     * Find the latest activation record for a given SIM.
     */
    Optional<Activation> findTopBySim_SimIdOrderByCreatedAtDesc(Long simId);
}
