# Checklist de limpieza AWS — TODO lo de hoy SE DESTRUYE hoy (S3D3) · **REFERENCIA (cerrado)**

> **La regla de oro del día**, marcada como en la corrida de referencia. La facturación NO es tiempo
> real (dolor #5): se apaga por **checklist**, no por "ver el costo". Correo de mañana con centavos =
> normal; con dólares = avisar al instructor.

Región: **`us-east-1`** (confirmar el selector antes de empezar).

## 1) EC2 → **Terminate** (NO Stop)

- [x] EC2 → Instances → seleccionar `taskflow-ec2`.
- [x] Instance state → **Terminate instance** → confirmar.
- [x] Verificado `terminated`. **Hora: 14:35.**

> **Terminate, no Stop.** *Stop* deja el disco (EBS) y la IP potencialmente cobrando; *Terminate*
> borra también el volumen raíz.

## 2) RDS → **Delete** (sin snapshot final)

- [x] RDS → Databases → seleccionar `taskflow-db`.
- [x] Deletion protection ya estaba OFF (se dejó OFF al crear, para facilitar la limpieza).
- [x] Actions → **Delete**.
- [x] **Desmarcado** "Create final snapshot" (es material de curso).
- [x] **Desmarcado** "Retain automated backups".
- [x] Confirmación escrita → **Delete**. **Hora: 14:38.**

## 3) S3 → **Vaciar y borrar** el bucket

- [x] S3 → `taskflow-anagarcia-reportes` → **Empty** → confirmar.
- [x] Bucket vacío → **Delete** → confirmar. **Hora: 14:40.**

## 4) Security Groups y key pair de la consola (opcional — costo $0, higiénico)

- [x] Borrados `taskflow-ec2-sg` y `taskflow-rds-sg` (tras terminar EC2/RDS).
- [x] `taskflow-key` borrada de la consola (opcional).
- [x] **Conservado** el archivo local `taskflow-key.pem` por si D4 relanza.

## 5) Billing → revisar el estimado del día

- [x] Estimado del día revisado: **~$0.18** (se reflejará con horas de retraso). < $1 ✅.

## 6) El budget **NO se borra**

- [x] Presupuesto de $5 con alerta **VIVO**: guardián el resto de la semana.

## 7) Commit de cierre

- [x] Fila "recursos destruidos a las 14:40" completada en `evidencia-deploy.md`.
- [x] `git commit -m "chore: limpieza aws d3 completa"` + push.

---

### Pase de lista nominal (wrap-up)

> **"EC2 terminated · RDS deleted · bucket borrado · budget vivo."** ✅

**Nadie cierra sesión con fierros vivos.**
