package com.thiago.imageprocessor.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class RegisterRequest {
    @NotBlank(message="Error el usuario no puede estar vacio")
    @Size(min=3, max=20, message="Error el usuario debe tener entre 3 y 20 caracteres")
    @Pattern(regexp="^[a-zA-Z0-9]+$", message="Error el usuario solo puede contener letras y numeros")
    String username;
    @NotBlank(message="Error la contraseña no puede estar vacia")
    @Size(min=6, max=20, message="Error la contraseña debe tener entre 6 y 20 caracteres")
    String password;
}
    