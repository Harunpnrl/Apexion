package com.hbpnrl.apexion.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class WorkoutRequest {
    private Integer userId;
    private String name;
    private List<WorkoutExerciseRequest> exercises;
}