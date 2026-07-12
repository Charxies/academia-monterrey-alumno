# 02 — Stage Source (CodeConnections a GitHub)

**Qué se ve:** el detalle del stage **Source**: un **CodeConnection** (antes "CodeStar Connections")
apuntando al repo `taskflow-api-<usuario>` en GitHub, rama `main`. Un badge "Succeeded" y el SHA del
último commit que disparó el pipeline.

**Qué decir:** "Este stage es su **`on: push`**. En Actions, GitHub dispara el workflow solo porque el
código vive ahí. En AWS, como el código está afuera (en GitHub), hay que **conectar** los dos mundos
con CodeConnections — un paso que ustedes no tuvieron que dar. Mismo disparo: un push a `main` arranca
todo. Fíjense en el SHA: es el mismo commit que verían en su run de Actions."

**Equivale a:**
```yaml
on:
  push:
    branches: [main]
  pull_request:
    branches: [main]
```
