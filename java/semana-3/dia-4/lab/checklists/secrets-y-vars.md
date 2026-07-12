# Checklist — Secrets vs Vars del pipeline (S3D4, MP-6) · **A LLENAR**

> Regla de oro: **NUNCA un secreto en el yml.** Tus repos son PÚBLICOS desde S1D1 — un secreto
> commiteado es un secreto PUBLICADO (y quemado: se rota, no se "borra el commit", dolor 10).
>
> El criterio de clasificación (llénalo tú en la última columna):
> - ¿Filtrarlo **compromete** algo (da acceso, roba identidad)? → **Secret** (GitHub lo enmascara en logs).
> - ¿Solo **configura** (a dónde apuntar, un interruptor)? → **Var** (visible, no sensible).
> - ¿GitHub lo emite **solo**? → **Ninguno** (no lo creas ni lo pegues).

## 1) Clasifica cada dato (rellena "¿Dónde?" y "¿Por qué ahí?")

| Dato | Nombre canónico | ¿Dónde? (Secret / Var / Ninguno) | ¿Por qué ahí? |
|---|---|---|---|
| Llave SSH (el `.pem` `taskflow-key` de D3, COMPLETO) | `EC2_SSH_KEY` | __________ | ______________________________ |
| IP/host público de la EC2 | `EC2_HOST` | __________ | ______________________________ |
| Usuario SSH de la instancia (`ec2-user`) | `EC2_USER` | __________ | ______________________________ |
| Interruptor del deploy (`ec2` o sin setear) | `DEPLOY_TARGET` | __________ | ______________________________ |
| Token del pipeline para GHCR | `GITHUB_TOKEN` | __________ | ______________________________ |
| `JWT_SECRET` y el password de la BD | (viven en la EC2) | __________ | ______________________________ |

> Pista de la última fila: ¿quién USA `JWT_SECRET` y el password de la BD — el pipeline o la APP?
> Si es la APP, viven donde corre la app (el `.env`/export de la EC2, D3), y GitHub **nunca los ve**.

## 2) Dónde cargarlos en GitHub (MP-6)

Repo → **Settings → Secrets and variables → Actions**. Dos pestañas:
- **Secrets** (enmascarados): `EC2_SSH_KEY`.  *New repository secret* → nombre EXACTO → pega el `.pem`
  **COMPLETO**, con las líneas `-----BEGIN ...-----` y `-----END ...-----` incluidas (dolor 8).
- **Variables** (a la vista): `EC2_HOST`, `EC2_USER`, `DEPLOY_TARGET`.  *New repository variable*.

Marca al cargarlos (Plan A):
- [ ] Secret `EC2_SSH_KEY` = `.pem` completo (BEGIN/END incluidos).
- [ ] Var `EC2_HOST` = IP pública NUEVA de la EC2 re-aprovisionada hoy (cambia en cada lanzamiento, dolor 9).
- [ ] Var `EC2_USER` = `ec2-user`.
- [ ] Var `DEPLOY_TARGET` = `ec2`  (Plan B: **NO** la creas → el job `deploy` queda `skipped`).
- [ ] `GITHUB_TOKEN`: **nada que hacer** — es automático por run.

## 3) Auditoría cruzada (integrador, fase 1)

- [ ] Un compañero revisa TU `ci-cd.yml`: ¿algún valor sensible hardcodeado? ¿algún `echo` de un secret?
- [ ] Tú revisas el SUYO con el mismo ojo. Regla: si aparece un secreto en el yml, se **rota** (no se borra el log).
