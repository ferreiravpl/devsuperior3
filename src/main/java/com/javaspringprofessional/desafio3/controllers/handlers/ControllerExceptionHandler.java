package com.javaspringprofessional.desafio3.controllers.handlers;

import com.javaspringprofessional.desafio3.dto.CustomErrorDTO;
import com.javaspringprofessional.desafio3.dto.ValidationErrorDTO;
import com.javaspringprofessional.desafio3.services.exceptions.DatabaseException;
import com.javaspringprofessional.desafio3.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

import static com.javaspringprofessional.desafio3.util.Constants.INVALID_DATA;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorDTO> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomErrorDTO err = createCustomErrorDTO(e, request, status);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<CustomErrorDTO> database(DatabaseException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomErrorDTO err = createCustomErrorDTO(e, request, status);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorDTO> methodArgumentNotValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationErrorDTO err = createValidationErrorDTO(request, status);

        e.getBindingResult().getFieldErrors()
                .forEach(fieldError -> err.addError(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.status(status).body(err);
    }

    private static CustomErrorDTO createCustomErrorDTO(RuntimeException e, HttpServletRequest request, HttpStatus status) {
        return new CustomErrorDTO(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
    }

    private static ValidationErrorDTO createValidationErrorDTO(HttpServletRequest request, HttpStatus status) {
        return new ValidationErrorDTO(Instant.now(), status.value(), INVALID_DATA, request.getRequestURI());
    }
}
