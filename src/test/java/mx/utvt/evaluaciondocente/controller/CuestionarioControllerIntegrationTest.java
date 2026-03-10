package mx.utvt.evaluaciondocente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.utvt.evaluaciondocente.config.TestConfig;
import mx.utvt.evaluaciondocente.dto.CuestionarioDTO;
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
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(TestConfig.class)
@Transactional
class CuestionarioControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setupMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void crearCuestionario_retorna201() throws Exception {
        List<String> preguntas = Arrays.asList(
                "¿El docente explica con claridad los temas?",
                "¿El docente fomenta la participación en clase?",
                "¿El docente cumple con el programa de la materia?"
        );

        CuestionarioDTO cuestionario = new CuestionarioDTO(
                null,
                "Cuestionario de Desempeño Académico 2025",
                preguntas
        );

        mockMvc.perform(post("/api/cuestionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuestionario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.titulo").value("Cuestionario de Desempeño Académico 2025"))
                .andExpect(jsonPath("$.preguntas").isArray())
                .andExpect(jsonPath("$.preguntas.length()").value(3));
    }

    @Test
    void crearCuestionario_menosDeTresPreguntas_retorna400() throws Exception {
        List<String> preguntas = Arrays.asList(
                "¿El docente explica con claridad los temas?",
                "¿El docente fomenta la participación en clase?"
        );

        CuestionarioDTO cuestionario = new CuestionarioDTO(
                null,
                "Cuestionario de Desempeño Académico 2025",
                preguntas
        );

        mockMvc.perform(post("/api/cuestionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuestionario)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearCuestionario_sinTitulo_retorna400() throws Exception {
        List<String> preguntas = Arrays.asList(
                "¿El docente explica con claridad los temas?",
                "¿El docente fomenta la participación en clase?",
                "¿El docente cumple con el programa de la materia?"
        );

        CuestionarioDTO cuestionario = new CuestionarioDTO(
                null,
                "",
                preguntas
        );

        mockMvc.perform(post("/api/cuestionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuestionario)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearCuestionario_listaVacia_retorna400() throws Exception {
        CuestionarioDTO cuestionario = new CuestionarioDTO(
                null,
                "Cuestionario de Desempeño Académico 2025",
                Collections.emptyList()
        );

        mockMvc.perform(post("/api/cuestionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuestionario)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerTodosCuestionarios_retorna200() throws Exception {
        mockMvc.perform(get("/api/cuestionarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void obtenerCuestionarioPorId_existente_retorna200() throws Exception {
        // Primero crear un cuestionario
        List<String> preguntas = Arrays.asList(
                "¿El docente explica con claridad los temas?",
                "¿El docente fomenta la participación en clase?",
                "¿El docente cumple con el programa de la materia?"
        );

        CuestionarioDTO cuestionario = new CuestionarioDTO(
                null,
                "Cuestionario de Desempeño Académico 2025",
                preguntas
        );

        String responseString = mockMvc.perform(post("/api/cuestionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuestionario)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        CuestionarioDTO creado = objectMapper.readValue(responseString, CuestionarioDTO.class);

        // Ahora obtener por ID
        mockMvc.perform(get("/api/cuestionarios/" + creado.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(creado.id()))
                .andExpect(jsonPath("$.titulo").value("Cuestionario de Desempeño Académico 2025"))
                .andExpect(jsonPath("$.preguntas.length()").value(3));
    }

    @Test
    void obtenerCuestionarioPorId_noExistente_retorna404() throws Exception {
        mockMvc.perform(get("/api/cuestionarios/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarCuestionario_datosValidos_retorna200() throws Exception {
        // Crear cuestionario
        List<String> preguntas = Arrays.asList(
                "¿El docente explica con claridad los temas?",
                "¿El docente fomenta la participación en clase?",
                "¿El docente cumple con el programa de la materia?"
        );

        CuestionarioDTO cuestionario = new CuestionarioDTO(
                null,
                "Cuestionario de Desempeño Académico 2025",
                preguntas
        );

        String responseString = mockMvc.perform(post("/api/cuestionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuestionario)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        CuestionarioDTO creado = objectMapper.readValue(responseString, CuestionarioDTO.class);

        // Actualizar
        List<String> nuevasPreguntas = Arrays.asList(
                "¿El docente domina la materia que imparte?",
                "¿El docente responde dudas oportunamente?",
                "¿El docente utiliza materiales didácticos adecuados?",
                "¿El docente promueve el pensamiento crítico?"
        );

        CuestionarioDTO actualizado = new CuestionarioDTO(
                creado.id(),
                "Cuestionario de Desempeño Académico 2025 - Actualizado",
                nuevasPreguntas
        );

        mockMvc.perform(put("/api/cuestionarios/" + creado.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Cuestionario de Desempeño Académico 2025 - Actualizado"))
                .andExpect(jsonPath("$.preguntas.length()").value(4));
    }

    @Test
    void actualizarCuestionario_noExistente_retorna404() throws Exception {
        List<String> preguntas = Arrays.asList(
                "¿El docente explica con claridad los temas?",
                "¿El docente fomenta la participación en clase?",
                "¿El docente cumple con el programa de la materia?"
        );

        CuestionarioDTO cuestionario = new CuestionarioDTO(
                999L,
                "Cuestionario de Desempeño Académico 2025",
                preguntas
        );

        mockMvc.perform(put("/api/cuestionarios/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuestionario)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarCuestionario_existente_retorna204() throws Exception {
        // Crear cuestionario
        List<String> preguntas = Arrays.asList(
                "¿El docente explica con claridad los temas?",
                "¿El docente fomenta la participación en clase?",
                "¿El docente cumple con el programa de la materia?"
        );

        CuestionarioDTO cuestionario = new CuestionarioDTO(
                null,
                "Cuestionario de Desempeño Académico 2025",
                preguntas
        );

        String responseString = mockMvc.perform(post("/api/cuestionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuestionario)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        CuestionarioDTO creado = objectMapper.readValue(responseString, CuestionarioDTO.class);

        // Eliminar
        mockMvc.perform(delete("/api/cuestionarios/" + creado.id()))
                .andExpect(status().isNoContent());
    }
}
