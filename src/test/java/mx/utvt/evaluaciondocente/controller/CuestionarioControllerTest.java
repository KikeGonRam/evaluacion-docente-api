package mx.utvt.evaluaciondocente.controller;

import mx.utvt.evaluaciondocente.dto.CuestionarioDTO;
import mx.utvt.evaluaciondocente.dto.CuestionarioRequestDTO;
import mx.utvt.evaluaciondocente.service.CuestionarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuestionarioControllerTest {

    @Mock
    private CuestionarioService cuestionarioService;

    @InjectMocks
    private CuestionarioController cuestionarioController;

    private CuestionarioDTO cuestionarioDTO;
    private List<CuestionarioDTO> cuestionarios;

    @BeforeEach
    void setUp() {
        List<String> preguntas1 = Arrays.asList("¿El docente explica?", "¿Es puntual?", "¿Resuelve dudas?");
        cuestionarioDTO = new CuestionarioDTO(1L, "Evaluación Desempeño", preguntas1);

        List<String> preguntas2 = Arrays.asList("¿Preparado?", "¿Domina?", "¿Promueve?", "¿Recursos?");
        CuestionarioDTO cuestionarioDTO2 = new CuestionarioDTO(2L, "Evaluación Avanzada", preguntas2);
        cuestionarios = Arrays.asList(cuestionarioDTO, cuestionarioDTO2);
    }

    @Test
    void testObtenerTodosCuestionarios() {
        when(cuestionarioService.obtenerTodos()).thenReturn(cuestionarios);
        var resultado = cuestionarioController.obtenerTodos();
        assertNotNull(resultado);
        assertEquals(2, resultado.getBody().size());
        verify(cuestionarioService, times(1)).obtenerTodos();
    }

    @Test
    void testObtenerTodosCuestionariosVacio() {
        when(cuestionarioService.obtenerTodos()).thenReturn(Arrays.asList());
        var resultado = cuestionarioController.obtenerTodos();
        assertNotNull(resultado);
        assertEquals(0, resultado.getBody().size());
        verify(cuestionarioService, times(1)).obtenerTodos();
    }

    @Test
    void testObtenerCuestionarioPorId() {
        when(cuestionarioService.obtenerPorId(1L)).thenReturn(cuestionarioDTO);
        var resultado = cuestionarioController.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getBody().id());
        assertEquals(3, resultado.getBody().preguntas().size());
        verify(cuestionarioService, times(1)).obtenerPorId(1L);
    }

    @Test
    void testObtenerCuestionarioPorIdNoEncontrado() {
        when(cuestionarioService.obtenerPorId(999L)).thenThrow(new RuntimeException("No encontrado"));
        assertThrows(RuntimeException.class, () -> cuestionarioController.obtenerPorId(999L));
        verify(cuestionarioService, times(1)).obtenerPorId(999L);
    }

    @Test
    void testCrearCuestionario() {
        List<String> preguntas = Arrays.asList("P1", "P2", "P3");
        CuestionarioRequestDTO nuevo = new CuestionarioRequestDTO("Nuevo", preguntas);
        CuestionarioDTO creado = new CuestionarioDTO(3L, "Nuevo", preguntas);
        when(cuestionarioService.guardar(any(CuestionarioRequestDTO.class))).thenReturn(creado);
        var resultado = cuestionarioController.guardar(nuevo);
        assertEquals(3L, resultado.getBody().id());
        assertEquals(201, resultado.getStatusCode().value());
        verify(cuestionarioService, times(1)).guardar(any(CuestionarioRequestDTO.class));
    }

    @Test
    void testActualizarCuestionario() {
        List<String> nuevas = Arrays.asList("NP1", "NP2", "NP3");
        CuestionarioRequestDTO actualizado = new CuestionarioRequestDTO("Actualizado", nuevas);
        CuestionarioDTO respuesta = new CuestionarioDTO(1L, "Actualizado", nuevas);
        when(cuestionarioService.actualizar(1L, actualizado)).thenReturn(respuesta);
        var resultado = cuestionarioController.actualizar(1L, actualizado);
        assertEquals("Actualizado", resultado.getBody().titulo());
        verify(cuestionarioService, times(1)).actualizar(1L, actualizado);
    }

    @Test
    void testActualizarCuestionarioNoEncontrado() {
        List<String> preguntas = Arrays.asList("P1", "P2", "P3");
        CuestionarioRequestDTO actualizado = new CuestionarioRequestDTO("T", preguntas);
        when(cuestionarioService.actualizar(999L, actualizado))
                .thenThrow(new RuntimeException("No encontrado"));
        assertThrows(RuntimeException.class, () -> cuestionarioController.actualizar(999L, actualizado));
        verify(cuestionarioService, times(1)).actualizar(999L, actualizado);
    }

    @Test
    void testEliminarCuestionario() {
        doNothing().when(cuestionarioService).eliminar(1L);
        var resultado = cuestionarioController.eliminar(1L);
        assertEquals(204, resultado.getStatusCode().value());
        verify(cuestionarioService, times(1)).eliminar(1L);
    }

    @Test
    void testEliminarCuestionarioNoEncontrado() {
        doThrow(new RuntimeException("No encontrado")).when(cuestionarioService).eliminar(999L);
        assertThrows(RuntimeException.class, () -> cuestionarioController.eliminar(999L));
        verify(cuestionarioService, times(1)).eliminar(999L);
    }

    @Test
    void testCrearCuestionarioConMuchasPreguntas() {
        List<String> muchas = Arrays.asList("P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8", "P9", "P10");
        CuestionarioRequestDTO nuevo = new CuestionarioRequestDTO("Completo", muchas);
        CuestionarioDTO creado = new CuestionarioDTO(4L, "Completo", muchas);
        when(cuestionarioService.guardar(any(CuestionarioRequestDTO.class))).thenReturn(creado);
        var resultado = cuestionarioController.guardar(nuevo);
        assertEquals(10, resultado.getBody().preguntas().size());
        verify(cuestionarioService, times(1)).guardar(any(CuestionarioRequestDTO.class));
    }

    @Test
    void testObtenerMultiplesCuestionarios() {
        List<CuestionarioDTO> lista = Arrays.asList(
                new CuestionarioDTO(1L, "C1", Arrays.asList("P1", "P2", "P3")),
                new CuestionarioDTO(2L, "C2", Arrays.asList("P1", "P2", "P3")),
                new CuestionarioDTO(3L, "C3", Arrays.asList("P1", "P2", "P3")),
                new CuestionarioDTO(4L, "C4", Arrays.asList("P1", "P2", "P3", "P4"))
        );
        when(cuestionarioService.obtenerTodos()).thenReturn(lista);
        var resultado = cuestionarioController.obtenerTodos();
        assertEquals(4, resultado.getBody().size());
        verify(cuestionarioService, times(1)).obtenerTodos();
    }

    @Test
    void testCuestionarioConPreguntasMinimas() {
        List<String> minimas = Arrays.asList("P1", "P2", "P3");
        CuestionarioDTO dto = new CuestionarioDTO(1L, "Minimo", minimas);
        assertEquals(3, dto.preguntas().size());
    }

    @Test
    void testCuestionarioVerificaTitulo() {
        assertEquals("Evaluación Desempeño", cuestionarioDTO.titulo());
        assertTrue(cuestionarioDTO.titulo().length() > 0);
    }
}

