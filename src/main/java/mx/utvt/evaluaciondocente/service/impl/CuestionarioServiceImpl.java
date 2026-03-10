package mx.utvt.evaluaciondocente.service.impl;

import lombok.RequiredArgsConstructor;
import mx.utvt.evaluaciondocente.dto.CuestionarioDTO;
import mx.utvt.evaluaciondocente.dto.CuestionarioRequestDTO;
import mx.utvt.evaluaciondocente.entity.Cuestionario;
import mx.utvt.evaluaciondocente.mapper.CuestionarioMapper;
import mx.utvt.evaluaciondocente.repository.CuestionarioRepository;
import mx.utvt.evaluaciondocente.service.CuestionarioService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuestionarioServiceImpl implements CuestionarioService {

    private static final Logger log = LogManager.getLogger(CuestionarioServiceImpl.class);

    private final CuestionarioRepository cuestionarioRepository;
    private final CuestionarioMapper cuestionarioMapper;

    @Override
    public List<CuestionarioDTO> obtenerTodos() {
        log.info("Consultando todos los cuestionarios");
        return cuestionarioRepository.findAll()
                .stream()
                .map(cuestionarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CuestionarioDTO obtenerPorId(Long id) {
        log.info("Consultando cuestionario con ID: {}", id);
        Cuestionario cuestionario = cuestionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuestionario no encontrado con ID: " + id));
        return cuestionarioMapper.toDTO(cuestionario);
    }

    @Override
    public CuestionarioDTO guardar(CuestionarioRequestDTO dto) {
        log.info("Guardando cuestionario: {}", dto.titulo());
        Cuestionario cuestionario = new Cuestionario();
        cuestionario.setTitulo(dto.titulo());
        cuestionario.setPreguntas(dto.preguntas());
        Cuestionario saved = cuestionarioRepository.save(cuestionario);
        return cuestionarioMapper.toDTO(saved);
    }

    @Override
    public CuestionarioDTO actualizar(Long id, CuestionarioRequestDTO dto) {
        log.info("Actualizando cuestionario con ID: {}", id);
        Cuestionario cuestionario = cuestionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuestionario no encontrado con ID: " + id));

        cuestionario.setTitulo(dto.titulo());
        cuestionario.setPreguntas(dto.preguntas());

        Cuestionario updated = cuestionarioRepository.save(cuestionario);
        return cuestionarioMapper.toDTO(updated);
    }

    @Override
    public void eliminar(Long id) {
        log.warn("Eliminando cuestionario con ID: {}", id);
        if (!cuestionarioRepository.existsById(id)) {
            throw new RuntimeException("Cuestionario no encontrado con ID: " + id);
        }
        cuestionarioRepository.deleteById(id);
    }
}
