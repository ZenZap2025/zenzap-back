package com.zenzap.zenzap.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "El nombre es obligatorio.")
    private String username;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "El correo debe ser válido.")
    private String email;

    @NotBlank(message = "La fecha de nacimiento es obligatoria.")
    private String birthday;

    private String newPassword;

    private String confirmNewPassword;
}
