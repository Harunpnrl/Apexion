package com.hbpnrl.apexion.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkoutExerciseResponse {
    private Integer id;
    private String exerciseName;
    private Integer sets;
    private Integer reps;
    private Double weight;
    private Integer sortOrder;
}