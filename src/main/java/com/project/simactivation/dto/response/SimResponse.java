package com.project.simactivation.dto.response;

import com.project.simactivation.entity.Sim;
import lombok.Builder;
import lombok.Data;

/**
 * DTO returned for SIM-related responses.
 */
@Data
@Builder
public class SimResponse {

    private Long simId;
    private String simIccid;
    private String mobileNumber;
    private String status;
    private String simType;
    private String operatorCode;
    private boolean isValid;

    public static SimResponse fromEntity(Sim sim) {
        return SimResponse.builder()
                .simId(sim.getSimId())
                .simIccid(sim.getSimIccid())
                .mobileNumber(sim.getMobileNumber())
                .status(sim.getStatus().name())
                .simType(sim.getSimType().name())
                .operatorCode(sim.getOperatorCode())
                .isValid(sim.getStatus() == Sim.SimStatus.INACTIVE)
                .build();
    }
}
