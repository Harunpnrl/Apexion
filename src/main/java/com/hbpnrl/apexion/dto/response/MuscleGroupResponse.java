package com.hbpnrl.apexion.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MuscleGroupResponse {
    private Integer id;
    private String name;
    private Integer parentId;
    private String parentName;
}