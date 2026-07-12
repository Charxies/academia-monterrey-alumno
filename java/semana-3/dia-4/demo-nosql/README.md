# Demo NoSQL — DynamoDB en acción (S3D4, PM)

> Paga la promesa sembrada en **S2D4 T1** ("demo real con DynamoDB en S3D4"). Es una **demo del
> instructor**; los alumnos observan y **quien quiera la repite** con el Plan B de aquí. La lección
> es el **modelo de partition key**, no la sintaxis: se diseña por **patrón de acceso**, no por
> normalización. **Esta tabla NO entra a la API** — el capstone vive en Postgres.

Contenido de esta carpeta:

| Archivo | Qué es |
|---|---|
| `docker-compose.dynamodb.yml` | levanta `amazon/dynamodb-local` en `:8000` (Plan B, sin cuenta AWS) |
| `guion-dynamodb.sh` | la demo de corrido: `create-table` → `put-item` ×3 → `get-item` → `query` → `scan` → `delete-table` (Plan A y Plan B con el MISMO archivo) |
| `README.md` | este documento: el modelo de clave, credenciales dummy, límites del free tier y los dos caminos de Plan B |

---

## El modelo: partition key + sort key

**Tabla `taskflow-activity`** — el feed de actividad ("ana completó la tarea X"):

| Rol | Atributo | Tipo | Para qué |
|---|---|---|---|
| **Partition key** (HASH) | `username` | `S` (string) | decide en qué **partición física** vive el item; es por donde se consulta ("dame los eventos de ana") |
| **Sort key** (RANGE) | `ocurrioEn` | `S` (ISO-8601) | **ordena** los eventos dentro de la partición de cada usuario (del más reciente al más viejo) |
| atributo libre | `evento` | `S` | el texto del evento |
| atributo **opcional** | `detalle` | `S` | **solo el 2.º item lo tiene** — DynamoDB NO exige esquema más allá de la clave (schema flexible, sin `ALTER TABLE`) |

Reglas que la demo hace **ver**:
- **`get-item`** exige la **clave completa** (partition **y** sort). No hay "busca por título".
- **`query`** trae toda una **partición** (todos los eventos de `ana`) usando el índice → **barato y rápido**.
- **`scan`** lee la **tabla entera** para filtrar → **caro y lento** en tablas de millones. Regla NoSQL: **diseña para `query`, teme al `scan`.**
- **Cuándo NoSQL SÍ:** acceso por clave a escala, esquema flexible, eventos/sesiones/feeds, serverless.
  **Cuándo NO:** relaciones + joins + transacciones multi-entidad ad-hoc — el CRUD de TaskFlow.

---

## Plan A — AWS real (demo del instructor)

Con AWS CLI configurada (perfil/región reales):

```bash
ENDPOINT_URL="" ./guion-dynamodb.sh        # ENDPOINT_URL vacío = AWS real (sin --endpoint-url)
```

> **⚠ Costos:** free tier de DynamoDB = **25 GB** de almacenamiento + capacidad de prueba. Esta demo en
> modo **on-demand** (`PAY_PER_REQUEST`) cuesta **centavos**. Aun así, el guion hace `delete-table`
> **en vivo** al terminar: la disciplina de limpieza de D3 sigue viva — **no se deja nada creado**.

---

## Plan B — DynamoDB local (repetible por el alumno, SIN cuenta AWS)

`amazon/dynamodb-local` es la **misma API** de DynamoDB corriendo en tu máquina, gratis y sin
credenciales reales. Los comandos son **idénticos**: solo cambian `--endpoint-url http://localhost:8000`
y unas **credenciales dummy** (la CLI exige que existan, aunque local no las valide).

```bash
# 1) Levanta la BD NoSQL local:
docker compose -f docker-compose.dynamodb.yml up -d      # dynamodb-local en :8000

# 2) Corre la demo (default = Plan B, ya apunta a localhost:8000 con creds dummy):
./guion-dynamodb.sh

# 3) Apaga al terminar (es -inMemory: no queda nada que borrar):
docker compose -f docker-compose.dynamodb.yml down
```

### D3 NO instaló AWS CLI en tu máquina — elige un camino

**Camino A — instalar AWS CLI v2 (una vez).** Es la CLI oficial; después `guion-dynamodb.sh` corre tal cual.

```bash
# macOS (Homebrew):
brew install awscli
# macOS (instalador oficial):
curl "https://awscli.amazonaws.com/AWSCLIV2.pkg" -o AWSCLIV2.pkg && sudo installer -pkg AWSCLIV2.pkg -target /
# Linux x86_64:
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o awscliv2.zip && unzip awscliv2.zip && sudo ./aws/install
# Windows: descarga e instala https://awscli.amazonaws.com/AWSCLIV2.msi
aws --version                                   # verifica: aws-cli/2.x
```

Con dynamodb-local ya levantado, no necesitas `aws configure`: el guion exporta credenciales dummy.

**Camino B — SIN instalar nada** (contenedor `amazon/aws-cli` en la MISMA red del compose).
`docker-compose.dynamodb.yml` crea la red `dynamodb-demo`; el contenedor resuelve el host
`dynamodb-local:8000`:

```bash
docker compose -f docker-compose.dynamodb.yml up -d      # dynamodb-local en la red 'dynamodb-demo'

# create-table (idéntico, con --endpoint-url http://dynamodb-local:8000):
docker run --rm --network dynamodb-demo \
  -e AWS_ACCESS_KEY_ID=dummy -e AWS_SECRET_ACCESS_KEY=dummy -e AWS_DEFAULT_REGION=us-east-1 \
  amazon/aws-cli dynamodb create-table --endpoint-url http://dynamodb-local:8000 \
    --table-name taskflow-activity \
    --attribute-definitions AttributeName=username,AttributeType=S AttributeName=ocurrioEn,AttributeType=S \
    --key-schema AttributeName=username,KeyType=HASH AttributeName=ocurrioEn,KeyType=RANGE \
    --billing-mode PAY_PER_REQUEST

# query (idéntico):
docker run --rm --network dynamodb-demo \
  -e AWS_ACCESS_KEY_ID=dummy -e AWS_SECRET_ACCESS_KEY=dummy -e AWS_DEFAULT_REGION=us-east-1 \
  amazon/aws-cli dynamodb query --endpoint-url http://dynamodb-local:8000 \
    --table-name taskflow-activity \
    --key-condition-expression "username = :u" \
    --expression-attribute-values '{":u":{"S":"ana"}}'
```

La lección (partition key, `query` vs `scan`, esquema flexible) es **idéntica** en los tres modos:
solo cambia el `--endpoint-url` y las credenciales dummy.
