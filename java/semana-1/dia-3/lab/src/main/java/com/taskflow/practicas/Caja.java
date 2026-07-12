package com.taskflow.practicas;

/**
 * Caja&lt;T&gt; — clase genérica mínima (esqueleto de MP-9).
 *
 * El &lt;T&gt; es un parámetro de tipo: al crear la caja decides QUÉ guarda (Caja&lt;Task&gt;,
 * Caja&lt;String&gt;) y el compilador vigila que solo entre y salga ese tipo, sin repetir la
 * clase por cada tipo. Es la misma idea detrás de List&lt;Task&gt; o Map&lt;Long,Task&gt;.
 *
 * El esqueleto COMPILA. Tu trabajo: llenar los TODO.
 */
public class Caja<T> {

    private T contenido;

    /** TODO: guarda el elemento -> this.contenido = contenido; */
    public void guardar(T contenido) {
        // TODO
    }

    /** TODO: devuelve el elemento guardado (del tipo T, sin casts). */
    public T sacar() {
        // TODO
        return null;
    }
}
