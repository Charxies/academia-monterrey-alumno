# Checklist de COSTOS — S3D4 (hereda la regla de oro de D3)

> **La facturación NO es tiempo real** (dolor #5 de D3): se apaga por **checklist**, no por "ver el
> costo". Correo de mañana con **centavos** = normal; con **dólares** = avisar al instructor.
>
> El **budget de $5 con alerta** creado en D3 sigue VIVO y es el guardián de hoy — **NO se borra**.
> Región: **`us-east-1`** (confirmar el selector).

## Qué se toca hoy y cuánto cuesta

| Recurso | Cuándo | Costo | Acción |
|---|---|---|---|
| **EC2 `t3.micro`** (re-aprovisionada en el warm-up) | activa todo el día | free-tier, pero **CUENTA horas** mientras vive (~$0.01/h fuera de free-tier) | **TERMINATE al cierre** (no Stop) |
| **Tabla DynamoDB** `taskflow-activity` (demo) | ~25 min en la demo | on-demand: **centavos**; free tier 25 GB | **`delete-table` en vivo** al terminar la demo |
| **GitHub Actions** | todo el día | **$0** en repos públicos (minutos ilimitados) | nada |
| **GHCR** (imágenes) | todo el día | **$0** en repos públicos | nada |
| IPv4 pública de la EC2 | mientras la instancia vive | se va al terminar la instancia | (cubierto por el terminate) |

> Actions y GHCR son **$0** porque tus repos son públicos desde S1D1. Si privatizaste uno: regrésalo a
> público o asume el límite (privados: 2,000 min/mes; el storage de GHCR también cuenta, dolor 4).

## Wrap-up — pase de lista nominal (se ejecuta EN VIVO)

- [ ] **EC2 → Terminate** (no Stop): Instances → `taskflow-ec2` → Instance state → **Terminate** → verificado `terminated`. Hora: ______
- [ ] **DynamoDB** `taskflow-activity` **borrada** (se hizo en la demo con `delete-table`) — verificar que ya no aparece.
- [ ] **Security Group** `taskflow-ec2-sg` (opcional, higiénico, $0): borrado tras terminar la EC2.
- [ ] `.pem` `taskflow-key` **CONSERVADO** en local (D5 podría re-lanzar).
- [ ] **Billing** revisado: el día debió costar **~$0** (free tier) / **< $1**.
- [ ] **Budget de $5**: sigue **VIVO** (NO se borra).

> Frase de cierre: **"EC2 terminated · DynamoDB deleted · budget vivo."**
> **Nadie cierra sesión con fierros vivos.**

## Plan B (sin cuentas AWS)

Nada que apagar en AWS. La demo DynamoDB corre en `amazon/dynamodb-local` (Docker): al terminar,
`docker compose -f demo-nosql/docker-compose.dynamodb.yml down -v`. Actions/GHCR siguen en **$0**.
