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
 *   - Por defecto            -> sitio externo the-internet.herokuapp.com
 *   - Con -Dpaginas.local=true -> archivos file:// de src/test/resources/practice-pages/,
 *                                 resueltos por el CLASSPATH (multiplataforma).
 *
 * Es el helper que D2 y D3 EXTIENDEN (le agregan métodos), sin renombrarlo.
 * Hoy solo necesita home() y login().
 *
 * Ya te dejamos resuelto el pedazo difícil: uriLocal(...) (resolución por classpath).
 * Tú completas home() y login().
 */
public final class Paginas {

    /** Raíz del sitio externo (sin barra final). */
    private static final String BASE_EXTERNA = "https://the-internet.herokuapp.com";

    /** Flag ÚNICO de la semana. -Dpaginas.local=true activa el fallback local. */
    private static final boolean LOCAL = Boolean.getBoolean("paginas.local");

    private Paginas() {
        // Clase de utilería: no se instancia.
    }

    /** Home del sitio de práctica (título "The Internet", h1.heading, lista de links). */
    public static String home() {
        // TODO 1: devuelve la home.
        //   - Si LOCAL es true  -> uriLocal("index.html")
        //   - Si LOCAL es false -> BASE_EXTERNA + "/"
        //   Pista: return LOCAL ? uriLocal("index.html") : BASE_EXTERNA + "/";
        throw new UnsupportedOperationException("TODO 1: implementa Paginas.home()");
    }

    /** Página de login (h2 "Login Page"). El assert de navegación usará contains("login"),
     *  verdadero tanto para .../login (externo) como para .../login.html (local). */
    public static String login() {
        // TODO 2: devuelve la página de login (espejo "login.html" en local,
        //   BASE_EXTERNA + "/login" en externo). Mismo patrón que home().
        throw new UnsupportedOperationException("TODO 2: implementa Paginas.login()");
    }

    /**
     * Convierte un espejo de practice-pages en una URI file:// absoluta y portable.
     * Lo busca por el classpath (target/test-classes/practice-pages/...), no por ruta
     * física, para que funcione igual en cualquier máquina y SO. (YA RESUELTO.)
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
