# Academia Monterrey — Repositorio del alumno 🎓

Proyectos **base** de la Academia **Backend · QE · GitHub Copilot** (5 semanas). Aquí está el andamiaje (starter) de cada taller para que arranques sin fricción. Toda la teoría, quizzes, entregables y el certificado viven en **Moodle**.

> Las **soluciones de referencia** NO están en este repo: se comentan y liberan en clase.

---

## 🚀 Cómo usar este repositorio (una sola vez)

1. **Haz fork** de este repo a tu cuenta (botón *Fork* arriba a la derecha). Ese fork es **tu** repositorio de trabajo.
2. **Clónalo una vez** en tu máquina:
   ```bash
   git clone https://github.com/<tu-usuario>/academia-monterrey-alumno.git
   cd academia-monterrey-alumno
   ```
3. **Trabaja y entrega desde tu fork.** El entregable de cada día es un **commit + push** a tu repositorio:
   ```bash
   git add .
   git commit -m "feat: taskflow cli v0 - dia 1"
   git push
   ```
   En Moodle pegas el enlace a tu repo/commit en la tarea del día.

Con eso ya está: **un solo clone**, y tu proyecto crece toda la semana en tu propio repo.

---

## 🧩 ¿Dónde está el proyecto base de cada taller?

Cada día tiene su base en la carpeta correspondiente. **Ábrela, verás que ya compila y tiene los `// TODO` que debes completar.**

| Track | Carpeta | Ejemplo |
|---|---|---|
| Java — Semana 1 (Core) | `java/semana-1/dia-N/lab/` | `java/semana-1/dia-1/lab/` |
| Java — Semana 2 (Spring) | `java/semana-2/dia-N/lab/` | `java/semana-2/dia-3/lab/` |
| Java — Semana 3 (Calidad/Docker/Cloud) | `java/semana-3/dia-N/lab/` | `java/semana-3/dia-2/lab/` |
| QE — Semana 4 (Automation) | `qe/dia-N/lab/` | `qe/dia-4/lab/` |
| Copilot — Semana 5 | `copilot/dia-N/` | `copilot/dia-2/lab/` |

> **Los base son acumulativos:** el de cada día ya trae listo lo de los días anteriores, para que puedas empezar el tema de hoy aunque te hayas atorado ayer. Tu flujo normal es seguir en **tu** repo; la carpeta del día es tu red de seguridad si necesitas la base limpia.

### Assets provistos (no los construyes, los usas)
- `recursos/taskflow-ui/` — el frontend de TaskFlow (lo automatizas en QE).
- `qe/recursos/practice-pages/` — gimnasio de práctica de Selenium (formularios, tablas, frames, alertas, mini-tienda).

---

## 🛠️ Requisitos

- **JDK 21** y **Maven** (todas las semanas).
- **Docker Desktop** (Semana 3).
- **Chrome** actualizado (Semana 4 — el driver lo resuelve Selenium Manager, no instalas nada).
- Cuenta de **GitHub** (tu repo) y, en Semana 5, licencia de **GitHub Copilot**.

Guía de instalación paso a paso: `recursos/guia-dia-0-ambiente.md`.

## ▶️ Cómo correr un taller

```bash
cd java/semana-1/dia-1/lab
mvn compile        # compila
mvn test           # corre los tests
```

---

## 📚 El hilo conductor: TaskFlow

Un solo proyecto que crece las 5 semanas: **CLI en consola → API REST con JWT → dockerizada y desplegada con CI/CD → automatizada por su framework de QE → extendida con IA (Copilot)**. Cada día avanza este capstone.

**Todo el curso (teoría, videos-guía, quizzes, entregables, insignias y certificado) está en Moodle.**
