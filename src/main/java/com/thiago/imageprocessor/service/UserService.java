package com.thiago.imageprocessor.service;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thiago.imageprocessor.dto.AuthResponse;
import com.thiago.imageprocessor.dto.LoginRequest;
import com.thiago.imageprocessor.dto.RegisterRequest;
import com.thiago.imageprocessor.model.User;
import com.thiago.imageprocessor.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import com.thiago.imageprocessor.model.Role;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    public AuthResponse registerUsuario(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        // Crear nuevo usuario
        var user = new com.thiago.imageprocessor.model.User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        // Generar token JWT
        String token = jwtService.generateToken(user.getUsername(), user.getId());
        return new AuthResponse(user.getId(), user.getUsername(), token);
    }
    public AuthResponse loginUsuario(LoginRequest loginRequest) {
        var user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        // Generar token JWT
        String token = jwtService.generateToken(user.getUsername(), user.getId());
        return new AuthResponse(user.getId(), user.getUsername(), token);
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    public User updateUser(Long id, String newUsername, String newPassword) {
        // Validar que el nuevo nombre de usuario no esté en uso por otro usuario
        if (newUsername != null && !newUsername.isBlank()) {
            var existingUser = userRepository.findByUsername(newUsername);
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                throw new RuntimeException("El nombre de usuario ya está en uso por otro usuario");
            }
        }
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (newUsername != null && !newUsername.isBlank()) {
            user.setUsername(newUsername);
        }
        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        return userRepository.save(user);
    }
    public void deleteUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        userRepository.delete(user);
    }
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
     @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
