package mx.utvt.evaluaciondocente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.utvt.evaluaciondocente.config.TestConfig;
import mx.utvt.evaluaciondocente.dto.DocenteDTO;
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
class DocenteControllerIntegrationTest {

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
    void crearDocente_retorna201() throws Exception {
        DocenteDTO docente = new DocenteDTO(
                null,
                "Jorge Almeida Montiel",
                "Desarrollo Web Profesional",
                "jorge.almeida@utvt.edu.mx"
        );

        mockMvc.perform(post("/api/docentes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(docente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nombre").value("Jorge Almeida Montiel"))
                .andExpect(jsonPath("$.materia").value("Desarrollo Web Profesional"))
                .andExpect(jsonPath("$.email").value("jorge.almeida@utvt.edu.mx"));
    }

    @Test
    void crearDocente_sinNombre_retorna400() throws Exception {
        DocenteDTO docente = new DocenteDTO(
                null,
                "",
                "Desarrollo Web Profesional",
                "jorge.almeida@utvt.edu.mx"
        );

        mockMvc.perform(post("/api/docentes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(docente)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearDocente_sinMateria_retorna400() throws Exception {
        DocenteDTO docente = new DocenteDTO(
                null,
                "Jorge Almeida Montiel",
                "",
                "jorge.almeida@utvt.edu.mx"
        );

        mockMvc.perform(post("/api/docentes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(docente)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearDocente_emailInvalido_retorna400() throws Exception {
        DocenteDTO docente = new DocenteDTO(
                null,
                "Jorge Almeida Montiel",
                "Desarrollo Web Profesional",
                "email-invalido-sin-arroba"
        );

        mockMvc.perform(post("/api/docentes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(docente)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerTodosDocentes_retorna200() throws Exception {
        mockMvc.perform(get("/api/docentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void obtenerDocentePorId_existente_retorna200() throws Exception {
        // Primero crear un docente
        DocenteDTO docente = new DocenteDTO(
                null,
                "Jorge Almeida Montiel",
                "Desarrollo Web Profesional",
                "jorge.almeida@utvt.edu.mx"
        );

        String responseString = mockMvc.perform(post("/api/docentes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(docente)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        DocenteDTO creado = objectMapper.readValue(responseString, DocenteDTO.class);

        // Ahora obtener por ID
        mockMvc.perform(get("/api/docentes/" + creado.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(creado.id()))
                .andExpect(jsonPath("$.nombre").value("Jorge Almeida Montiel"))
                .andExpect(jsonPath("$.materia").value("Desarrollo Web Profesional"));
    }

    @Test
    void obtenerDocentePorId_noExistente_retorna404() throws Exception {
        mockMvc.perform(get("/api/docentes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarDocente_datosValidos_retorna200() throws Exception {
        // Crear docente
        DocenteDTO docente = new DocenteDTO(
                null,
                "Jorge Almeida Montiel",
                "Desarrollo Web Profesional",
                "jorge.almeida@utvt.edu.mx"
        );

        String responseString = mockMvc.perform(post("/api/docentes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(docente)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        DocenteDTO creado = objectMapper.readValue(responseString, DocenteDTO.class);

        // Actualizar
        DocenteDTO actualizado = new DocenteDTO(
                creado.id(),
                "Jorge Almeida Montiel PhD",
                "Desarrollo Web Profesional Avanzado",
                "jorge.almeida.phd@utvt.edu.mx"
        );

        mockMvc.perform(put("/api/docentes/" + creado.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Jorge Almeida Montiel PhD"))
                .andExpect(jsonPath("$.materia").value("Desarrollo Web Profesional Avanzado"))
                .andExpect(jsonPath("$.email").value("jorge.almeida.phd@utvt.edu.mx"));
    }

    @Test
    void actualizarDocente_noExistente_retorna404() throws Exception {
        DocenteDTO docente = new DocenteDTO(
                999L,
                "Jorge Almeida Montiel",
                "Desarrollo Web Profesional",
                "jorge.almeida@utvt.edu.mx"
        );

        mockMvc.perform(put("/api/docentes/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(docente)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarDocente_existente_retorna204() throws Exception {
        // Crear docente
        DocenteDTO docente = new DocenteDTO(
                null,
                "Jorge Almeida Montiel",
                "Desarrollo Web Profesional",
                "jorge.almeida@utvt.edu.mx"
        );

        String responseString = mockMvc.perform(post("/api/docentes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(docente)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        DocenteDTO creado = objectMapper.readValue(responseString, DocenteDTO.class);

        // Eliminar
        mockMvc.perform(delete("/api/docentes/" + creado.id()))
                .andExpect(status().isNoContent());
    }
}
