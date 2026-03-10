package mx.utvt.evaluaciondocente.service;

import mx.utvt.evaluaciondocente.dto.EstudianteDTO;
import java.util.List;

public interface EstudianteService {

    List<EstudianteDTO> obtenerTodos();

    EstudianteDTO obtenerPorId(Long id);

    EstudianteDTO guardar(EstudianteDTO dto);

    EstudianteDTO actualizar(Long id, EstudianteDTO dto);

    void eliminar(Long id);
}

