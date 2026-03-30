package com.project.simactivation.service.impl;

import com.project.simactivation.dto.response.SimResponse;
import com.project.simactivation.entity.Sim;
import com.project.simactivation.exception.SimNotFoundException;
import com.project.simactivation.repository.SimRepository;
import com.project.simactivation.service.SimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of SimService.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SimServiceImpl implements SimService {

    private final SimRepository simRepository;

    // -------------------------------------------------------
    //   Validate SIM — ICCID + mobile number must both match
    // -------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public SimResponse validateSim(String simIccid, String mobileNumber) {
        log.info("Validating SIM — ICCID: {}, Mobile: {}", simIccid, mobileNumber);

        Sim sim = simRepository.findBySimIccidAndMobileNumber(simIccid, mobileNumber)
                .orElseThrow(() -> {
                    log.warn("SIM validation failed — ICCID: {}, Mobile: {}", simIccid, mobileNumber);
                    return new SimNotFoundException(
                            "No SIM found matching ICCID [" + simIccid + "] and mobile [" + mobileNumber + "]");
                });

        log.info("SIM found — ID: {}, Status: {}", sim.getSimId(), sim.getStatus());

        // Build response with validity flag
        SimResponse response = SimResponse.fromEntity(sim);
        // A SIM is eligible for activation only if it's INACTIVE
        response.setValid(sim.getStatus() == Sim.SimStatus.INACTIVE);

        return response;
    }

    // -------------------------------------------------------
    //   Fetch SIM by primary key
    // -------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public SimResponse getSimById(Long simId) {
        log.info("Fetching SIM by ID: {}", simId);

        Sim sim = simRepository.findById(simId)
                .orElseThrow(() -> new SimNotFoundException("ID: " + simId));

        return SimResponse.fromEntity(sim);
    }
}
