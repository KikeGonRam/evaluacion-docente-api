package mx.utvt.evaluaciondocente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DocenteDTO(
        Long id,

        @NotBlank(message = "El nombre es requerido")
        String nombre,

        @NotBlank(message = "La materia es requerida")
        String materia,

        @Email(message = "El email debe ser válido")
        @NotBlank(message = "El email es requerido")
        String email
) {
}

