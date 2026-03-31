package com.simportal.service;

import com.simportal.dto.AddressUpdateDTO;
import com.simportal.dto.CustomerRequestDTO;
import com.simportal.dto.CustomerResponseDTO;
import com.simportal.entity.Customer;
import com.simportal.exception.BusinessException;
import com.simportal.exception.ResourceNotFoundException;
import com.simportal.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerResponseDTO validateAndSave(CustomerRequestDTO dto) {
        log.info("Validating/saving customer with email: {}", dto.getEmail());

        // Return existing customer if email already registered
        return customerRepository.findByEmail(dto.getEmail())
                .map(this::toDTO)
                .orElseGet(() -> {
                    Customer customer = Customer.builder()
                            .firstName(dto.getFirstName())
                            .lastName(dto.getLastName())
                            .email(dto.getEmail())
                            .dob(dto.getDob())
                            .address(dto.getAddress())
                            .idProof(dto.getIdProof())
                            .build();
                    Customer saved = customerRepository.save(customer);
                    log.info("New customer created with ID: {}", saved.getCustomerId());
                    return toDTO(saved);
                });
    }

    @Transactional
    public CustomerResponseDTO updateAddress(AddressUpdateDTO dto) {
        log.info("Updating address for customer ID: {}", dto.getCustomerId());
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with ID: " + dto.getCustomerId()));

        customer.setAddress(dto.getAddress());
        Customer updated = customerRepository.save(customer);
        log.info("Address updated for customer ID: {}", updated.getCustomerId());
        return toDTO(updated);
    }

    public CustomerResponseDTO getById(Long id) {
        return customerRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
    }

    private CustomerResponseDTO toDTO(Customer c) {
        return CustomerResponseDTO.builder()
                .customerId(c.getCustomerId())
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .email(c.getEmail())
                .dob(c.getDob())
                .address(c.getAddress())
                .idProof(c.getIdProof())
                .build();
    }
}
