package com.hbpnrl.apexion.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {

    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String gender;
    private LocalDate birthDate;
    private LocalDateTime createdAt;
    private boolean hasFitnessProfile;
}