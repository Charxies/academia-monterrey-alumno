# 07 — El agente CodeDeploy en la EC2

**Qué se ve:** una terminal SSH a la EC2 con:

```bash
sudo service codedeploy-agent status
#  The AWS CodeDeploy agent is running as PID 1234
```

y el directorio `/opt/codedeploy-agent/deployment-root/...` donde el agente dejó los archivos del
deployment (el `docker-compose.prod.yml` y los scripts de hook copiados por el `appspec`).

**Qué decir:** "Esta es la pieza que **no se ve** en el diagrama: un **agente** corriendo en el
servidor, esperando órdenes de CodeDeploy. Cuando el pipeline llega al stage Deploy, este agente jala
la imagen de ECR y levanta el contenedor — **exactamente lo que su `ssh-action` hacía**, pero sin que
nadie abra un puerto 22 ni copie una llave: el agente ya está adentro y la instancia **asume un rol**.
Ese es el gran cambio de modelo: en Actions ustedes **entran** al server (SSH + llave secreta); en
AWS, el server **jala** el trabajo (agente + rol IAM). Menos superficie de ataque, más setup."

**Equivale a:** lo que el `script:` de tu `appleboy/ssh-action` ejecutaba dentro de la EC2.
