package mx.utvt.evaluaciondocente.service;

import mx.utvt.evaluaciondocente.dto.EvaluacionDTO;
import mx.utvt.evaluaciondocente.dto.EvaluacionRequestDTO;
import java.util.List;

public interface EvaluacionService {

    List<EvaluacionDTO> obtenerTodos();

    EvaluacionDTO obtenerPorId(Long id);

    EvaluacionDTO guardar(EvaluacionRequestDTO dto);

    void eliminar(Long id);
}
