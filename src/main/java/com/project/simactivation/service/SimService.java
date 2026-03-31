package com.project.simactivation.service;

import com.project.simactivation.dto.response.SimResponse;

/**
 * Service contract for SIM card operations.
 */
public interface SimService {

    /**
     * Validate a SIM card by matching its ICCID and mobile number.
     * A SIM is considered valid only if:
     *   1. The ICCID + mobile number combination exists in the database.
     *   2. The SIM is currently in INACTIVE status (not already active/blocked).
     *
     * @param simIccid    the 19-22 digit SIM identifier
     * @param mobileNumber the associated mobile number
     * @return SimResponse with validity flag and SIM details
     */
    SimResponse validateSim(String simIccid, String mobileNumber);

    /**
     * Fetch SIM status and details by SIM database ID.
     *
     * @param simId primary key
     * @return SimResponse
     */
    SimResponse getSimById(Long simId);
}
