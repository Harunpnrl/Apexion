package com.hbpnrl.apexion.dto.request;

import com.hbpnrl.apexion.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "The firstname field cannot be left blank.")
    @Size(max = 50, message = "The firstname must be at most 50 characters.")
    private String firstName;

    @NotBlank(message = "The lastname field cannot be left blank.")
    @Size(max = 50, message = "The lastname must be at most 50 characters.")
    private String lastName;

    @NotBlank(message = "The e-mail field cannot be left blank.")
    @Email(message = "Please enter a valid e-mail.")
    private String email;

    @NotBlank(message = "The password field cannot be left blank.z")
    @Size(min = 6, message = "The password must be at least 6 characters long.")
    private String password;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phoneNumber;

    private User.Gender gender;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;
}