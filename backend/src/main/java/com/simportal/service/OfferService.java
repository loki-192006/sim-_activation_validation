package com.simportal.service;

import com.simportal.dto.OfferResponseDTO;
import com.simportal.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferService {

    private final OfferRepository offerRepository;

    public List<OfferResponseDTO> getAllOffers() {
        log.info("Fetching all offers");
        List<OfferResponseDTO> offers = offerRepository.findAll().stream()
                .map(o -> OfferResponseDTO.builder()
                        .offerId(o.getOfferId())
                        .planName(o.getPlanName())
                        .price(o.getPrice())
                        .validity(o.getValidity())
                        .description(o.getDescription())
                        .dataLimit(o.getDataLimit())
                        .calls(o.getCalls())
                        .sms(o.getSms())
                        .popular(o.getPlanName().equalsIgnoreCase("Standard"))
                        .build())
                .collect(Collectors.toList());
        log.info("Returning {} offers", offers.size());
        return offers;
    }
}
