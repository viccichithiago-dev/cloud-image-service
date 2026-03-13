package com.thiago.imageprocessor.exception;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.thiago.imageprocessor.dto.ErrorResponse;

/**
 * Manejador global de excepciones. Convierte cualquier excepción en un
 * {@link ErrorResponse} uniforme con message, statusCode y timestamp.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        logger.error("RuntimeException capturada", ex);
        ErrorResponse error = new ErrorResponse();
        error.setMensaje(ex.getMessage());
        error.setTimestamp(LocalDateTime.now());

        if (ex instanceof InvalidImageFormatException) {
            error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        if (ex.getMessage().contains("El nombre de usuario ya está en uso")) {
            error.setStatusCode(409);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
        if (ex.getMessage().contains("Usuario no encontrado")) {
            error.setStatusCode(404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        if (ex.getMessage().contains("Contraseña incorrecta")) {
            error.setStatusCode(401);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        // caso por defecto 400
        error.setStatusCode(400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        logger.warn("Error de validación en petición", ex);
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "Datos inválidos";
        ErrorResponse error = new ErrorResponse();
        error.setMensaje(message);
        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        error.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex){
        logger.error("Error general", ex);
        ErrorResponse error = new ErrorResponse();
        error.setMensaje(ex.getMessage());
        error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(error);

    }
    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleImageNotFoundException(ImageNotFoundException ex)  {
        logger.error("ImageNotFoundException captured", ex);
        ErrorResponse error = new ErrorResponse();
        error.setMensaje(ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setStatusCode(HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}


