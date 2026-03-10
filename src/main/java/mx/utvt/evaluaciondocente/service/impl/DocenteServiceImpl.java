package mx.utvt.evaluaciondocente.service.impl;

import lombok.RequiredArgsConstructor;
import mx.utvt.evaluaciondocente.dto.DocenteDTO;
import mx.utvt.evaluaciondocente.dto.DocenteRequestDTO;
import mx.utvt.evaluaciondocente.entity.Docente;
import mx.utvt.evaluaciondocente.mapper.DocenteMapper;
import mx.utvt.evaluaciondocente.repository.DocenteRepository;
import mx.utvt.evaluaciondocente.service.DocenteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocenteServiceImpl implements DocenteService {

    private static final Logger log = LogManager.getLogger(DocenteServiceImpl.class);

    private final DocenteRepository docenteRepository;
    private final DocenteMapper docenteMapper;

    @Override
    public List<DocenteDTO> obtenerTodos() {
        log.info("Consultando todos los docentes");
        return docenteRepository.findAll()
                .stream()
                .map(docenteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DocenteDTO obtenerPorId(Long id) {
        log.info("Consultando docente con ID: {}", id);
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado con ID: " + id));
        return docenteMapper.toDTO(docente);
    }

    @Override
    public DocenteDTO guardar(DocenteRequestDTO dto) {
        log.info("Guardando docente: {}", dto.nombre());
        Docente docente = new Docente();
        docente.setNombre(dto.nombre());
        docente.setMateria(dto.materia());
        docente.setEmail(dto.email());
        Docente saved = docenteRepository.save(docente);
        return docenteMapper.toDTO(saved);
    }

    @Override
    public DocenteDTO actualizar(Long id, DocenteRequestDTO dto) {
        log.info("Actualizando docente id: {}", id);
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado: " + id));

        docente.setNombre(dto.nombre());
        docente.setMateria(dto.materia());
        docente.setEmail(dto.email());

        Docente updated = docenteRepository.save(docente);
        return docenteMapper.toDTO(updated);
    }

    @Override
    public void eliminar(Long id) {
        log.warn("Eliminando docente con ID: {}", id);
        if (!docenteRepository.existsById(id)) {
            throw new RuntimeException("Docente no encontrado con ID: " + id);
        }
        docenteRepository.deleteById(id);
    }
}

