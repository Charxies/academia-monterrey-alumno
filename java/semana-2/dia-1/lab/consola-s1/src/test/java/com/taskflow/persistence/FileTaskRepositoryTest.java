package com.taskflow.persistence;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FileTaskRepositoryTest — STRETCH: round-trip completo a ARCHIVO con @TempDir (SOLUCIÓN).
 *
 * @TempDir le pide a JUnit una carpeta temporal (que borra al terminar): así probamos escritura +
 * lectura reales SIN tocar el data/tasks.csv del proyecto (punto de dolor #10: nunca corromper la
 * semilla del integrador desde los tests).
 *
 * Prueba la promesa del integrador: guardar -> "reabrir" (un repo nuevo apuntando al mismo archivo)
 * -> las tareas siguen ahí, y la secuencia de ids respeta lo cargado.
 */
class FileTaskRepositoryTest {

    @Test
    void save_luegoLoadEnRepoNuevo_persisteLasTareas(@TempDir Path carpetaTemp) throws TaskValidationException {
        Path csv = carpetaTemp.resolve("tasks.csv");

        // 1) Repo A: agrega 2 tareas y guarda a disco.
        FileTaskRepository repoA = new FileTaskRepository(csv);
        repoA.save(new Task(null, "Persistente 1", "d", TaskStatus.TODO, Priority.HIGH, 1L, 1L, null));
        repoA.save(new Task(null, "Persistente 2", "d", TaskStatus.DONE, Priority.LOW, 1L, 2L, null));
        int guardadas = repoA.save();   // snapshot a disco

        assertEquals(2, guardadas);
        assertTrue(Files.exists(csv), "el CSV debe existir tras guardar");

        // 2) Repo B (simula reabrir la app): carga del MISMO archivo.
        FileTaskRepository repoB = new FileTaskRepository(csv);
        int cargadas = repoB.load();

        assertEquals(2, cargadas);
        assertEquals(2, repoB.findAll().size());
        assertTrue(repoB.findById(1L).isPresent());   // el id 1 sobrevivió al viaje a disco
    }

    @Test
    void load_archivoInexistente_arrancaVacioSinCrashear(@TempDir Path carpetaTemp) {
        Path csv = carpetaTemp.resolve("no-existe.csv");

        FileTaskRepository repo = new FileTaskRepository(csv);
        int cargadas = repo.load();   // no debe lanzar: arranca vacío

        assertEquals(0, cargadas);
        assertTrue(repo.findAll().isEmpty());
    }

    @Test
    void load_lineaCorrupta_arrancaVacioSinCrashear(@TempDir Path carpetaTemp) throws Exception {
        // Una description con coma de más -> 9 columnas -> línea CORRUPTA. Simula el CSV editado a
        // mano (o una tarea agregada con coma en la descripción, pese al aviso). load() NO debe
        // tumbar el arranque del menú (DoD: "el menú nunca muere por un problema de archivo").
        Path csv = carpetaTemp.resolve("corrupto.csv");
        Files.writeString(csv, CsvTaskParser.ENCABEZADO + System.lineSeparator()
                + "1,Con coma,tiene, una coma,TODO,LOW,1,1," + System.lineSeparator());

        FileTaskRepository repo = new FileTaskRepository(csv);
        int cargadas = repo.load();   // no debe lanzar pese a la línea corrupta

        assertEquals(0, cargadas);
        assertTrue(repo.findAll().isEmpty());
    }
}
