package mx.utvt.evaluaciondocente.service;

import mx.utvt.evaluaciondocente.dto.DocenteDTO;
import mx.utvt.evaluaciondocente.dto.DocenteRequestDTO;
import java.util.List;

public interface DocenteService {

    List<DocenteDTO> obtenerTodos();

    DocenteDTO obtenerPorId(Long id);

    DocenteDTO guardar(DocenteRequestDTO dto);

    DocenteDTO actualizar(Long id, DocenteRequestDTO dto);

    void eliminar(Long id);
}
