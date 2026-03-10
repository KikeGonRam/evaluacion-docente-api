package mx.utvt.evaluaciondocente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DocenteRequestDTO(
    @NotBlank(message = "El nombre es obligatorio") String nombre,
    @NotBlank(message = "La materia es obligatoria") String materia,
    @Email(message = "Email inválido") @NotBlank String email
) {}

