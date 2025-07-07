package com.zenzap.zenzap.controller;

import com.zenzap.zenzap.controller.dto.LoginResponse;
import com.zenzap.zenzap.controller.dto.UserProfileResponse;
import com.zenzap.zenzap.controller.dto.UserRegisterRequest;
import com.zenzap.zenzap.controller.dto.UserUpdateRequest;
import com.zenzap.zenzap.entity.User;
import com.zenzap.zenzap.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<User> userOptional = authService.validateLogin(username, password);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("este es el id devuelto del login: " + user.getId());
            LoginResponse response = new LoginResponse("Login exitoso", user.getId(), user.getTypeUser());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }


    @PostMapping("/recover-password")
    public ResponseEntity<String> recoverPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        authService.sendRecoveryEmail(email);
        return ResponseEntity.ok("Recovery email sent if the address exists.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Contraseña actualizada.");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest request,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Las contraseñas no coinciden.");
        }
        authService.createUser(request);
        return ResponseEntity.ok("Usuario creado exitosamente.");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @Valid @RequestBody UserUpdateRequest request,
                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        // Validar contraseña solo si se va a actualizar
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                return ResponseEntity.badRequest().body("Las contraseñas nuevas no coinciden.");
            }
        }
        authService.updateUser(id, request);
        return ResponseEntity.ok("Usuario actualizado correctamente.");
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        System.out.println("este es el id del usuario: " + id);
        try {
            UserProfileResponse profile = authService.getUserProfile(id);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}

