#!/usr/bin/env bash
# =============================================================================
# guion-dynamodb.sh — la demo NoSQL de S3D4 de corrido (paga la promesa de S2D4).
#
# Tabla: taskflow-activity · partition key 'username' (S, HASH) · sort key 'ocurrioEn' (S, RANGE).
# Narrativa: el FEED DE ACTIVIDAD de TaskFlow ("ana completó la tarea X") — millones de eventos,
# SIEMPRE consultados por usuario: caso NoSQL de libro. La clave se diseña por PATRÓN DE ACCESO
# ("dame los eventos de ana"), no por normalización.
#
# Corre en DOS modos con el MISMO archivo:
#   Plan B (default): contra 'amazon/dynamodb-local' en http://localhost:8000 + credenciales dummy.
#                     Requiere:  docker compose -f docker-compose.dynamodb.yml up -d   (ver README).
#   Plan A:           contra AWS real. Ejecuta con  ENDPOINT_URL="" ./guion-dynamodb.sh
#                     (usa tus credenciales/región reales; ⚠ cuesta centavos — la tabla se borra al final).
#
# Sin AWS CLI nativa (D3 no la instaló en máquinas de alumnos): ver "Camino B" en el README
# (contenedor amazon/aws-cli en la red del compose, --endpoint-url http://dynamodb-local:8000).
# =============================================================================
set -euo pipefail

# --- Configuración de endpoint ------------------------------------------------
# Plan B por default (dynamodb-local). Para Plan A (AWS real):  ENDPOINT_URL="" ./guion-dynamodb.sh
ENDPOINT_URL="${ENDPOINT_URL-http://localhost:8000}"

ENDPOINT_ARGS=()
if [[ -n "$ENDPOINT_URL" ]]; then
  ENDPOINT_ARGS=(--endpoint-url "$ENDPOINT_URL")
  # dynamodb-local no valida credenciales, pero la CLI EXIGE que existan: dummy.
  export AWS_ACCESS_KEY_ID="${AWS_ACCESS_KEY_ID:-dummy}"
  export AWS_SECRET_ACCESS_KEY="${AWS_SECRET_ACCESS_KEY:-dummy}"
  export AWS_DEFAULT_REGION="${AWS_DEFAULT_REGION:-us-east-1}"
  echo ">> Modo Plan B: dynamodb-local en $ENDPOINT_URL (credenciales dummy)"
else
  echo ">> Modo Plan A: AWS real (credenciales/región de tu perfil). ⚠ Cuesta centavos."
fi

TABLE="taskflow-activity"
run() { echo; echo "+ aws dynamodb $*"; aws dynamodb "$@" "${ENDPOINT_ARGS[@]}"; }

# --- 1) Crear la tabla: el modelo de clave ES la lección ----------------------
# partition key username (HASH) decide en qué partición vive el item;
# sort key ocurrioEn (RANGE) ordena los eventos dentro de la partición de cada usuario.
run create-table --table-name "$TABLE" \
  --attribute-definitions \
      AttributeName=username,AttributeType=S \
      AttributeName=ocurrioEn,AttributeType=S \
  --key-schema \
      AttributeName=username,KeyType=HASH \
      AttributeName=ocurrioEn,KeyType=RANGE \
  --billing-mode PAY_PER_REQUEST

# Esperar a que la tabla esté ACTIVE (relevante en AWS real; en local es inmediato).
run wait table-exists --table-name "$TABLE" || true

# --- 2) put-item x3: el 2.º gana 'detalle' SIN ALTER = esquema flexible --------
run put-item --table-name "$TABLE" \
  --item '{"username":{"S":"ana"},"ocurrioEn":{"S":"2026-07-11T09:00:00Z"},"evento":{"S":"completo Configurar CI"}}'
run put-item --table-name "$TABLE" \
  --item '{"username":{"S":"ana"},"ocurrioEn":{"S":"2026-07-11T09:05:00Z"},"evento":{"S":"creo el proyecto Pipeline"},"detalle":{"S":"prioridad HIGH"}}'
run put-item --table-name "$TABLE" \
  --item '{"username":{"S":"luis"},"ocurrioEn":{"S":"2026-07-11T09:10:00Z"},"evento":{"S":"comento en la tarea X"}}'

# --- 3) get-item: exige la clave COMPLETA (partition + sort) -------------------
# No hay "busca por título": un get necesita username Y ocurrioEn exactos.
run get-item --table-name "$TABLE" \
  --key '{"username":{"S":"ana"},"ocurrioEn":{"S":"2026-07-11T09:00:00Z"}}'

# --- 4) query (por PARTICIÓN, BARATO) vs scan (tabla ENTERA, CARO) -------------
# query: va directo a la partición de 'ana' (usa el índice) -> barato y rápido.
run query --table-name "$TABLE" \
  --key-condition-expression "username = :u" \
  --expression-attribute-values '{":u":{"S":"ana"}}'
# scan: lee TODA la tabla para filtrar -> caro y lento en tablas de millones. "Diseña para query".
run scan --table-name "$TABLE"

# --- 5) delete-table EN VIVO al cerrar: modelar el hábito de destruir lo desechable
run delete-table --table-name "$TABLE"

echo
echo ">> Demo completa. Tabla '$TABLE' borrada (limpieza — la disciplina de D3 sigue viva)."
