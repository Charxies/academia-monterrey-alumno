# Topología AWS de TaskFlow — el dibujo del día (VPC conceptual) · **REFERENCIA**

> Versión de referencia con los **ids de ejemplo de la corrida del instructor** (los tuyos serán
> distintos). Hoy usamos la **VPC default** de la cuenta (todas sus subnets son públicas): no se
> construye VPC custom — se DIBUJA para entender dónde vive cada pieza. En **Plan B** este archivo es
> EL entregable de diseño.

## Datos de la corrida (ejemplo)

| Dato | Valor (ejemplo) |
|---|---|
| Región | `us-east-1` |
| VPC | VPC **default** — `vpc-0a1b2c3d4e5f67890` |
| IP pública de la EC2 | `54.87.123.45` |
| Security Group de la EC2 | `taskflow-ec2-sg` — `sg-0ec2aaa111bbb2222` |
| Endpoint del RDS | `taskflow-db.abcd1234efgh.us-east-1.rds.amazonaws.com` |
| Security Group del RDS | `taskflow-rds-sg` — `sg-0rds3333ccc4444dd` |
| Bucket S3 | `taskflow-anagarcia-reportes` |

## El dibujo canónico

```
                         INTERNET
                            │
                            │  HTTP :8080  (Swagger PÚBLICO, a propósito — ambiente desechable)
                            ▼
        ┌───────────────────────────────────────────────────────┐
        │  VPC default (us-east-1)   vpc-0a1b2c3d4e5f67890       │
        │                                                        │
        │   ┌────────────── subnet PÚBLICA ──────────────────┐   │
        │   │  (ruta al Internet Gateway)                    │   │
        │   │                                                │   │
        │   │   [SG: taskflow-ec2-sg  sg-0ec2aaa111bbb2222]  │   │
        │   │      ├─ inbound 22   (SSH)  ← 189.203.45.7/32  │   │  (My IP)
        │   │      └─ inbound 8080 (HTTP) ← 0.0.0.0/0        │   │  (público, a propósito)
        │   │   ┌────────────────────────────────────────┐   │   │
        │   │   │  EC2  taskflow-ec2  (t3.micro)         │   │   │
        │   │   │  IP pública 54.87.123.45               │   │   │
        │   │   │  Docker → contenedor 'taskflow'        │   │   │
        │   │   │  (imagen taskflow-api:local, perfil    │   │   │
        │   │   │   'docker')                            │   │   │
        │   │   └───────────────┬────────────────────────┘   │   │
        │   │                   │  TCP :5432                  │   │
        │   └───────────────────┼─────────────────────────────┘  │
        │                       ▼                                 │
        │   [SG: taskflow-rds-sg  sg-0rds3333ccc4444dd]           │
        │      └─ inbound 5432 ← source = sg-0ec2aaa111bbb2222  (¡el SG de la EC2, NO una IP!)
        │   ┌────────────────────────────────────────────────┐   │
        │   │  RDS  taskflow-db  (PostgreSQL 16, Single-AZ)  │   │
        │   │  Public access: NO  → SIN IP pública           │   │
        │   │  endpoint taskflow-db.abcd1234efgh.us-east-1.rds.amazonaws.com
        │   └────────────────────────────────────────────────┘   │
        └───────────────────────────────────────────────────────┘

     S3 (fuera de la VPC, servicio global):
        bucket taskflow-anagarcia-reportes   Block Public Access: ON
        → se comparte con PRESIGNED URL (compartir ≠ publicar)
```

## Tabla de decisiones

| Pieza | Dónde vive | Qué SG la cuida | Por qué así |
|---|---|---|---|
| EC2 `taskflow-ec2` | subnet **pública** de la VPC default | `taskflow-ec2-sg` (`sg-0ec2aaa111bbb2222`) | necesita IP pública para servir Swagger a internet |
| Puerto 22 (SSH) | inbound de `taskflow-ec2-sg` | — | **My IP** solamente (`189.203.45.7/32`): 0.0.0.0/0 = bots probando credenciales 24/7 |
| Puerto 8080 (HTTP) | inbound de `taskflow-ec2-sg` | — | `0.0.0.0/0` **a propósito** (demo pública desechable). En prod real: nunca Swagger sin auth por http |
| RDS `taskflow-db` | subnet de la VPC default, **Public access: NO** | `taskflow-rds-sg` (`sg-0rds3333ccc4444dd`) | la BD nunca da la cara a internet; sin IP pública aunque la subnet sea pública |
| Puerto 5432 (Postgres) | inbound de `taskflow-rds-sg` | — | source = `taskflow-ec2-sg` (**SG→SG**), no una IP: sobrevive a cambios de IP y a N EC2 |
| Bucket S3 de reportes | servicio global (fuera de la VPC) | — (Block Public Access ON) | privado; se comparte con presigned URL de vida corta |

## Preguntas de control (respondidas)

- **¿Por qué el RDS no tiene IP pública?** Porque una base de datos jamás debe ser alcanzable desde
  internet: reduce la superficie de ataque a cero desde afuera. Solo la EC2 (dentro de la VPC, con el
  SG correcto) la toca. "Public access: NO" en el wizard hace que RDS no reciba IP pública, aunque la
  subnet de la VPC default sea pública.
- **¿Qué cambia si mañana hay 3 EC2?** Nada en el SG del RDS: la regla apunta al **SG** `taskflow-ec2-sg`,
  así que cualquier instancia que pertenezca a ese SG queda autorizada automáticamente. Si en su lugar
  hubiéramos puesto la **IP privada** de UNA EC2, habría que editar la regla por cada instancia nueva —
  y se rompería en cuanto una cambie de IP.
- **¿Dónde entraría un balanceador de carga (ALB)?** Delante de las EC2, en la(s) subnet(s) pública(s):
  internet → ALB (:80/:443) → las EC2 (:8080). El SG del ALB abre 80/443 al mundo; el SG de las EC2
  pasa a aceptar 8080 **solo desde el SG del ALB**; y el SG→SG EC2→RDS no se toca: sigue vivo.
