# Topología AWS de TaskFlow — el dibujo del día (VPC conceptual)

> Plantilla. Complétala con **TUS ids reales** (los `TODO: …`). En **Plan A** es tu evidencia de
> arquitectura; en **Plan B** es EL entregable: el diseño AWS que construirías, con SGs, subnets y
> porqués. Hoy usamos la **VPC default** de la cuenta (todas sus subnets son públicas): no se
> construye VPC custom — se DIBUJA para entender dónde vive cada pieza.

## Datos de mi corrida (rellenar)

| Dato | Valor |
|---|---|
| Región | `us-east-1`  *(no moverse)* |
| VPC | VPC **default** de la cuenta — `TODO: vpc-________` |
| IP pública de la EC2 | `TODO: __.__.__.__` |
| Security Group de la EC2 | `taskflow-ec2-sg` — `TODO: sg-________` |
| Endpoint del RDS | `TODO: taskflow-db.________.us-east-1.rds.amazonaws.com` |
| Security Group del RDS | `taskflow-rds-sg` — `TODO: sg-________` |
| Bucket S3 | `TODO: taskflow-________-reportes` |

## El dibujo canónico (completar los ids)

```
                         INTERNET
                            │
                            │  HTTP :8080  (Swagger PÚBLICO, a propósito — ambiente desechable)
                            ▼
        ┌───────────────────────────────────────────────────────┐
        │  VPC default (us-east-1)   TODO: vpc-________          │
        │                                                        │
        │   ┌────────────── subnet PÚBLICA ──────────────────┐   │
        │   │  (ruta al Internet Gateway)                    │   │
        │   │                                                │   │
        │   │   [SG: taskflow-ec2-sg  TODO: sg-________ ]    │   │
        │   │      ├─ inbound 22   (SSH)  ← SOLO My IP       │   │
        │   │      └─ inbound 8080 (HTTP) ← 0.0.0.0/0        │   │
        │   │   ┌────────────────────────────────────────┐   │   │
        │   │   │  EC2  taskflow-ec2  (t3.micro)         │   │   │
        │   │   │  IP pública TODO: __.__.__.__          │   │   │
        │   │   │  Docker → contenedor 'taskflow'        │   │   │
        │   │   │  (imagen taskflow-api:local, perfil    │   │   │
        │   │   │   'docker')                            │   │   │
        │   │   └───────────────┬────────────────────────┘   │   │
        │   │                   │  TCP :5432                  │   │
        │   └───────────────────┼─────────────────────────────┘  │
        │                       ▼                                 │
        │   [SG: taskflow-rds-sg  TODO: sg-________ ]             │
        │      └─ inbound 5432 ← source = taskflow-ec2-sg  (¡el SG, NO una IP!)
        │   ┌────────────────────────────────────────────────┐   │
        │   │  RDS  taskflow-db  (PostgreSQL 16, Single-AZ)  │   │
        │   │  Public access: NO  → SIN IP pública           │   │
        │   │  endpoint TODO: taskflow-db.____.rds.amazonaws.com │
        │   └────────────────────────────────────────────────┘   │
        └───────────────────────────────────────────────────────┘

     S3 (fuera de la VPC, servicio global):
        bucket TODO: taskflow-________-reportes   Block Public Access: ON
        → se comparte con PRESIGNED URL (compartir ≠ publicar)
```

## Tabla de decisiones (completar la última columna con TUS ids donde aplique)

| Pieza | Dónde vive | Qué SG la cuida | Por qué así |
|---|---|---|---|
| EC2 `taskflow-ec2` | subnet **pública** de la VPC default | `taskflow-ec2-sg` (`TODO: sg-…`) | necesita IP pública para servir Swagger a internet |
| Puerto 22 (SSH) | inbound de `taskflow-ec2-sg` | — | **My IP** solamente: si fuera 0.0.0.0/0 los bots prueban credenciales 24/7 |
| Puerto 8080 (HTTP) | inbound de `taskflow-ec2-sg` | — | `0.0.0.0/0` **a propósito**: la demo pública. En prod real: JAMÁS Swagger sin auth por http |
| RDS `taskflow-db` | subnet de la VPC default, **Public access: NO** | `taskflow-rds-sg` (`TODO: sg-…`) | la BD nunca da la cara a internet; sin IP pública aunque la subnet sea pública |
| Puerto 5432 (Postgres) | inbound de `taskflow-rds-sg` | — | source = `taskflow-ec2-sg` (**SG→SG**), no una IP: sobrevive a cambios de IP y a N EC2 |
| Bucket S3 de reportes | servicio global (fuera de la VPC) | — (Block Public Access ON) | privado; se comparte con presigned URL de vida corta |

## Preguntas de control (respóndelas en 1 línea cada una)

- **¿Por qué el RDS no tiene IP pública?**
  `TODO: ____________________________________________`
- **¿Qué cambia si mañana hay 3 EC2?** (pista: la regla SG→SG vs una regla por IP)
  `TODO: ____________________________________________`
- **¿Dónde entraría un balanceador de carga (ALB)?**
  `TODO: ____________________________________________`
