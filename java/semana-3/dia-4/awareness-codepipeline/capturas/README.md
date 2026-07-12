# Capturas numeradas del tour CodePipeline (storyboard 1–7)

Úsalas en **Plan B** (sin cuenta AWS): lee el storyboard de cada paso en orden. En **Plan A** recorre
la consola real en ESTE mismo orden. Si capturaste PNG reales antes de clase, nómbralas con la misma
numeración (`01-*.png`, …) y proyéctalas junto a cada storyboard.

| # | Pantalla | Equivale en tu `ci-cd.yml` |
|---|---|---|
| 01 | Vista del pipeline completo (Source → Build → Deploy) | el workflow completo, "dibujado" |
| 02 | Stage **Source** (CodeConnections a GitHub) | `on: push` / `pull_request` |
| 03 | Stage **Build** (CodeBuild + `buildspec.yml`) | jobs `test` + `build-and-push` |
| 04 | Stage **Deploy** (CodeDeploy + `appspec.yml` + hooks) | job `deploy` por SSH |
| 05 | Una **ejecución** verde de punta a punta | un run verde en la pestaña Actions |
| 06 | Dónde viven los **logs** (CloudWatch) | pestaña Actions → step → log |
| 07 | El **agente CodeDeploy** en la EC2 | lo que tu `ssh-action` ejecutaba en el server |

Frase ancla del tour: **"quien entiende su pipeline de hoy, lee un CodePipeline mañana — es la misma
idea con otra sintaxis."**
