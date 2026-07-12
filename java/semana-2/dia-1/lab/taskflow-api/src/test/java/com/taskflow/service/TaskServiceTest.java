package com.taskflow.service;

import com.taskflow.repository.InMemoryTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * TaskServiceTest — testing de la capa de HOY, SIN mocks y SIN arrancar Spring (ESQUELETO).
 *
 * @BeforeEach construye TaskService con 'new' sobre un InMemoryTaskRepository REAL, instancia NUEVA
 * por test: posible JUSTO PORQUE la inyección es por constructor (contraste con el Singleton de MP-4,
 * donde 'new' estaba prohibido). Un repo en memoria rápido y determinista NO se mockea; los mocks
 * llegan cuando haya capas que aislar (MockMvc en S2D2, Mockito en S3D1).
 *
 * OJO: hoy los 6 tests están VACÍOS -> pasan en verde sin probar NADA (el "test mentiroso" de
 * S1D5 MP-6). Escribe cada cuerpo cuando completes TaskService. Naming: metodo_escenario_resultado.
 * Ayudas por test:
 *   - crear_datosValidos...:           crear una tarea válida; assertNotNull(id) y listar().size()==1
 *   - crear_tituloDe2Chars...:         assertThrows(TaskValidationException.class, () -> service.crear("ab", ...))
 *   - crear_dueDatePasado...:          assertThrows(...) con LocalDate.now().minusDays(1)
 *   - completar_conAssignee_quedaDone: sembrar con el CONSTRUCTOR (assigneeId puesto), completar -> DONE
 *   - completar_sinAssignee...:        sembrar sin assignee, assertThrows(TaskValidationException...)
 *   - completar_idInexistente...:      assertThrows(TaskNotFoundException.class, () -> service.completar(999L))
 */
class TaskServiceTest {

    private InMemoryTaskRepository repository;
    private TaskService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();   // repo REAL, limpio por test
        service = new TaskService(repository);       // 'new' posible = argumento #1 de la DI por constructor
    }

    @Test
    void crear_datosValidos_asignaIdYApareceEnListar() {
        // TODO
    }

    @Test
    void crear_tituloDe2Chars_lanzaTaskValidationException() {
        // TODO
    }

    @Test
    void crear_dueDatePasado_lanzaTaskValidationException() {
        // TODO
    }

    @Test
    void completar_conAssignee_quedaDone() {
        // TODO
    }

    @Test
    void completar_sinAssignee_lanzaTaskValidationException() {
        // TODO
    }

    @Test
    void completar_idInexistente_lanzaTaskNotFoundException() {
        // TODO
    }
}
