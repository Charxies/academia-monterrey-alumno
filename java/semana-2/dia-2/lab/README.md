# taskflow-api — starter S2D2 (lab)

Este `lab/` es tu **red de seguridad**. El flujo normal del día es trabajar sobre TU repo
`taskflow-api-<usuario>` (el entregable de ayer); este starter existe para quien venga atrasado o
quiera un punto de partida limpio.

## En qué estado está

Es el proyecto **en su estado final de D1** (dominio de S1 cableado por Spring, `SeedRunner` que
siembra y termina) **+ los esqueletos de hoy**. En concreto, respecto a ayer ya trae, listos para
que los actives o completes:

- `repository/InMemoryProjectRepository` — **PROVISTO** (`@Repository`, `findAll`/`findById`/`save`).
- `config/DataSeeder` — **PROVISTO** con la `@Component` **comentada** (lo activas en MP-4). Ojo:
  este archivo es la **versión FINAL** (inyecta `TaskRepository` **y** `InMemoryProjectRepository`, y
  siembra los 3 proyectos + 9 tareas) — es tu red de seguridad. El `alumno.md` lo construye **por
  etapas**: en MP-4 solo las tareas (constructor con `TaskRepository` a secas) y en el integrador
  (Paso 3b) añade los proyectos. Los datos (títulos, usuarios, proyectos) son los mismos por ambos caminos.
- `service/ProjectService` — esqueleto con TODOs (integrador).
- `service/TaskService` — con `porEstado(...)` (MP-5) y `buscarPorId(...)` (MP-7) como TODOs.
- `controller/PingController`, `TaskController`, `ProjectController` — esqueletos; todo lo que
  depende de `spring-web` vive en **comentarios TODO** (por eso compila SIN el starter-web todavía).
- `src/test/.../controller/TaskControllerTest`, `ProjectControllerTest` — nombres de tests dados,
  cuerpos MockMvc en TODO.

> ⚠ **Todavía NO tiene `spring-boot-starter-web`.** Eso es deliberado: agregarlo es el **paso 0 del
> día (MP-2)** — tu primer cambio de dependencias consciente. Hasta que lo agregues, la app arranca,
> siembra y **termina** (no hay servidor).

## Cómo compilar (antes de tocar nada)

```bash
mvn -q compile          # BUILD SUCCESS aunque falte el starter-web (lo "web" está comentado)
mvn -q test-compile     # los tests esqueleto también compilan
```

## El orden del día sobre este starter

1. **MP-2** — agrega `spring-boot-starter-web` al `pom.xml` (bloque en el handout) → Load Maven
   Changes → arranca y lee `Tomcat started on port 8080`.
2. **MP-3** — descomenta el `PingController` para estrenar `@GetMapping`.
3. **MP-4** — activa `config/DataSeeder` (descomenta `@Component`) y **BORRA** `runner/SeedRunner.java`;
   descomenta `TaskController` (`GET /tasks`).
4. **MP-5** — completa `TaskService.porEstado(...)` y el filtro `?status=` del controller.
5. **MP-7** — completa `TaskService.buscarPorId(...)` y `GET /tasks/{id}` con `ResponseEntity`.
6. **MP-8** — crea `application.yml` (3 llaves) y **BORRA** `application.properties`.
7. **MP-9 + integrador** — descomenta y escribe los tests MockMvc; completa `ProjectService` y
   `ProjectController`; borra `PingController` y `/demo/task`.

El enunciado paso a paso, la teoría y el bloque XML del starter-web están en `alumno.md`.
