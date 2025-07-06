package com.zenzap.zenzap.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "productos")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "precio")
    private Double price;

    @Column(name = "url_imagen")
    private String urlimagen;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "creado_en")
    private OffsetDateTime createdAt;

    @Column(name = "actualizado_en")
    private OffsetDateTime updatedAt;


    public void setId(Long id) {
        this.id = id;
    }
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }




}

