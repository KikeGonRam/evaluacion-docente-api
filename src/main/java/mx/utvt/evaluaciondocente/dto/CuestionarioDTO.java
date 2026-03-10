package mx.utvt.evaluaciondocente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CuestionarioDTO(
        Long id,

        @NotBlank(message = "El título es requerido")
        String titulo,

        @Size(min = 3, message = "Debe contener al menos 3 preguntas")
        List<String> preguntas
) {
}

