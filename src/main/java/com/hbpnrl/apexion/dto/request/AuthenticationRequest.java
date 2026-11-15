package com.hbpnrl.apexion.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Please enter a valid email address.")
    private String email;

    @NotBlank(message = "Password cannot be blank.")
    private String password;
}
