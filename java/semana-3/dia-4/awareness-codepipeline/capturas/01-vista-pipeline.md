# 01 — Vista del pipeline completo

**Qué se ve:** la consola de **CodePipeline** con el pipeline `taskflow-pipeline` y sus **3 stages en
fila**, cada uno en una tarjeta verde:

```
[ Source ]  ──►  [ Build ]  ──►  [ Deploy ]
 GitHub          CodeBuild        CodeDeploy
```

**Qué decir:** "Esto es su `ci-cd.yml`, **dibujado**. Tres cajas encadenadas, exactamente como sus tres
jobs `test → build-and-push → deploy` unidos por `needs`. La flecha entre cajas es su `needs:`; que
todo esté verde es su badge del README. No hay nada nuevo aquí: es su pipeline, con la cara de AWS."

**Equivale a:** el workflow completo (`ci-cd.yml`) con los 3 jobs.
