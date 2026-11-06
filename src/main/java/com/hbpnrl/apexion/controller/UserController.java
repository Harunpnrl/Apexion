package com.hbpnrl.apexion.controller;

import com.hbpnrl.apexion.dto.request.UserUpdateRequest;
import com.hbpnrl.apexion.dto.response.ExerciseResponse;
import com.hbpnrl.apexion.dto.response.UserResponse;
import com.hbpnrl.apexion.entity.User;
import com.hbpnrl.apexion.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUserProfile(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.getUserProfile(currentUser));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUserProfile(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUserProfile(currentUser, request));
    }

    @PostMapping("/me/favorites/{exerciseId}")
    public ResponseEntity<String> addFavorite(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Integer exerciseId) {
        userService.addExerciseToFavorites(currentUser.getId(), exerciseId);
        return ResponseEntity.ok("Exercise successfully added to favorites.");
    }

    @DeleteMapping("/me/favorites/{exerciseId}")
    public ResponseEntity<String> removeFavorite(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Integer exerciseId) {
        userService.removeExerciseFromFavorites(currentUser.getId(), exerciseId);
        return ResponseEntity.ok("Exercise successfully removed from favorites.");
    }

    @GetMapping("/me/favorites")
    public ResponseEntity<List<ExerciseResponse>> getFavorites(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.getFavoriteExercises(currentUser.getId()));
    }
}
