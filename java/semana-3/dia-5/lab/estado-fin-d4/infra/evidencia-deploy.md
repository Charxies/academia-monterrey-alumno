# Evidencia de despliegue — TaskFlow en AWS (S3D3) · **REFERENCIA**

> Ejemplo lleno de la corrida del instructor: sirve como **referencia de formato**. Los valores
> (IPs, endpoint, horas) serán distintos para cada alumno. **La evidencia ES el entregable
> duradero: en menos de una hora nada de esto existió.** Commit: `feat: taskflow desplegada en aws -
> ec2 + rds (evidencia)`.

## Datos del despliegue

| Campo | Valor (ejemplo) |
|---|---|
| Alumno | Ana García |
| Región | `us-east-1` |
| Fecha / hora del despliegue | 2026-07-11 11:20 (America/Mexico_City, UTC-6) |
| URL pública del Swagger | `http://54.87.123.45:8080/swagger-ui/index.html` |
| Instancia EC2 | `taskflow-ec2` · IP pública `54.87.123.45` |
| Endpoint RDS | `taskflow-db.abcd1234efgh.us-east-1.rds.amazonaws.com` |
| Imagen desplegada | `taskflow-api:local` · IMAGE ID `sha256:9f3c…a71b` (el MISMO que en la laptop) |

## Screenshots (obligatorias)

1. **Budget activo** — presupuesto de $5 con alerta por email al 50% y 100% (MP-1).
   `![budget](img/budget.png)`
2. **Swagger PÚBLICO servido desde la EC2** — barra del navegador con `http://54.87.123.45:8080/...`.
   `![swagger](img/swagger-publico.png)`
3. **Run de Postman en verde** — colección v2 contra `{{baseUrl}} = http://54.87.123.45:8080`.
   `![postman](img/postman-run.png)`
4. **Persistencia contra RDS (doble)** — tarea creada → `docker rm -f taskflow` + re-run del MISMO
   comando → la tarea **sigue ahí** (vive en RDS, no en el contenedor).
   `![antes](img/datos-antes.png)  ![despues](img/datos-despues-rerun.png)`
5. **Prueba social (consumo cruzado)** — usuario `ana` registrado en la API de Luis + tarea
   "saludos desde ana".
   `![prueba-social](img/prueba-social.png)`

## La moraleja, en tus palabras

- **¿Por qué mover la API de `compose` a RDS no tocó código?** Porque la configuración vive en el
  ambiente (12-factor): la URL de la BD, el usuario y el secret llegan por variables de entorno con
  defaults. Para RDS solo se sobreescribió `DB_HOST` (y las credenciales/`JWT_SECRET`) en el
  `docker run`; el jar y la imagen son los mismos, sin recompilar.
- **`timeout` vs `connection refused`:** *timeout* = el paquete salió y nadie contestó (un firewall/SG
  se lo comió en silencio → faltaba la regla SG→SG en `taskflow-rds-sg`). *refused* = el host contestó
  "aquí no escucha nadie" (puerto equivocado o la BD apagada).

## Cierre — recursos destruidos

| Recurso | Acción | Hora |
|---|---|---|
| EC2 `taskflow-ec2` | **Terminated** | 14:35 |
| RDS `taskflow-db` | **Deleted** (sin snapshot final) | 14:38 |
| Bucket S3 `taskflow-anagarcia-reportes` | Vaciado y **borrado** | 14:40 |
| Budget de $5 | **SIGUE VIVO** (guardián) ✅ | — |

**Recursos destruidos a las 14:40.** Commit de cierre: `chore: limpieza aws d3 completa`.
