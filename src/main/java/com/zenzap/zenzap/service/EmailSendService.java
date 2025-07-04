package com.zenzap.zenzap.service;

import com.zenzap.zenzap.entity.User;
import com.zenzap.zenzap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmailSendService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void sendRecoveryEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // ✅ 1. Generar token único
            String token = UUID.randomUUID().toString();

            // ✅ 2. Guardar token en base de datos (ejemplo: campo user.resetToken)
            user.setResetToken(token);
            userRepository.save(user);

            // ✅ 3. Construir link de recuperación
            String resetLink = "http://tu-frontend.com/reset-password?token=" + token;

            // ✅ 4. Enviar correo real
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmailAddress());
            message.setSubject("Recupera tu contraseña");
            message.setText("Haz clic en el siguiente enlace para restablecer tu contraseña: " + resetLink);

            mailSender.send(message);

            System.out.println("Correo de recuperación enviado a: " + user.getEmailAddress());

        } else {
            System.out.println("No existe usuario con ese email: " + email);
        }
    }
}

