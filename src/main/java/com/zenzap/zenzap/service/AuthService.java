package com.zenzap.zenzap.service;

import com.zenzap.zenzap.controller.dto.UserRegisterRequest;
import com.zenzap.zenzap.controller.dto.UserUpdateRequest;
import com.zenzap.zenzap.entity.User;
import com.zenzap.zenzap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean validateLogin(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    public void sendRecoveryEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("Sending email to: " + user.getEmailAddress());
        } else {
            System.out.println("No user with email: " + email);
        }
    }

    public void resetPassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(newPassword);
            user.setResetToken(null);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Token inválido.");
        }
    }

    public void createUser(UserRegisterRequest request) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date birthday = formatter.parse(request.getBirthday());

            String hashedPassword = passwordEncoder.encode(request.getPassword());

            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmailAddress(request.getEmail());
            user.setBirthday(birthday);
            user.setPassword(hashedPassword);
            user.setIsActive("Y");
            user.setCreateIn(new Date().toString());
            user.setTypeUser("user");

            userRepository.save(user);

            System.out.println("Usuario creado: " + user.getEmailAddress());
        } catch (Exception e) {
            throw new RuntimeException("Error al crear usuario: " + e.getMessage());
        }
    }

    public void updateUser(Long id, UserUpdateRequest request) {
        try {
            Optional<User> userOptional = userRepository.findById(id);

            if (userOptional.isEmpty()) {
                throw new RuntimeException("Usuario no encontrado.");
            }

            User user = userOptional.get();

            user.setUsername(request.getUsername());
            user.setEmailAddress(request.getEmail());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date birthday = formatter.parse(request.getBirthday());
            user.setBirthday(birthday);
            if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
                String hashedPassword = passwordEncoder.encode(request.getNewPassword());
                user.setPassword(hashedPassword);
            }
            userRepository.save(user);
            System.out.println("Usuario actualizado: " + user.getEmailAddress());
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage());
        }
    }


}

