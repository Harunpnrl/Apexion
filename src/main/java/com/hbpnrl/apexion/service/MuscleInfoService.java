package com.hbpnrl.apexion.service;

import com.hbpnrl.apexion.dto.response.MuscleInfoResponse;

public interface MuscleInfoService {

    MuscleInfoResponse getMuscleInfoByName(String muscleName);
}