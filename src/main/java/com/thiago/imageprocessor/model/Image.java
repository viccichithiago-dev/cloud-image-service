package com.thiago.imageprocessor.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message="Error el nombre del archivo no puede estar vacio")
    @Column(nullable = false)
    private String nombrearchivo;
    
    @Column()
    @NotBlank(message="Error el nombre original del archivo no puede estar vacio")
    private String originalNombrearchivo;
    
    @Column()
    @NotBlank(message="Error la url no puede estar vacia")
    private String url;
    
    @Column()
    @Max(value=10485760, message="Error el tamaño del archivo no puede ser mayor a 10MB")
    private Long size;
    
    @Column()
    @Min(value=1, message="Error el ancho debe ser mayor a 0")
    private int width;
    
    @Column()
    @Min(value=1, message="Error la altura debe ser mayor a 0")
    private int height;
    
    @Column()
    @NotBlank(message="Error el tipo MIME no puede estar vacio")
    private String mimeType;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column()
    private LocalDateTime updatedAt;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User usuario;
    
    @Column()
    private ImageStatus status;
    
    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = ImageStatus.ESPERANDO;
        }
    }
    public Image() {
    }
    public Image(String nombrearchivo, String url, User usuario) {
        this.nombrearchivo = nombrearchivo;
        this.url = url;
        this.usuario = usuario;
    }
}
