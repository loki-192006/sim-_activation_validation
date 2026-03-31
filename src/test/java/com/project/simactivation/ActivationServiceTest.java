package com.project.simactivation;

import com.project.simactivation.dto.request.ActivationRequest;
import com.project.simactivation.dto.response.ActivationResponse;
import com.project.simactivation.entity.Activation;
import com.project.simactivation.entity.Customer;
import com.project.simactivation.entity.Sim;
import com.project.simactivation.exception.SimActivationException;
import com.project.simactivation.repository.ActivationRepository;
import com.project.simactivation.repository.CustomerRepository;
import com.project.simactivation.repository.SimRepository;
import com.project.simactivation.service.impl.ActivationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

/**
 * Unit tests for ActivationServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ActivationService Unit Tests")
class ActivationServiceTest {

    @Mock private ActivationRepository activationRepository;
    @Mock private CustomerRepository   customerRepository;
    @Mock private SimRepository        simRepository;

    @InjectMocks
    private ActivationServiceImpl activationService;

    private Customer customer;
    private Sim      inactiveSim;
    private Sim      activeSim;
    private ActivationRequest request;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .firstName("Priya")
                .lastName("Shah")
                .email("priya.shah@example.com")
                .dob(LocalDate.of(1993, 4, 22))
                .address("456 Ring Road, Surat")
                .kycStatus(Customer.KycStatus.VERIFIED)
                .build();

        inactiveSim = Sim.builder()
                .simId(10L)
                .simIccid("8991101200003204510")
                .mobileNumber("9876543210")
                .status(Sim.SimStatus.INACTIVE)
                .simType(Sim.SimType.PREPAID)
                .build();

        activeSim = Sim.builder()
                .simId(11L)
                .simIccid("8991101200003204511")
                .mobileNumber("9876543211")
                .status(Sim.SimStatus.ACTIVE)
                .simType(Sim.SimType.PREPAID)
                .build();

        request = new ActivationRequest();
        request.setCustomerId(1L);
        request.setSimIccid("8991101200003204510");
        request.setMobileNumber("9876543210");
        request.setPlanSelected("Starter Pack");
    }

    @Test
    @DisplayName("startActivation: should activate SIM successfully")
    void startActivation_success() {
        given(customerRepository.findById(1L)).willReturn(Optional.of(customer));
        given(simRepository.findBySimIccidAndMobileNumber(anyString(), anyString()))
                .willReturn(Optional.of(inactiveSim));
        given(activationRepository.existsBySim_SimIdAndActivationStatus(anyLong(), any()))
                .willReturn(false);

        Activation savedActivation = Activation.builder()
                .activationId(100L)
                .customer(customer)
                .sim(inactiveSim)
                .activationStatus(Activation.ActivationStatus.SUCCESS)
                .planSelected("Starter Pack")
                .activatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        given(activationRepository.save(any(Activation.class))).willReturn(savedActivation);

        ActivationResponse result = activationService.startActivation(request);

        assertThat(result).isNotNull();
        assertThat(result.getActivationStatus()).isEqualTo("SUCCESS");
        assertThat(result.getMobileNumber()).isEqualTo("9876543210");
        // Verify SIM status was updated to ACTIVE
        then(simRepository).should(times(1)).save(argThat(sim ->
                sim.getStatus() == Sim.SimStatus.ACTIVE));
    }

    @Test
    @DisplayName("startActivation: should fail when SIM is already ACTIVE")
    void startActivation_simAlreadyActive_throwsException() {
        given(customerRepository.findById(1L)).willReturn(Optional.of(customer));

        ActivationRequest activeRequest = new ActivationRequest();
        activeRequest.setCustomerId(1L);
        activeRequest.setSimIccid("8991101200003204511");
        activeRequest.setMobileNumber("9876543211");

        given(simRepository.findBySimIccidAndMobileNumber(anyString(), anyString()))
                .willReturn(Optional.of(activeSim));

        assertThatThrownBy(() -> activationService.startActivation(activeRequest))
                .isInstanceOf(SimActivationException.class)
                .hasMessageContaining("ACTIVE");
    }

    @Test
    @DisplayName("startActivation: should fail when customer KYC is rejected")
    void startActivation_kycRejected_throwsException() {
        customer.setKycStatus(Customer.KycStatus.REJECTED);
        given(customerRepository.findById(1L)).willReturn(Optional.of(customer));

        assertThatThrownBy(() -> activationService.startActivation(request))
                .isInstanceOf(SimActivationException.class)
                .hasMessageContaining("KYC");
    }

    @Test
    @DisplayName("startActivation: should fail when SIM already has a SUCCESS activation")
    void startActivation_alreadyActivated_throwsException() {
        given(customerRepository.findById(1L)).willReturn(Optional.of(customer));
        given(simRepository.findBySimIccidAndMobileNumber(anyString(), anyString()))
                .willReturn(Optional.of(inactiveSim));
        given(activationRepository.existsBySim_SimIdAndActivationStatus(anyLong(), any()))
                .willReturn(true);

        assertThatThrownBy(() -> activationService.startActivation(request))
                .isInstanceOf(SimActivationException.class)
                .hasMessageContaining("already activated");
    }
}
