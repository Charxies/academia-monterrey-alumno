#!/bin/bash
# Script para sembrar la API de TaskFlow con datos de prueba (via API, respeta las reglas de negocio).
# Requiere: curl, jq.  Compatible con macOS, Linux y Git Bash (Windows).
# Es re-ejecutable: si el registro falla (usuario ya existe / 409), continúa con el login.

BASE_URL="http://localhost:8080"
echo "Empezando la siembra de datos en $BASE_URL..."

# Fecha futura en formato yyyy-MM-dd, portable:
# - GNU date (Linux / Git Bash): date -d "+N days"
# - BSD date (macOS): date -v+Nd
future_date() {
    local days="$1"
    date -d "+${days} days" +%Y-%m-%d 2>/dev/null || date -v+"${days}"d +%Y-%m-%d
}

# Registro silencioso: no aborta si el usuario ya existe.
register_user() {
    local username="$1"
    local email="$2"
    local password="$3"
    echo "   - Registrando usuario '$username'..."
    local code
    code=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/auth/register" \
        -H "Content-Type: application/json" \
        -d "{\"username\": \"$username\", \"email\": \"$email\", \"password\": \"$password\"}")
    if [ "$code" = "201" ] || [ "$code" = "200" ]; then
        echo "     Usuario '$username' creado."
    else
        echo "     Usuario '$username' no se creó (HTTP $code); probablemente ya existía. Continuando."
    fi
}

# 1. Registrar usuarios canónicos de la semana QE
register_user "demo" "demo@taskflow.dev" "Demo123!"
register_user "ana" "ana@taskflow.dev" "Ana1234!"

# 2. Iniciar sesión como 'demo' y capturar el token JWT
echo "   - Iniciando sesión como 'demo' para obtener el token JWT..."
TOKEN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d "{\"username\": \"demo\", \"password\": \"Demo123!\"}")

# Tolerante a token / accessToken / jwt (igual que api.js)
TOKEN=$(echo "$TOKEN_RESPONSE" | jq -r '.token // .accessToken // .jwt // empty')

if [ -z "$TOKEN" ] || [ "$TOKEN" = "null" ]; then
    echo "Error: no se pudo obtener el token de autenticación. Respuesta:"
    echo "$TOKEN_RESPONSE"
    exit 1
fi
echo "     Token JWT capturado."

AUTH_HEADER="Authorization: Bearer $TOKEN"

# Helper para POST autenticado con cuerpo JSON; imprime la respuesta.
api_post() {
    local path="$1"
    local body="$2"
    curl -s -X POST "$BASE_URL$path" \
        -H "Content-Type: application/json" \
        -H "$AUTH_HEADER" \
        -d "$body"
}

# 3. Crear proyectos
echo "   - Creando proyectos..."
PROJECT1_ID=$(api_post "/projects" \
    "{\"name\": \"Rediseño del sitio\", \"description\": \"Actualizar el frontend y la experiencia de usuario de la web principal.\"}" \
    | jq -r '.id')
echo "     Proyecto 'Rediseño del sitio' creado con ID: $PROJECT1_ID"

PROJECT2_ID=$(api_post "/projects" \
    "{\"name\": \"API v2\", \"description\": \"Desarrollar la segunda versión del API con nuevas funcionalidades.\"}" \
    | jq -r '.id')
echo "     Proyecto 'API v2' creado con ID: $PROJECT2_ID"

# 4. Crear tareas para el proyecto 1 (matriz: 3 estados, 3 prioridades, con/sin assignee, con/sin dueDate)
echo "   - Creando tareas..."
DUE_5=$(future_date 5)
DUE_10=$(future_date 10)

# HIGH, sin assignee, sin dueDate
api_post "/projects/$PROJECT1_ID/tasks" \
    "{\"title\": \"Definir paleta de colores\", \"description\": \"Investigar y proponer 3 paletas de colores para la nueva marca.\", \"priority\": \"HIGH\"}" > /dev/null

# HIGH, con dueDate -> se llevará a DONE con un PATCH (la regla exige assignee para DONE, así que le ponemos uno)
TASK_TO_BE_DONE_ID=$(api_post "/projects/$PROJECT1_ID/tasks" \
    "{\"title\": \"Maquetar la nueva home page\", \"description\": \"Crear el HTML y CSS de la página de inicio según los mockups.\", \"priority\": \"HIGH\", \"dueDate\": \"$DUE_5\", \"assigneeId\": 1}" \
    | jq -r '.id')

# MED, assignee ana (id 2)
api_post "/projects/$PROJECT1_ID/tasks" \
    "{\"title\": \"Implementar formulario de contacto\", \"description\": \"El formulario debe tener validación en cliente y enviar los datos al backend.\", \"priority\": \"MED\", \"assigneeId\": 2}" > /dev/null

# LOW, con dueDate
api_post "/projects/$PROJECT1_ID/tasks" \
    "{\"title\": \"Configurar análisis de tráfico\", \"description\": \"Integrar Google Analytics o una alternativa open source.\", \"priority\": \"LOW\", \"dueDate\": \"$DUE_10\"}" > /dev/null

# MED, SIN assignee y en TODO -> habilita E6 (pasar a DONE debe fallar con 400)
api_post "/projects/$PROJECT1_ID/tasks" \
    "{\"title\": \"Test de usabilidad con usuarios piloto\", \"description\": \"Tarea sin asignar para probar la regla de negocio de DONE.\", \"priority\": \"MED\"}" > /dev/null

# LOW, assignee demo (id 1)
api_post "/projects/$PROJECT1_ID/tasks" \
    "{\"title\": \"Revisar copy de la sección Sobre nosotros\", \"description\": \"Asegurar que el texto sea claro, conciso y sin errores.\", \"priority\": \"LOW\", \"assigneeId\": 1}" > /dev/null

# MED, assignee demo -> se llevará a IN_PROGRESS con un PATCH (el status en el POST puede ser ignorado por el API del capstone)
TASK_TO_BE_IN_PROGRESS_ID=$(api_post "/projects/$PROJECT1_ID/tasks" \
    "{\"title\": \"Optimizar imágenes para la web\", \"description\": \"Comprimir todas las imágenes del sitio sin perder calidad visible.\", \"priority\": \"MED\", \"assigneeId\": 1}" \
    | jq -r '.id')
echo "     7 tareas creadas para el proyecto 1."

# Tarea única para el proyecto 2 (para probar contadores y un estado casi-vacío)
api_post "/projects/$PROJECT2_ID/tasks" \
    "{\"title\": \"Definir el esquema OpenAPI v2\", \"description\": \"Documentar todos los nuevos endpoints, modelos y códigos de respuesta.\", \"priority\": \"HIGH\", \"assigneeId\": 1}" > /dev/null
echo "     1 tarea creada para el proyecto 2."

# 5. Llevar tareas a sus estados finales vía PATCH (la regla de negocio se respeta)
echo "   - Actualizando estados (IN_PROGRESS y DONE)..."
curl -s -o /dev/null -X PATCH "$BASE_URL/tasks/$TASK_TO_BE_IN_PROGRESS_ID/status" \
    -H "Content-Type: application/json" -H "$AUTH_HEADER" \
    -d "{\"status\": \"IN_PROGRESS\"}"
curl -s -o /dev/null -X PATCH "$BASE_URL/tasks/$TASK_TO_BE_DONE_ID/status" \
    -H "Content-Type: application/json" -H "$AUTH_HEADER" \
    -d "{\"status\": \"DONE\"}"
echo "     Tarea $TASK_TO_BE_IN_PROGRESS_ID -> IN_PROGRESS; tarea $TASK_TO_BE_DONE_ID -> DONE."

echo ""
echo "--- IDs de referencia ---"
echo "Proyecto 'Rediseño del sitio': $PROJECT1_ID"
echo "Proyecto 'API v2': $PROJECT2_ID"
echo "Usuario 'demo' tiene ID: 1 (asumido)"
echo "Usuario 'ana' tiene ID: 2 (asumido)"
echo "-------------------------"
echo "Siembra de datos completada."
