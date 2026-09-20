package com.hbpnrl.apexion.service;

import com.hbpnrl.apexion.dto.request.FitnessProfileRequest;
import com.hbpnrl.apexion.dto.response.FitnessProfileResponse;

public interface FitnessProfileService {

    FitnessProfileResponse createOrUpdateProfile(Integer userId, FitnessProfileRequest request);
    FitnessProfileResponse getProfileByUserId(Integer userId);
}