<!-- ============================================================================
     ERR-1 — "El README que miente" (MP-1). Este es un README HEREDADO realista,
     de la era S2D5, que era válido HACE UNA SEMANA y hoy MIENTE (la BD se mudó al
     compose en S3D2; el perfil 'postgres' contra una Postgres local ya no existe).

     El instructor lo AUDITA corriéndolo TAL CUAL en un clon limpio: el comando
     estrella da 'connection refused'. Regla que se instala:
       "si no está en el README no existe; si está y no corre, es un bug".

     NO copies este archivo a tu repo: es el ANTIejemplo. El bueno está en
     ../plantillas/README-plantilla.md y ../../solucion/README-ejemplo-final.md
     ============================================================================ -->

# TaskFlow API

API REST del gestor de tareas TaskFlow. Hecha con Spring Boot.

## Requisitos

- Java 21
- Maven
- PostgreSQL corriendo en `localhost:5432` con una base `taskflow`

## Cómo correr

```bash
git clone https://github.com/anagarcia/taskflow-api-anagarcia.git
cd taskflow-api-anagarcia
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

<!-- ▲▲▲ AQUÍ MUERE LA PRUEBA DEL CLON ▲▲▲
     - En un clon limpio NO hay ninguna Postgres en localhost:5432 -> el arranque
       da "Connection to localhost:5432 refused". La BD hoy vive en el
       docker-compose (perfil 'docker', host 'db'), no en un Postgres local.
     - El perfil se llama 'docker' (application-docker.yml), NO 'postgres':
       -Dspring-boot.run.profiles=postgres levanta un perfil que no existe.
     - No se menciona Docker, ni .env, ni el compose: el camino que SÍ funciona
       está ausente -> "si no está en el README, no existe". -->

La API queda en `http://localhost:9090`.
<!-- ▲ el puerto real es 8080 (server.port en application.yml): otro dato viejo. -->

## Endpoints

Ver el código.
<!-- ▲ no menciona Swagger (/swagger-ui.html), que es justo la pantalla de la demo. -->

## Tests

```bash
mvn test
```
<!-- ▲ 'mvn test' NO corre el gate JaCoCo (vive en la fase 'verify'): el README
     promete una calidad que este comando no verifica. Debe ser 'mvn verify'. -->
