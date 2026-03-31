package com.project.simactivation.service;

import com.project.simactivation.dto.response.OfferResponse;

import java.util.List;

/**
 * Service contract for fetching telecom offers.
 */
public interface OfferService {

    /**
     * Fetch eligible offers for a specific customer.
     *
     * Eligibility logic:
     *   - Looks up the customer's most recently activated SIM type (PREPAID/POSTPAID).
     *   - Returns all active offers that match that SIM type and are within the validity window.
     *   - If the customer has no active SIM, returns all currently active general offers.
     *
     * @param customerId the customer's ID
     * @return list of eligible OfferResponse DTOs
     */
    List<OfferResponse> getOffersForCustomer(Long customerId);

    /**
     * Fetch all currently active offers regardless of SIM type.
     *
     * @return list of OfferResponse DTOs
     */
    List<OfferResponse> getAllActiveOffers();
}
