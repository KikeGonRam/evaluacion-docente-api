package mx.utvt.evaluaciondocente.service.impl;

import lombok.RequiredArgsConstructor;
import mx.utvt.evaluaciondocente.dto.EstudianteDTO;
import mx.utvt.evaluaciondocente.entity.Estudiante;
import mx.utvt.evaluaciondocente.mapper.EstudianteMapper;
import mx.utvt.evaluaciondocente.repository.EstudianteRepository;
import mx.utvt.evaluaciondocente.service.EstudianteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl implements EstudianteService {

    private static final Logger log = LogManager.getLogger(EstudianteServiceImpl.class);

    private final EstudianteRepository estudianteRepository;
    private final EstudianteMapper estudianteMapper;

    @Override
    public List<EstudianteDTO> obtenerTodos() {
        log.info("Consultando todos los estudiantes");
        return estudianteRepository.findAll()
                .stream()
                .map(estudianteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EstudianteDTO obtenerPorId(Long id) {
        log.info("Consultando estudiante con ID: {}", id);
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + id));
        return estudianteMapper.toDTO(estudiante);
    }

    @Override
    public EstudianteDTO guardar(EstudianteDTO dto) {
        log.info("Guardando nuevo estudiante: {}", dto.nombre());
        Estudiante estudiante = estudianteMapper.toEntity(dto);
        Estudiante saved = estudianteRepository.save(estudiante);
        return estudianteMapper.toDTO(saved);
    }

    @Override
    public EstudianteDTO actualizar(Long id, EstudianteDTO dto) {
        log.info("Actualizando estudiante con ID: {}", id);
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + id));

        estudiante.setNombre(dto.nombre());
        estudiante.setMatricula(dto.matricula());
        estudiante.setEmail(dto.email());

        Estudiante updated = estudianteRepository.save(estudiante);
        return estudianteMapper.toDTO(updated);
    }

    @Override
    public void eliminar(Long id) {
        log.warn("Eliminando estudiante con ID: {}", id);
        if (!estudianteRepository.existsById(id)) {
            throw new RuntimeException("Estudiante no encontrado con ID: " + id);
        }
        estudianteRepository.deleteById(id);
    }
}

