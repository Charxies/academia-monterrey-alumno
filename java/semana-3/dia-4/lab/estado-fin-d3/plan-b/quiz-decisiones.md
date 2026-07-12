# Quiz de decisiones de diseño — AWS (S3D3) · **RESPUESTAS MODELO**

> No se comparte hasta cerrar el lab. Respuestas de referencia; se acepta cualquier redacción que
> capture la idea. El objetivo es defender **decisiones de arquitectura**, no memorizar.

### 1. ¿Por qué el **budget** va ANTES de crear el primer recurso?
Porque el cobro de la nube corre por hora y la facturación **no es en tiempo real** (se refleja con
horas de retraso): si algo se queda encendido, el aviso llega tarde. El budget de $5 con alerta es la
red de seguridad que existe **antes** de que haya nada que pueda cobrar. Es higiene de cuenta: así se
empieza SIEMPRE una cuenta nueva.

### 2. ¿Dónde pondrías el **RDS** y por qué?
En una subnet **sin exponerlo a internet**, con **"Public access: NO"** → sin IP pública. La BD nunca
debe dar la cara a internet; solo la EC2 (dentro de la VPC, con el SG correcto) la alcanza. Reduce la
superficie de ataque a cero desde afuera. (Hoy usamos la VPC default, cuyas subnets son públicas, pero
"Public access: NO" ya deja al RDS sin IP pública.)

### 3. ¿Qué abre el **Security Group del RDS** y qué NO?
Abre **solo el puerto 5432 (PostgreSQL)** y **solo con source = el SG de la EC2** (`taskflow-ec2-sg`).
NO abre nada a `0.0.0.0/0`, NO abre otros puertos, NO acepta a una IP suelta. Todo lo demás está
cerrado por default (el SG niega salvo lo que abras).

### 4. ¿Qué cambia con **3 EC2** en vez de una?
Con la regla **SG→SG**: nada. Cualquier instancia que pertenezca a `taskflow-ec2-sg` queda autorizada
automáticamente. Con una regla por **IP** habría que editar el SG del RDS por cada instancia nueva, y
se rompería en cuanto una cambie de IP. Por eso la fuente es el SG, no una IP (dolor #12).

### 5. **SSH (22) solo My IP** pero **8080 a 0.0.0.0/0**. ¿Por qué?
El 22 es el acceso administrativo a la máquina: abierto al mundo, los bots prueban credenciales 24/7
(el hallazgo de auditoría #1). El 8080 es Swagger, **público a propósito** hoy porque es un ambiente
desechable de juguete de ~2 h. En producción real, JAMÁS se expone Swagger sin auth ni una API por
`http://IP:8080`: llevaría TLS, dominio y autenticación.

### 6. La API pasó de `compose` a **RDS** sin tocar código. ¿Qué lo hizo posible?
El **12-factor** (config en el ambiente): la URL de la BD está parametrizada
(`jdbc:postgresql://${DB_HOST:db}:${DB_PORT:5432}/${DB_NAME:taskflow}`) y las credenciales y el
`JWT_SECRET` llegan por variables de entorno. Para RDS solo se sobreescribió `DB_HOST` (y credenciales/
secret) en el `docker run`; el jar, la imagen y el `IMAGE ID` son los mismos, sin recompilar ni
reconstruir.

### 7. Bucket con **Block Public Access ON** + **presigned URL**: ¿por qué no es "hacerlo público"?
Público = cualquiera con la URL entra, para siempre, sin límite. Una **presigned URL** es un enlace
**firmado con tus credenciales y con expiración** (p.ej. 15 min): concede acceso temporal a **un**
objeto sin abrir el bucket. **Compartir ≠ publicar.** Cuando expira, el enlace deja de servir (403) y
el bucket sigue privado. (Los buckets públicos protagonizan fugas de datos célebres: por eso Block
Public Access se queda ON.)

### 8. `Connection timed out` vs `Connection refused`.
- **timed out**: el paquete salió y **nadie contestó** → un firewall/SG se lo comió en silencio.
  Hoy: falta la regla SG→SG en `taskflow-rds-sg`, o el endpoint/IP está mal, o no hay ruta. Se busca
  en el **SG / la red**.
- **refused**: el host **contestó "aquí no escucha nadie"** → llegaste al host pero al puerto
  equivocado o la BD está apagada. Se busca en el **puerto / el servicio**.
- (Y si fuera `401/403` HTTP: la red está bien, es la APP — eso ya se domina desde S2D5.)

---

### Bonus: ¿Dónde entraría un **ALB** y por qué sobrevive la regla SG→SG?
El ALB va **delante** de las EC2, en la(s) subnet(s) pública(s): internet → ALB (:80/:443) → EC2
(:8080). El SG del ALB abre 80/443 al mundo; el SG de las EC2 pasa a aceptar 8080 **solo desde el SG
del ALB** (otra vez SG→SG). La regla EC2→RDS **no se toca**: como apunta al SG de las EC2, sigue
autorizando a todas las instancias detrás del balanceador. Ese es el punto de encadenar SGs: la
arquitectura crece sin reescribir reglas.
