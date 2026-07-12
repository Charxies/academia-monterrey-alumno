package com.taskflow.persistence;

import org.junit.jupiter.api.Test;

/**
 * CsvTaskParserTest — tests del parser CSV (ESQUELETO, MP-8).
 *
 * Aquí vive el ciclo ROJO->VERDE del día:
 *   - Los tests de línea válida y round-trip son verde directo una vez que parse/format existan.
 *   - El de "línea corrupta" se escribe EN ROJO PRIMERO: hoy el parser truena feo
 *     (ArrayIndexOutOfBounds / DateTimeParseException). El test exige una excepción CLARA
 *     (IllegalArgumentException con la línea en el mensaje) -> velo rojo -> arregla el parser
 *     (valida nº de columnas) -> velo verde.
 *
 * Cuerpos VACÍOS = verde mentiroso (MP-6). Llénalos.
 */
class CsvTaskParserTest {

    private final CsvTaskParser parser = new CsvTaskParser();

    @Test
    void parse_lineaValida_devuelveTaskConLos8Campos() {
        // TODO MP-8: parse de "3,Implementar API,Endpoints CRUD,IN_PROGRESS,HIGH,1,7,2026-07-20"
        //   -> assertEquals campo por campo (id, título, estado, prioridad, projectId, assignee, fecha).
    }

    @Test
    void roundTrip_parseFormat_conservaLosCampos() {
        // TODO MP-8: crea una Task; parse(format(task)) debe reconstruirla campo por campo.
    }

    @Test
    void parse_lineaConMenosCampos_lanzaIllegalArgumentConLaLinea() {
        // TODO MP-8 (ROJO PRIMERO): línea de 7 campos -> assertThrows(IllegalArgumentException.class,...)
        //   y el mensaje debe CONTENER la línea corrupta. Míralo rojo, arregla el parser, míralo verde.
    }
}
