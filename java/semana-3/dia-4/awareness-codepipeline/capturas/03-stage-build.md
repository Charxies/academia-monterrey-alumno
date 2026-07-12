# 03 — Stage Build (CodeBuild + buildspec.yml)

**Qué se ve:** el proyecto de **CodeBuild** abierto, con su `buildspec.yml` en pantalla. Al lado,
`awareness-codepipeline/buildspec-equivalente.yml` y el `ci-cd.yml` del alumno — **los tres juntos**.

**Qué decir (EL momento del lado a lado):** "Miren la fase `build` del `buildspec`:
`phases: build: commands: - mvn -B verify`. Es **su MISMO comando**, el mismo que corre su job `test`.
CodeBuild hace lo que hacen sus jobs `test` + `build-and-push`: instala Java 21 (su `setup-java`),
corre la suite y el gate (su `mvn -B verify`), y construye y publica la imagen. Otro archivo, otra
palabra (`phases` en vez de `steps`), **idéntica idea**."

**Diferencias a nombrar (1 línea cada una):**
- La imagen va a **ECR** (el GHCR de AWS), no a GHCR.
- El "cache: maven" de Actions aquí es la caché de CodeBuild (S3/local).
- El reporte JaCoCo (su `upload-artifact`) aquí es un `reports:` group + `artifacts:` a S3.

**Equivale a:** los jobs `test` y `build-and-push` de `ci-cd.yml`.
