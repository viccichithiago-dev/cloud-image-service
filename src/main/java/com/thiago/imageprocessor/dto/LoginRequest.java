package com.thiago.imageprocessor.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class LoginRequest {
    @NotBlank(message="Error el usuario no puede estar vacio")
    @Size(min=3, max=20, message="Error el usuario debe tener entre 3 y 20 caracteres")
    String username;
    @NotBlank(message="Error la contraseña no puede estar vacia")
    @Size(min=6, max=20, message="Error la contraseña debe tener entre 6 y 20 caracteres")
    String password;
}
