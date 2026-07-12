# 04 — Stage Deploy (CodeDeploy + appspec.yml + hooks)

**Qué se ve:** el detalle del stage **Deploy** (una aplicación de **CodeDeploy** con su deployment
group apuntando a la EC2). En pantalla, `awareness-codepipeline/appspec-equivalente.yml` con sus
**hooks** `BeforeInstall`, `ApplicationStart`, `ValidateService`.

**Qué decir:** "Su job `deploy` era un `ssh` de tres líneas: `pull` de la imagen y `compose up -d`.
CodeDeploy hace lo mismo, pero con un **agente** instalado en la EC2 que ejecuta estos **hooks** por
fase: `BeforeInstall` = su `compose pull api`; `ApplicationStart` = su `up -d`; `ValidateService` = su
`curl /info`. Dos diferencias que valen oro en producción: **no hay llave SSH que copiar** — la EC2
**asume un rol IAM** —, y CodeDeploy suma **blue/green** (dos flotas, cambio de tráfico atómico, y
rollback si el smoke test falla)."

**Equivale a:**
```yaml
deploy:
  needs: build-and-push
  if: vars.DEPLOY_TARGET == 'ec2'
  steps:
    - uses: appleboy/ssh-action@v1
      with: { host, username, key, script: "git pull; compose pull api; up -d" }
```
