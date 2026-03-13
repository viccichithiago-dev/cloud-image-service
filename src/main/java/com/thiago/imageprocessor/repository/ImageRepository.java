package com.thiago.imageprocessor.repository;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thiago.imageprocessor.model.Image;
import com.thiago.imageprocessor.model.User;
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    // Metodo para encontrar una imagen por su usuario
    Page<Image> findByUsuario(User usuario,Pageable pageable);
    // Metodo para encontrar una imagen por ID y Usuario
    Optional<Image> findByIdAndUsuario(Long id, User usuario);
    // Metodo para buscar una imagen por su ID
    Optional<Image> findById(Long id);
    // Metodo para buscar por Status
    Page<Image> findByUsuarioIdAndStatus(Long usuarioId, String status,Pageable pageable);
    // Lista por Paginacion
    Page<Image> findAllByUsuario(User usuario, Pageable pageable);
    // Metodo para eliminar una imagen por su ID y Usuario
    void deleteByIdAndUsuario(Long id, User usuario);
    // Metodo para buscar por nombre original
    Page<Image> findByUsuarioIdAndOriginalNombreArchivoContainingIgnoreCase(Long userId, String nombre, Pageable pageable);
    // Metodo para get image
}
