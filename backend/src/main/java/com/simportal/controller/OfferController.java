package com.simportal.controller;

import com.simportal.dto.ApiResponse;
import com.simportal.dto.OfferResponseDTO;
import com.simportal.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Offers", description = "APIs for fetching available plans")
public class OfferController {

    private final OfferService offerService;

    @GetMapping
    @Operation(summary = "Get all offers", description = "Returns all available SIM plans/offers")
    public ResponseEntity<ApiResponse<List<OfferResponseDTO>>> getAllOffers() {
        log.info("GET /api/offers");
        List<OfferResponseDTO> offers = offerService.getAllOffers();
        return ResponseEntity.ok(ApiResponse.success("Offers retrieved successfully.", offers));
    }
}
