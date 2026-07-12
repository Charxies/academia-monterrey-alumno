# Fallback enlatado — Demo D2 (código generado inseguro/alucinado)

> **Cuándo usarlo:** cuando la demo en vivo NO coopera — el modelo mejoró o ese día responde bien y te
> da código correcto (punto de dolor #4). El punto pedagógico **no depende de la suerte del prompt**:
> "leer / probar / verificar antes de pegar" se enseña igual con estos ejemplos enlatados. Ten esta
> página abierta en una pestaña durante D2. Los tres artefactos también son la kata de MP-2
> (`snippets-auditoria/`).

---

## Acto 1 — SQL injection (concatenación en un query "rápido")

### El prompt en vivo (intenta primero)
> *"Dame un método de repositorio en Spring para buscar tareas por título; algo rápido con una query
> nativa."*

Si el modelo devuelve algo seguro (parametrizado o query derivada), **cambia al enlatado** y di:
"así se ve cuando NO tienes suerte — y no puedes construir tu criterio sobre la suerte".

### El enlatado (lo que Copilot ofrece con total confianza)

```java
@Repository
public class TaskSearchRepository {
    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<Task> buscarPorTitulo(String titulo) {
        String sql = "SELECT * FROM tasks WHERE title LIKE '%" + titulo + "%'";
        return em.createNativeQuery(sql, Task.class).getResultList();
    }
}
```

### Por qué está mal (ellos ya lo saben desde S2D4 — la novedad es que la herramienta lo ofreció seguro de sí)

El `titulo` del usuario se **concatena** al SQL. Deja de ser un dato y se vuelve **código ejecutable**.

**Demostrable sin BD** (proyéctalo — verificado, este es el output literal):

| Entrada | SQL resultante |
|---|---|
| `login` | `SELECT * FROM tasks WHERE title LIKE '%login%'` |
| `x%'; DROP TABLE tasks; --` | `SELECT * FROM tasks WHERE title LIKE '%x%'; DROP TABLE tasks; --%'` |

La entrada maliciosa **cierra la comilla**, **termina la sentencia** con `;` y **añade un
`DROP TABLE tasks`**. El `--` comenta el resto. No hace falta una BD para verlo: la cadena que se le
mandaría al motor ya trae el `DROP` como sentencia aparte.

> Guion: *"Esto no es teórico. La cadena que sale ya contiene `DROP TABLE tasks` como una sentencia
> ejecutable. Contra una BM real, con permisos, se ejecuta. Y Copilot lo escribió sin una sola señal de
> alarma."*

### El arreglo (escríbelo en vivo)

**Opción A — query nativa con parámetro nombrado** (mínima, misma forma):
```java
public List<Task> buscarPorTitulo(String titulo) {
    return em.createNativeQuery(
                "SELECT * FROM tasks WHERE title LIKE :patron", Task.class)
            .setParameter("patron", "%" + titulo + "%")
            .getResultList();
}
```
Ahora `titulo` viaja como **dato bindeado**: el motor nunca lo interpreta como SQL.

**Opción B — query derivada de Spring Data** (lo idiomático que ya usan):
```java
// en CommentRepository/TaskRepository extends JpaRepository<Task, Long>
List<Task> findByTitleContainingIgnoreCase(String titulo);
```
Cero SQL escrito a mano → cero superficie de inyección.

**El cierre:** *"Ambas versiones las conocen desde S2. El punto de hoy no es aprender JPA — es que la
herramienta les ofreció la versión insegura con la misma confianza que la segura. El filtro son
ustedes."*

---

## Acto 2 — Dependencia Maven alucinada (*slopsquatting*)

### El prompt en vivo (intenta primero)
> *"Resuélveme el envío de correo en Spring Boot; dame la dependencia de Maven y cómo mandar un mail."*

### El enlatado (lo que Copilot sugiere para el `pom.xml`)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-email</artifactId>
    <version>3.2.0</version>
</dependency>
```

Suena impecable: el `groupId` es el real de Spring Boot, el patrón `spring-boot-starter-*` es el
correcto, la versión es plausible. **Y no existe.**

### La verificación en vivo (búscalo en search.maven.org — hazlo en cámara)

Ve a [search.maven.org](https://search.maven.org) (o `central.sonatype.com`) y busca la coordenada.
Resultado **verificado** (Maven Central REST API, 2026-07 — reproducible):

```
g:org.springframework.boot  a:spring-boot-starter-email   -> numFound 0   (NO EXISTE)
g:org.springframework.boot  a:spring-boot-starter-mail    -> 236 versiones (SÍ existe: el real)
```

Y el remate de *slopsquatting* (esto es lo que da miedo, dilo despacio):

```
a:spring-boot-starter-email  (cualquier groupId) -> SÍ hay un registro:
    com.github.luues:spring-boot-starter-email:1.3.0.5.RELEASE
```

> Guion: *"El nombre que Copilot alucinó **está registrado** en Central — pero por un tercero
> desconocido, no por Spring. Si yo verifico solo el `artifactId` ('¿existe spring-boot-starter-email?
> sí'), lo doy por bueno y meto al build el código de un extraño. Eso es **slopsquatting**: alguien
> registra a propósito los nombres que los modelos alucinan, esperando que alguien los pegue sin mirar
> el groupId. Verificar significa la **coordenada completa** `groupId:artifactId:version`, no el nombre
> suelto."*

### El arreglo

- El real es `org.springframework.boot:spring-boot-starter-mail` (versión gestionada por el BOM del
  parent, así que **ni lleva `<version>`**).
- **Y para el capstone (notificaciones):** ni siquiera eso. El alcance mínimo es **log-based, sin
  ninguna dependencia nueva** (ver `features/notificaciones.md`). La mejor respuesta a "resuélveme el
  correo" en este alcance es **rechazar la dependencia entera**.

> Puente al capstone: *"Las parejas de notificaciones van a ver ESTA sugerencia otra vez esta tarde, en
> su propio `pom.xml`. Cuando aparezca, ya saben: verificar la coordenada… o darse cuenta de que su
> alcance no la necesita."*

---

## Acto 3 (opcional, 30 s) — la versión-fantasma

Variante del mismo error, por si preguntan "¿y si el artefacto sí existe?": el artefacto real con una
**versión que no existe**. Verificado:

```
g:org.springframework.boot a:spring-boot-starter-mail v:9.9.9  -> numFound 0  (versión fantasma)
```

Mismo antídoto: verificar la **coordenada completa**, incluida la versión, contra Central.

---

## Resumen para el semáforo (T2)

Los tres actos son el mismo hábito visto en tres superficies: **código** (inyección), **dependencias**
(slopsquatting) y **versión** (fantasma). En los tres, la herramienta produjo algo con total confianza
y el criterio humano —leer, probar, verificar en la fuente— fue lo único que lo atrapó. Eso es el 🟡/🔴
del semáforo del criterio.
