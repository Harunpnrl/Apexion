package com.hbpnrl.apexion.controller;

import com.hbpnrl.apexion.dto.request.WorkoutRequest;
import com.hbpnrl.apexion.dto.response.WorkoutResponse;
import com.hbpnrl.apexion.entity.User;
import com.hbpnrl.apexion.service.WorkoutService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workouts")
@RequiredArgsConstructor
@Validated
public class WorkoutController {

    private final WorkoutService workoutService;

    @GetMapping("/my-workouts")
    public ResponseEntity<List<WorkoutResponse>> getMyWorkouts(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<WorkoutResponse> myWorkouts = workoutService.getUserWorkouts(currentUser.getId());
        return ResponseEntity.ok(myWorkouts);
    }

    @PostMapping
    public ResponseEntity<String> saveWorkout(@Valid @RequestBody WorkoutRequest request, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        workoutService.createWorkout(request, currentUser.getId());
        return ResponseEntity.ok("Workout saved successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@Positive(message = "Workout ID must be positive.") @PathVariable Integer id,
                                               Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        workoutService.deleteWorkout(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
