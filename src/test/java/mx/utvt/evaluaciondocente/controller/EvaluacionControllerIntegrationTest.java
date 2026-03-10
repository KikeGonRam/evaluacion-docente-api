package mx.utvt.evaluaciondocente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.utvt.evaluaciondocente.config.TestConfig;
import mx.utvt.evaluaciondocente.dto.EvaluacionDTO;
import mx.utvt.evaluaciondocente.entity.Cuestionario;
import mx.utvt.evaluaciondocente.entity.Docente;
import mx.utvt.evaluaciondocente.entity.Estudiante;
import mx.utvt.evaluaciondocente.repository.CuestionarioRepository;
import mx.utvt.evaluaciondocente.repository.DocenteRepository;
import mx.utvt.evaluaciondocente.repository.EstudianteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(TestConfig.class)
@Transactional
class EvaluacionControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private CuestionarioRepository cuestionarioRepository;

    private Long estudianteId;
    private Long docenteId;
    private Long cuestionarioId;

    @BeforeEach
    void setUp() {
        // Configurar MockMvc
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        // Crear estudiante
        Estudiante estudiante = Estudiante.builder()
                .nombre("Luis González Ramírez")
                .matricula("21030001")
                .email("luis.gonzalez@utvt.edu.mx")
                .build();
        estudiante = estudianteRepository.save(estudiante);
        estudianteId = estudiante.getId();

        // Crear docente
        Docente docente = Docente.builder()
                .nombre("Jorge Almeida Montiel")
                .materia("Desarrollo Web Profesional")
                .email("jorge.almeida@utvt.edu.mx")
                .build();
        docente = docenteRepository.save(docente);
        docenteId = docente.getId();

        // Crear cuestionario
        List<String> preguntas = Arrays.asList(
                "¿El docente explica con claridad los temas?",
                "¿El docente fomenta la participación en clase?",
                "¿El docente cumple con el programa de la materia?"
        );
        Cuestionario cuestionario = Cuestionario.builder()
                .titulo("Cuestionario de Desempeño Académico 2025")
                .preguntas(preguntas)
                .build();
        cuestionario = cuestionarioRepository.save(cuestionario);
        cuestionarioId = cuestionario.getId();
    }

    @Test
    void crearEvaluacion_datosValidos_retorna201() throws Exception {
        List<Integer> respuestas = Arrays.asList(5, 4, 5);

        EvaluacionDTO evaluacion = new EvaluacionDTO(
                null,
                estudianteId,
                docenteId,
                cuestionarioId,
                respuestas,
                "Excelente docente, explica muy bien los temas.",
                null,
                null
        );

        mockMvc.perform(post("/api/evaluaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluacion)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.evaluadorId").value(estudianteId))
                .andExpect(jsonPath("$.evaluadoId").value(docenteId))
                .andExpect(jsonPath("$.cuestionarioId").value(cuestionarioId))
                .andExpect(jsonPath("$.comentarioGeneral").value("Excelente docente, explica muy bien los temas."))
                .andExpect(jsonPath("$.respuestas").isArray())
                .andExpect(jsonPath("$.respuestas.length()").value(3));
    }

    @Test
    void crearEvaluacion_sinComentario_retorna400() throws Exception {
        List<Integer> respuestas = Arrays.asList(5, 4, 5);

        EvaluacionDTO evaluacion = new EvaluacionDTO(
                null,
                estudianteId,
                docenteId,
                cuestionarioId,
                respuestas,
                "",
                null,
                null
        );

        mockMvc.perform(post("/api/evaluaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluacion)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearEvaluacion_menosDeTresRespuestas_retorna400() throws Exception {
        List<Integer> respuestas = Arrays.asList(5, 4);

        EvaluacionDTO evaluacion = new EvaluacionDTO(
                null,
                estudianteId,
                docenteId,
                cuestionarioId,
                respuestas,
                "Excelente docente, explica muy bien los temas.",
                null,
                null
        );

        mockMvc.perform(post("/api/evaluaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluacion)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearEvaluacion_estudianteNoExiste_retorna500() throws Exception {
        List<Integer> respuestas = Arrays.asList(5, 4, 5);

        EvaluacionDTO evaluacion = new EvaluacionDTO(
                null,
                999L,
                docenteId,
                cuestionarioId,
                respuestas,
                "Excelente docente, explica muy bien los temas.",
                null,
                null
        );

        mockMvc.perform(post("/api/evaluaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluacion)))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearEvaluacion_docenteNoExiste_retorna500() throws Exception {
        List<Integer> respuestas = Arrays.asList(5, 4, 5);

        EvaluacionDTO evaluacion = new EvaluacionDTO(
                null,
                estudianteId,
                999L,
                cuestionarioId,
                respuestas,
                "Excelente docente, explica muy bien los temas.",
                null,
                null
        );

        mockMvc.perform(post("/api/evaluaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluacion)))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearEvaluacion_calculaPuntajeFinal() throws Exception {
        List<Integer> respuestas = Arrays.asList(5, 4, 5);

        EvaluacionDTO evaluacion = new EvaluacionDTO(
                null,
                estudianteId,
                docenteId,
                cuestionarioId,
                respuestas,
                "Excelente docente, explica muy bien los temas.",
                null,
                null
        );

        mockMvc.perform(post("/api/evaluaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluacion)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.puntajeFinal").isNotEmpty())
                .andExpect(jsonPath("$.puntajeFinal").isNumber())
                .andExpect(jsonPath("$.fechaEvaluacion").isNotEmpty());
    }

    @Test
    void obtenerTodasEvaluaciones_retorna200() throws Exception {
        mockMvc.perform(get("/api/evaluaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void obtenerEvaluacionPorId_existente_retorna200() throws Exception {
        // Primero crear una evaluación
        List<Integer> respuestas = Arrays.asList(5, 4, 5);

        EvaluacionDTO evaluacion = new EvaluacionDTO(
                null,
                estudianteId,
                docenteId,
                cuestionarioId,
                respuestas,
                "Excelente docente, explica muy bien los temas.",
                null,
                null
        );

        String responseString = mockMvc.perform(post("/api/evaluaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluacion)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        EvaluacionDTO creada = objectMapper.readValue(responseString, EvaluacionDTO.class);

        // Ahora obtener por ID
        mockMvc.perform(get("/api/evaluaciones/" + creada.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(creada.id()))
                .andExpect(jsonPath("$.evaluadorId").value(estudianteId))
                .andExpect(jsonPath("$.evaluadoId").value(docenteId))
                .andExpect(jsonPath("$.comentarioGeneral").value("Excelente docente, explica muy bien los temas."));
    }

    @Test
    void obtenerEvaluacionPorId_noExistente_retorna404() throws Exception {
        mockMvc.perform(get("/api/evaluaciones/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarEvaluacion_existente_retorna204() throws Exception {
        // Crear evaluación
        List<Integer> respuestas = Arrays.asList(5, 4, 5);

        EvaluacionDTO evaluacion = new EvaluacionDTO(
                null,
                estudianteId,
                docenteId,
                cuestionarioId,
                respuestas,
                "Excelente docente, explica muy bien los temas.",
                null,
                null
        );

        String responseString = mockMvc.perform(post("/api/evaluaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluacion)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        EvaluacionDTO creada = objectMapper.readValue(responseString, EvaluacionDTO.class);

        // Eliminar
        mockMvc.perform(delete("/api/evaluaciones/" + creada.id()))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarEvaluacion_noExistente_retorna404() throws Exception {
        mockMvc.perform(delete("/api/evaluaciones/999"))
                .andExpect(status().isNotFound());
    }
}
