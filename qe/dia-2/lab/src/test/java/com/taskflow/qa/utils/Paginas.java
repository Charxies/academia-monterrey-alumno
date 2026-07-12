package com.taskflow.qa.utils;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Paginas — el helper ÚNICO de la semana para resolver URLs del sitio de práctica.
 *
 * Ningún test escribe una URL literal: todas salen de aquí. Así, el MISMO test corre
 * contra el sitio externo o contra el espejo local sin tocar un solo locator:
 *
 *   - Por defecto              -> sitio externo the-internet.herokuapp.com
 *   - Con -Dpaginas.local=true -> archivos file:// de src/test/resources/practice-pages/,
 *                                 resueltos por el CLASSPATH (multiplataforma: no hay rutas
 *                                 a mano; en Windows sale file:///C:/... y en macOS/Linux
 *                                 file:///Users/...).
 *
 * D2 EXTIENDE esta MISMA clase (no la renombra): agrega checkboxes(), tabla(), dinamicos(),
 * perfilV1(), perfilV2() y el resolver explícito local(...). Dos matices de D2:
 *   - perfil-v1/v2 SIEMPRE resuelven a local (no existen en the-internet: son el gimnasio
 *     de "clase de framework que se regenera" y "XPath absoluto que se rompe").
 *   - local(archivo) fuerza el espejo local sin depender del flag: lo usa el integrador,
 *     que debe correr SIEMPRE contra páginas locales (estabilidad + data-testid garantizados).
 */
public final class Paginas {

    /** Raíz del sitio externo (sin barra final). */
    private static final String BASE_EXTERNA = "https://the-internet.herokuapp.com";

    /** Flag ÚNICO de la semana. -Dpaginas.local=true activa el fallback local. */
    private static final boolean LOCAL = Boolean.getBoolean("paginas.local");

    private Paginas() {
        // Clase de utilería: no se instancia.
    }

    // ------------------------------------------------------------------
    // D1 (sin cambios): home + login
    // ------------------------------------------------------------------

    /** Home del sitio de práctica (título "The Internet", h1.heading, lista de links). */
    public static String home() {
        return LOCAL ? uriLocal("index.html") : BASE_EXTERNA + "/";
    }

    /** Página de login (h2 "Login Page"). El assert de navegación usa contains("login"),
     *  verdadero tanto para .../login (externo) como para .../login.html (local). */
    public static String login() {
        return LOCAL ? uriLocal("login.html") : BASE_EXTERNA + "/login";
    }

    // ------------------------------------------------------------------
    // D2 (nuevas): checkboxes, tabla, dinamicos — externo por defecto, local con el flag
    // ------------------------------------------------------------------

    /** /checkboxes: dos input[type='checkbox'], el 2º pre-marcado (local añade radios que D2 no usa). */
    public static String checkboxes() {
        return LOCAL ? uriLocal("checkboxes.html") : BASE_EXTERNA + "/checkboxes";
    }

    /** /tables (table1): thead + 4 filas con links edit/delete por fila. */
    public static String tabla() {
        return LOCAL ? uriLocal("tabla.html") : BASE_EXTERNA + "/tables";
    }

    /** /add_remove_elements/: "Add Element" crea botones Delete de forma SÍNCRONA (sin waits). */
    public static String dinamicos() {
        return LOCAL ? uriLocal("dinamicos.html") : BASE_EXTERNA + "/add_remove_elements/";
    }

    // ------------------------------------------------------------------
    // D2 (solo local): perfiles v1/v2 y el resolver explícito del integrador
    // ------------------------------------------------------------------

    /** Tarjeta de perfil v1: clase de framework css-a1b2c3 + data-testid. SIEMPRE local. */
    public static String perfilV1() {
        return uriLocal("perfil-v1.html");
    }

    /** Perfil v2: MISMO contenido, un div contenedor extra (rompe XPath absoluto) y clase
     *  regenerada css-x9y8z7 (rompe selector por clase); data-testid intacto. SIEMPRE local. */
    public static String perfilV2() {
        return uriLocal("perfil-v2.html");
    }

    /**
     * Resolver EXPLÍCITO al espejo local, ignorando el flag. Lo usa el integrador
     * (SuiteLocalizacionTest): sus 15 objetivos deben correr SIEMPRE contra las páginas
     * locales, con o sin -Dpaginas.local (estabilidad + los data-testid que the-internet
     * no tiene). Ejemplo: Paginas.local("tabla.html").
     */
    public static String local(String archivo) {
        return uriLocal(archivo);
    }

    // ------------------------------------------------------------------

    /**
     * Convierte un espejo de practice-pages en una URI file:// absoluta y portable.
     * Lo busca por el classpath (target/test-classes/practice-pages/...), no por ruta
     * física, para que funcione igual en cualquier máquina y SO.
     */
    private static String uriLocal(String archivo) {
        String recursoEnClasspath = "practice-pages/" + archivo;
        URL url = Paginas.class.getClassLoader().getResource(recursoEnClasspath);
        if (url == null) {
            throw new IllegalStateException(
                    "No se encontró '" + recursoEnClasspath + "' en el classpath. "
                  + "¿Copiaste los espejos a src/test/resources/practice-pages/?");
        }
        try {
            // Path.toUri() produce la forma canónica file:///... que el navegador acepta.
            return Paths.get(url.toURI()).toUri().toString();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("URI inválida para el espejo local: " + archivo, e);
        }
    }
}
