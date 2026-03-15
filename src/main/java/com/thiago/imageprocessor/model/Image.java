package com.thiago.imageprocessor.model;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "images")
@Data // Genera getters, setters, toString, etc.
@Builder // <--- ESTO ES LO QUE TE FALTA
@AllArgsConstructor // Necesario para @Builder
@NoArgsConstructor  // Necesario para JPA
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombreArchivo;
    
    @Column()
    private String originalNombreArchivo;
    
    @Column()
    private String url;
    
    @Column()
    private Long size;
    
    @Column()
    private int width;
    
    @Column()
    private int height;
    
    @Column()
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
    @Enumerated(EnumType.STRING)
    private ImageStatus status;
    @ElementCollection
    @CollectionTable(
        name = "image_transformed_urls", 
        joinColumns = @JoinColumn(name = "image_id")
    )
    @Column(name = "url")
    private List<String> transformedUrls;
    
    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = ImageStatus.ESPERANDO;
        }
    }
    public Image(String nombreArchivo, String url, User usuario) {
        this.nombreArchivo = nombreArchivo;
        this.url = url;
        this.usuario = usuario;
        this.status = ImageStatus.ESPERANDO;
    }
}
