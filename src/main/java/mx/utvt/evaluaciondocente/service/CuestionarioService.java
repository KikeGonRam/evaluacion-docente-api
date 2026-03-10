package mx.utvt.evaluaciondocente.service;

import mx.utvt.evaluaciondocente.dto.CuestionarioDTO;
import mx.utvt.evaluaciondocente.dto.CuestionarioRequestDTO;
import java.util.List;

public interface CuestionarioService {

    List<CuestionarioDTO> obtenerTodos();

    CuestionarioDTO obtenerPorId(Long id);

    CuestionarioDTO guardar(CuestionarioRequestDTO dto);

    CuestionarioDTO actualizar(Long id, CuestionarioRequestDTO dto);

    void eliminar(Long id);
}
