package com.taskflow.persistence;

import com.taskflow.model.Task;
import com.taskflow.repository.InMemoryTaskRepository;
import com.taskflow.repository.TaskRepository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * FileTaskRepository — repositorio con PERSISTENCIA a archivo CSV (SOLUCIÓN del integrador).
 *
 * Diseño canónico del día: implementa {@link TaskRepository} y COMPONE un
 * {@link InMemoryTaskRepository} (el CRUD rápido, ya probado) + un {@link CsvTaskParser}
 * (la traducción texto&lt;-&gt;Task). El CRUD se DELEGA en memoria; lo único propio de esta clase
 * es mover datos entre la memoria y el disco: load() al arrancar, save() al salir.
 *
 * Regla de la capa de persistencia: la IOException (checked) se maneja AQUÍ, con un mensaje
 * claro. El menú nunca muere por un problema de archivo — a lo sumo arranca vacío o avisa que
 * no pudo guardar.
 */
public class FileTaskRepository implements TaskRepository {

    private final Path csvPath;
    // Composición: el repo en memoria hace el CRUD; el parser traduce cada línea.
    private final InMemoryTaskRepository memoria = new InMemoryTaskRepository();
    private final CsvTaskParser parser = new CsvTaskParser();

    public FileTaskRepository(Path csvPath) {
        this.csvPath = csvPath;
    }

    // ---- CRUD del contrato: DELEGACIÓN pura en el repo en memoria ----

    @Override
    public Task save(Task task) {
        return memoria.save(task);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return memoria.findById(id);
    }

    @Override
    public List<Task> findAll() {
        return memoria.findAll();
    }

    @Override
    public boolean deleteById(Long id) {
        return memoria.deleteById(id);
    }

    // ---- Persistencia: lo propio de esta implementación ----

    /**
     * Carga el CSV al repositorio en memoria. Devuelve cuántas tareas cargó.
     *
     *   - Si el archivo NO existe -> arranca vacío SIN crashear y avisa "sin datos previos".
     *   - Si existe -> lo lee con Files.readAllLines y lo parsea con STREAMS (los de D4):
     *       lines.stream().skip(1)              // salta el encabezado
     *            .filter(l -> !l.isBlank())      // ignora líneas en blanco
     *            .map(parser::parse)             // cada línea -> Task (rehidratación)
     *            .toList();
     *     y guarda cada tarea con save(...) usando su id EXPLÍCITO: el upsert de D3 avanza la
     *     secuencia al máximo id visto, así una tarea nueva no chocará con los ids cargados.
     *   - La IOException (archivo ilegible) y una línea corrupta (el parser lanza
     *     IllegalArgumentException con la línea culpable) se atrapan AQUÍ, con mensaje claro, y se
     *     arranca vacío: el menú sobrevive (nunca muere por un problema de archivo). El .toList()
     *     corre ANTES del forEach(memoria::save), así una línea corrupta NO deja el repo a medias.
     */
    public int load() {
        if (!Files.exists(csvPath)) {
            System.out.println("(sin datos previos: no existe " + csvPath + " — arranco vacío)");
            return 0;
        }
        try {
            List<String> lineas = Files.readAllLines(csvPath);
            List<Task> tareas = lineas.stream()
                    .skip(1)                          // encabezado
                    .filter(l -> !l.isBlank())
                    .map(parser::parse)
                    .toList();
            tareas.forEach(memoria::save);            // id explícito -> avanza la secuencia
            return tareas.size();
        } catch (IOException e) {
            System.out.println("No se pudo leer " + csvPath + ": " + e.getMessage()
                    + " — arranco vacío.");
            return 0;
        } catch (IllegalArgumentException e) {
            // Línea corrupta (columnas != 8, id/enum/fecha inválidos): el parser la reempaqueta como
            // IllegalArgumentException con la línea. No dejamos que tumbe el arranque del menú.
            System.out.println("Datos corruptos en " + csvPath + ": " + e.getMessage()
                    + " — arranco vacío.");
            return 0;
        }
    }

    /**
     * Escribe el SNAPSHOT COMPLETO de la memoria al CSV (sobrescribe el archivo). Devuelve
     * cuántas tareas guardó, o -1 si falló la escritura.
     *
     * Sobrescribir = guardar la foto entera cada vez (no un append incremental): simple y sin
     * duplicados. Usa try-with-resources sobre newBufferedWriter para que el archivo SIEMPRE se
     * cierre y haga flush (el "archivo vacío por no cerrar" del error intencional #2 no ocurre).
     *
     * Nota sobre el nombre: esta es la sobrecarga sin argumentos save(); NO choca con el
     * save(Task) del contrato — Java las distingue por su firma (overloading).
     */
    public int save() {
        List<Task> tareas = memoria.findAll();
        try {
            if (csvPath.getParent() != null) {
                Files.createDirectories(csvPath.getParent());   // crea data/ si no existía
            }
            try (BufferedWriter writer = Files.newBufferedWriter(csvPath)) {
                writer.write(CsvTaskParser.ENCABEZADO);
                writer.newLine();
                for (Task t : tareas) {
                    writer.write(parser.format(t));
                    writer.newLine();
                }
            } // <- aquí se cierra y se hace flush, pase lo que pase
            return tareas.size();
        } catch (IOException e) {
            System.out.println("No se pudo guardar en " + csvPath + ": " + e.getMessage());
            return -1;
        }
    }
}
