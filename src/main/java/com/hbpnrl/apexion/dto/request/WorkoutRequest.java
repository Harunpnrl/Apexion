package com.hbpnrl.apexion.dto.request;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Data
public class WorkoutRequest {

    @NotBlank(message = "Workout name cannot be blank.")
    @Size(max = 100, message = "Workout name cannot exceed 100 characters.")
    private String name;

    @NotEmpty(message = "A workout must contain at least one exercise.")
    @Size(max = 50, message = "A workout cannot contain more than 50 exercises.")
    private List<@NotNull(message = "Workout exercise cannot be null.") @Valid WorkoutExerciseRequest> exercises;
}
