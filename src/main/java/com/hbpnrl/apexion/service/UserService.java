package com.hbpnrl.apexion.service;

import com.hbpnrl.apexion.dto.request.UserUpdateRequest;
import com.hbpnrl.apexion.dto.response.UserResponse;
import com.hbpnrl.apexion.entity.User;

public interface UserService {

    UserResponse getUserProfile(User currentUser);

    UserResponse updateUserProfile(User currentUser, UserUpdateRequest request);

    void addExerciseToFavorites(Integer userId, Integer exerciseId);

    void removeExerciseFromFavorites(Integer userId, Integer exerciseId);

    java.util.List<com.hbpnrl.apexion.dto.response.ExerciseResponse> getFavoriteExercises(Integer userId);
}