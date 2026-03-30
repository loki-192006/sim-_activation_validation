package com.project.simactivation.service.impl;

import com.project.simactivation.dto.response.OfferResponse;
import com.project.simactivation.entity.Activation;
import com.project.simactivation.entity.Sim;
import com.project.simactivation.exception.CustomerNotFoundException;
import com.project.simactivation.repository.ActivationRepository;
import com.project.simactivation.repository.CustomerRepository;
import com.project.simactivation.repository.OfferRepository;
import com.project.simactivation.service.OfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of OfferService.
 *
 * Offer eligibility is determined by:
 *   1. The SIM type (PREPAID / POSTPAID) of the customer's most recently activated SIM.
 *   2. Whether the offer is currently within its validity window.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OfferServiceImpl implements OfferService {

    private final OfferRepository      offerRepository;
    private final ActivationRepository activationRepository;
    private final CustomerRepository   customerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OfferResponse> getOffersForCustomer(Long customerId) {
        log.info("Fetching offers for customer ID: {}", customerId);

        // Verify the customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        LocalDate today = LocalDate.now();

        // Try to find the customer's most recently activated SIM type
        Optional<Activation> latestActivation = activationRepository
                .findTopBySim_SimIdOrderByCreatedAtDesc(customerId);

        if (latestActivation.isPresent()) {
            Sim.SimType simType = latestActivation.get().getSim().getSimType();
            log.info("Customer's SIM type: {}. Fetching targeted offers.", simType);

            List<OfferResponse> offers = offerRepository
                    .findEligibleOffers(simType, today)
                    .stream()
                    .map(OfferResponse::fromEntity)
                    .collect(Collectors.toList());

            log.info("Found {} eligible offers for SIM type: {}", offers.size(), simType);
            return offers;
        }

        // Fallback: customer hasn't activated a SIM yet — return all active offers
        log.info("No active SIM found for customer. Returning all active offers.");
        return getAllActiveOffers();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferResponse> getAllActiveOffers() {
        return offerRepository.findByIsActiveTrueOrderByPriceAsc()
                .stream()
                .map(OfferResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
