# Portafolio final — los 3 repos presentables

> El cierre del programa. Tu portafolio son **3 repos** que cuentan una historia completa: de consola a
> API desplegada, auditada por tu propio framework, con una feature nueva construida con IA y medida.
> Esta plantilla es el checklist para dejarlos **presentables** hoy, y el guion para contarlos en una
> entrevista.

## 1. Los 3 repos

| Repo | Qué es (1 frase para el README) |
|---|---|
| `taskflow` | Gestor de tareas de **consola** en Java puro: modelos, repositorio, reportes con streams, tests JUnit (S1). |
| `taskflow-api` | **API REST** Spring Boot con JWT, Postgres, Docker y CI/CD desplegada — con la **feature nueva** hecha con Copilot (S2–S3, S5). |
| `taskflow-qa` | Framework de automatización **RestAssured + Selenium/POM** con Allure en CI que prueba la API (S4). |

## 2. Checklist de README por repo (los 3)

Para **cada** repo, el README responde en los primeros 30 segundos de lectura:

- [ ] **Qué es** — una frase, arriba del todo.
- [ ] **Stack** — badges o lista (Java 21, Spring Boot, Postgres, Docker, JUnit/RestAssured…).
- [ ] **Cómo correrlo** — comandos copiables (`docker compose up`, `mvn test`, `mvn spring-boot:run`).
- [ ] **Badge de CI** — el estado del pipeline (verde) visible arriba.
- [ ] **Screenshot / GIF** donde aplique — `taskflow` (menú de consola), `taskflow-qa` (reporte Allure),
      `taskflow-api` (Swagger UI con los endpoints, incluida tu feature nueva).
- [ ] **Sección de la feature de S5** en el README de `taskflow-api` (la que agregaste hoy).

## 3. Pins de GitHub

- [ ] Los **3 repos fijados** (pinned) arriba de tu perfil de GitHub: perfil → **Customize your pins** →
      selecciona `taskflow`, `taskflow-api`, `taskflow-qa`. Son lo primero que ve un reclutador.
- [ ] Foto/bio del perfil mínimamente presentable (no es decoración: es la portada).

## 4. Guion de entrevista — 90 segundos (ensáyalo en voz alta)

> Es la **regla (d) del programa aplicada al programa entero**: explicar lo que construiste, sin leer.

**Estructura (adáptala con tus palabras):**

1. **El arco (15 s):** *"Construí TaskFlow, un gestor de tareas, a lo largo de 5 semanas: empezó como
   app de consola en Java y terminó como una API REST desplegada en la nube con su propio framework de
   pruebas."*
2. **La API (25 s):** *"`taskflow-api` es Spring Boot con **JWT** y roles, persistencia en **Postgres**,
   **dockerizada** y desplegada con **CI/CD** en GitHub Actions. CRUD de proyectos y tareas con
   validación, DTOs y manejo de errores centralizado."*
3. **La calidad (20 s):** *"Le escribí `taskflow-qa`, un framework de automatización con **RestAssured**
   contra la API y **Selenium** con Page Object contra la UI, con reportes Allure corriendo en CI. La
   API no se prueba a mano: se prueba sola."*
4. **La feature con IA (25 s):** *"En la última semana le agregué **`<comentarios/etiquetas/notificaciones>`**
   de punta a punta con **GitHub Copilot**: espec escrita, implementación, tests que **auditamos y
   saboteamos** para probar que protegen, y un **reporte de aceleración** midiendo dónde la herramienta
   ayudó y dónde la rechazamos —por ejemplo, `<un rechazo concreto: SQL concatenado / dependencia no
   verificada>`."*
5. **El cierre (5 s):** *"Puedo explicarte cualquier línea de los tres repos — están fijados en mi perfil."*

## 5. Checklist final (wrap-up)

- [ ] Los 3 repos con su README al día y pusheados.
- [ ] PR del capstone **mergeado** en `taskflow-api`, `reporte-aceleracion.md` en `docs/`.
- [ ] Los 3 repos **pineados** en el perfil.
- [ ] Ensayaste el guion de 90 s **una vez en voz alta** (si no lo dijiste, no lo tienes).

> **Preview de la vida real:** la UI de Copilot que viste esta semana cambiará en meses; los conceptos
> —contexto, verificación, criterio, el semáforo— no. Mantén los 3 repos vivos, practica fundamentos
> sin IA de vez en cuando (el músculo se atrofia), y cuando tu empleador pregunte si sabes usar IA para
> programar, la respuesta ya está pusheada en tu perfil.
