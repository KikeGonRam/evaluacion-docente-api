package mx.utvt.evaluaciondocente.controller;

import mx.utvt.evaluaciondocente.dto.EvaluacionDTO;
import mx.utvt.evaluaciondocente.dto.EvaluacionRequestDTO;
import mx.utvt.evaluaciondocente.service.EvaluacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluacionControllerTest {

    @Mock
    private EvaluacionService evaluacionService;

    @InjectMocks
    private EvaluacionController evaluacionController;

    private EvaluacionDTO evaluacionDTO;
    private List<EvaluacionDTO> evaluaciones;

    @BeforeEach
    void setUp() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Integer> respuestas = Arrays.asList(5, 4, 5);
        evaluacionDTO = new EvaluacionDTO(1L, 1L, 1L, 1L, respuestas, "Excelente desempeño", 4.67, ahora);

        List<Integer> respuestas2 = Arrays.asList(3, 4, 4, 5);
        EvaluacionDTO evaluacionDTO2 = new EvaluacionDTO(2L, 2L, 2L, 2L, respuestas2, "Muy buen profesor", 4.0, ahora);
        evaluaciones = Arrays.asList(evaluacionDTO, evaluacionDTO2);
    }

    @Test
    void testObtenerTodasEvaluaciones() {
        when(evaluacionService.obtenerTodos()).thenReturn(evaluaciones);
        var resultado = evaluacionController.obtenerTodos();
        assertNotNull(resultado);
        assertEquals(2, resultado.getBody().size());
        verify(evaluacionService, times(1)).obtenerTodos();
    }

    @Test
    void testObtenerTodasEvaluacionesVacio() {
        when(evaluacionService.obtenerTodos()).thenReturn(Arrays.asList());
        var resultado = evaluacionController.obtenerTodos();
        assertNotNull(resultado);
        assertEquals(0, resultado.getBody().size());
        verify(evaluacionService, times(1)).obtenerTodos();
    }

    @Test
    void testObtenerEvaluacionPorId() {
        when(evaluacionService.obtenerPorId(1L)).thenReturn(evaluacionDTO);
        var resultado = evaluacionController.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getBody().id());
        assertEquals(1L, resultado.getBody().evaluadorId());
        assertEquals(3, resultado.getBody().respuestas().size());
        verify(evaluacionService, times(1)).obtenerPorId(1L);
    }

    @Test
    void testObtenerEvaluacionPorIdNoEncontrada() {
        when(evaluacionService.obtenerPorId(999L))
                .thenThrow(new RuntimeException("No encontrada"));
        assertThrows(RuntimeException.class, () -> evaluacionController.obtenerPorId(999L));
        verify(evaluacionService, times(1)).obtenerPorId(999L);
    }

    @Test
    void testCrearEvaluacion() {
        List<Integer> respuestas = Arrays.asList(5, 4, 5);
        EvaluacionRequestDTO nueva = new EvaluacionRequestDTO(1L, 1L, 1L, respuestas, "Muy buen docente");
        LocalDateTime ahora = LocalDateTime.now();
        EvaluacionDTO creada = new EvaluacionDTO(3L, 1L, 1L, 1L, respuestas, "Muy buen docente", 4.67, ahora);
        when(evaluacionService.guardar(any(EvaluacionRequestDTO.class))).thenReturn(creada);
        var resultado = evaluacionController.guardar(nueva);
        assertNotNull(resultado);
        assertEquals(3L, resultado.getBody().id());
        assertEquals(4.67, resultado.getBody().puntajeFinal());
        assertEquals(201, resultado.getStatusCode().value());
        verify(evaluacionService, times(1)).guardar(any(EvaluacionRequestDTO.class));
    }

    @Test
    void testEliminarEvaluacion() {
        doNothing().when(evaluacionService).eliminar(1L);
        var resultado = evaluacionController.eliminar(1L);
        assertNotNull(resultado);
        assertEquals(204, resultado.getStatusCode().value());
        verify(evaluacionService, times(1)).eliminar(1L);
    }

    @Test
    void testEliminarEvaluacionNoEncontrada() {
        doThrow(new RuntimeException("No encontrada")).when(evaluacionService).eliminar(999L);
        assertThrows(RuntimeException.class, () -> evaluacionController.eliminar(999L));
        verify(evaluacionService, times(1)).eliminar(999L);
    }

    @Test
    void testCrearEvaluacionConPuntajePerfecto() {
        List<Integer> respuestas = Arrays.asList(5, 5, 5, 5);
        EvaluacionRequestDTO nueva = new EvaluacionRequestDTO(1L, 1L, 1L, respuestas, "Excelente");
        LocalDateTime ahora = LocalDateTime.now();
        EvaluacionDTO creada = new EvaluacionDTO(4L, 1L, 1L, 1L, respuestas, "Excelente", 5.0, ahora);
        when(evaluacionService.guardar(any(EvaluacionRequestDTO.class))).thenReturn(creada);
        var resultado = evaluacionController.guardar(nueva);
        assertEquals(5.0, resultado.getBody().puntajeFinal());
        verify(evaluacionService, times(1)).guardar(any(EvaluacionRequestDTO.class));
    }

    @Test
    void testCrearEvaluacionConPuntajeBajo() {
        List<Integer> respuestas = Arrays.asList(1, 1, 1);
        EvaluacionRequestDTO nueva = new EvaluacionRequestDTO(1L, 1L, 1L, respuestas, "Necesita mejorar");
        LocalDateTime ahora = LocalDateTime.now();
        EvaluacionDTO creada = new EvaluacionDTO(5L, 1L, 1L, 1L, respuestas, "Necesita mejorar", 1.0, ahora);
        when(evaluacionService.guardar(any(EvaluacionRequestDTO.class))).thenReturn(creada);
        var resultado = evaluacionController.guardar(nueva);
        assertEquals(1.0, resultado.getBody().puntajeFinal());
        verify(evaluacionService, times(1)).guardar(any(EvaluacionRequestDTO.class));
    }

    @Test
    void testObtenerMultiplesEvaluaciones() {
        List<EvaluacionDTO> lista = Arrays.asList(
                new EvaluacionDTO(1L, 1L, 1L, 1L, Arrays.asList(5, 4, 5), "C1", 4.67, LocalDateTime.now()),
                new EvaluacionDTO(2L, 2L, 2L, 2L, Arrays.asList(3, 4, 4), "C2", 3.67, LocalDateTime.now()),
                new EvaluacionDTO(3L, 3L, 3L, 3L, Arrays.asList(4, 4, 4), "C3", 4.0, LocalDateTime.now()),
                new EvaluacionDTO(4L, 4L, 4L, 4L, Arrays.asList(5, 5, 5, 5), "C4", 5.0, LocalDateTime.now()),
                new EvaluacionDTO(5L, 5L, 5L, 5L, Arrays.asList(2, 2, 2), "C5", 2.0, LocalDateTime.now())
        );
        when(evaluacionService.obtenerTodos()).thenReturn(lista);
        var resultado = evaluacionController.obtenerTodos();
        assertNotNull(resultado);
        assertEquals(5, resultado.getBody().size());
        assertEquals(4.67, resultado.getBody().get(0).puntajeFinal());
        assertEquals(2.0, resultado.getBody().get(4).puntajeFinal());
        verify(evaluacionService, times(1)).obtenerTodos();
    }

    @Test
    void testEvaluacionVerificaRespuestas() {
        List<Integer> respuestas = Arrays.asList(5, 4, 5);
        assertEquals(3, respuestas.size());
        assertTrue(respuestas.stream().allMatch(r -> r >= 1 && r <= 5));
    }

    @Test
    void testEvaluacionVerificaComentario() {
        assertNotNull(evaluacionDTO.comentarioGeneral());
        assertTrue(evaluacionDTO.comentarioGeneral().length() > 0);
        assertEquals("Excelente desempeño", evaluacionDTO.comentarioGeneral());
    }

    @Test
    void testEvaluacionVerificaPuntajeFinal() {
        assertNotNull(evaluacionDTO.puntajeFinal());
        assertTrue(evaluacionDTO.puntajeFinal() > 0);
        assertTrue(evaluacionDTO.puntajeFinal() <= 5);
    }

    @Test
    void testCrearEvaluacionConPuntajePromedio() {
        List<Integer> respuestas = Arrays.asList(3, 3, 3);
        EvaluacionRequestDTO nueva = new EvaluacionRequestDTO(1L, 1L, 1L, respuestas, "Normal");
        LocalDateTime ahora = LocalDateTime.now();
        EvaluacionDTO creada = new EvaluacionDTO(6L, 1L, 1L, 1L, respuestas, "Normal", 3.0, ahora);
        when(evaluacionService.guardar(any(EvaluacionRequestDTO.class))).thenReturn(creada);
        var resultado = evaluacionController.guardar(nueva);
        assertEquals(3.0, resultado.getBody().puntajeFinal());
        verify(evaluacionService, times(1)).guardar(any(EvaluacionRequestDTO.class));
    }
}

