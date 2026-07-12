# TaskFlow UI — Mini-frontend estático para la semana QE

Este directorio contiene una mini-aplicación web de TaskFlow (HTML, CSS y JavaScript vanilla) diseñada para ser el objeto de pruebas de automatización durante la Semana 4 (QE).

**NO ABRAS ESTOS ARCHIVOS DIRECTAMENTE EN EL NAVEGADOR (`file://...`).** Deben ser servidos por tu API de TaskFlow para funcionar correctamente.

## Instalación y verificación (5 minutos)

Sigue estos pasos para instalar y probar la UI. Esto es un requisito para el Día 4 de QE.

### 1. Copiar los archivos

-   Copia **todo el contenido** de la carpeta `static/` de este recurso a la carpeta `src/main/resources/static/` de tu proyecto `taskflow-api`. Si la carpeta `static` no existe en tu proyecto, créala.
-   Copia el archivo `application-h2.yml` de este recurso a la raíz de `src/main/resources/` de tu proyecto `taskflow-api`.

### 2. Verificar la dependencia de H2

Abre tu `pom.xml` y asegúrate de tener la dependencia de la base de datos H2 con el `scope` en `runtime`. Si no la tienes, añádela:

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 3. Arrancar la API con el perfil H2

Inicia tu aplicación `taskflow-api` especificando el perfil `h2`. En IntelliJ, puedes hacerlo editando tu configuración de Run/Debug y añadiendo `h2` al campo "Active profiles".

Si lo corres por línea de comandos, usa:

```sh
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

La base de datos H2 se creará en memoria y se destruirá cada vez que detengas la aplicación, dándote un ambiente limpio para cada sesión.

### 4. Sembrar los datos de prueba

Con tu API corriendo, ejecuta uno de los scripts del directorio `seed/` para poblar la base de datos con usuarios, proyectos y tareas de prueba.

-   Si usas IntelliJ, abre `seed/seed.http` y ejecuta todas las peticiones.
-   Si usas la terminal, ejecuta `seed/seed.sh`.

Las credenciales de los usuarios de prueba son:

-   **Usuario:** `demo` / **Contraseña:** `Demo123!`
-   **Usuario:** `ana` / **Contraseña:** `Ana1234!`

### 5. Abrir la UI en el navegador

Abre tu navegador y ve a `http://localhost:8080/`. Deberías ver la página de login de TaskFlow.

**Smoke Test (Prueba rápida):**
1.  Inicia sesión con `demo` y `Demo123!`.
2.  Deberías ser redirigido a la lista de proyectos.
3.  Crea un nuevo proyecto.
4.  Abre el proyecto y crea una nueva tarea.
5.  La tarea debería aparecer en la tabla.

Si esto funciona, ¡estás listo para la semana de QE!

## Troubleshooting (Solución de problemas)

-   **"No se ven mis cambios"**: Si modificas los archivos después de haber arrancado la API, recuerda que `mvn spring-boot:run` empaqueta los recursos al inicio. **Reinicia tu API** para ver los cambios. Además, fuerza un refresco de caché en tu navegador (Ctrl+Shift+R o Cmd+Shift+R).
-   **Error de CORS o `fetch` fallido**: Esto ocurre si abres los `index.html` directamente desde tu sistema de archivos (`file://...`). La UI está diseñada para detectar esto y mostrar un error. Asegúrate de acceder a través de `http://localhost:8080`.
-   **La página de inicio (`/`) no funciona**: Si tienes un `@RestController` que mapea la ruta `/`, puede entrar en conflicto con la `welcome page` de Spring Boot. Usa la URL explícita: `http://localhost:8080/index.html`.
-   **El token de sesión expira**: Si dejas la página inactiva por mucho tiempo, tu sesión JWT puede expirar. La UI te redirigirá al login. Simplemente vuelve a iniciar sesión. No es un error, es una característica de seguridad.
-   **Los datos desaparecen**: La base de datos H2 vive en memoria. Cada vez que reinicias la API, se borra todo. **Vuelve a ejecutar el script de `seed`** para tener datos frescos.
