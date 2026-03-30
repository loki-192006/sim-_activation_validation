package com.project.simactivation.service.impl;

import com.project.simactivation.dto.request.AddressUpdateRequest;
import com.project.simactivation.dto.request.CustomerRegisterRequest;
import com.project.simactivation.dto.request.CustomerValidateRequest;
import com.project.simactivation.dto.response.CustomerResponse;
import com.project.simactivation.entity.Customer;
import com.project.simactivation.exception.CustomerNotFoundException;
import com.project.simactivation.exception.CustomerValidationException;
import com.project.simactivation.exception.DuplicateResourceException;
import com.project.simactivation.repository.CustomerRepository;
import com.project.simactivation.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of CustomerService.
 *
 * All database calls are wrapped in @Transactional to ensure atomicity.
 * Read-only methods use readOnly=true for performance (prevents dirty checks).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    // -------------------------------------------------------
    //   Register a new customer
    // -------------------------------------------------------

    @Override
    @Transactional
    public CustomerResponse registerCustomer(CustomerRegisterRequest request) {
        log.info("Registering new customer with email: {}", request.getEmail());

        // Guard: prevent duplicate registrations
        if (customerRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration attempt with existing email: {}", request.getEmail());
            throw new DuplicateResourceException(
                    "A customer is already registered with email: " + request.getEmail());
        }

        // Map DTO → Entity
        Customer customer = Customer.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail().toLowerCase().trim())
                .dob(request.getDob())
                .address(request.getAddress().trim())
                .phoneNumber(request.getPhoneNumber())
                .kycStatus(Customer.KycStatus.PENDING)
                .build();

        Customer saved = customerRepository.save(customer);
        log.info("Customer registered successfully. ID: {}", saved.getId());

        return CustomerResponse.fromEntity(saved);
    }

    // -------------------------------------------------------
    //   Validate customer identity (email + DOB)
    // -------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse validateCustomer(CustomerValidateRequest request) {
        log.info("Validating customer with email: {}", request.getEmail());

        Customer customer = customerRepository
                .findByEmailAndDob(request.getEmail().toLowerCase().trim(), request.getDob())
                .orElseThrow(() -> {
                    log.warn("Validation failed for email: {}", request.getEmail());
                    return new CustomerValidationException(
                            "Invalid credentials. Email and date-of-birth do not match.");
                });

        log.info("Customer validated successfully. ID: {}", customer.getId());
        return CustomerResponse.fromEntity(customer);
    }

    // -------------------------------------------------------
    //   Fetch customer by ID
    // -------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long customerId) {
        log.info("Fetching customer by ID: {}", customerId);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        return CustomerResponse.fromEntity(customer);
    }

    // -------------------------------------------------------
    //   Update address
    // -------------------------------------------------------

    @Override
    @Transactional
    public CustomerResponse updateAddress(AddressUpdateRequest request) {
        log.info("Updating address for customer ID: {}", request.getCustomerId());

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));

        String oldAddress = customer.getAddress();
        customer.setAddress(request.getNewAddress().trim());
        Customer updated = customerRepository.save(customer);

        log.info("Address updated for customer ID: {}. Old: '{}' → New: '{}'",
                customer.getId(), oldAddress, updated.getAddress());

        return CustomerResponse.fromEntity(updated);
    }
}
