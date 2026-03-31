package com.project.simactivation.controller;

import com.project.simactivation.dto.response.ApiResponse;
import com.project.simactivation.dto.response.OfferResponse;
import com.project.simactivation.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Offer/Plan retrieval.
 *
 * Base URL: /api/v1/offers
 *
 * Endpoints:
 *   GET  /{customerId}  → Fetch eligible offers for a customer
 *   GET  /              → Fetch all active offers
 */
@RestController
@RequestMapping("/api/v1/offers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Offers", description = "Telecom plans and special offers")
public class OfferController {

    private final OfferService offerService;

    // -------------------------------------------------------
    //   GET /{customerId}
    // -------------------------------------------------------

    @GetMapping("/{customerId}")
    @Operation(
        summary = "Get offers for a customer",
        description = "Returns personalized offers based on the customer's SIM type (PREPAID/POSTPAID). " +
                      "Falls back to all active offers if the customer has no activated SIM."
    )
    public ResponseEntity<ApiResponse<List<OfferResponse>>> getOffersForCustomer(
            @PathVariable Long customerId) {

        log.info("GET /api/v1/offers/{}", customerId);
        List<OfferResponse> data = offerService.getOffersForCustomer(customerId);

        return ResponseEntity.ok(ApiResponse.success(
                "Found " + data.size() + " offer(s) for customer.", data));
    }

    // -------------------------------------------------------
    //   GET /
    // -------------------------------------------------------

    @GetMapping
    @Operation(summary = "Get all active offers",
               description = "Returns all currently active offers, sorted by price ascending.")
    public ResponseEntity<ApiResponse<List<OfferResponse>>> getAllActiveOffers() {

        log.info("GET /api/v1/offers");
        List<OfferResponse> data = offerService.getAllActiveOffers();

        return ResponseEntity.ok(ApiResponse.success(
                "Found " + data.size() + " active offer(s).", data));
    }
}
