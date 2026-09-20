package com.hbpnrl.apexion.service;

import com.hbpnrl.apexion.dto.response.MuscleGroupResponse;
import java.util.List;

public interface MuscleGroupService {

    List<MuscleGroupResponse> getAllMuscleGroups();
    List<MuscleGroupResponse> getPrimaryMuscleGroups();
}