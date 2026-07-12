# Evidencia de despliegue — TaskFlow en AWS (S3D3)

> **La evidencia ES el entregable duradero: en menos de una hora nada de esto existirá.** Llena esta
> plantilla durante el integrador y commitéala con
> `feat: taskflow desplegada en aws - ec2 + rds (evidencia)`. Las screenshots van en `infra/img/`
> (crea la carpeta) y se enlazan abajo.

## Datos del despliegue

| Campo | Valor |
|---|---|
| Alumno | `TODO: <nombre>` |
| Región | `us-east-1` |
| Fecha / hora del despliegue | `TODO: 2026-__-__ HH:MM` (zona: `TODO`) |
| URL pública del Swagger | `TODO: http://<IP>:8080/swagger-ui/index.html` |
| Instancia EC2 | `taskflow-ec2` · IP pública `TODO: __.__.__.__` |
| Endpoint RDS | `TODO: taskflow-db.____.us-east-1.rds.amazonaws.com` |
| Imagen desplegada | `taskflow-api:local` · IMAGE ID `TODO: ____` (el MISMO que en la laptop) |

## Screenshots (obligatorias)

1. **Budget activo** — el presupuesto de $5 con la alerta por email configurada (MP-1).
   `TODO: ![budget](img/budget.png)`
2. **Swagger PÚBLICO servido desde la EC2** — barra del navegador con `http://<IP>:8080/...` visible.
   `TODO: ![swagger](img/swagger-publico.png)`
3. **Run de Postman en verde** — la colección v2 corrida contra `http://<IP>:8080` (`{{baseUrl}}`).
   `TODO: ![postman](img/postman-run.png)`
4. **Persistencia contra RDS (screenshot DOBLE)** — una tarea creada, luego `docker rm -f taskflow`
   + re-run del MISMO comando, y la tarea **sigue ahí** (vive en RDS, no en el contenedor).
   `TODO: ![antes](img/datos-antes.png)  ![despues](img/datos-despues-rerun.png)`
5. **Prueba social (consumo cruzado)** — tu usuario registrado en la API de un compañero (o el suyo
   en la tuya) + una tarea "saludos desde `<tu-nombre>`".
   `TODO: ![prueba-social](img/prueba-social.png)`

## La moraleja, en tus palabras (1-2 líneas)

- **¿Por qué mover la API de `compose` a RDS no tocó código?**
  `TODO: ____________________________________________`
- **`timeout` vs `connection refused`, en una frase cada uno:**
  `TODO: ____________________________________________`

## Cierre — recursos destruidos

> Se llena al terminar la limpieza (`checklist-limpieza-aws.md`). Sin esta fila, el despliegue no
> está cerrado.

| Recurso | Acción | Hora |
|---|---|---|
| EC2 `taskflow-ec2` | **Terminated** | `TODO: HH:MM` |
| RDS `taskflow-db` | **Deleted** (sin snapshot final) | `TODO: HH:MM` |
| Bucket S3 `taskflow-…-reportes` | Vaciado y **borrado** | `TODO: HH:MM` |
| Budget de $5 | **SIGUE VIVO** (guardián) ✅ | — |

**Recursos destruidos a las `TODO: HH:MM`.** Commit de cierre: `chore: limpieza aws d3 completa`.
