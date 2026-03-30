package com.project.simactivation.repository;

import com.project.simactivation.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repository interface for Customer entity.
 * Spring Data JPA auto-implements all CRUD operations.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Find a customer by their email address.
     * Used for duplicate-check during registration.
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Validate a customer by email AND date-of-birth.
     * Used in the customer-validation flow before activation.
     */
    Optional<Customer> findByEmailAndDob(String email, LocalDate dob);

    /**
     * Check if a customer already exists with the given email.
     */
    boolean existsByEmail(String email);
}
