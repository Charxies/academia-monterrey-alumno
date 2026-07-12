# Checklist de limpieza AWS — TODO lo de hoy SE DESTRUYE hoy (S3D3)

> **La regla de oro del día.** Se presenta desde el warm-up y se cierra con **pase de lista nominal**
> en el wrap-up: nadie cierra sesión con fierros vivos. Marca cada casilla al hacerlo y anota la hora
> en `evidencia-deploy.md`. Recuerda: la facturación NO es tiempo real (dolor #5) — se apaga por
> **checklist**, no por "ver el costo". Un correo de mañana con **centavos** = normal; con **dólares**
> = avisa al instructor.

Región: **`us-east-1`** (confirma el selector antes de empezar — si no ves tus recursos, es la región).

## 1) EC2 → **Terminate** (NO Stop)

- [ ] EC2 → Instances → selecciona `taskflow-ec2`.
- [ ] Instance state → **Terminate instance** → confirmar.
- [ ] Verifica que pasa a `shutting-down` → `terminated`.

> **Terminate, no Stop.** *Stop* apaga pero **deja el disco (EBS) y la IP potencialmente cobrando**;
> *Terminate* borra también el volumen raíz. Anota la hora de terminación.

## 2) RDS → **Delete** (sin snapshot final)

- [ ] RDS → Databases → selecciona `taskflow-db`.
- [ ] Si activaste **deletion protection**: Modify → desactívala → aplica **Immediately** primero (dolor #14).
- [ ] Actions → **Delete**.
- [ ] **Desmarca** "Create final snapshot" (es material de curso; un snapshot olvidado también cuesta).
- [ ] **Desmarca** "Retain automated backups".
- [ ] Escribe la confirmación (`delete me`) → **Delete**.
- [ ] Verifica que pasa a `Deleting` → desaparece.

## 3) S3 → **Vaciar y borrar** el bucket

- [ ] S3 → selecciona `taskflow-<usuario>-reportes` → **Empty** (escribe `permanently delete`) → confirma.
- [ ] Con el bucket vacío → **Delete** (escribe el nombre del bucket) → confirma.

> Un bucket no se borra si tiene objetos: primero **Empty**, luego **Delete**.

## 4) Security Groups y key pair de la consola (opcional — costo $0, pero higiénico)

- [ ] EC2 → Security Groups → borra `taskflow-ec2-sg` y `taskflow-rds-sg` (si dan error "en uso",
      espera a que la EC2/RDS terminen de borrarse y reintenta).
- [ ] EC2 → Key Pairs → borra `taskflow-key` **de la consola** (opcional).
- [ ] **CONSERVA** tu archivo local `taskflow-key.pem` por si D4 relanza. (El SG y la key pair no
      cobran; se borran solo por higiene.)

## 5) Billing → revisar el estimado del día

- [ ] Billing → Cost Explorer / Bills: mira el costo del día (recuerda: **se refleja con horas de
      retraso**, hasta 24 h — dolor #5). El día apagándolo todo debe ser **< $1**.

## 6) El budget **NO se borra**

- [ ] **Deja vivo** el presupuesto de $5 con su alerta: queda de **guardián** el resto de la semana.
      (Si mañana llega el correo del budget con centavos, es la facturación diferida: normal.)

## 7) Commit de cierre

- [ ] Completa la fila "recursos destruidos a las HH:MM" en `evidencia-deploy.md`.
- [ ] `git add infra/ && git commit -m "chore: limpieza aws d3 completa" && git push`

---

### Pase de lista nominal (wrap-up)

Confírmalo en voz alta con este checklist commiteado:

> **"EC2 terminated · RDS deleted · bucket borrado · budget vivo."**

Quien no terminó se queda acompañado hasta terminar. **Nadie cierra sesión con fierros vivos.**
