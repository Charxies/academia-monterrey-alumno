package com.taskflow.demo;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DemoJdbc — la DEMO DEL INSTRUCTOR de AM-1 (D1). NO es práctica del alumno: el alumno la LEE, no la
 * escribe. Un 'main' PLANO, sin Spring, sin JPA: JDBC crudo contra H2 en archivo.
 *
 * El PUNTO de la demo es CONTAR LÍNEAS: ~30 para UN SELECT. Todo esto es lo que dejamos de escribir
 * hoy cuando Spring Data JPA hace el mapeo por nosotros — pero EXISTE debajo de TODO lo que sigue.
 *
 * Ecos de S1D5, explícitos:
 *   - SQLException es CHECKED, como IOException: el compilador obliga a declararla o manejarla.
 *   - try-with-resources cierra Connection/PreparedStatement/ResultSet solos (como el BufferedReader).
 *   - PreparedStatement con '?' (NUNCA concatenar SQL: concatenar = SQL injection).
 *   - ResultSet + while(rs.next()): mapear la fila a un objeto A MANO, columna por columna.
 *
 * Corre con la app DETENIDA (H2 en archivo permite una sola conexión exclusiva). Crea su propia tabla
 * demo_tasks para no tocar el esquema que administra JPA.
 */
public class DemoJdbc {

    // La MISMA URL del yml canónico del día: jdbc:h2:file:./data/taskflow (ruta relativa al working dir).
    private static final String URL = "jdbc:h2:file:./data/taskflow";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void main(String[] args) throws SQLException, TaskValidationException {
        // try-with-resources: la Connection se cierra sola al salir del bloque (eco S1D5).
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            crearTablaSiNoExiste(conn);
            insertarTarea(conn, "Aprender JDBC crudo", TaskStatus.TODO, Priority.HIGH,
                    LocalDate.now().plusDays(3), 1L, 1L);
            insertarTarea(conn, "Contar las líneas de un SELECT", TaskStatus.IN_PROGRESS, Priority.MED,
                    null, 1L, null);

            List<Task> tareas = leerTareas(conn);

            System.out.println("== " + tareas.size() + " tareas leídas con JDBC crudo ==");
            for (Task t : tareas) {
                System.out.println("  " + t);
            }
        }
        // Sin catch: la SQLException checked SUBE al 'throws' del main (nadie por encima puede hacer
        // más que reventar la demo — es un 'main', no una capa con handler global como en la API).
    }

    /** CREATE TABLE de arranque. IF NOT EXISTS para poder correr la demo varias veces. */
    private static void crearTablaSiNoExiste(Connection conn) throws SQLException {
        String ddl = """
                CREATE TABLE IF NOT EXISTS demo_tasks (
                    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                    title       VARCHAR(120) NOT NULL,
                    status      VARCHAR(20)  NOT NULL,
                    priority    VARCHAR(10)  NOT NULL,
                    due_date    DATE,
                    project_id  BIGINT       NOT NULL,
                    assignee_id BIGINT
                )
                """;
        try (PreparedStatement ps = conn.prepareStatement(ddl)) {
            ps.execute();
        }
    }

    /**
     * INSERT con PreparedStatement y '?' (jamás concatenando strings: eso sería SQL injection). Nótese
     * el plomo: setString/setDate/setObject uno por uno, cuidando los null y el enum->texto a mano.
     */
    private static void insertarTarea(Connection conn, String title, TaskStatus status, Priority priority,
                                      LocalDate dueDate, Long projectId, Long assigneeId) throws SQLException {
        String sql = "INSERT INTO demo_tasks (title, status, priority, due_date, project_id, assignee_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, status.name());                                  // enum -> texto A MANO
            ps.setString(3, priority.name());
            ps.setDate(4, dueDate == null ? null : Date.valueOf(dueDate));    // LocalDate -> java.sql.Date
            ps.setLong(5, projectId);
            if (assigneeId == null) {
                ps.setNull(6, java.sql.Types.BIGINT);                         // el null se declara con su tipo
            } else {
                ps.setLong(6, assigneeId);
            }
            ps.executeUpdate();
        }
    }

    /**
     * SELECT + ResultSet: se recorre con while(rs.next()) y CADA fila se mapea a un Task A MANO,
     * columna por columna. ESTO es lo que Spring Data JPA hace por nosotros (mapeo objeto<->fila).
     */
    private static List<Task> leerTareas(Connection conn) throws SQLException, TaskValidationException {
        String sql = "SELECT id, title, status, priority, due_date, project_id, assignee_id "
                + "FROM demo_tasks ORDER BY id";
        List<Task> tareas = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                TaskStatus status = TaskStatus.valueOf(rs.getString("status"));     // texto -> enum
                Priority priority = Priority.valueOf(rs.getString("priority"));
                Date sqlDate = rs.getDate("due_date");
                LocalDate dueDate = sqlDate == null ? null : sqlDate.toLocalDate();
                long projectId = rs.getLong("project_id");
                long assigneeRaw = rs.getLong("assignee_id");
                Long assigneeId = rs.wasNull() ? null : assigneeRaw;               // getLong da 0 en null: hay que preguntar wasNull

                tareas.add(new Task(id, title, "(demo)", status, priority, projectId, assigneeId, dueDate));
            }
        }
        return tareas;
    }
}
