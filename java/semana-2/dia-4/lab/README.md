# taskflow-api — Semana 2, Día 4 · STARTER (lab)

Punto de partida del día: la API de D3 **tal cual** (compila, arranca y `mvn test` pasa) más los
**esqueletos y TODOs** de hoy. Vas a persistir el capstone con **Spring Data JPA + H2** sin tocar
`TaskService`.

## Estado del starter (importante)

- **Compila y arranca YA.** `spring-boot-starter-data-jpa` y `h2` ya están en el `pom` (se LEEN en
  MP-1, no se teclean). Como el `application.yml` aún NO tiene datasource, Spring Boot autoconfigura
  una **H2 en memoria** — por eso la app levanta. En MP-1 la haces TUYA y **en archivo**.
- Las clases del dominio (`Task`, `Project`, `User`) siguen siendo las de D3, **sin anotar**: cada
  una lleva un bloque `TODO` con los pasos exactos para volverlas `@Entity`.
- `TaskRepository`/`ProjectRepository` siguen siendo interfaces "a mano" con su `InMemory*`; los `TODO`
  explican el swap a `JpaRepository` y qué se elimina.
- `mvn test` pasa desde el minuto cero: la suite de D2/D3 corre contra los repos en memoria. El
  `repository/TaskRepositoryTest` es un **esqueleto** `@DataJpaTest` de 6 tests con cuerpos vacíos
  (salen "verdes" sin probar nada — el **test mentiroso** de S1D5: hay que rellenarlos).

```bash
mvn spring-boot:run      # arranca (H2 en memoria autoconfigurada mientras el yml tiene TODOs)
mvn test                 # verde de arranque (regresión D2/D3)
```

## Mapa del día (qué tocar, en orden)

| Bloque | Archivo(s) | Qué hacer |
|---|---|---|
| MP-1 | `application.yml` | Descomentar el bloque de persistencia: H2 en archivo, `ddl-auto`, `show-sql`, `open-in-view`, consola. |
| MP-2/3 | `model/Task.java` | `@Entity`/`@Table`/`@Id`/`@GeneratedValue`, no-arg protegido, `@Column`, `@Enumerated(STRING)`. Quitar `final`. |
| MP-4 | `model/Project.java`, `mapper/ProjectMapper`, `service/ProjectService` | Aplanar `User owner` → `Long ownerId`; anotar; `id` a `Long`. |
| MP-5 | `model/User.java` | De record a clase `@Entity` (`@Table("users")` — `USER` es reservada). |
| MP-6 | `repository/*` , `service/ProjectService` | `extends JpaRepository`, borrar `InMemory*`, derived queries; `tareasDe` → `findByProjectId`. |
| MP-8 | `model/Task.java` | `@ManyToOne` de solo lectura hacia `Project` (la columna manda, el objeto navega). |
| MP-9 | `config/DataSeeder.java` | `count()==0`, usuarios `ana`/`luis`, `id=null`, orden users→projects→tasks. |
| Integr. | `service/ReportService`, `repository/TaskRepositoryTest`, `repository/UserRepository` (nuevo) | Reescritura con juicio; rellenar los 6 `@DataJpaTest`; crear `UserRepository`; `@ActiveProfiles("test")` en los MockMvc. |

## Demo del instructor (no la escribes)

`demo/DemoJdbc.java` — JDBC crudo, `main` plano, ~30 líneas para UN `SELECT`. La LEES en AM-1; es lo
que Spring Data JPA deja de hacerte escribir. Corre con la app detenida.

## DoD (criterios de listo)

- Prueba de fuego: crear tareas por Swagger → reiniciar → siguen ahí (H2 archivo, `data/taskflow.mv.db`).
- Cero clases `InMemory*` en `src/`; los 3 repos extienden `JpaRepository`; existe `UserRepository`.
- `git diff` de `TaskService.java` contra ayer = **vacío**.
- `status`/`priority`/`role` como TEXTO en la consola H2; tablas `tasks`/`projects`/`users`.
- `mvn test` verde: D2/D3 intactos + ≥6 `@DataJpaTest` **reales** (ya no vacíos).
- `data/*.mv.db` ignorado por git; commit + push `feat: taskflow api - persistencia con spring data jpa y h2`.
