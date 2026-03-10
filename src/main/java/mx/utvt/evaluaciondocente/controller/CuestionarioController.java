package mx.utvt.evaluaciondocente.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.utvt.evaluaciondocente.dto.CuestionarioDTO;
import mx.utvt.evaluaciondocente.dto.CuestionarioRequestDTO;
import mx.utvt.evaluaciondocente.service.CuestionarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuestionarios")
@RequiredArgsConstructor
public class CuestionarioController {

    private final CuestionarioService cuestionarioService;

    @GetMapping
    public ResponseEntity<List<CuestionarioDTO>> obtenerTodos() {
        return ResponseEntity.ok(cuestionarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuestionarioDTO> obtenerPorId(
            @PathVariable
            Long id) {
        return ResponseEntity.ok(cuestionarioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CuestionarioDTO> guardar(@Valid @RequestBody CuestionarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cuestionarioService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuestionarioDTO> actualizar(
            @PathVariable
            Long id,
            @Valid @RequestBody CuestionarioRequestDTO dto) {
        return ResponseEntity.ok(cuestionarioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable
            Long id) {
        cuestionarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
