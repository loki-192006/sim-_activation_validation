package com.project.simactivation.config;

import com.project.simactivation.entity.Offer;
import com.project.simactivation.entity.Sim;
import com.project.simactivation.repository.OfferRepository;
import com.project.simactivation.repository.SimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Seeds the database with sample SIM cards and offers on application startup.
 * Only runs when the "default" or "dev" profile is active.
 * In production, remove @Profile or use a migration tool like Flyway.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final SimRepository simRepository;
    private final OfferRepository offerRepository;

    @Bean
    @Profile({"default", "dev"})
    public CommandLineRunner seedData() {
        return args -> {
            seedSims();
            seedOffers();
        };
    }

    private void seedSims() {
        if (simRepository.count() > 0) {
            log.info("SIM data already seeded. Skipping.");
            return;
        }

        List<Sim> sims = List.of(
                Sim.builder()
                        .simIccid("8991101200003204510")
                        .mobileNumber("9876543210")
                        .status(Sim.SimStatus.INACTIVE)
                        .simType(Sim.SimType.PREPAID)
                        .operatorCode("IND01")
                        .build(),
                Sim.builder()
                        .simIccid("8991101200003204511")
                        .mobileNumber("9876543211")
                        .status(Sim.SimStatus.INACTIVE)
                        .simType(Sim.SimType.POSTPAID)
                        .operatorCode("IND01")
                        .build(),
                Sim.builder()
                        .simIccid("8991101200003204512")
                        .mobileNumber("9876543212")
                        .status(Sim.SimStatus.INACTIVE)
                        .simType(Sim.SimType.PREPAID)
                        .operatorCode("IND02")
                        .build(),
                Sim.builder()
                        .simIccid("8991101200003204513")
                        .mobileNumber("9876543213")
                        .status(Sim.SimStatus.ACTIVE)       // already active — for testing rejection
                        .simType(Sim.SimType.PREPAID)
                        .operatorCode("IND02")
                        .build()
        );

        simRepository.saveAll(sims);
        log.info("Seeded {} SIM cards.", sims.size());
    }

    private void seedOffers() {
        if (offerRepository.count() > 0) {
            log.info("Offer data already seeded. Skipping.");
            return;
        }

        LocalDate today = LocalDate.now();

        List<Offer> offers = List.of(
                Offer.builder()
                        .offerName("Starter Pack")
                        .description("Entry-level prepaid plan with basic data and calls.")
                        .price(new BigDecimal("99.00"))
                        .validityDays(28)
                        .dataGb(new BigDecimal("1.00"))
                        .callingMinutes(100)
                        .smsCount(100)
                        .simTypeEligible(Sim.SimType.PREPAID)
                        .validFrom(today.minusDays(30))
                        .validTo(today.plusDays(180))
                        .isActive(true)
                        .build(),
                Offer.builder()
                        .offerName("Power User")
                        .description("High-data prepaid plan for heavy users.")
                        .price(new BigDecimal("299.00"))
                        .validityDays(56)
                        .dataGb(new BigDecimal("50.00"))
                        .callingMinutes(500)
                        .smsCount(200)
                        .simTypeEligible(Sim.SimType.PREPAID)
                        .validFrom(today.minusDays(10))
                        .validTo(today.plusDays(365))
                        .isActive(true)
                        .build(),
                Offer.builder()
                        .offerName("Business Unlimited")
                        .description("Unlimited calls and data for postpaid business customers.")
                        .price(new BigDecimal("799.00"))
                        .validityDays(30)
                        .dataGb(new BigDecimal("100.00"))
                        .callingMinutes(null)     // unlimited
                        .smsCount(500)
                        .simTypeEligible(Sim.SimType.POSTPAID)
                        .validFrom(today.minusDays(5))
                        .validTo(today.plusDays(365))
                        .isActive(true)
                        .build(),
                Offer.builder()
                        .offerName("Weekend Special")
                        .description("Limited-time offer with free weekend data.")
                        .price(new BigDecimal("149.00"))
                        .validityDays(14)
                        .dataGb(new BigDecimal("10.00"))
                        .callingMinutes(200)
                        .smsCount(50)
                        .simTypeEligible(Sim.SimType.PREPAID)
                        .validFrom(today.minusDays(1))
                        .validTo(today.plusDays(30))
                        .isActive(true)
                        .build()
        );

        offerRepository.saveAll(offers);
        log.info("Seeded {} offers.", offers.size());
    }
}
