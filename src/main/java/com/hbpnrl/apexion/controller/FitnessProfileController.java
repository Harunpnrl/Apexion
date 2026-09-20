package com.hbpnrl.apexion.controller;

import com.hbpnrl.apexion.dto.request.FitnessProfileRequest;
import com.hbpnrl.apexion.dto.response.FitnessProfileResponse;
import com.hbpnrl.apexion.entity.User;
import com.hbpnrl.apexion.service.FitnessProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FitnessProfileController {

    private final FitnessProfileService fitnessProfileService;

    @GetMapping("/me")
    public ResponseEntity<FitnessProfileResponse> getMyProfile(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(fitnessProfileService.getProfileByUserId(currentUser.getId()));
    }

    @PostMapping("/me")
    public ResponseEntity<FitnessProfileResponse> createMyProfile(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody FitnessProfileRequest request) {
        return ResponseEntity.ok(fitnessProfileService.createOrUpdateProfile(currentUser.getId(), request));
    }

    @PutMapping("/me")
    public ResponseEntity<FitnessProfileResponse> updateMyProfile(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody FitnessProfileRequest request) {
        return ResponseEntity.ok(fitnessProfileService.createOrUpdateProfile(currentUser.getId(), request));
    }
}