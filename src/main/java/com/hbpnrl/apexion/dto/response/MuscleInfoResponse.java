package com.hbpnrl.apexion.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MuscleInfoResponse {
    private Integer id;
    private String muscleName;
    private String overview;
    private String anatomy;
    private String muscleFunction;
    private String exampleExercises;
    private String synergistMuscles;
    private String recoveryWindow;
}