package mx.utvt.evaluaciondocente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EstudianteDTO(
    Long id,
    @NotBlank(message = "El nombre es obligatorio") String nombre,
    @NotBlank(message = "La matrícula es obligatoria") String matricula,
    @Email(message = "Email inválido") @NotBlank String email
) {}
