# TaskFlow API — Starter S2D5 (Spring Security + JWT)

Punto de partida del **Integrador de Semana 2**. Es la API del final de D4 (persistida con JPA/H2) MÁS
los **esqueletos** de la seguridad de hoy. La dependencia `spring-boot-starter-security` YA está en el
`pom.xml`: por eso al primer arranque **todo está bloqueado (401)** — ese es el lockdown que vamos a
domar. (Quien trabaja en su propio repo `taskflow-api-<usuario>` añade la dependencia a mano en MP-1:
ese ES el ejercicio.)

> El starter **COMPILA y `mvn test-compile` pasa desde el minuto cero.** La suite heredada de D2–D4
> saldrá **EN ROJO** al correr `mvn test` (401 masivo por el lockdown): **no es un bug, es la feature**
> — repararla con el token real es parte del integrador. Los tests nuevos (`AuthControllerTest`,
> `SecurityRulesTest`) vienen `@Disabled`: les quitas el `@Disabled` a medida que implementas.

## Cómo arrancar

```bash
mvn spring-boot:run          # arranca en http://localhost:8080 (hoy: 401 en todo)
mvn compile                  # debe compilar
mvn test-compile             # debe compilar los tests
```

## Usuarios semilla (objetivo del día)

| username | password | role | Nota |
|---|---|---|---|
| `admin` | `admin123` | `ADMIN` | puede borrar cualquier proyecto |
| `ana` | `ana123` | `USER` | owner del proyecto semilla 1 |
| `luis` | `luis123` | `USER` | NO es owner de nada → protagonista del 403 |

> El starter arranca con la semilla de D4 (ana ADMIN, luis USER, sin `passwordHash`). **MP-3** la lleva
> a esta tabla (los 3 usuarios con hash BCrypt). Al añadir la columna `password_hash`: borra
> `data/*.mv.db` y deja que el `DataSeeder` resiembre.

## Qué vas a completar (los TODO del día)

| Ítem | Archivo(s) | Qué |
|---|---|---|
| MP-2 | `config/SecurityConfig` | escribir el `SecurityFilterChain` (lambda DSL: `/auth/**` público, `anyRequest().authenticated()`, `csrf.disable()`, `httpBasic` temporal) |
| MP-3 | `model/User`, `config/DataSeeder` | campo `passwordHash`; sembrar los 3 usuarios con el `PasswordEncoder` |
| MP-4 | `repository/UserRepository`, `service/JpaUserDetailsService` | `findByUsername`/`existsByUsername`; `loadUserByUsername` con `.roles(...)` |
| MP-5 | `dto/auth/*`, `service/AuthService`, `controller/AuthController`, `advice/GlobalExceptionHandler` | validación de los DTOs; `register` (409) y `login` (401); handlers 409/401 |
| MP-7 | `service/AuthService` (+ leer `security/JwtService`) | conectar el token real (`jwtService.generateToken`) — muere el stub `"pendiente-jwt"` |
| MP-8 | `security/JwtAuthenticationFilter`, `config/SecurityConfig` | la lógica del filtro (Bearer, validar, `SecurityContext`, try/catch→401); registrar `STATELESS` + `addFilterBefore` + entry point |
| MP-9 | `security/ProjectSecurity`, `controller/ProjectController`, `service/ProjectService` | `esOwner`; `@PreAuthorize` en el DELETE; `ownerId` desde el `Authentication` en el POST |

Ya viene **PROVISTO** (se lee, no se escribe): `security/JwtService` (jjwt 0.12.6, comentado línea a
línea), y en `SecurityConfig` los beans `PasswordEncoder`, `AuthenticationManager` y el entry point
401 JSON. El `application.yml` trae `jwt.secret` (64 chars) y `jwt.expiration-ms=3600000`.

## Tabla canónica 401 vs 403 (la cierras hoy)

| Código | Falla de | La API dice | Ejemplo |
|---|---|---|---|
| **401 Unauthorized** | **autenticación** | "No sé quién eres" | `GET /projects` sin token |
| **403 Forbidden** | **autorización** | "Sé quién eres y NO puedes" | `luis` hace `DELETE /projects/1` |

## Entregable (DoD)

- register/login end-to-end (hash BCrypt en `users`, login devuelve JWT).
- todos los `/projects` y `/tasks` exigen token (sin token → 401); `DELETE /projects/{id}` → 403 a un
  USER no-owner, 204 a owner/ADMIN.
- `mvn test` **verde total**: ≥4 tests de seguridad nuevos + la suite D2–D4 reparada con token real
  (**prohibido** `permitAll("/**")`).
- colección Postman con `{{token}}` auto-seteado en `postman/`.
- push `feat: taskflow api v2.0 - seguridad jwt` + demo de 5 min por pareja del flujo completo.
