package com.taskflow.unit;

import com.taskflow.repository.TaskRepository;
import com.taskflow.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// TODO MP-1/MP-2/MP-3: al escribir los cuerpos vas a necesitar estos imports (hoy comentados para que
//   el esqueleto compile sin "unused import"). Descoméntalos conforme los uses:
// import com.taskflow.dto.TaskRequest;
// import com.taskflow.exception.TaskNotFoundException;
// import com.taskflow.exception.TaskStateException;
// import com.taskflow.exception.TaskValidationException;
// import com.taskflow.model.Priority;
// import com.taskflow.model.Task;
// import com.taskflow.model.TaskStatus;
// import org.mockito.ArgumentCaptor;
// import java.util.Optional;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.Mockito.*;

/**
 * TaskServiceTest — ESQUELETO NUEVO, Mockito PURO (S3D1, MP-1/MP-2/MP-3, @Nested en MP-5).
 *
 * Por qué aquí NO arranca Spring: en unit/ probamos el service EN AISLAMIENTO. El colaborador real
 * (TaskRepository = proxy de Spring Data sobre una BD) se sustituye por un @Mock. Corre en MILISEGUNDOS
 * — cronométralo y anótalo para la tabla del README (contraste con el parche @SpringBootTest que hoy se
 * borra: service/TaskServiceTest).
 *
 * @ExtendWith(MockitoExtension.class): SIN ella, @Mock queda null (dolor #1). Modo strict-stubs: un stub
 * que ningún test usa FALLA la clase (UnnecessaryStubbingException, dolor #2) — bórralo, no lo tapes con
 * lenient(). Regla T3: los mocks son para COLABORADORES (repos), jamás para DATOS (Task se construye REAL).
 *
 * Los métodos de abajo ya traen su NOMBRE canónico (metodo_escenario_resultado) y el cuerpo en TODO.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    // ==================== MP-1: crear + cambiarStatus (when/thenReturn/thenThrow) ====================

    @Test
    void crear_valida_delegaEnSaveYDevuelveTareaConId() {
        // TODO MP-1: TaskRequest válido; when(repository.save(any(Task.class))).thenReturn(tareaConId);
        //   service.crear(request, 1L); assertNotNull(creada.getId()).
    }

    @Test
    void cambiarStatus_conAssignee_guardaDone() {
        // TODO MP-1/MP-3: construir la tarea REAL con assignee (constructor de rehidratación);
        //   when(repository.findById(1L)).thenReturn(Optional.of(tarea)); when(save).thenAnswer(...);
        //   service.cambiarStatus(1L, DONE); y CAPTURAR con ArgumentCaptor lo que viajó a save (MP-3).
    }

    @Test
    void cambiarStatus_idInexistente_lanzaTaskNotFoundException() {
        // TODO MP-1: when(repository.findById(999L)).thenReturn(Optional.empty());
        //   assertThrows(TaskNotFoundException.class, () -> service.cambiarStatus(999L, DONE)).
    }

    @Test
    void crear_conBdCaida_propagaLaExcepcion() {
        // TODO MP-1 (demo thenThrow): when(repository.save(any(Task.class))).thenThrow(new RuntimeException("BD caída"));
        //   el mock da a demanda lo que el colaborador real no te da nunca.
    }

    // ==================== MP-2: verify (never/times) + argument matchers ====================

    @Test
    void eliminar_existente_llamaDeleteById() {
        // TODO MP-2: when(findById(1L)).thenReturn(Optional.of(tarea)); service.eliminar(1L);
        //   verify(repository).deleteById(1L).
    }

    @Test
    void eliminar_inexistente_lanzaTaskNotFoundExceptionYNoBorra() {
        // TODO MP-2: when(findById(999L)).thenReturn(Optional.empty()); assertThrows(...);
        //   verify(repository, never()).deleteById(anyLong())  <- el never() es el assert más valioso.
    }

    @Test
    void cambiarStatus_sinAssignee_lanzaTaskStateExceptionYNoGuarda() {
        // TODO MP-2: tarea SIN assignee; assertThrows(TaskStateException.class, ...);
        //   verify(repository, never()).save(any(Task.class))  <- la regla PROHIBIÓ persistir.
    }

    // TODO MP-5: reorganizar estos tests con @Nested por método (Crear, CambiarStatus, Eliminar) +
    //   @DisplayName con frase en español; correr y LEER el árbol como una especificación ejecutable.
}
