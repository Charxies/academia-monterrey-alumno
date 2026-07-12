# Galería de tests decorativos — taller MP-4 (Parte 1)

> **Material de LECTURA, no un proyecto compilable.** Son 6 tests "generados" sobre el dominio real de
> `taskflow-api` (las clases `Task`, `TaskService`, `TaskRepository`, `TaskMapper` que TÚ escribiste en
> S1–S2). Tu trabajo, en parejas: **clasificar cada uno** `legítimo` / `decorativo` **citando la
> pregunta de la checklist** (`checklist-lectura-critica.md`) que lo condena.
>
> **Sin veredicto a propósito:** la clave la tiene el instructor. No todo es trampa: **hay 2 legítimos
> de control** entre los 6 — el objetivo es criterio, no paranoia. Un test con mocks bien usado es
> legítimo.
>
> Notación del dominio (la tuya, de S2): `TaskRequest` es el record DTO de entrada
> `(title, description, priority, assigneeId, dueDate)` — `priority` es el enum `Priority` y el record
> NO lleva `projectId`; `crear(request, projectId)` recibe el `projectId` aparte (del PATH), valida vía
> `Task.crear` y persiste; `cambiarStatus(id, status)` aplica `Task.setStatus`. Excepciones canónicas:
> `TaskValidationException` (título / `dueDate` / `projectId == null`), `TaskNotFoundException`,
> `TaskStateException` (no `DONE` sin assignee, mapeada a 422 en S2D3). Ojo: `ProjectNotFoundException`
> (proyecto inexistente → 404) la valida el **controller** (`TaskController`), NO `TaskService` — por
> eso no aparece en estos tests de service. Todos los tests asumen `@ExtendWith(MockitoExtension.class)`
> con `@Mock TaskRepository taskRepository`, `@InjectMocks TaskService taskService` (el ÚNICO
> colaborador que `TaskService` inyecta es `TaskRepository`) salvo donde el propio código diga otra cosa.

---

## Espécimen 1

```java
@Test
void crear_tituloValido_creaTask() {
    TaskService spy = mock(TaskService.class);
    Task esperada = Task.crear("Configurar CI", null, Priority.HIGH, null, 1L, 7L);
    when(spy.crear(any(), any())).thenReturn(esperada);

    Task actual = spy.crear(new TaskRequest("Configurar CI", null, Priority.HIGH, null, null), 7L);

    assertEquals("Configurar CI", actual.getTitle());
}
```

---

## Espécimen 2

```java
@Test
void crear_datosValidos_noEsNull() {
    when(taskRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    Task t = taskService.crear(new TaskRequest("Configurar CI", null, Priority.HIGH, null, null), 7L);
    assertNotNull(t);
}
```

---

## Espécimen 3

```java
@Test
void crear_tituloEnLimiteInferior_valido() {
    when(taskRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    Task t = taskService.crear(new TaskRequest("Comprar leche", null, Priority.LOW, null, null), 7L);  // 13 chars
    assertEquals(TaskStatus.TODO, t.getStatus());
}
```

---

## Espécimen 4

```java
@Test
void crear_persisteYDevuelve() {
    Task fija = Task.crear("Configurar CI", null, Priority.HIGH, null, 1L, 7L);
    when(taskRepository.save(any())).thenReturn(fija);

    Task r = taskService.crear(new TaskRequest("Otra cosa distinta", null, Priority.LOW, null, null), 7L);

    assertEquals("Configurar CI", r.getTitle());
}
```

---

## Espécimen 5

```java
@Test
void crear_tituloLongitud2_lanzaTaskValidationException() {
    var req = new TaskRequest("ab", null, Priority.LOW, null, null);   // 2 chars
    assertThrows(TaskValidationException.class, () -> taskService.crear(req, 7L));
    verify(taskRepository, never()).save(any());
}
```

---

## Espécimen 6

```java
@Test
void cambiarStatus_doneSinAssignee_lanzaTaskStateException() {
    Task sinAssignee = new Task(3L, "Configurar CI", null, TaskStatus.IN_PROGRESS,
                                Priority.HIGH, 1L, null, null);   // assigneeId null
    when(taskRepository.findById(3L)).thenReturn(Optional.of(sinAssignee));

    assertThrows(TaskStateException.class,
                 () -> taskService.cambiarStatus(3L, TaskStatus.DONE));
    verify(taskRepository, never()).save(any());
}
```

---

## Cómo entregar la clasificación (para la puesta en común)

Por cada espécimen, una línea:

```
Espécimen N — <legítimo | decorativo> — condena la pregunta ①/②/③/④ — <por qué en 1 frase>
```

Ejemplo de formato (NO es la respuesta de ninguno):
`Espécimen 0 — decorativo — condena la ① — ningún cambio en src/main lo pone rojo.`

Lleva a la puesta en común **los 2 que más te costó decidir**. Los legítimos de control valen igual
que los decorativos: reconocer un buen test es tan importante como cazar el teatro.

> **Recordatorio de la ①:** ante cada espécimen, la pregunta que desempata es *"¿qué cambio concreto en
> `src/main` lo pondría rojo?"*. Si no encuentras ninguno, ya lo clasificaste. Y si crees que sí,
> nómbralo como una **mutación** (M1/M2/M3) — mañana en MP-5 lo compruebas.
