# Checklist — Secrets vs Vars del pipeline (S3D4, MP-6) · **REFERENCIA (lleno)**

> Regla de oro: **NUNCA un secreto en el yml.** Tus repos son PÚBLICOS desde S1D1 — un secreto
> commiteado es un secreto PUBLICADO (y quemado: se rota, no se "borra el commit", dolor 10).
>
> Criterio: ¿filtrarlo **compromete** algo? → **Secret**. ¿solo **configura**? → **Var**.
> ¿GitHub lo emite **solo**? → **Ninguno**.

## 1) La tabla canónica (la misma de `alumno.md`)

| Dato | Nombre canónico | ¿Dónde? | ¿Por qué ahí? |
|---|---|---|---|
| Llave SSH (el `.pem` `taskflow-key` de D3, COMPLETO) | `EC2_SSH_KEY` | **Secret** | credencial: su filtración = acceso a la máquina; se enmascara en logs |
| IP/host público de la EC2 | `EC2_HOST` | **Var** | configura, no compromete (el acceso lo da la llave) |
| Usuario SSH de la instancia (`ec2-user`) | `EC2_USER` | **Var** | ídem: solo dice "con quién" me conecto, no abre nada |
| Interruptor del deploy (`ec2` o sin setear) | `DEPLOY_TARGET` | **Var** | Plan A lo enciende (`ec2`); Plan B lo deja sin setear → job `deploy` `skipped` |
| Token del pipeline para GHCR | `GITHUB_TOKEN` | **Ninguno** (automático) | GitHub lo emite efímero por run, scoped al repo; nadie lo crea ni lo pega |
| `JWT_SECRET` y el password de la BD | (viven en la EC2) | **En la EC2** (runtime, D3) | los usa la APP, no el pipeline — GitHub nunca los ve (el `.env`/export de D3) |

> Muchos tutoriales meten TODO a secrets; funciona, pero pierde la distinción. La regla:
> ¿filtrarlo compromete algo? secret. ¿solo configura? var. ¿y en el yml? **NUNCA** — el repo es público.
>
> **Build-time vs runtime:** la llave SSH la usa el PIPELINE (GitHub Secrets). `JWT_SECRET` y el
> password de la BD los usa la APP y viven en la EC2 (el `.env` del warm-up). GitHub jamás los toca.

## 2) Dónde se cargaron (MP-6) — Plan A

Repo → **Settings → Secrets and variables → Actions**:
- [x] **Secret** `EC2_SSH_KEY` = `.pem` `taskflow-key` COMPLETO (líneas `-----BEGIN/END-----` incluidas, dolor 8).
- [x] **Var** `EC2_HOST` = IP pública NUEVA de la EC2 re-aprovisionada hoy (se actualiza en cada lanzamiento, dolor 9).
- [x] **Var** `EC2_USER` = `ec2-user`.
- [x] **Var** `DEPLOY_TARGET` = `ec2`.
- [x] `GITHUB_TOKEN`: automático — nada que cargar.

> **Plan B:** NO se crea `DEPLOY_TARGET` → el guard `if: vars.DEPLOY_TARGET == 'ec2'` es falso →
> el job `deploy` queda `skipped` (gris) y el pipeline sigue **verde**. Nada más cambia.

## 3) Auditoría cruzada (integrador, fase 1) — hecha

- [x] Un compañero auditó este `ci-cd.yml`: ningún valor sensible, ningún `echo` de secret. ✅
- [x] Se auditó el del compañero con el mismo criterio. ✅
