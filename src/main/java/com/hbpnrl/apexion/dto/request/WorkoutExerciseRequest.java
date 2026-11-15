package com.hbpnrl.apexion.dto.request;

import lombok.Data;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class WorkoutExerciseRequest {

    @NotNull(message = "Exercise ID cannot be null.")
    @Positive(message = "Exercise ID must be positive.")
    private Integer exerciseId;

    @Min(value = 0, message = "Sort order cannot be negative.")
    @Max(value = 49, message = "Sort order cannot exceed 49.")
    private Integer sortOrder;

    @NotNull(message = "Sets cannot be null.")
    @Min(value = 1, message = "Sets must be at least 1.")
    @Max(value = 10, message = "Sets cannot exceed 10.")
    private Integer sets;

    @NotNull(message = "Reps cannot be null.")
    @Min(value = 1, message = "Reps must be at least 1.")
    @Max(value = 100, message = "Reps cannot exceed 100.")
    private Integer reps;

    @NotNull(message = "Weight cannot be null.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Weight cannot be negative.")
    @DecimalMax(value = "500.0", inclusive = true, message = "Weight cannot exceed 500 kg.")
    @Digits(integer = 3, fraction = 2, message = "Weight can have at most 2 decimal places.")
    private Double weight;
}
