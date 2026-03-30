package com.project.simactivation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the SIM Activation Portal.
 *
 * Telecom domain application that automates the SIM card activation
 * process — including customer registration, SIM validation,
 * ID verification, and activation workflows.
 *
 * @author  SIM Activation Team
 * @version 1.0.0
 */
@SpringBootApplication
public class SimActivationPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimActivationPortalApplication.class, args);
        System.out.println("""
                ╔══════════════════════════════════════════════╗
                ║       SIM Activation Portal — STARTED        ║
                ║   Swagger UI: http://localhost:8080/swagger-ui.html  ║
                ╚══════════════════════════════════════════════╝
                """);
    }
}
