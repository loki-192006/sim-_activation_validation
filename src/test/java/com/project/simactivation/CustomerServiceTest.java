package com.project.simactivation;

import com.project.simactivation.dto.request.CustomerRegisterRequest;
import com.project.simactivation.dto.request.CustomerValidateRequest;
import com.project.simactivation.dto.response.CustomerResponse;
import com.project.simactivation.entity.Customer;
import com.project.simactivation.exception.CustomerValidationException;
import com.project.simactivation.exception.DuplicateResourceException;
import com.project.simactivation.repository.CustomerRepository;
import com.project.simactivation.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

/**
 * Unit tests for CustomerServiceImpl.
 *
 * Uses Mockito to mock the repository layer — no database required.
 * Tests cover the happy path and key failure scenarios.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerService Unit Tests")
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer sampleCustomer;
    private CustomerRegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        sampleCustomer = Customer.builder()
                .id(1L)
                .firstName("Raj")
                .lastName("Patel")
                .email("raj.patel@example.com")
                .dob(LocalDate.of(1995, 6, 15))
                .address("123 MG Road, Surat, Gujarat 395001")
                .kycStatus(Customer.KycStatus.PENDING)
                .build();

        registerRequest = new CustomerRegisterRequest();
        registerRequest.setFirstName("Raj");
        registerRequest.setLastName("Patel");
        registerRequest.setEmail("raj.patel@example.com");
        registerRequest.setDob(LocalDate.of(1995, 6, 15));
        registerRequest.setAddress("123 MG Road, Surat, Gujarat 395001");
        registerRequest.setPhoneNumber("9876543210");
    }

    // ---- registerCustomer ----

    @Test
    @DisplayName("registerCustomer: should register new customer successfully")
    void registerCustomer_success() {
        given(customerRepository.existsByEmail(anyString())).willReturn(false);
        given(customerRepository.save(any(Customer.class))).willReturn(sampleCustomer);

        CustomerResponse result = customerService.registerCustomer(registerRequest);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("raj.patel@example.com");
        assertThat(result.getFirstName()).isEqualTo("Raj");
        then(customerRepository).should(times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("registerCustomer: should throw DuplicateResourceException for existing email")
    void registerCustomer_duplicateEmail_throwsException() {
        given(customerRepository.existsByEmail(anyString())).willReturn(true);

        assertThatThrownBy(() -> customerService.registerCustomer(registerRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("raj.patel@example.com");

        then(customerRepository).should(never()).save(any());
    }

    // ---- validateCustomer ----

    @Test
    @DisplayName("validateCustomer: should validate customer with correct email + DOB")
    void validateCustomer_success() {
        given(customerRepository.findByEmailAndDob(anyString(), any(LocalDate.class)))
                .willReturn(Optional.of(sampleCustomer));

        CustomerValidateRequest request = new CustomerValidateRequest();
        request.setEmail("raj.patel@example.com");
        request.setDob(LocalDate.of(1995, 6, 15));

        CustomerResponse result = customerService.validateCustomer(request);

        assertThat(result).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("validateCustomer: should throw CustomerValidationException for wrong DOB")
    void validateCustomer_wrongDob_throwsException() {
        given(customerRepository.findByEmailAndDob(anyString(), any(LocalDate.class)))
                .willReturn(Optional.empty());

        CustomerValidateRequest request = new CustomerValidateRequest();
        request.setEmail("raj.patel@example.com");
        request.setDob(LocalDate.of(2000, 1, 1)); // wrong DOB

        assertThatThrownBy(() -> customerService.validateCustomer(request))
                .isInstanceOf(CustomerValidationException.class)
                .hasMessageContaining("Invalid credentials");
    }

    // ---- getCustomerById ----

    @Test
    @DisplayName("getCustomerById: should return customer for valid ID")
    void getCustomerById_found() {
        given(customerRepository.findById(1L)).willReturn(Optional.of(sampleCustomer));

        CustomerResponse result = customerService.getCustomerById(1L);

        assertThat(result.getCustomerId()).isEqualTo(1L);
        assertThat(result.getFullName()).isEqualTo("Raj Patel");
    }

    @Test
    @DisplayName("getCustomerById: should throw ResourceNotFoundException for invalid ID")
    void getCustomerById_notFound() {
        given(customerRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomerById(99L))
                .hasMessageContaining("99");
    }
}
