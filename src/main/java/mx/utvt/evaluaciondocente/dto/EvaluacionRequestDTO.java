package mx.utvt.evaluaciondocente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EvaluacionRequestDTO(
    @NotNull Long evaluadorId,
    @NotNull Long evaluadoId,
    @NotNull Long cuestionarioId,
    @Size(min = 3, message = "Mínimo 3 respuestas") List<Integer> respuestas,
    @NotBlank(message = "El comentario es obligatorio") String comentarioGeneral
) {}

