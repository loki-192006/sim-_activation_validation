package com.project.simactivation.service.impl;

import com.project.simactivation.dto.request.ActivationRequest;
import com.project.simactivation.dto.response.ActivationResponse;
import com.project.simactivation.entity.Activation;
import com.project.simactivation.entity.Activation.ActivationStatus;
import com.project.simactivation.entity.Customer;
import com.project.simactivation.entity.Sim;
import com.project.simactivation.exception.CustomerNotFoundException;
import com.project.simactivation.exception.SimActivationException;
import com.project.simactivation.exception.SimNotFoundException;
import com.project.simactivation.repository.ActivationRepository;
import com.project.simactivation.repository.CustomerRepository;
import com.project.simactivation.repository.SimRepository;
import com.project.simactivation.service.ActivationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ActivationService.
 *
 * The full activation workflow is a single @Transactional method.
 * If anything fails mid-way (e.g., SIM update fails), the entire
 * transaction is rolled back to keep data consistent.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ActivationServiceImpl implements ActivationService {

    private final ActivationRepository activationRepository;
    private final CustomerRepository   customerRepository;
    private final SimRepository        simRepository;

    // -------------------------------------------------------
    //   MAIN: Start Activation
    // -------------------------------------------------------

    @Override
    @Transactional
    public ActivationResponse startActivation(ActivationRequest request) {
        log.info("Starting SIM activation — CustomerID: {}, ICCID: {}, Mobile: {}",
                request.getCustomerId(), request.getSimIccid(), request.getMobileNumber());

        // Step 1: Verify the customer exists
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));

        // Step 2: KYC check — only verified customers can activate SIMs
        if (customer.getKycStatus() == Customer.KycStatus.REJECTED) {
            throw new SimActivationException(
                    "Customer KYC has been rejected. Activation is not allowed.");
        }

        // Step 3: Find the SIM — ICCID + mobile must both match
        Sim sim = simRepository
                .findBySimIccidAndMobileNumber(request.getSimIccid(), request.getMobileNumber())
                .orElseThrow(() -> new SimNotFoundException(
                        "ICCID [" + request.getSimIccid() + "] with mobile [" + request.getMobileNumber() + "]"));

        // Step 4: SIM must be INACTIVE — not already active or blocked
        if (sim.getStatus() != Sim.SimStatus.INACTIVE) {
            log.warn("Activation attempted on SIM with status: {}", sim.getStatus());
            throw new SimActivationException(
                    "SIM cannot be activated. Current status: " + sim.getStatus() +
                    ". Only INACTIVE SIMs can be activated.");
        }

        // Step 5: Prevent duplicate activation records for this SIM
        boolean alreadyActivated = activationRepository
                .existsBySim_SimIdAndActivationStatus(sim.getSimId(), ActivationStatus.SUCCESS);
        if (alreadyActivated) {
            throw new SimActivationException(
                    "SIM with mobile number [" + sim.getMobileNumber() + "] is already activated.");
        }

        // Step 6: Create the activation record
        Activation activation = Activation.builder()
                .customer(customer)
                .sim(sim)
                .activationStatus(ActivationStatus.SUCCESS)
                .planSelected(request.getPlanSelected())
                .remarks("SIM activated successfully via portal.")
                .activatedAt(LocalDateTime.now())
                .build();

        Activation saved = activationRepository.save(activation);

        // Step 7: Update SIM status → ACTIVE
        sim.setStatus(Sim.SimStatus.ACTIVE);
        simRepository.save(sim);

        log.info("SIM activated successfully — ActivationID: {}, Mobile: {}",
                saved.getActivationId(), sim.getMobileNumber());

        return ActivationResponse.fromEntity(saved);
    }

    // -------------------------------------------------------
    //   Fetch activations by customer
    // -------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<ActivationResponse> getActivationsByCustomer(Long customerId) {
        log.info("Fetching activations for customer ID: {}", customerId);

        // Verify customer exists first
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        return activationRepository.findByCustomer_Id(customerId)
                .stream()
                .map(ActivationResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
