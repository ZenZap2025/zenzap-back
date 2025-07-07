package com.zenzap.zenzap.service;

import com.zenzap.zenzap.controller.dto.PurchaseRequest;
import com.zenzap.zenzap.entity.Product;
import com.zenzap.zenzap.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ProductRepository productRepository;

    public void sendPurchaseConfirmation(String email, String clientName, String address, List<PurchaseRequest> products) {
        StringBuilder message = new StringBuilder();
        message.append("Hola ").append(clientName).append(",\n\n");
        message.append("✅ ¡Gracias por tu compra en Zenzap Shop! 🧘\n\n");
        message.append("📦 Tu pedido será enviado a:\n").append(address).append("\n\n");
        message.append("🧾 Detalles del pedido:\n");

        for (PurchaseRequest product : products) {
            productRepository.findById(product.getProductId()).ifPresentOrElse(prod -> {
                message.append("- ").append(prod.getName())
                        .append(" | Cantidad: ").append(product.getQuantity()).append("\n");
            }, () -> {
                message.append("- Producto no encontrado (ID: ")
                        .append(product.getProductId())
                        .append(")\n");
            });
        }

        message.append("\nSi tienes alguna pregunta, no dudes en contactarnos.\n\n");
        message.append("🧘 ¡Que tengas un excelente día!\n\nZenzap Shop");

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject("Confirmación de tu compra - Zenzap Shop");
        mail.setText(message.toString());

        mailSender.send(mail);
    }
}
