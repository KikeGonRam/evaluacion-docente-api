package mx.utvt.evaluaciondocente.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.utvt.evaluaciondocente.dto.EvaluacionDTO;
import mx.utvt.evaluaciondocente.dto.EvaluacionRequestDTO;
import mx.utvt.evaluaciondocente.service.EvaluacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
@RequiredArgsConstructor
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    @GetMapping
    public ResponseEntity<List<EvaluacionDTO>> obtenerTodos() {
        return ResponseEntity.ok(evaluacionService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionDTO> obtenerPorId(
            @PathVariable
            Long id) {
        return ResponseEntity.ok(evaluacionService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EvaluacionDTO> guardar(@Valid @RequestBody EvaluacionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evaluacionService.guardar(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable
            Long id) {
        evaluacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
