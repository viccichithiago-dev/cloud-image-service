package com.thiago.imageprocessor.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.*;
import java.time.LocalDateTime;
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
    @Size(min=6, max=20, message="Error la contraseña debe tener entre 6 y 20 caracteres")
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
