package com.hbpnrl.apexion.service;

import com.hbpnrl.apexion.dto.response.ExerciseResponse;
import java.util.List;

public interface ExerciseService {

    List<ExerciseResponse> getAllExercises();
    ExerciseResponse getExerciseById(Integer id);
    List<ExerciseResponse> getRandomExercises(int limit);
}