package mx.utvt.evaluaciondocente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.utvt.evaluaciondocente.config.TestConfig;
import mx.utvt.evaluaciondocente.dto.EstudianteDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(TestConfig.class)
@Transactional
class EstudianteControllerIntegrationTest {

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
    void crearEstudiante_retorna201() throws Exception {
        EstudianteDTO estudiante = new EstudianteDTO(
                null,
                "Luis González Ramírez",
                "21030001",
                "luis.gonzalez@utvt.edu.mx"
        );

        mockMvc.perform(post("/api/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nombre").value("Luis González Ramírez"))
                .andExpect(jsonPath("$.matricula").value("21030001"))
                .andExpect(jsonPath("$.email").value("luis.gonzalez@utvt.edu.mx"));
    }

    @Test
    void crearEstudiante_sinNombre_retorna400() throws Exception {
        EstudianteDTO estudiante = new EstudianteDTO(
                null,
                "",
                "21030001",
                "luis.gonzalez@utvt.edu.mx"
        );

        mockMvc.perform(post("/api/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearEstudiante_sinMatricula_retorna400() throws Exception {
        EstudianteDTO estudiante = new EstudianteDTO(
                null,
                "Luis González Ramírez",
                "",
                "luis.gonzalez@utvt.edu.mx"
        );

        mockMvc.perform(post("/api/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearEstudiante_emailInvalido_retorna400() throws Exception {
        EstudianteDTO estudiante = new EstudianteDTO(
                null,
                "Luis González Ramírez",
                "21030001",
                "email-invalido"
        );

        mockMvc.perform(post("/api/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerTodosEstudiantes_retorna200() throws Exception {
        mockMvc.perform(get("/api/estudiantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void obtenerEstudiantePorId_existente_retorna200() throws Exception {
        // Primero crear un estudiante
        EstudianteDTO estudiante = new EstudianteDTO(
                null,
                "Luis González Ramírez",
                "21030001",
                "luis.gonzalez@utvt.edu.mx"
        );

        String responseString = mockMvc.perform(post("/api/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        EstudianteDTO creado = objectMapper.readValue(responseString, EstudianteDTO.class);

        // Ahora obtener por ID
        mockMvc.perform(get("/api/estudiantes/" + creado.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(creado.id()))
                .andExpect(jsonPath("$.nombre").value("Luis González Ramírez"))
                .andExpect(jsonPath("$.matricula").value("21030001"));
    }

    @Test
    void obtenerEstudiantePorId_noExistente_retorna404() throws Exception {
        mockMvc.perform(get("/api/estudiantes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarEstudiante_datosValidos_retorna200() throws Exception {
        // Crear estudiante
        EstudianteDTO estudiante = new EstudianteDTO(
                null,
                "Luis González Ramírez",
                "21030001",
                "luis.gonzalez@utvt.edu.mx"
        );

        String responseString = mockMvc.perform(post("/api/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        EstudianteDTO creado = objectMapper.readValue(responseString, EstudianteDTO.class);

        // Actualizar
        EstudianteDTO actualizado = new EstudianteDTO(
                creado.id(),
                "Luis González Ramírez Actualizado",
                "21030001",
                "luis.gonzalez.nuevo@utvt.edu.mx"
        );

        mockMvc.perform(put("/api/estudiantes/" + creado.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Luis González Ramírez Actualizado"))
                .andExpect(jsonPath("$.email").value("luis.gonzalez.nuevo@utvt.edu.mx"));
    }

    @Test
    void actualizarEstudiante_noExistente_retorna404() throws Exception {
        EstudianteDTO estudiante = new EstudianteDTO(
                999L,
                "Luis González Ramírez",
                "21030001",
                "luis.gonzalez@utvt.edu.mx"
        );

        mockMvc.perform(put("/api/estudiantes/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarEstudiante_existente_retorna204() throws Exception {
        // Crear estudiante
        EstudianteDTO estudiante = new EstudianteDTO(
                null,
                "Luis González Ramírez",
                "21030001",
                "luis.gonzalez@utvt.edu.mx"
        );

        String responseString = mockMvc.perform(post("/api/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        EstudianteDTO creado = objectMapper.readValue(responseString, EstudianteDTO.class);

        // Eliminar
        mockMvc.perform(delete("/api/estudiantes/" + creado.id()))
                .andExpect(status().isNoContent());
    }
}
