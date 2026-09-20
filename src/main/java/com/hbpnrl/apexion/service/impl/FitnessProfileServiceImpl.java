package com.hbpnrl.apexion.service.impl;

import com.hbpnrl.apexion.dto.request.FitnessProfileRequest;
import com.hbpnrl.apexion.dto.response.FitnessProfileResponse;
import com.hbpnrl.apexion.entity.FitnessProfile;
import com.hbpnrl.apexion.entity.User;
import com.hbpnrl.apexion.repository.FitnessProfileRepository;
import com.hbpnrl.apexion.repository.UserRepository;
import com.hbpnrl.apexion.service.FitnessProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FitnessProfileServiceImpl implements FitnessProfileService {

    private final FitnessProfileRepository fitnessProfileRepository;
    private final UserRepository userRepository;

    @Override
    public FitnessProfileResponse createOrUpdateProfile(Integer userId, FitnessProfileRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        FitnessProfile profile = fitnessProfileRepository.findByUserId(userId)
                .orElse(new FitnessProfile());

        profile.setUser(user);
        profile.setHeightCm(BigDecimal.valueOf(request.getHeightCm()));
        profile.setWeightKg(BigDecimal.valueOf(request.getWeightKg()));
        profile.setFitnessGoal(request.getFitnessGoal());
        profile.setActivityLevel(request.getActivityLevel());

        FitnessProfile savedProfile = fitnessProfileRepository.save(profile);

        return mapToResponse(savedProfile);
    }

    @Override
    public FitnessProfileResponse getProfileByUserId(Integer userId) {
        FitnessProfile profile = fitnessProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No fitness profile was found for this user!"));

        return mapToResponse(profile);
    }

    private FitnessProfileResponse mapToResponse(FitnessProfile profile) {
        return FitnessProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .heightCm(profile.getHeightCm())
                .weightKg(profile.getWeightKg())
                .fitnessGoal(profile.getFitnessGoal().name())
                .activityLevel(profile.getActivityLevel().name())
                .build();
    }
}