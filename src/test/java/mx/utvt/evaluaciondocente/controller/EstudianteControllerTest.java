package mx.utvt.evaluaciondocente.controller;

import mx.utvt.evaluaciondocente.dto.EstudianteDTO;
import mx.utvt.evaluaciondocente.service.EstudianteService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstudianteControllerTest {

    @Mock
    private EstudianteService estudianteService;

    @InjectMocks
    private EstudianteController estudianteController;

    private EstudianteDTO estudianteDTO;
    private List<EstudianteDTO> estudiantes;

    @BeforeEach
    void setUp() {
        estudianteDTO = new EstudianteDTO(1L, "Juan Pérez", "A00123456", "juan.perez@example.com");
        EstudianteDTO estudianteDTO2 = new EstudianteDTO(2L, "María García", "A00123457", "maria.garcia@example.com");
        estudiantes = Arrays.asList(estudianteDTO, estudianteDTO2);
    }

    @Test
    void testObtenerTodosEstudiantes() {
        when(estudianteService.obtenerTodos()).thenReturn(estudiantes);

        var resultado = estudianteController.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.getBody().size());
        assertEquals("Juan Pérez", resultado.getBody().get(0).nombre());

        verify(estudianteService, times(1)).obtenerTodos();
    }

    @Test
    void testObtenerTodosEstudiantesVacio() {
        when(estudianteService.obtenerTodos()).thenReturn(Arrays.asList());

        var resultado = estudianteController.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(0, resultado.getBody().size());

        verify(estudianteService, times(1)).obtenerTodos();
    }

    @Test
    void testObtenerEstudiantePorId() {
        when(estudianteService.obtenerPorId(1L)).thenReturn(estudianteDTO);

        var resultado = estudianteController.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getBody().id());
        assertEquals("Juan Pérez", resultado.getBody().nombre());
        assertEquals("A00123456", resultado.getBody().matricula());

        verify(estudianteService, times(1)).obtenerPorId(1L);
    }

    @Test
    void testObtenerEstudiantePorIdNoEncontrado() {
        when(estudianteService.obtenerPorId(999L))
                .thenThrow(new RuntimeException("Estudiante no encontrado con ID: 999"));

        assertThrows(RuntimeException.class, () -> {
            estudianteController.obtenerPorId(999L);
        });

        verify(estudianteService, times(1)).obtenerPorId(999L);
    }

    @Test
    void testCrearEstudiante() {
        EstudianteDTO nuevoEstudiante = new EstudianteDTO(null, "Carlos López", "A00123458", "carlos.lopez@example.com");
        EstudianteDTO estudianteCreado = new EstudianteDTO(3L, "Carlos López", "A00123458", "carlos.lopez@example.com");

        when(estudianteService.guardar(any(EstudianteDTO.class))).thenReturn(estudianteCreado);

        var resultado = estudianteController.guardar(nuevoEstudiante);

        assertNotNull(resultado);
        assertEquals(3L, resultado.getBody().id());
        assertEquals("Carlos López", resultado.getBody().nombre());
        assertEquals(201, resultado.getStatusCode().value());

        verify(estudianteService, times(1)).guardar(any(EstudianteDTO.class));
    }

    @Test
    void testActualizarEstudiante() {
        EstudianteDTO estudianteActualizado = new EstudianteDTO(1L, "Juan Pérez Actualizado", "A00123456", "juan.actualizado@example.com");

        when(estudianteService.actualizar(1L, estudianteActualizado)).thenReturn(estudianteActualizado);

        var resultado = estudianteController.actualizar(1L, estudianteActualizado);

        assertNotNull(resultado);
        assertEquals("Juan Pérez Actualizado", resultado.getBody().nombre());
        assertEquals("juan.actualizado@example.com", resultado.getBody().email());

        verify(estudianteService, times(1)).actualizar(1L, estudianteActualizado);
    }

    @Test
    void testActualizarEstudianteNoEncontrado() {
        EstudianteDTO estudianteActualizado = new EstudianteDTO(999L, "Nombre", "MAT", "email@example.com");

        when(estudianteService.actualizar(999L, estudianteActualizado))
                .thenThrow(new RuntimeException("Estudiante no encontrado con ID: 999"));

        assertThrows(RuntimeException.class, () -> {
            estudianteController.actualizar(999L, estudianteActualizado);
        });

        verify(estudianteService, times(1)).actualizar(999L, estudianteActualizado);
    }

    @Test
    void testEliminarEstudiante() {
        doNothing().when(estudianteService).eliminar(1L);

        var resultado = estudianteController.eliminar(1L);

        assertNotNull(resultado);
        assertEquals(204, resultado.getStatusCode().value());

        verify(estudianteService, times(1)).eliminar(1L);
    }

    @Test
    void testEliminarEstudianteNoEncontrado() {
        doThrow(new RuntimeException("Estudiante no encontrado con ID: 999"))
                .when(estudianteService).eliminar(999L);

        assertThrows(RuntimeException.class, () -> {
            estudianteController.eliminar(999L);
        });

        verify(estudianteService, times(1)).eliminar(999L);
    }

    @Test
    void testObtenerMultiplesEstudiantes() {
        List<EstudianteDTO> listaEstudiantes = Arrays.asList(
                new EstudianteDTO(1L, "Est1", "MAT001", "est1@example.com"),
                new EstudianteDTO(2L, "Est2", "MAT002", "est2@example.com"),
                new EstudianteDTO(3L, "Est3", "MAT003", "est3@example.com"),
                new EstudianteDTO(4L, "Est4", "MAT004", "est4@example.com")
        );

        when(estudianteService.obtenerTodos()).thenReturn(listaEstudiantes);

        var resultado = estudianteController.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(4, resultado.getBody().size());
        assertEquals("Est1", resultado.getBody().get(0).nombre());
        assertEquals("Est4", resultado.getBody().get(3).nombre());

        verify(estudianteService, times(1)).obtenerTodos();
    }

    @Test
    void testObtenerEstudianteVerificaDatos() {
        when(estudianteService.obtenerPorId(1L)).thenReturn(estudianteDTO);

        var resultado = estudianteController.obtenerPorId(1L);

        assertNotNull(resultado.getBody());
        assertEquals("juan.perez@example.com", resultado.getBody().email());
        assertEquals("A00123456", resultado.getBody().matricula());

        verify(estudianteService, times(1)).obtenerPorId(1L);
    }
}


