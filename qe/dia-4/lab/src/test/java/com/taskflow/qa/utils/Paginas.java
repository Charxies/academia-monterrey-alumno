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

    /** Raíz de saucedemo.com (D3): el flujo de tienda del integrador corre contra este sitio
     *  o contra el espejo tienda/ local — mismos locators y mismas credenciales. */
    private static final String BASE_SAUCEDEMO = "https://www.saucedemo.com";

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
    // D3 (nuevas): interacción + esperas. Externo por defecto, local con el flag.
    // MISMO patrón que D1/D2 — ningún test escribe una URL literal. La EXTENSIÓN de
    // D3 se provee COMPLETA (no hay TODOs aquí): es plomería, no lo que se practica hoy.
    // ------------------------------------------------------------------

    /** /inputs (ampliado en local): #nombre PRECARGADO ("Texto de ejemplo") para el bug de
     *  NO limpiar antes de escribir, #edad numérico y #resultado. Usado por MP-1. */
    public static String formulario() {
        return LOCAL ? uriLocal("formulario.html") : BASE_EXTERNA + "/inputs";
    }

    /** /dropdown: <select id="dropdown"> con "Option 1"/"Option 2". En local se AÑADE un
     *  dropdown custom (div+ul, patrón de 2 pasos) que the-internet no tiene. Usado por MP-3. */
    public static String dropdowns() {
        return LOCAL ? uriLocal("dropdowns.html") : BASE_EXTERNA + "/dropdown";
    }

    /** /javascript_alerts: alert/confirm/prompt NATIVOS + #result. Usado por MP-4 e integrador. */
    public static String alertas() {
        return LOCAL ? uriLocal("alertas.html") : BASE_EXTERNA + "/javascript_alerts";
    }

    /** /iframe: editor TinyMCE dentro de un iframe id="mce_0_ifr" (body#tinymce editable).
     *  Usado por MP-5 e integrador. */
    public static String frames() {
        return LOCAL ? uriLocal("frames.html") : BASE_EXTERNA + "/iframe";
    }

    /** /nested_frames: frame-top (left/middle/right) + frame-bottom; el objetivo es frame-middle.
     *  Usado por MP-5. */
    public static String framesAnidados() {
        return LOCAL ? uriLocal("frames-anidados.html") : BASE_EXTERNA + "/nested_frames";
    }

    /** /windows: link "Click Here" que abre una pestaña nueva (title "New Window"). Usado por MP-6. */
    public static String ventanas() {
        return LOCAL ? uriLocal("ventanas.html") : BASE_EXTERNA + "/windows";
    }

    /**
     * /dynamic_loading con retraso REAL: los dos ejemplos del sitio en UNA llamada.
     *   - "oculto"      → ej. 1: #finish EXISTE en el DOM pero oculto (presence pasa ya; visibility espera)
     *   - "inexistente" → ej. 2: #finish NO existe hasta terminar la carga
     * Externo: /dynamic_loading/1 y /2. Local: carga-dinamica.html?modo=... (el file:// acepta query).
     * Usado por MP-7 (anti-patrón sleep) y MP-8 (presence vs visibility).
     */
    public static String cargaDinamica(String modo) {
        if (LOCAL) {
            return uriLocal("carga-dinamica.html") + "?modo=" + modo;
        }
        String ejemplo = "inexistente".equals(modo) ? "2" : "1";
        return BASE_EXTERNA + "/dynamic_loading/" + ejemplo;
    }

    /** /dynamic_controls: remove/add del checkbox (fuente del StaleElementReference) y
     *  enable/disable del input, con retraso real de ~1.5 s. Usado por MP-8. */
    public static String controlesDinamicos() {
        return LOCAL ? uriLocal("controles-dinamicos.html") : BASE_EXTERNA + "/dynamic_controls";
    }

    /**
     * Login de la tienda (espejo de saucedemo.com). Punto de ENTRADA del flujo: el resto de
     * páginas se alcanzan navegando por la propia UI (mismos ids/data-test y credenciales en
     * ambos modos, así que el mismo test corre contra saucedemo o contra tienda/ local).
     * Usado por MP-9 e Integrador (SauceDemoE2ETest).
     */
    public static String tienda() {
        return LOCAL ? uriLocal("tienda/index.html") : BASE_SAUCEDEMO + "/";
    }

    // ------------------------------------------------------------------
    // D3 (solo local): toast.html es la página PRIMARIA de MP-2, no un fallback —
    // the-internet no tiene un equivalente confiable. SIEMPRE local (como perfilV1/V2 de D2).
    // ------------------------------------------------------------------

    /** Página del toast que intercepta clicks (#btn-guardar, #btn-continuar, #toast). SIEMPRE local. */
    public static String toast() {
        return uriLocal("toast.html");
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
