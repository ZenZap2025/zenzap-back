package com.zenzap.zenzap.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterRequest {

    @NotBlank(message = "El nombre es obligatorio.")
    private String username;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "El correo debe ser válido.")
    private String email;

    @NotBlank(message = "La fecha de nacimiento es obligatoria.")
    private String birthday;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;

    @NotBlank(message = "La confirmación de contraseña es obligatoria.")
    private String confirmPassword;

    public UserRegisterRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

