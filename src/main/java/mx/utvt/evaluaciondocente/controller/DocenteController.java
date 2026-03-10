package mx.utvt.evaluaciondocente.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.utvt.evaluaciondocente.dto.DocenteDTO;
import mx.utvt.evaluaciondocente.dto.DocenteRequestDTO;
import mx.utvt.evaluaciondocente.service.DocenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/docentes")
@RequiredArgsConstructor
public class DocenteController {

    private final DocenteService docenteService;

    @GetMapping
    public ResponseEntity<List<DocenteDTO>> obtenerTodos() {
        return ResponseEntity.ok(docenteService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocenteDTO> obtenerPorId(
            @PathVariable
            Long id) {
        return ResponseEntity.ok(docenteService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<DocenteDTO> guardar(@Valid @RequestBody DocenteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(docenteService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocenteDTO> actualizar(
            @PathVariable
            Long id,
            @Valid @RequestBody DocenteRequestDTO dto) {
        return ResponseEntity.ok(docenteService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable
            Long id) {
        docenteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
