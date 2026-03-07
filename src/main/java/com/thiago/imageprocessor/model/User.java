package com.thiago.imageprocessor.model;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Long id;
    @NotBlank(message="Error el usuario no puede estar vacio")
    @Size(min=3, max=20, message="Error el usuario debe tener entre 3 y 20 caracteres")
    @Pattern(regexp="^[a-zA-Z0-9]+$", message="Error el usuario solo puede contener letras y numeros")
    @Column(nullable = false, unique = true)
    private String username;
    @NotBlank(message="Error la contraseña no puede estar vacia")
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    @Column()
    private Role role;
    @Column()
    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = Role.USER; // Asignar rol USER por defecto
    }
}
