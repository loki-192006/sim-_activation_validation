package com.project.simactivation.service;

import com.project.simactivation.dto.request.ActivationRequest;
import com.project.simactivation.dto.response.ActivationResponse;

import java.util.List;

/**
 * Service contract for SIM Activation workflows.
 */
public interface ActivationService {

    /**
     * Start the SIM activation process.
     *
     * Activation flow:
     *   1. Verify the customer exists and is KYC-verified.
     *   2. Validate the SIM (ICCID + mobile number must match, status must be INACTIVE).
     *   3. Check the SIM is not already activated.
     *   4. Create an Activation record with status = SUCCESS.
     *   5. Update the SIM status to ACTIVE.
     *   6. Return the activation details.
     *
     * @param request activation payload
     * @return ActivationResponse with result
     */
    ActivationResponse startActivation(ActivationRequest request);

    /**
     * Fetch all activation records for a given customer.
     *
     * @param customerId the customer's ID
     * @return list of ActivationResponse DTOs
     */
    List<ActivationResponse> getActivationsByCustomer(Long customerId);
}
