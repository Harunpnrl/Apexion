package com.hbpnrl.apexion.dto.request;

import lombok.Data;

@Data
public class WorkoutExerciseRequest {
    private Integer exerciseId;
    private Integer sortOrder;
    private Integer sets;
    private Integer reps;
    private Double weight;
}