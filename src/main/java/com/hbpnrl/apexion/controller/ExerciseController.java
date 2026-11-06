package com.hbpnrl.apexion.controller;

import com.hbpnrl.apexion.dto.response.ExerciseResponse;
import com.hbpnrl.apexion.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    // GET http://localhost:8080/api/v1/exercises
    @GetMapping
    public ResponseEntity<List<ExerciseResponse>> getAllExercises() {
        return ResponseEntity.ok(exerciseService.getAllExercises());
    }

    // GET http://localhost:8080/api/v1/exercises/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponse> getExerciseById(@PathVariable Integer id) {
        return ResponseEntity.ok(exerciseService.getExerciseById(id));
    }

    // GET http://localhost:8080/api/v1/exercises/random?limit=3
    @GetMapping("/random")
    public ResponseEntity<List<ExerciseResponse>> getRandomExercises(
            @RequestParam(defaultValue = "3") int limit) {
        return ResponseEntity.ok(exerciseService.getRandomExercises(limit));
    }

}
