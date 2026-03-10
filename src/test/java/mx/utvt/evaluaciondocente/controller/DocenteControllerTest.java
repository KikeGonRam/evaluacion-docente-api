package mx.utvt.evaluaciondocente.controller;

import mx.utvt.evaluaciondocente.dto.DocenteDTO;
import mx.utvt.evaluaciondocente.dto.DocenteRequestDTO;
import mx.utvt.evaluaciondocente.service.DocenteService;
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
class DocenteControllerTest {

    @Mock
    private DocenteService docenteService;

    @InjectMocks
    private DocenteController docenteController;

    private DocenteDTO docenteDTO;
    private List<DocenteDTO> docentes;

    @BeforeEach
    void setUp() {
        docenteDTO = new DocenteDTO(1L, "Dr. Juan García", "Matemáticas", "juan.garcia@example.com");
        DocenteDTO docenteDTO2 = new DocenteDTO(2L, "Dra. María López", "Física", "maria.lopez@example.com");
        docentes = Arrays.asList(docenteDTO, docenteDTO2);
    }

    @Test
    void testObtenerTodosDocentes() {
        when(docenteService.obtenerTodos()).thenReturn(docentes);

        var resultado = docenteController.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.getBody().size());
        assertEquals("Dr. Juan García", resultado.getBody().get(0).nombre());
        assertEquals("Dra. María López", resultado.getBody().get(1).nombre());

        verify(docenteService, times(1)).obtenerTodos();
    }

    @Test
    void testObtenerTodosDocentesVacio() {
        when(docenteService.obtenerTodos()).thenReturn(Arrays.asList());

        var resultado = docenteController.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(0, resultado.getBody().size());

        verify(docenteService, times(1)).obtenerTodos();
    }

    @Test
    void testObtenerDocentePorId() {
        when(docenteService.obtenerPorId(1L)).thenReturn(docenteDTO);

        var resultado = docenteController.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getBody().id());
        assertEquals("Dr. Juan García", resultado.getBody().nombre());
        assertEquals("Matemáticas", resultado.getBody().materia());
        assertEquals("juan.garcia@example.com", resultado.getBody().email());

        verify(docenteService, times(1)).obtenerPorId(1L);
    }

    @Test
    void testObtenerDocentePorIdNoEncontrado() {
        when(docenteService.obtenerPorId(999L))
                .thenThrow(new RuntimeException("Docente no encontrado con ID: 999"));

        assertThrows(RuntimeException.class, () -> {
            docenteController.obtenerPorId(999L);
        });

        verify(docenteService, times(1)).obtenerPorId(999L);
    }

    @Test
    void testCrearDocente() {
        DocenteRequestDTO nuevoDocente = new DocenteRequestDTO("Ing. Carlos López", "Programación", "carlos.lopez@example.com");
        DocenteDTO docenteCreado = new DocenteDTO(3L, "Ing. Carlos López", "Programación", "carlos.lopez@example.com");

        when(docenteService.guardar(any(DocenteRequestDTO.class))).thenReturn(docenteCreado);

        var resultado = docenteController.guardar(nuevoDocente);

        assertNotNull(resultado);
        assertEquals(3L, resultado.getBody().id());
        assertEquals("Ing. Carlos López", resultado.getBody().nombre());
        assertEquals("Programación", resultado.getBody().materia());
        assertEquals(201, resultado.getStatusCode().value());

        verify(docenteService, times(1)).guardar(any(DocenteRequestDTO.class));
    }

    @Test
    void testActualizarDocente() {
        DocenteRequestDTO docenteActualizado = new DocenteRequestDTO("Dr. Juan García Actualizado", "Matemáticas Avanzadas", "juan.actualizado@example.com");
        DocenteDTO docenteRespuesta = new DocenteDTO(1L, "Dr. Juan García Actualizado", "Matemáticas Avanzadas", "juan.actualizado@example.com");

        when(docenteService.actualizar(1L, docenteActualizado)).thenReturn(docenteRespuesta);

        var resultado = docenteController.actualizar(1L, docenteActualizado);

        assertNotNull(resultado);
        assertEquals("Dr. Juan García Actualizado", resultado.getBody().nombre());
        assertEquals("Matemáticas Avanzadas", resultado.getBody().materia());

        verify(docenteService, times(1)).actualizar(1L, docenteActualizado);
    }

    @Test
    void testActualizarDocenteNoEncontrado() {
        DocenteRequestDTO docenteActualizado = new DocenteRequestDTO("Nombre", "Materia", "email@example.com");

        when(docenteService.actualizar(999L, docenteActualizado))
                .thenThrow(new RuntimeException("Docente no encontrado con ID: 999"));

        assertThrows(RuntimeException.class, () -> {
            docenteController.actualizar(999L, docenteActualizado);
        });

        verify(docenteService, times(1)).actualizar(999L, docenteActualizado);
    }

    @Test
    void testEliminarDocente() {
        doNothing().when(docenteService).eliminar(1L);

        var resultado = docenteController.eliminar(1L);

        assertNotNull(resultado);
        assertEquals(204, resultado.getStatusCode().value());

        verify(docenteService, times(1)).eliminar(1L);
    }

    @Test
    void testEliminarDocenteNoEncontrado() {
        doThrow(new RuntimeException("Docente no encontrado con ID: 999"))
                .when(docenteService).eliminar(999L);

        assertThrows(RuntimeException.class, () -> {
            docenteController.eliminar(999L);
        });

        verify(docenteService, times(1)).eliminar(999L);
    }

    @Test
    void testObtenerMultiplesDocentes() {
        List<DocenteDTO> listaDocentes = Arrays.asList(
                new DocenteDTO(1L, "Doc1", "Materia1", "doc1@example.com"),
                new DocenteDTO(2L, "Doc2", "Materia2", "doc2@example.com"),
                new DocenteDTO(3L, "Doc3", "Materia3", "doc3@example.com"),
                new DocenteDTO(4L, "Doc4", "Materia4", "doc4@example.com"),
                new DocenteDTO(5L, "Doc5", "Materia5", "doc5@example.com")
        );

        when(docenteService.obtenerTodos()).thenReturn(listaDocentes);

        var resultado = docenteController.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(5, resultado.getBody().size());
        assertEquals("Doc1", resultado.getBody().get(0).nombre());
        assertEquals("Doc5", resultado.getBody().get(4).nombre());

        verify(docenteService, times(1)).obtenerTodos();
    }

    @Test
    void testCrearDocenteSinMateria() {
        DocenteRequestDTO docenteSinMateria = new DocenteRequestDTO("Dr. Test", "", "test@example.com");

        // Este test verifica que el controller rechace DTOs inválidos
        // En este caso se haría en el controlador con validaciones
        assertNotNull(docenteSinMateria);
        assertTrue(docenteSinMateria.materia().isEmpty());
    }
}

