package com.project.simactivation.controller;

import com.project.simactivation.dto.request.ActivationRequest;
import com.project.simactivation.dto.response.ActivationResponse;
import com.project.simactivation.dto.response.ApiResponse;
import com.project.simactivation.service.ActivationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for SIM Activation workflows.
 *
 * Base URL: /api/v1/activation
 *
 * Endpoints:
 *   POST  /start                        → Begin SIM activation
 *   GET   /customer/{customerId}        → Fetch all activations for a customer
 */
@RestController
@RequestMapping("/api/v1/activation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Activation", description = "SIM activation workflow management")
public class ActivationController {

    private final ActivationService activationService;

    // -------------------------------------------------------
    //   POST /start
    // -------------------------------------------------------

    @PostMapping("/start")
    @Operation(
        summary = "Start SIM Activation",
        description = "Triggers the full SIM activation workflow: " +
                      "validates customer → validates SIM → creates activation record → marks SIM active."
    )
    public ResponseEntity<ApiResponse<ActivationResponse>> startActivation(
            @Valid @RequestBody ActivationRequest request) {

        log.info("POST /api/v1/activation/start — CustomerID: {}, Mobile: {}",
                request.getCustomerId(), request.getMobileNumber());

        ActivationResponse data = activationService.startActivation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "SIM activated successfully. Welcome to the network!", data));
    }

    // -------------------------------------------------------
    //   GET /customer/{customerId}
    // -------------------------------------------------------

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get activations by customer",
               description = "Returns the full activation history for a given customer.")
    public ResponseEntity<ApiResponse<List<ActivationResponse>>> getActivationsByCustomer(
            @PathVariable Long customerId) {

        log.info("GET /api/v1/activation/customer/{}", customerId);
        List<ActivationResponse> data = activationService.getActivationsByCustomer(customerId);

        return ResponseEntity.ok(ApiResponse.success(
                "Fetched " + data.size() + " activation record(s).", data));
    }
}
