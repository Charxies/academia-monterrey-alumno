# 06 — Dónde viven los logs (CloudWatch)

**Qué se ve:** el log del stage Build abierto en **CloudWatch Logs**: el output de `mvn -B verify`
línea por línea (los mismos `Tests run: ... BUILD SUCCESS` de Surefire que ya saben leer).

**Qué decir:** "Cuando algo se rompe, aquí se **lee** — no se re-lanza 'a ver si ahora sí' (la regla de
ERR-1). En Actions abren el run → el step → el log. En AWS es el mismo reflejo: van a **CloudWatch** y
buscan el output del build. **El mensaje de error es idéntico** porque el comando es idéntico:
`mvn -B verify` truena igual en un runner de GitHub que en un contenedor de CodeBuild. Saber leer un
log de Maven — que aprendieron en S3D1 — les sirve en las dos plataformas."

**Equivale a:** pestaña Actions → run → step → log.
