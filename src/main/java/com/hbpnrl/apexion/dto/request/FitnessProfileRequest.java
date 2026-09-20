package com.hbpnrl.apexion.dto.request;

import com.hbpnrl.apexion.entity.FitnessProfile.ActivityLevel;
import com.hbpnrl.apexion.entity.FitnessProfile.FitnessGoal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class FitnessProfileRequest {

    @NotNull(message = "The height field cannot be left blank.")
    @Positive(message = "The height must be greater than zero")
    private Double heightCm;

    @NotNull(message = "The weight field cannot be left blank.")
    @Positive(message = "The weight must be greater than zero.")
    private Double weightKg;

    @NotNull(message = "A fitness goal must be selected.")
    private FitnessGoal fitnessGoal;

    @NotNull(message = "An Activity level must be selected")
    private ActivityLevel activityLevel;
}