package com.taskflow.persistence;

import com.taskflow.model.Task;
import com.taskflow.repository.InMemoryTaskRepository;
import com.taskflow.repository.TaskRepository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * FileTaskRepository — repositorio con PERSISTENCIA a archivo CSV (ESQUELETO — integrador).
 *
 * Diseño canónico del día: implementa {@link TaskRepository} y COMPONE un
 * {@link InMemoryTaskRepository} (el CRUD ya probado) + un {@link CsvTaskParser} (la traducción
 * texto&lt;-&gt;Task). El CRUD ya está DELEGADO en memoria (abajo). Tu trabajo del integrador es
 * mover datos entre memoria y disco: load() al arrancar, save() al salir.
 *
 * Regla de la capa de persistencia: la IOException (checked) se maneja AQUÍ, con mensaje claro;
 * el menú nunca debe morir por un problema de archivo.
 *
 * El esqueleto COMPILA (load/save devuelven 0). Tu trabajo: llenar los TODO.
 */
public class FileTaskRepository implements TaskRepository {

    private final Path csvPath;
    // Composición: el repo en memoria hace el CRUD; el parser traduce cada línea.
    private final InMemoryTaskRepository memoria = new InMemoryTaskRepository();
    private final CsvTaskParser parser = new CsvTaskParser();

    public FileTaskRepository(Path csvPath) {
        this.csvPath = csvPath;
    }

    // ---- CRUD del contrato: DELEGACIÓN pura en el repo en memoria (ya resuelto) ----

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

    // ---- Persistencia: lo que TÚ implementas en el integrador ----

    /**
     * TODO Integrador — load: carga el CSV al repositorio en memoria. Devuelve cuántas cargó.
     *
     * Pasos:
     *   1. Si !Files.exists(csvPath) -> avisa "sin datos previos" y return 0 (arranca vacío, NO crashea).
     *   2. Files.readAllLines(csvPath) y parsea con STREAMS (los de D4):
     *        lineas.stream().skip(1).filter(l -> !l.isBlank()).map(parser::parse).toList();
     *   3. Guarda cada tarea: tareas.forEach(memoria::save);   // id explícito -> avanza la secuencia
     *   4. Atrapa la IOException AQUÍ (mensaje claro) y arranca vacío: el menú sobrevive.
     */
    public int load() {
        // TODO Integrador (usa parser + memoria + Files.readAllLines)
        return 0;
    }

    /**
     * TODO Integrador — save (snapshot): escribe TODAS las tareas al CSV. Devuelve cuántas guardó.
     *
     * Pasos:
     *   1. List<Task> tareas = memoria.findAll();
     *   2. Files.createDirectories(csvPath.getParent()) por si data/ no existe.
     *   3. try (BufferedWriter w = Files.newBufferedWriter(csvPath)) { ... }  // try-with-resources:
     *      escribe ENCABEZADO + newLine(), luego una línea parser.format(t) + newLine() por tarea.
     *      (El cierre automático hace flush; sin él el archivo saldría vacío -> error intencional #2.)
     *   4. Atrapa la IOException AQUÍ; devuelve -1 si falló.
     *
     * Nota: esta es la sobrecarga sin argumentos save(); NO choca con save(Task) del contrato
     * (Java las distingue por su firma — overloading).
     */
    public int save() {
        // TODO Integrador (try-with-resources + Files.newBufferedWriter + parser.format)
        return 0;
    }
}
