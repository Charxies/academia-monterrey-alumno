# demo-oauth2 — Login with Google (awareness OAuth2/OIDC) · SOLO INSTRUCTOR

> Proyecto **aparte** del `taskflow-api`. Se usa en **PM-1 (S2D5), demo de 15 min** para mostrar en vivo
> el flujo OAuth2/OIDC. **Los alumnos NO lo montan** (requiere registrar una app en Google Cloud y no
> aporta al capstone). El capstone usa el **JWT propio** que los alumnos ya construyeron.

## Qué demuestra

El **contraste** con lo que se construyó hoy:

| | taskflow-api (hoy) | demo-oauth2 (este proyecto) |
|---|---|---|
| ¿Quién autentica? | **Nosotros** (tabla `users` + BCrypt) | **Google** (authorization server) |
| ¿Quién emite el token? | **Nuestro** `JwtService` (lo firmamos) | Google (id_token OIDC) |
| Nuestra app es… | el emisor | el **client** que recibe el resultado |

Vocabulario mínimo (pizarra): **authorization server** (Google, emite el token) · **client** (esta app,
lo pide) · **resource server** (la API que valida el token). **OIDC** = capa de identidad sobre OAuth2.

## Obtener credenciales de Google (una vez, ANTES de clase)

1. [Google Cloud Console](https://console.cloud.google.com/) → crea (o elige) un proyecto.
2. **APIs & Services → OAuth consent screen**: tipo *External*, pon un nombre de app y tu correo. Añádete
   como *test user* (así no necesitas verificación de Google para la demo).
3. **APIs & Services → Credentials → Create credentials → OAuth client ID**:
   - Application type: **Web application**.
   - **Authorized redirect URI** (exacta, o el callback falla):
     `http://localhost:8081/login/oauth2/code/google`
4. Copia el **Client ID** y el **Client secret**.

## Configurar los secretos (nunca se commitean)

Copia la plantilla y pega tus credenciales reales:

```bash
cp src/main/resources/application-local.yml.example src/main/resources/application-local.yml
# edita application-local.yml con tu client-id / client-secret
```

`application-local.yml` está en `.gitignore`. Alternativa sin archivo: exporta
`GOOGLE_CLIENT_ID` y `GOOGLE_CLIENT_SECRET` como variables de entorno (el `application.yml` las lee).

## Correr la demo

```bash
mvn spring-boot:run
```

1. Abre `http://localhost:8081` → como no hay sesión, Spring te manda a `/login` con el enlace
   **"Login with Google"**.
2. Click → **redirect a Google** → **pantalla de consentimiento** → aceptas.
3. **Callback** a `http://localhost:8081/login/oauth2/code/google` → vuelves a `/` y ves el **principal
   con los claims de Google** (`sub`, `nombre`, `email`) en JSON.

> Si el callback da error `redirect_uri_mismatch`: la URI del paso 3 de "Obtener credenciales" debe ser
> **idéntica** (`http://localhost:8081/login/oauth2/code/google`). Si sale `access_blocked`: te falta
> agregarte como *test user* en la OAuth consent screen.

## Estructura

```
demo-oauth2/
  pom.xml                                   # Spring Boot 3.5.3, Java 21, web + oauth2-client
  src/main/java/com/taskflow/demooauth2/
    DemoOauth2Application.java              # main (:8081)
    SecurityConfig.java                    # oauth2Login() + anyRequest().authenticated()
    HomeController.java                    # "/" -> claims del principal de Google
  src/main/resources/
    application.yml                        # :8081 + registration.google con PLACEHOLDERS
    application-local.yml.example          # plantilla de secretos reales (se copia sin .example)
  .gitignore                               # ignora application-local.yml y target/
```
