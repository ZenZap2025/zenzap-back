package com.zenzap.zenzap.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "items_pedido")
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Product product;

    @Column(name = "cantidad")
    private Integer quantity;

    @Column(name = "precio_unitario")
    private Double unitPrice;
}

