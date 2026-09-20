package com.hbpnrl.apexion.service;

import com.hbpnrl.apexion.dto.request.WorkoutRequest;
import com.hbpnrl.apexion.dto.response.WorkoutResponse;
import java.util.List;

public interface WorkoutService {

    List<WorkoutResponse> getUserWorkouts(Integer userId);

    void createWorkout(WorkoutRequest request, Integer authenticatedUserId);

    void deleteWorkout(Integer workoutId, Integer userId);
}