package com.project.simactivation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger / OpenAPI 3.0 configuration.
 * Accessible at: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI simActivationOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SIM Activation Portal API")
                        .description("""
                                ## Telecom SIM Activation Portal
                                
                                A production-grade REST API for automating the end-to-end SIM card
                                activation process in a telecom environment.
                                
                                ### Key Modules
                                - **Customer** — Registration, identity validation, address management
                                - **SIM** — SIM card validation and status checks
                                - **Activation** — Full activation workflow with KYC checks
                                - **Offers** — Personalized plan recommendations
                                
                                ### Activation Flow
                                1. Register customer → `POST /api/v1/customers/register`
                                2. Validate identity → `POST /api/v1/customers/validate`
                                3. Validate SIM     → `GET  /api/v1/sim/validate`
                                4. Activate SIM     → `POST /api/v1/activation/start`
                                5. Browse offers    → `GET  /api/v1/offers/{customerId}`
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SIM Activation Team")
                                .email("support@simportal.com")
                                .url("https://github.com/sim-activation-portal"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development"),
                        new Server().url("https://api.simportal.com").description("Production")
                ));
    }
}
