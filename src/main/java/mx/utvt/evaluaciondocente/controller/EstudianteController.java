package mx.utvt.evaluaciondocente.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.utvt.evaluaciondocente.dto.EstudianteDTO;
import mx.utvt.evaluaciondocente.service.EstudianteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService estudianteService;

    @GetMapping
    public ResponseEntity<List<EstudianteDTO>> obtenerTodos() {
        return ResponseEntity.ok(estudianteService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteDTO> obtenerPorId(
            @PathVariable
            Long id) {
        return ResponseEntity.ok(estudianteService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EstudianteDTO> guardar(@Valid @RequestBody EstudianteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estudianteService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteDTO> actualizar(
            @PathVariable
            Long id,
            @Valid @RequestBody EstudianteDTO dto) {
        return ResponseEntity.ok(estudianteService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable
            Long id) {
        estudianteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
