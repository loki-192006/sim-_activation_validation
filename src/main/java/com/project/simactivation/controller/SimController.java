package com.project.simactivation.controller;

import com.project.simactivation.dto.response.ApiResponse;
import com.project.simactivation.dto.response.SimResponse;
import com.project.simactivation.service.SimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for SIM card operations.
 *
 * Base URL: /api/v1/sim
 *
 * Endpoints:
 *   GET  /validate  → Validate a SIM by ICCID + mobile number
 *   GET  /{simId}   → Fetch SIM details by ID
 */
@RestController
@RequestMapping("/api/v1/sim")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "SIM", description = "SIM card validation and status check")
public class SimController {

    private final SimService simService;

    // -------------------------------------------------------
    //   GET /validate?simIccid=...&mobileNumber=...
    // -------------------------------------------------------

    @GetMapping("/validate")
    @Operation(
        summary = "Validate a SIM card",
        description = "Validates a SIM by matching its ICCID and mobile number. " +
                      "Returns SIM details and an 'isValid' flag indicating activation eligibility."
    )
    public ResponseEntity<ApiResponse<SimResponse>> validateSim(
            @Parameter(description = "19-22 digit SIM ICCID", required = true)
            @RequestParam @NotBlank String simIccid,

            @Parameter(description = "10-digit mobile number", required = true)
            @RequestParam @NotBlank String mobileNumber) {

        log.info("GET /api/v1/sim/validate — ICCID: {}, Mobile: {}", simIccid, mobileNumber);
        SimResponse data = simService.validateSim(simIccid, mobileNumber);

        String message = data.isValid()
                ? "SIM is valid and ready for activation."
                : "SIM found but not eligible for activation. Status: " + data.getStatus();

        return ResponseEntity.ok(ApiResponse.success(message, data));
    }

    // -------------------------------------------------------
    //   GET /{simId}
    // -------------------------------------------------------

    @GetMapping("/{simId}")
    @Operation(summary = "Get SIM by ID",
               description = "Fetches SIM card details by its database ID.")
    public ResponseEntity<ApiResponse<SimResponse>> getSimById(
            @PathVariable Long simId) {

        log.info("GET /api/v1/sim/{}", simId);
        SimResponse data = simService.getSimById(simId);

        return ResponseEntity.ok(ApiResponse.success("SIM details fetched successfully.", data));
    }
}
