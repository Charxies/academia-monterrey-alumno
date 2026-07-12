# Checklist de limpieza AWS — integrador, FASE 3 (solo quien re-aprovisionó hoy)

> **Orden estricto, no negociable:** la **evidencia del despliegue se captura en la FASE 1** (URL viva +
> un request con respuesta, ANTES de tocar nada) → aquí, en la fase 3, se **APAGA todo**. Si llegas
> aquí sin evidencia capturada, vuelve a la fase 1: apagar primero rompe el release.
>
> La facturación NO es tiempo real (llega con horas de retraso): se apaga por **checklist**, no por
> "ver el costo". Decirlo sin rodeos: **una RDS olvidada cobra en semanas lo que costó el free tier entero.**

Región: **`us-east-1`** (confirma el selector antes de empezar).

## 0) Antes de apagar — confirma que la evidencia YA está (fase 1)

- [ ] URL viva capturada en las notas del Release (o en `infra/evidencia-deploy.md`).
- [ ] Un `curl http://<EC2>:8080/info` → `"version":"3.0.0"` guardado (captura o texto).
- [ ] Pipeline **verde** sobre el commit del tag `v3.0`.

> Si las tres están ✅ → apaga. Si no → **NO apagues todavía**.

## 1) EC2 → **Terminate** (NO Stop)

- [ ] EC2 → Instances → selecciona tu instancia (la re-aprovisionada hoy, `t3.micro`).
- [ ] Instance state → **Terminate instance** → confirmar.
- [ ] Verificado `terminated`. Hora: ____.

> **Terminate, no Stop.** *Stop* deja el disco (EBS) y la IP potencialmente cobrando; *Terminate*
> borra también el volumen raíz. (Misma regla de D3/D4.)

## 2) RDS → **Delete** (si creaste una para la evidencia)

- [ ] RDS → Databases → selecciona tu instancia.
- [ ] Deletion protection **OFF**.
- [ ] Actions → **Delete**.
- [ ] "Create final snapshot": opcional (es material de curso — normalmente **desmarcado**).
- [ ] "Retain automated backups": **desmarcado**.
- [ ] Confirmación escrita → **Delete**. Hora: ____.

> Si tu demo corrió con Postgres **en el mismo compose de la EC2** (sin RDS), este paso no aplica: la
> BD murió con la instancia.

## 3) S3 → **vaciar y borrar** buckets del curso (si los hay)

- [ ] S3 → tu bucket → **Empty** → confirmar.
- [ ] Bucket vacío → **Delete** → confirmar. Hora: ____.

## 4) Security Groups y key pair (opcional — costo $0, higiénico)

- [ ] Borrados los SG creados hoy (tras terminar EC2/RDS).
- [ ] Key pair de la consola borrada (opcional); **conserva** el `.pem` local por si acaso.

## 5) Billing → verificación final

- [ ] Revisado el estimado del día (se refleja con horas de retraso). Objetivo: **< $1** (free tier ~$0).
- [ ] **Nada facturable vivo:** EC2 terminated · RDS deleted · buckets borrados.
- [ ] La **alarma de presupuesto de D3 sigue VIVA** — red de seguridad si algo se escapó. No se borra.

## 6) Cierre

- [ ] El entregable está **congelado**: nada se pushea después del tag `v3.0`.

---

### Pase de lista nominal (wrap-up)

> **"EC2 terminated · RDS deleted · buckets borrados · budget vivo · evidencia en el release."** ✅
>
> **Nadie cierra el track con fierros vivos.**
