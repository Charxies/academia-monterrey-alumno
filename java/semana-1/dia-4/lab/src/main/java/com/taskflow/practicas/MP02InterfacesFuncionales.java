package com.taskflow.practicas;

/**
 * MP-2 — Las 4 interfaces funcionales core con el dominio TaskFlow.
 *
 * Qué construir (importa java.util.function.*):
 *   1. Predicate<Task> esUrgente   -> t.getPriority() == Priority.HIGH && t.getStatus() != TaskStatus.DONE
 *   2. Function<Task,String> aResumen -> "[" + t.getPriority() + "] " + t.getTitle() + " — " + t.getStatus()
 *   3. Consumer<Task> imprimirTarea -> System.out.println("  * " + t.getTitle())
 *   4. Supplier<Task> tareaDemo     -> fabrica una tarea de prueba (Task.crear lanza checked:
 *      envuélvelo en un método privado que relance IllegalStateException; Supplier no declara checked).
 *   5. Úsalas: esUrgente.test(demo), aResumen.apply(demo), imprimirTarea.accept(demo), tareaDemo.get().
 *   6. Composición: esUrgente.and(t -> t.getAssigneeId() != null), esUrgente.negate(),
 *      aResumen.andThen(String::toUpperCase).
 *
 * Correr: mvn -q compile exec:java "-Dexec.mainClass=com.taskflow.practicas.MP02InterfacesFuncionales"
 */
public class MP02InterfacesFuncionales {

    public static void main(String[] args) {
        // TODO 1-4: declara Predicate, Function, Consumer, Supplier del dominio.
        // TODO 5: úsalas (test / apply / accept / get).
        // TODO 6: compón con and / negate / andThen.
        System.out.println("MP-2: pendiente. Predicate, Function, Consumer, Supplier.");
    }
}
