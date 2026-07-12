# Checklist PREVIO a la semana — para el COORDINADOR / INSTRUCTOR (no es del alumno)

> Se ejecuta **≥3 días antes** del día de AWS. Materializa la mitigación del **riesgo abierto §10 del
> MASTER-PLAN** (nadie ha confirmado quién provee las cuentas). Una cuenta AWS recién nacida puede
> quedar en *"account pending verification"* y **no lanzar EC2** (dolor #1): por eso, cuentas creadas
> con días de antelación, no la mañana del día.

## A) Decisión de cuentas (bloqueante — resolver PRIMERO)

- [ ] **¿Quién provee las cuentas AWS?** (marca una)
  - [ ] Cuentas individuales de alumnos (Plan A total).
  - [ ] Cuentas parciales: algunos tienen, otros van en Plan B / apareados.
  - [ ] Sin cuentas de alumnos → **Plan B total** (demo del instructor + topología local). El día es
        **impartible completo** en Plan B; no bloquea.
- [ ] Nombre del responsable que confirma el estado de cuentas: `________________`
- [ ] Fecha límite para tener esta decisión cerrada: `________________`

## B) Si hay cuentas de alumnos (Plan A / parcial)

- [ ] Cuentas **creadas ≥3 días antes** del día de AWS.
- [ ] **Verificación de tarjeta y teléfono completada** en cada cuenta (una cuenta sin verificar no
      lanza EC2 ni RDS).
- [ ] Cada alumno sabe entrar a la consola de AWS (usuario/contraseña root a mano, MFA opcional).
- [ ] Región por defecto entendida: **`us-east-1`** para todos (soporte con las mismas pantallas).
- [ ] Se comunicó a los alumnos: **no crear ningún recurso antes del día** (el primer paso es el budget).

## C) Ensayo del instructor (SU cuenta — obligatorio para Plan A y Plan B)

- [ ] Cuenta propia del instructor **operativa y probada**.
- [ ] **Budget de $5 ensayado** (crearlo y verlo activo) — es el primer paso en vivo.
- [ ] Usuario IAM `dev-<instructor>` con `AdministratorAccess` creado; login IAM guardado.
- [ ] Imagen **`taskflow-api:local` construida** en la laptop del instructor (D2) — lista para `save/scp`.
- [ ] Corrida de ensayo **de punta a punta**: EC2 lanzada, imagen transportada, API pública, RDS creado,
      SG→SG, la API contra RDS, presigned URL probada — y **RDS de ensayo BORRADA** al terminar (no dejar
      fierros del ensayo cobrando).
- [ ] **Toda demo se hará en vivo y GRABADA** (rerun asíncrono; imprescindible en Plan B).

## D) Infra del salón

- [ ] Ancho de banda del salón revisado: cada `scp` sube ~200 MB (previsto, no imprevisto).
- [ ] Canal de atascos listo (chat/Slack) para pegar errores en vivo.
- [ ] Docker Desktop en verde en cada laptop (heredado de D2; necesario para Plan B y para `docker save`).

## E) Materiales listos

- [ ] `instructor.md`, `alumno.md`, `lab/` a la mano; capturas de referencia de cada wizard revisadas.
- [ ] Checklist de limpieza (`checklist-limpieza-aws.md`) impreso/compartido — se muestra **desde el
      warm-up**.
- [ ] Tabla de cohorte para el **pase de lista nominal** de limpieza preparada (vive en `instructor.md`).
