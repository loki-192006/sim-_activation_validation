package com.project.simactivation.repository;

import com.project.simactivation.entity.Sim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Sim entity.
 */
@Repository
public interface SimRepository extends JpaRepository<Sim, Long> {

    /**
     * Validate a SIM by its ICCID and mobile number together.
     * Both must match for the SIM to be considered valid.
     */
    Optional<Sim> findBySimIccidAndMobileNumber(String simIccid, String mobileNumber);

    /**
     * Find a SIM by mobile number alone.
     */
    Optional<Sim> findByMobileNumber(String mobileNumber);

    /**
     * Check if a mobile number is already registered.
     */
    boolean existsByMobileNumber(String mobileNumber);
}
