package com.thiago.imageprocessor.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thiago.imageprocessor.dto.AuthResponse;
import com.thiago.imageprocessor.dto.LoginRequest;
import com.thiago.imageprocessor.dto.RegisterRequest;
import com.thiago.imageprocessor.dto.UserResponse;
import com.thiago.imageprocessor.model.User;
import com.thiago.imageprocessor.service.JwtService;
import com.thiago.imageprocessor.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService,JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = userService.registerUsuario(registerRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = userService.loginUsuario(loginRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@RequestHeader("Authorization") String authHeader){
        // extraer toker del header
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);
        // Validar el token
        if(!jwtService.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = jwtService.extractUsername(token);
        User usuario = userService.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        UserResponse response = new UserResponse(usuario.getId(), usuario.getUsername());
        return ResponseEntity.ok(response);
    }
}
