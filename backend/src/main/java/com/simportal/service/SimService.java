package com.simportal.service;

import com.simportal.dto.SimActivationRequestDTO;
import com.simportal.dto.SimResponseDTO;
import com.simportal.entity.Customer;
import com.simportal.entity.Offer;
import com.simportal.entity.Sim;
import com.simportal.exception.BusinessException;
import com.simportal.exception.ResourceNotFoundException;
import com.simportal.repository.CustomerRepository;
import com.simportal.repository.OfferRepository;
import com.simportal.repository.SimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimService {

    private final SimRepository simRepository;
    private final CustomerRepository customerRepository;
    private final OfferRepository offerRepository;

    public SimResponseDTO validateSim(String simNumber) {
        log.info("Validating SIM number: {}", simNumber);
        Sim sim = simRepository.findBySimNumber(simNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SIM not found with number: " + simNumber));

        if (sim.getStatus() == Sim.SimStatus.ACTIVE) {
            throw new BusinessException("SIM is already activated.");
        }
        if (sim.getStatus() == Sim.SimStatus.BLOCKED) {
            throw new BusinessException("SIM is blocked. Please contact support.");
        }

        log.info("SIM {} is valid and available.", simNumber);
        return toDTO(sim);
    }

    @Transactional
    public SimResponseDTO activateSim(SimActivationRequestDTO dto) {
        log.info("Activating SIM: {} for customer: {}", dto.getSimNumber(), dto.getCustomerId());

        Sim sim = simRepository.findBySimNumber(dto.getSimNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SIM not found: " + dto.getSimNumber()));

        if (sim.getStatus() != Sim.SimStatus.AVAILABLE) {
            throw new BusinessException("SIM is not available for activation. Status: " + sim.getStatus());
        }

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with ID: " + dto.getCustomerId()));

        // Validate offer exists
        offerRepository.findById(dto.getOfferId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Offer not found with ID: " + dto.getOfferId()));

        sim.setStatus(Sim.SimStatus.ACTIVE);
        sim.setCustomer(customer);
        Sim activated = simRepository.save(sim);

        log.info("SIM {} successfully activated for customer {}", dto.getSimNumber(), customer.getEmail());
        return toDTO(activated);
    }

    private SimResponseDTO toDTO(Sim sim) {
        return SimResponseDTO.builder()
                .simId(sim.getSimId())
                .simNumber(sim.getSimNumber())
                .status(sim.getStatus().name())
                .customerId(sim.getCustomer() != null ? sim.getCustomer().getCustomerId() : null)
                .build();
    }
}
