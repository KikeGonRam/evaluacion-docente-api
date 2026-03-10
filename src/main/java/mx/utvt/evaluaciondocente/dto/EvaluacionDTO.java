package mx.utvt.evaluaciondocente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public record EvaluacionDTO(
        Long id,

        @NotNull(message = "El ID del evaluador (estudiante) es requerido")
        Long evaluadorId,

        @NotNull(message = "El ID del evaluado (docente) es requerido")
        Long evaluadoId,

        @NotNull(message = "El ID del cuestionario es requerido")
        Long cuestionarioId,

        @NotNull(message = "Las respuestas son requeridas")
        @Size(min = 3, message = "Debe responder al menos 3 preguntas")
        List<Integer> respuestas,

        @NotBlank(message = "El comentario general es requerido")
        String comentarioGeneral,

        Double puntajeFinal,

        LocalDateTime fechaEvaluacion
) {
}

