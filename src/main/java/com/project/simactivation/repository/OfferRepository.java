package com.project.simactivation.repository;

import com.project.simactivation.entity.Offer;
import com.project.simactivation.entity.Sim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Offer entity.
 */
@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    /**
     * Fetch all currently active offers for a given SIM type.
     * An offer is active if isActive=true AND today is within validFrom–validTo.
     */
    @Query("""
        SELECT o FROM Offer o
        WHERE o.isActive = true
          AND o.simTypeEligible = :simType
          AND o.validFrom <= :today
          AND o.validTo >= :today
        ORDER BY o.price ASC
    """)
    List<Offer> findEligibleOffers(
            @Param("simType") Sim.SimType simType,
            @Param("today") LocalDate today
    );

    /**
     * Fetch all active offers regardless of SIM type (general offers).
     */
    List<Offer> findByIsActiveTrueOrderByPriceAsc();
}
