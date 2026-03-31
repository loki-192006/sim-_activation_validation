package com.simportal.config;

import com.simportal.entity.Offer;
import com.simportal.entity.Sim;
import com.simportal.repository.OfferRepository;
import com.simportal.repository.SimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final OfferRepository offerRepository;
    private final SimRepository simRepository;

    @Override
    public void run(String... args) {
        seedOffers();
        seedSims();
    }

    private void seedOffers() {
        if (offerRepository.count() == 0) {
            log.info("Seeding offers...");
            offerRepository.saveAll(List.of(
                Offer.builder()
                    .planName("Basic")
                    .price(new BigDecimal("99.00"))
                    .validity("28 Days")
                    .description("Great for light users")
                    .dataLimit("1 GB/day")
                    .calls("100 min/day")
                    .sms("100 SMS/day")
                    .build(),
                Offer.builder()
                    .planName("Standard")
                    .price(new BigDecimal("199.00"))
                    .validity("28 Days")
                    .description("Best value for daily users")
                    .dataLimit("2 GB/day")
                    .calls("Unlimited")
                    .sms("100 SMS/day")
                    .build(),
                Offer.builder()
                    .planName("Premium")
                    .price(new BigDecimal("399.00"))
                    .validity("56 Days")
                    .description("Ultimate plan for power users")
                    .dataLimit("3 GB/day")
                    .calls("Unlimited")
                    .sms("Unlimited")
                    .build()
            ));
            log.info("Offers seeded successfully.");
        }
    }

    private void seedSims() {
        if (simRepository.count() == 0) {
            log.info("Seeding SIM cards...");
            List<String> simNumbers = List.of(
                "8901260123456789012",
                "8901260123456789013",
                "8901260123456789014",
                "8901260123456789015",
                "8901260123456789016"
            );
            simNumbers.forEach(num ->
                simRepository.save(Sim.builder()
                    .simNumber(num)
                    .status(Sim.SimStatus.AVAILABLE)
                    .build())
            );
            log.info("SIM cards seeded successfully.");
        }
    }
}
