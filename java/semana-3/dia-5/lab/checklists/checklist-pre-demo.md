# Checklist pre-demo — antes de presentar (PM-1 y el integrador)

> Se corre **completo, en voz alta, justo antes** de cada pasada (ensayo y demo final). Reduce el
> pánico de demo en vivo (dolor 1) a casi cero. Regla: **UN solo entorno arriba** por demo (dolor 10).

## 1. Un solo entorno, y arriba

- [ ] Declarado el entorno de esta demo: **AWS vivo** o **compose local** (uno, no los dos).
- [ ] Ningún zombi ocupando el puerto:
  ```bash
  docker compose ps          # ¿qué contenedores están corriendo?
  lsof -i :8080              # ¿quedó un 'spring-boot:run' de la mañana? mátalo
  ```
- [ ] Entorno arriba y respondiendo: `curl http://<base>/info` → `{"app":"taskflow-api","version":"3.0.0"}`.

## 2. Datos del guion sembrados Y VERIFICADOS (ERR-3)

Un compose con volumen nuevo trae SOLO la semilla genérica del `DataSeeder` (`admin`/`ana`/`luis` + un
par de proyectos). El proyecto **"Sprint demo"** y las tareas de tu historia **NO** están hasta que los
siembras.

- [ ] Corrida la colección `postman/datos-demo.postman_collection.json` contra el entorno de la demo.
- [ ] **Verificado con un GET** que existen (no "al rato los cargo"):
  ```bash
  # login como ana -> token; luego:
  curl -H "Authorization: Bearer $TOKEN" http://<base>/projects        # ¿aparece "Sprint demo"?
  curl -H "Authorization: Bearer $TOKEN" http://<base>/projects/<id>/tasks   # ¿están las 5 tareas?
  ```
- [ ] El id del proyecto "Sprint demo" es el que usa el guion (guardado en `{{sprintProjectId}}`).
- [ ] Al menos una tarea **con assignee** para el PATCH a DONE del punto 5 (sin assignee → 422).

## 3. Pestañas listas (en el orden del guion)

- [ ] **README** con el diagrama (puntos 1–2).
- [ ] **IDE** en la pieza de código destacado (punto 3, p. ej. `ProjectController.deleteProject`).
- [ ] **Actions** en el run verde del commit del tag + **GHCR** en la imagen (punto 4).
- [ ] **Swagger** (`/swagger-ui.html`) + **Postman/terminal** con la request de login lista (punto 5).
- [ ] **Notas del Release** (punto 6).

## 4. Reparto y respaldo

- [ ] Reparto 50/50 claro: A hace 1–4, B hace 5–6, **cambio en el punto 5** (~min 5.5).
- [ ] Los dos repasaron los 6 puntos (el instructor pregunta al que **no** presentó esa parte).
- [ ] **Demo grabada de respaldo** a la mano (asset del Release o link Drive/Loom). Si algo muere >60 s en vivo → se cambia a la grabación con calma.
- [ ] El `.mp4` **NO** está en git (`git status` limpio de binarios grandes — dolor 12).

## 5. Reloj y config

- [ ] Cronómetro visible: **10 min** de demo (±1).
- [ ] Ningún flag nuevo sin commitear (p. ej. virtual threads de AM-1) — `git status`/`git diff` limpios (dolor 11).
