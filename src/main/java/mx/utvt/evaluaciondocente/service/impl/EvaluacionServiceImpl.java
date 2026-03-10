package mx.utvt.evaluaciondocente.service.impl;

import lombok.RequiredArgsConstructor;
import mx.utvt.evaluaciondocente.dto.EvaluacionDTO;
import mx.utvt.evaluaciondocente.dto.EvaluacionRequestDTO;
import mx.utvt.evaluaciondocente.entity.Evaluacion;
import mx.utvt.evaluaciondocente.entity.Estudiante;
import mx.utvt.evaluaciondocente.entity.Docente;
import mx.utvt.evaluaciondocente.entity.Cuestionario;
import mx.utvt.evaluaciondocente.repository.EvaluacionRepository;
import mx.utvt.evaluaciondocente.repository.EstudianteRepository;
import mx.utvt.evaluaciondocente.repository.DocenteRepository;
import mx.utvt.evaluaciondocente.repository.CuestionarioRepository;
import mx.utvt.evaluaciondocente.service.EvaluacionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluacionServiceImpl implements EvaluacionService {

    private static final Logger log = LogManager.getLogger(EvaluacionServiceImpl.class);

    private final EvaluacionRepository evaluacionRepository;
    private final EstudianteRepository estudianteRepository;
    private final DocenteRepository docenteRepository;
    private final CuestionarioRepository cuestionarioRepository;

    @Override
    public List<EvaluacionDTO> obtenerTodos() {
        log.info("Consultando todas las evaluaciones");
        return evaluacionRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluacionDTO obtenerPorId(Long id) {
        log.info("Consultando evaluación con ID: {}", id);
        Evaluacion evaluacion = evaluacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada con ID: " + id));
        return toDTO(evaluacion);
    }

    @Override
    public EvaluacionDTO guardar(EvaluacionRequestDTO dto) {
        log.info("Guardando nueva evaluación");

        Estudiante estudiante = estudianteRepository.findById(dto.evaluadorId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado: " + dto.evaluadorId()));

        Docente docente = docenteRepository.findById(dto.evaluadoId())
                .orElseThrow(() -> new RuntimeException("Docente no encontrado: " + dto.evaluadoId()));

        Cuestionario cuestionario = cuestionarioRepository.findById(dto.cuestionarioId())
                .orElseThrow(() -> new RuntimeException("Cuestionario no encontrado: " + dto.cuestionarioId()));

        Evaluacion evaluacion = Evaluacion.builder()
                .estudiante(estudiante)
                .docente(docente)
                .cuestionario(cuestionario)
                .respuestas(dto.respuestas())
                .comentarioGeneral(dto.comentarioGeneral())
                .build();

        Evaluacion saved = evaluacionRepository.save(evaluacion);
        log.info("Evaluación guardada con puntaje final: {}", saved.getPuntajeFinal());
        return toDTO(saved);
    }

    @Override
    public void eliminar(Long id) {
        log.warn("Eliminando evaluación con ID: {}", id);
        if (!evaluacionRepository.existsById(id)) {
            throw new RuntimeException("Evaluación no encontrada con ID: " + id);
        }
        evaluacionRepository.deleteById(id);
    }

    private EvaluacionDTO toDTO(Evaluacion evaluacion) {
        return new EvaluacionDTO(
                evaluacion.getId(),
                evaluacion.getEstudiante().getId(),
                evaluacion.getDocente().getId(),
                evaluacion.getCuestionario().getId(),
                evaluacion.getRespuestas(),
                evaluacion.getComentarioGeneral(),
                evaluacion.getPuntajeFinal(),
                evaluacion.getFechaEvaluacion()
        );
    }
}
