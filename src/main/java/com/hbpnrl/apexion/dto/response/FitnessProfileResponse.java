package com.hbpnrl.apexion.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FitnessProfileResponse {

    private Integer id;
    private Integer userId;
    private BigDecimal heightCm;
    private BigDecimal weightKg;
    private String fitnessGoal;
    private String activityLevel;
}