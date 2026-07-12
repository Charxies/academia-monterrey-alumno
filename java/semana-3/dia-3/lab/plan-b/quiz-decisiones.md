# Quiz de decisiones de diseño — AWS (S3D3)

> Responde con tus palabras (2-4 líneas por pregunta). No es un examen de memoria: es defender
> **decisiones de arquitectura**. En Plan B, este quiz + `infra/topologia-aws.md` completo SON tu
> entregable; se revisa en pareja. Las respuestas modelo están en `solucion/plan-b/`.

### 1. ¿Por qué el **budget** va ANTES de crear el primer recurso?
```
TODO:
```

### 2. ¿Dónde pondrías el **RDS** (subnet pública o privada, con o sin IP pública) y por qué?
```
TODO:
```

### 3. ¿Qué abre el **Security Group del RDS** y qué NO? (puerto y *source* exactos)
```
TODO:
```

### 4. ¿Qué cambia si mañana hay **3 EC2** en vez de una? (compara: regla SG→SG vs una regla por IP)
```
TODO:
```

### 5. El **SSH (22)** se abre solo a "My IP", pero el **8080** a `0.0.0.0/0`. ¿Por qué la diferencia?
```
TODO:
```

### 6. La API pasó de `docker-compose` (BD local) a **RDS** sin tocar código. ¿Qué lo hizo posible?
```
TODO:
```

### 7. Subes el reporte JaCoCo a un bucket con **Block Public Access ON** y lo compartes con una
**presigned URL**. ¿Por qué eso es distinto de "hacer el bucket público"? ¿Qué pasa cuando expira?
```
TODO:
```

### 8. Diagnóstico: la API contra la BD da `Connection timed out`. Otra vez, da `Connection refused`.
¿Qué te dice **cada** síntoma y dónde buscarías el problema en cada caso?
```
TODO:
```

---

### Bonus (oral): ¿Dónde entraría un **balanceador de carga (ALB)** en el dibujo, y por qué la regla
SG→SG entre EC2 y RDS sobrevive a que agregues el balanceador?
```
TODO:
```
