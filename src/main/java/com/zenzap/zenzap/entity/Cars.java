package com.zenzap.zenzap.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "carritos")
@Data
public class Cars {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    @Column(name = "estado")
    private String status;

    @Column(name = "creado_en")
    private OffsetDateTime createdAt;
}

