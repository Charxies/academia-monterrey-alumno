# Lab — S2D1: Patrones de diseño + arranque de Spring Boot (IoC/DI)

Dos proyectos, uno por acto del día:

| Carpeta | Qué es | Cuándo se usa |
|---|---|---|
| `consola-s1/` | La consola de S1 **completa y en verde** (estado de AYER) con dos huecos de MP-3. Es la FUENTE de la copia guiada del dominio y la base de los mini-prácticas de la mañana (Repository/Factory/Strategy/Singleton sobre TU propio código). | AM (patrones) |
| `taskflow-api/` | **Red de seguridad**: el proyecto Spring tal como queda tras MP-6 (Initializr) + el dominio ya copiado + los esqueletos de hoy. **El camino principal es GENERAR el tuyo** con Spring Initializr y copiar de TU consola; este starter es para quien se atoró con el wizard o la red. | PM + Integrador |

## Cómo se usa cada uno

```bash
# Consola de S1 (mañana de patrones). mvn test pasa TAL CUAL (los 2 tests de MP-3 están vacíos).
cd consola-s1 && mvn test

# taskflow-api (tarde + integrador). Compila y arranca; los run(...) vacíos no rompen nada.
cd taskflow-api && mvn spring-boot:run
cd taskflow-api && mvn test
```

## El día en dos actos

- **AM — ponerle NOMBRE a lo que ya escribiste (patrones):** en `consola-s1` reconoces Repository
  (`TaskRepository`/`InMemoryTaskRepository`), Factory (`Task.crear`), Strategy (los `Comparator`) y
  creas una estrategia nueva (`TaskOrders.POR_URGENCIA`, MP-3). El Singleton (MP-4) se prueba en vivo
  sobre `InMemoryTaskRepository` y se **revierte con git** — es el puente al contenedor de Spring.
- **PM — conocer al que construye por ti (Spring):** en `taskflow-api` el contenedor construye y
  cablea el dominio de S1 (IoC/DI). Anotas `@Repository`/`@Service`/`@Component`, inyectas por
  constructor y entregas `taskflow-api v0.1` arrancando: siembra 5 tareas y las lista.

> Regla de la cadena: `taskflow-api` usa el package base `com.taskflow`, así el dominio de S1 se
> copia sin tocar un solo import. `Describible` NO viaja; `User` va sin su `implements`;
> `FileTaskRepository`/`CsvTaskParser` no se copian (la persistencia de la API será JPA en D4).
