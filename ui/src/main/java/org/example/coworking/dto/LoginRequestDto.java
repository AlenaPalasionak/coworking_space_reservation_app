package org.example.coworking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "Username cannot be empty.")
    @Pattern(regexp = "^[a-zA-Z0-9]{1,10}$", message = "Username must be alphanumeric and between 1 and 10 characters.")
    private String username;

    @NotBlank(message = "Password cannot be empty.")
    @Pattern(regexp = "^[a-zA-Z0-9]{1,10}$", message = "Password must be alphanumeric and between 1 and 10 characters.")
    private String password;

    @NotBlank(message = "Role cannot be empty.")
    @Pattern(regexp = "ADMIN|CUSTOMER", message = "Role must be either ADMIN or CUSTOMER.")
    private String role;
}
