package com.simportal.controller;

import com.simportal.dto.ApiResponse;
import com.simportal.dto.SimActivationRequestDTO;
import com.simportal.dto.SimResponseDTO;
import com.simportal.service.SimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sim")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "SIM Management", description = "APIs for SIM validation and activation")
public class SimController {

    private final SimService simService;

    @GetMapping("/validate/{simNumber}")
    @Operation(summary = "Validate a SIM card", description = "Checks if a SIM number exists and is available for activation")
    public ResponseEntity<ApiResponse<SimResponseDTO>> validateSim(@PathVariable String simNumber) {
        log.info("GET /api/sim/validate/{}", simNumber);
        SimResponseDTO sim = simService.validateSim(simNumber);
        return ResponseEntity.ok(ApiResponse.success("SIM is valid and available.", sim));
    }

    @PostMapping("/activate")
    @Operation(summary = "Activate a SIM", description = "Links a SIM to a customer with a selected plan")
    public ResponseEntity<ApiResponse<SimResponseDTO>> activateSim(@Valid @RequestBody SimActivationRequestDTO dto) {
        log.info("POST /api/sim/activate - SIM: {}", dto.getSimNumber());
        SimResponseDTO sim = simService.activateSim(dto);
        return ResponseEntity.ok(ApiResponse.success("SIM activated successfully!", sim));
    }
}
