package com.taskflow.service;

import com.taskflow.model.Task;

import java.util.Comparator;

/**
 * TaskOrders — catálogo de ESTRATEGIAS de ordenamiento de tareas (MP-3, patrón Strategy).
 *
 * ESQUELETO DE MP-3. Cada constante es una estrategia empaquetada como VALOR (un {@link Comparator})
 * que se PASA a quien ordena (el contexto: sorted(...) de los streams). Es el mismo patrón que ya
 * usaron en S1D3/D4 sin nombre: un Comparator es una estrategia y sort/sorted el contexto.
 *
 * Los placeholders (a, b) -> 0 COMPILAN pero no ordenan nada; reemplázalos por la lógica indicada
 * en cada TODO. Cuando termines, esta clase se reutiliza tal cual en la API (TaskService.listar()
 * ordenará con POR_URGENCIA).
 */
public final class TaskOrders {

    private TaskOrders() {
        // no instanciable: es un catálogo de constantes
    }

    // TODO MP-3 (1): dueDate ascendente, con las tareas SIN fecha (null) al FINAL.
    //   Pista: Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()))
    public static final Comparator<Task> POR_FECHA = (a, b) -> 0;

    // TODO MP-3 (2): orden alfabético por título (sin distinguir mayúsculas/minúsculas).
    //   Pista: Comparator.comparing(Task::getTitle, String.CASE_INSENSITIVE_ORDER)
    public static final Comparator<Task> POR_TITULO = (a, b) -> 0;

    // TODO MP-3 (3): la estrategia NUEVA. Encadena con comparing/thenComparing (S1D3):
    //   1) las VENCIDAS primero (estaVencida() == true antes que false)
    //   2) a igualdad, prioridad de mayor a menor (HIGH -> MED -> LOW)   -> reverseOrder()
    //   3) a igualdad, dueDate ascendente con las sin fecha al final     -> nullsLast(naturalOrder())
    public static final Comparator<Task> POR_URGENCIA = (a, b) -> 0;
}
