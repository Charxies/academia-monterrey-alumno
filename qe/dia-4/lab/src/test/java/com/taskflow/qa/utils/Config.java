package com.taskflow.qa.utils;

/**
 * Config (MP-7) — carga config.properties del classpath UNA vez.
 *
 * Por qué config FUERA del código: mismo framework, distintos ambientes (mañana CI en headless
 * sin tocar Java). Override por System property: 'mvn test -Dheadless=true' gana sobre el archivo.
 *
 * TODO MP-7: cargar el Properties desde el classpath ("config.properties") en un bloque estático
 *            y devolver cada valor con System.getProperty(clave, PROPS.getProperty(clave)).
 */
public final class Config {

    private Config() {
        // Clase de utilería: no se instancia.
    }

    public static String baseUrl() {
        // TODO MP-7
        throw new UnsupportedOperationException("TODO MP-7");
    }

    public static String browser() {
        // TODO MP-7
        throw new UnsupportedOperationException("TODO MP-7");
    }

    public static boolean headless() {
        // TODO MP-7
        throw new UnsupportedOperationException("TODO MP-7");
    }

    public static int timeoutSeconds() {
        // TODO MP-7
        throw new UnsupportedOperationException("TODO MP-7");
    }
}
