package com.hbpnrl.apexion.dto.request;

import com.hbpnrl.apexion.entity.User.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Gender gender;
    private LocalDate birthDate;
}