package com.project.simactivation.service;

import com.project.simactivation.dto.request.AddressUpdateRequest;
import com.project.simactivation.dto.request.CustomerRegisterRequest;
import com.project.simactivation.dto.request.CustomerValidateRequest;
import com.project.simactivation.dto.response.CustomerResponse;

/**
 * Service contract for all Customer-related business operations.
 * The controller depends only on this interface — never on the implementation.
 * This makes it easy to swap implementations and write unit tests with mocks.
 */
public interface CustomerService {

    /**
     * Register a new customer in the system.
     * Throws DuplicateResourceException if the email is already registered.
     *
     * @param request validated registration payload
     * @return the saved customer as a response DTO
     */
    CustomerResponse registerCustomer(CustomerRegisterRequest request);

    /**
     * Validate a customer's identity using email + date-of-birth.
     * Used as a pre-step before SIM activation.
     *
     * @param request email and DOB
     * @return the matched customer as a response DTO
     * @throws com.project.simactivation.exception.CustomerValidationException if no match
     */
    CustomerResponse validateCustomer(CustomerValidateRequest request);

    /**
     * Fetch a customer's full profile by their database ID.
     *
     * @param customerId primary key
     * @return customer response DTO
     * @throws com.project.simactivation.exception.CustomerNotFoundException if not found
     */
    CustomerResponse getCustomerById(Long customerId);

    /**
     * Update the address of an existing customer.
     *
     * @param request contains customerId + newAddress
     * @return updated customer response DTO
     */
    CustomerResponse updateAddress(AddressUpdateRequest request);
}
