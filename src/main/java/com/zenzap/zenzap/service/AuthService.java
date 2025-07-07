package com.zenzap.zenzap.service;

import com.zenzap.zenzap.controller.dto.UserProfileResponse;
import com.zenzap.zenzap.controller.dto.UserRegisterRequest;
import com.zenzap.zenzap.controller.dto.UserUpdateRequest;
import com.zenzap.zenzap.entity.User;
import com.zenzap.zenzap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Optional<User> validateLogin(String username, String password) {
        Optional<User> user = userRepository.findByEmailAddress(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        } else {
            return Optional.empty();
        }
    }


    public void sendRecoveryEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmailAddress(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setExpireToken(new Date(System.currentTimeMillis() + 3600 * 1000));
            userRepository.save(user);
            String resetLink = "http://localhost:5500/src/pagina_inicio_login/pagina_recuperar_pw.html?token=" + token;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmailAddress());
            message.setSubject("Recupera tu contraseña");
            message.setText("Haz clic en el siguiente enlace para restablecer tu contraseña:\n" + resetLink);
            mailSender.send(message);
            System.out.println("✅ Correo enviado a: " + user.getEmailAddress());
        } else {
            System.out.println("❌ No existe usuario con correo: " + email);
        }
    }


    public void resetPassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getExpireToken().before(new Date())) {
                throw new RuntimeException("Token expirado");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            user.setExpireToken(null);
            userRepository.save(user);
            System.out.println("✅ Contraseña actualizada para usuario: " + user.getEmailAddress());
        } else {
            throw new RuntimeException("Token inválido");
        }
    }


    public void createUser(UserRegisterRequest request) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = formatter.parse(request.getBirthday());
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmailAddress(request.getEmail());
            user.setBirthday(birthday);
            user.setPassword(hashedPassword);
            user.setIsActive("true");
            user.setCreateIn(new Date().toString());
            user.setTypeUser("USER");
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = sdf.parse(request.getBirthday());
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

    public UserProfileResponse getUserProfile(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        User user = optionalUser.get();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String birthday = user.getBirthday() != null ? sdf.format(user.getBirthday()) : null;
        return new UserProfileResponse(
                user.getUsername(),
                user.getEmailAddress(),
                birthday
        );
    }
}

