package mx.utvt.evaluaciondocente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CuestionarioRequestDTO(
    @NotBlank(message = "El título es obligatorio") String titulo,
    @Size(min = 3, message = "Mínimo 3 preguntas") List<String> preguntas
) {}

