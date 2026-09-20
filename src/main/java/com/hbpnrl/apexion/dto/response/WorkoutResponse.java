package com.hbpnrl.apexion.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class WorkoutResponse {
    private Integer id;
    private String name;
    private String description;
    private Integer estimatedDurationMinutes;
    private LocalDateTime createdAt;
    private List<WorkoutExerciseResponse> workoutExercises;
}