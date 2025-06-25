package com.zenzap.zenzap.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    @Column(name = "monto_total")
    private Double totalAmount;

    @Column(name = "direccion_envio")
    private String shippingAddress;

    @Column(name = "creado_en")
    private OffsetDateTime createdAt;
}

