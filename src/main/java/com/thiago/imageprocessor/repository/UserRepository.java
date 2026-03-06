package com.thiago.imageprocessor.repository;
import com.thiago.imageprocessor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Método para encontrar un usuario por su nombre de usuario
    Optional<User> findByUsername(String username);
    // Método para verificar si un usuario con un nombre de usuario específico ya existe
    boolean existsByUsername(String username);
}
