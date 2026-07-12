package com.taskflow.qa.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Config — carga config.properties del classpath UNA sola vez.
 *
 * Por qué config fuera del código: mismo framework, distintos ambientes. Mañana lo corre
 * GitHub Actions en headless sin tocar una línea Java.
 *
 * Override por System property (la llave de CI): 'mvn test -Dheadless=true' gana sobre el
 * archivo. Regla: -D&lt;clave&gt; siempre pisa a config.properties.
 */
public final class Config {

    private static final Properties PROPS = cargar();

    private Config() {
        // Clase de utilería: no se instancia.
    }

    private static Properties cargar() {
        Properties p = new Properties();
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException(
                        "No se encontró config.properties en el classpath (src/test/resources/).");
            }
            p.load(in);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo cargar config.properties", e);
        }
        return p;
    }

    /** System property primero (override de CI), luego el archivo. */
    private static String get(String clave) {
        return System.getProperty(clave, PROPS.getProperty(clave));
    }

    public static String baseUrl() {
        return get("baseUrl");
    }

    public static String browser() {
        return get("browser");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static int timeoutSeconds() {
        return Integer.parseInt(get("timeoutSeconds"));
    }
}
