package com.zenzap.zenzap.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "items_carrito")
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private Cars cars;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Product product;

    @Column(name = "cantidad")
    private Integer quantity;
}

