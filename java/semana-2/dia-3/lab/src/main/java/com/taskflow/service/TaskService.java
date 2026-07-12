package com.taskflow.service;

import com.taskflow.exception.TaskNotFoundException;
import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * TaskService — el corazón del integrador de S2D1: la clase NUEVA de hoy.
 *
 * @Service marca esta clase como bean de la capa de negocio. La dependencia se declara en el
 * CONSTRUCTOR (inyección por constructor, la ÚNICA forma del curso): campo 'final', dependencia
 * obligatoria y visible, y la clase se puede construir con 'new' en un test (TaskServiceTest lo
 * hace, SIN arrancar Spring). Con un solo constructor NO hace falta @Autowired.
 *
 * Se depende de la INTERFAZ TaskRepository (no de InMemoryTaskRepository): ese es el as bajo la
 * manga de S2D4, cuando la implementación pase a ser JPA sin tocar este servicio.
 *
 * Las reglas de negocio siguen viviendo en Task (título 3-120, projectId obligatorio, dueDate no
 * en el pasado al crear, no DONE sin assignee); el servicio ORQUESTA, no re-valida.
 *
 * TODO (MP-2..MP-9): evoluciona la capa de negocio de hoy —
 *   - crear(...) EVOLUCIONA: recibe un TaskRequest y el projectId del path (muere el PROYECTO_DEMO=1L)
 *       y arma la entidad con TaskMapper.aEntidadNueva. (Ajusta TaskServiceTest a la nueva firma.)
 *   - reemplazar(Long id, TaskRequest): busca (404 si no) y arma la rehidratación con TaskMapper
 *       .aReemplazo conservando status y projectId actuales; repository.save (upsert).
 *   - eliminar(Long id): existe -> deleteById; no existe -> TaskNotFoundException (advice -> 404).
 *   - cambiarStatus(Long id, TaskStatus): busca (404), aplica setStatus y CAPTURA la checked
 *       TaskValidationException para relanzarla como TaskStateException (advice -> 422).
 *   - completar(Long id) queda DELEGANDO en cambiarStatus(id, DONE) (refactor de MP-9): ya no lanza
 *       la checked. Ajusta completar_sinAssignee en TaskServiceTest para esperar TaskStateException.
 */
@Service
public class TaskService {

    // El capstone exige "no hay Task sin Project"; en S2D1 el proyecto es solo un id demo (como en
    // S1 y S1D3). El CRUD real de Project llega en S2D3/D4.
    private static final Long PROYECTO_DEMO = 1L;

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    /**
     * Crea una tarea pasando por la FACTORY de negocio Task.crear (hoy con nombre: patrón Factory).
     * La factory aplica la regla "dueDate no en el pasado al crear" y nace con status TODO e id null;
     * el repositorio le asigna el id en save(...). projectId = demo, sin assignee (firma canónica
     * del apéndice del capstone).
     */
    public Task crear(String title, String description, Priority priority, LocalDate dueDate)
            throws TaskValidationException {
        Task task = Task.crear(title, description, priority, dueDate, PROYECTO_DEMO, null);
        return repository.save(task);
    }

    /**
     * Lista todas las tareas ordenadas con la estrategia POR_URGENCIA (reuso LITERAL de la Strategy
     * de MP-3): vencidas primero, luego prioridad HIGH->LOW, luego fecha ascendente (nulls al final).
     */
    public List<Task> listar() {
        return repository.findAll().stream()
                .sorted(TaskOrders.POR_URGENCIA)
                .toList();
    }

    /**
     * Completa una tarea (la pasa a DONE). findById(id).orElseThrow(...) trueca el viejo "if == null"
     * por un fallo explícito (el Optional de S1D4). setStatus(DONE) aplica la regla "no DONE sin
     * assignee" de S1D2. El save final hoy es redundante en memoria (misma referencia), pero es el
     * contrato correcto: cuando el repo sea JPA en D4, ESE save es el que persiste.
     */
    public Task completar(Long id) throws TaskValidationException {
        Task task = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setStatus(TaskStatus.DONE);
        return repository.save(task);
    }

    // ==================== NUEVOS DE HOY (S2D2) ====================

    /**
     * Filtra las tareas por estado (MP-5): el stream de S1D4, una línea. '==' entre enums es seguro.
     * El controller decide: ?status= null -> listar(); con valor -> porEstado(status).
     */
    public List<Task> porEstado(TaskStatus status) {
        return repository.findAll().stream()
                .filter(t -> t.getStatus() == status)
                .toList();
    }

    /**
     * Busca una tarea por id (MP-7): delega en el repositorio y deja subir el Optional TAL CUAL.
     * S1D4 paga aquí: el tipo Optional obliga al controller a decidir qué hacer con "no está" — y la
     * respuesta HTTP correcta es 404, no 500 ni null. El service NO sabe de HTTP: solo devuelve datos.
     */
    public Optional<Task> buscarPorId(Long id) {
        return repository.findById(id);
    }

    /**
     * STRETCH — filtro por prioridad (?priority=): MISMO patrón que porEstado. Reutiliza el stream y
     * la conversión automática String->enum del controller.
     */
    public List<Task> porPrioridad(Priority priority) {
        return repository.findAll().stream()
                .filter(t -> t.getPriority() == priority)
                .toList();
    }
}
