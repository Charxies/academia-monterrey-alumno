# Awareness — CodePipeline / CodeBuild / CodeDeploy (S3D4, PM · ~30 min)

> **NO se construye NADA en AWS.** El temario del cliente pide CodePipeline/CodeDeploy y así quedan
> cubiertos **con honestidad**: montar un pipeline en AWS **duplica** lo que ya construiste hoy en
> GitHub Actions, con costo y sin valor pedagógico nuevo. La idea es la **misma**, con otra sintaxis:
> **quien entiende su `ci-cd.yml` de hoy, lee un CodePipeline mañana.**

Contenido de esta carpeta (respaldo del tour — el **Plan B del awareness** siempre listo):

| Archivo | Qué es | Se usa para |
|---|---|---|
| `buildspec-equivalente.yml` | tu job `test`+`build` traducido a **CodeBuild** | el "lado a lado" con tu `ci-cd.yml` |
| `appspec-equivalente.yml` | tu job `deploy` traducido a **CodeDeploy** (hooks + agente) | el "lado a lado" con tu `ci-cd.yml` |
| `capturas/` | **capturas numeradas 1–7** del tour de consola (storyboard narrado) | el tour sin cuenta AWS (Plan B) |
| `README.md` | este documento + la **tabla de equivalencias** canónica | la charla de 30 min |

---

## La tabla de equivalencias (fila por fila — la misma de `alumno.md`)

| Lo que construyeron HOY | Equivalente AWS | Nota |
|---|---|---|
| `on: push` (webhook de GitHub) | **CodePipeline — stage Source** (CodeConnections a GitHub) | mismo disparo |
| job `test` + `build` (`ci-cd.yml`) | **CodeBuild** con `buildspec.yml` | otro yml, mismas fases y comandos (`mvn -B verify`) |
| GHCR | **ECR** | registry privado de AWS |
| job `deploy` por SSH | **CodeDeploy** (agente en la EC2 + `appspec.yml` + hooks) | y suma blue/green |
| el workflow completo | **CodePipeline** (orquesta los stages) | los artefactos viajan por S3 |
| secrets/vars del repo | **IAM roles** + Secrets Manager / SSM Parameter Store | la EC2 asume un rol: no hay llave que copiar |

**La frase del lado a lado:** abre `buildspec-equivalente.yml` y busca
`phases: build: commands: - mvn -B verify` — es tu **MISMO comando**, en otra sintaxis.

---

## Estructura de la charla (30 min)

- **(~15 min) La tabla, fila por fila**, con `buildspec-equivalente.yml` y `appspec-equivalente.yml`
  abiertos **LADO A LADO** con el `ci-cd.yml` del alumno.
- **(~10 min) Tour de la consola:**
  - **Plan A** — en vivo con la cuenta del instructor: un pipeline de ejemplo `Source → Build → Deploy`
    YA montado, una ejecución, dónde viven los logs (CloudWatch), el agente CodeDeploy en la EC2.
  - **Plan B** — las **capturas numeradas** de `capturas/` en el MISMO orden. El tour funciona igual.
- **(~5 min) Cuándo elegir cuál:**
  - **AWS-first (CodePipeline):** IAM en vez de secrets copiados (**la EC2 ASUME un rol** — no hay
    llave que filtrar), artefactos por S3, **blue/green de CodeDeploy**, todo dentro de la VPC,
    gobernanza/compliance.
  - **Actions:** el código YA vive en GitHub, marketplace enorme, **$0 en repos públicos** — por eso
    el curso lo eligió.

> **⚠ Costo (otra razón para no montarlo):** CodePipeline ≈ **$1/mes por pipeline** tras el free tier,
> **+ CodeBuild por minuto**. Actions en repos públicos: **$0**.

---

## Cómo usar las capturas (Plan B)

En `capturas/` hay **7 pasos numerados** (`01`…`07`) que son el **storyboard narrado** del tour: cada
uno describe qué muestra la pantalla y qué decir. En Plan A recorres la consola real en ESE mismo
orden; en Plan B lees el storyboard (y, si las tienes, proyectas las PNG reales que hayas capturado
antes de clase con la misma numeración). El punto pedagógico —**mapear 1:1**, no construir— es idéntico.
