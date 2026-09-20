package com.hbpnrl.apexion.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ExerciseResponse {
    private Integer id;
    private String name;
    private String description;
    private String difficulty;
    private String equipment;
    private String animationPath;
    private String thumbnailPath;
    private List<String> muscleGroups;
    private String instructions;
}