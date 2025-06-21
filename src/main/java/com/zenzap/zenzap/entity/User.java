package com.zenzap.zenzap.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String username;

    @Column(name = "correo_electronico")
    private String emailAddress;

    @Column(name = "fecha_nacimiento")
    private Date birthday;

    @Column(name = "hash_contrasena")
    private String password;

    @Column(name = "proveedor_oauth")
    private String provider;

    @Column(name = "id_proveedor_oauth")
    private String providerId;

    @Column(name = "esta_activo")
    private String isActive;

    @Column(name = "token_activacion")
    private String token;

    @Column(name = "expira_activacion")
    private String expireToken;

    @Column(name = "tipo_usuario")
    private String typeUser;

    @Column(name = "creado_en")
    private String createIn;
}
