package com.simportal.controller;

import com.simportal.dto.AddressUpdateDTO;
import com.simportal.dto.ApiResponse;
import com.simportal.dto.CustomerRequestDTO;
import com.simportal.dto.CustomerResponseDTO;
import com.simportal.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer Management", description = "APIs for customer creation and management")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/validate")
    @Operation(summary = "Validate/Register customer", description = "Creates a new customer or returns existing one by email")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> validateCustomer(
            @Valid @RequestBody CustomerRequestDTO dto) {
        log.info("POST /api/customer/validate - email: {}", dto.getEmail());
        CustomerResponseDTO customer = customerService.validateAndSave(dto);
        return ResponseEntity.ok(ApiResponse.success("Customer validated successfully.", customer));
    }

    @PutMapping("/update-address")
    @Operation(summary = "Update customer address", description = "Updates the address of an existing customer")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> updateAddress(
            @Valid @RequestBody AddressUpdateDTO dto) {
        log.info("PUT /api/customer/update-address - customerId: {}", dto.getCustomerId());
        CustomerResponseDTO updated = customerService.updateAddress(dto);
        return ResponseEntity.ok(ApiResponse.success("Address updated successfully.", updated));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Customer found.", customerService.getById(id)));
    }
}
