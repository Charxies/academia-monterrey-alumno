# Checklist pre-demo — ejecútalo ANTES de presentar (primeros 5 min del integrador)

> **TODAS las parejas lo corren a la vez**, en los primeros 5 min del integrador. Nadie demuestra sin
> tener las 5 casillas verdes. La demo con la API caída se previene AQUÍ, no se improvisa en vivo.

## Las 5 casillas (en orden)

- [ ] **1. API local arriba.** `docker compose up -d` → Postgres + api en verde.
      ```bash
      docker compose up -d
      docker compose ps          # todos los servicios "Up"/"healthy"
      ```
- [ ] **2. Datos semilla cargados.** Tu seed de S3 (o los datos de la colección Postman). Hay al menos
      un proyecto y una tarea con los que disparar tu feature.
- [ ] **3. Smoke request a la feature** (curl o Postman) responde lo esperado:
      ```bash
      # ejemplo comentarios — ajusta a TU feature y a TU token:
      curl -s -X POST localhost:8080/tasks/1/comments \
        -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
        -d '{"body":"smoke de la demo"}' -w '\n-> HTTP %{http_code}\n'
      # esperado: 201 y un CommentResponse
      ```
- [ ] **4. Pipeline del PR en verde** en pantalla (el **job de tests**; si el deploy murió por
      infraestructura AWS, está documentado/skipeado y NO bloquea — ver abajo).
- [ ] **5. Reporte de aceleración abierto** (`docs/reporte-aceleracion.md`) listo para el §3 de la demo.

## Si algo está rojo (plan B por casilla)

| Falla | Qué haces AHORA |
|---|---|
| API no levanta | Revisa puerto ocupado / Postgres. Si no sube en 3 min → pasas al **final de la cola** de demos y reinicias mientras las otras parejas presentan. |
| Smoke request 4xx/5xx inesperado | Verifica el token (login) y el seed. NO parchees código en vivo: usa el request grabado en Postman + el test de `taskflow-qa` verde como evidencia. |
| Job de **deploy** rojo por AWS expirada | No bloquea: el gate es el **job de tests**. Ten a la mano el `.github/workflows/*.yml` con el job de deploy skipeado/desactivado y explícalo (lectura de tu propio yml). |
| Job de **tests** rojo | Esto sí bloquea el DoD (a). Si es de entorno (Postgres/seed), arréglalo; si es de código, no debiste mergear — usa el plan B de demo y arréglalo en el bloque. |

## Plan B de demo en vivo (API muere a media presentación)

1. Muestra el **test de `taskflow-qa`** de tu feature en **verde**.
2. Muestra el **request grabado** en la colección Postman (con su respuesta).
3. Sigues con el reporte de aceleración; la parte "en vivo" pasa al final mientras reinicias.

> El objetivo del checklist es que la evaluación sea de tu **feature y tu criterio**, no de la suerte
> de la red del salón.
