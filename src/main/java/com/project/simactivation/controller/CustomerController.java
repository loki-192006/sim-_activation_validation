package com.project.simactivation.controller;

import com.project.simactivation.dto.request.AddressUpdateRequest;
import com.project.simactivation.dto.request.CustomerRegisterRequest;
import com.project.simactivation.dto.request.CustomerValidateRequest;
import com.project.simactivation.dto.response.ApiResponse;
import com.project.simactivation.dto.response.CustomerResponse;
import com.project.simactivation.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Customer operations.
 *
 * Base URL: /api/v1/customers
 *
 * Endpoints:
 *   POST   /register   → Register a new customer
 *   POST   /validate   → Validate customer identity (email + DOB)
 *   GET    /{id}       → Fetch customer by ID
 *   PUT    /address    → Update customer address
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer", description = "Customer registration, validation, and profile management")
public class CustomerController {

    private final CustomerService customerService;

    // -------------------------------------------------------
    //   POST /register
    // -------------------------------------------------------

    @PostMapping("/register")
    @Operation(summary = "Register a new customer",
               description = "Creates a new customer account. Email must be unique.")
    public ResponseEntity<ApiResponse<CustomerResponse>> registerCustomer(
            @Valid @RequestBody CustomerRegisterRequest request) {

        log.info("POST /api/v1/customers/register — email: {}", request.getEmail());
        CustomerResponse data = customerService.registerCustomer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer registered successfully.", data));
    }

    // -------------------------------------------------------
    //   POST /validate
    // -------------------------------------------------------

    @PostMapping("/validate")
    @Operation(summary = "Validate customer identity",
               description = "Verifies a customer using email and date-of-birth. Used before SIM activation.")
    public ResponseEntity<ApiResponse<CustomerResponse>> validateCustomer(
            @Valid @RequestBody CustomerValidateRequest request) {

        log.info("POST /api/v1/customers/validate — email: {}", request.getEmail());
        CustomerResponse data = customerService.validateCustomer(request);

        return ResponseEntity.ok(
                ApiResponse.success("Customer identity validated successfully.", data));
    }

    // -------------------------------------------------------
    //   GET /{id}
    // -------------------------------------------------------

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID",
               description = "Fetches full customer profile by their database ID.")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(
            @PathVariable Long id) {

        log.info("GET /api/v1/customers/{}", id);
        CustomerResponse data = customerService.getCustomerById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Customer fetched successfully.", data));
    }

    // -------------------------------------------------------
    //   PUT /address
    // -------------------------------------------------------

    @PutMapping("/address")
    @Operation(summary = "Update customer address",
               description = "Updates the residential address of an existing customer.")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateAddress(
            @Valid @RequestBody AddressUpdateRequest request) {

        log.info("PUT /api/v1/customers/address — customerID: {}", request.getCustomerId());
        CustomerResponse data = customerService.updateAddress(request);

        return ResponseEntity.ok(
                ApiResponse.success("Address updated successfully.", data));
    }
}
