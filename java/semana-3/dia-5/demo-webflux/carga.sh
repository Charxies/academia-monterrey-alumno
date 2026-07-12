#!/usr/bin/env bash
# ============================================================================
# carga.sh — 50 requests CONCURRENTES a un endpoint, e imprime el TIEMPO TOTAL.
# El numero que los alumnos PREDICEN antes de correrlo (Demo D1/D2/D3, AM-1).
#
# Uso:
#   ./carga.sh                                  # 50 requests a http://localhost:8091/tareas (MVC)
#   ./carga.sh http://localhost:8092/tareas     # el gemelo reactivo (Flux)
#   ./carga.sh http://localhost:8091/tareas 50  # url + cantidad explicitas
#
# Numeros esperados (mismo endpoint /tareas, latencia simulada de 2 s):
#   - MVC bloqueante (pool capado a 10):            ~10 s  (tandas de 10)
#   - MVC + virtual threads (Demo D3, flag ON):     ~2 s   (codigo imperativo INTACTO)
#   - Flux reactivo (event loop):                   ~2 s   (un puñado de threads)
#
# Portable mac/linux: usa 'hey' si esta instalado (mas preciso); si no, cae a
# curl en paralelo con 'wait'. No requiere instalar nada.
# ============================================================================
set -u

URL="${1:-http://localhost:8091/tareas}"
N="${2:-50}"

echo "==> Carga: $N requests concurrentes a $URL"
echo "    (predice el tiempo total ANTES de ver el resultado)"
echo

# --- Camino A: 'hey' si existe (https://github.com/rakyll/hey) ---
if command -v hey >/dev/null 2>&1; then
  echo "    (usando 'hey')"
  hey -n "$N" -c "$N" "$URL"
  exit 0
fi

# --- Camino B: curl en paralelo + wait (portable, sin dependencias) ---
echo "    ('hey' no encontrado -> curl en paralelo)"
start=$(date +%s)

for i in $(seq 1 "$N"); do
  # -s silencioso, -o /dev/null descarta el body, -w imprime solo el codigo HTTP
  curl -s -o /dev/null -w "" "$URL" &
done
wait   # espera a que TODAS las requests en background terminen

end=$(date +%s)
echo
echo "==> $N requests terminadas en ~$((end - start)) s"
echo "    Compara con tu prediccion. Bloqueante(pool 10) ~10 s | virtual threads / reactivo ~2 s."
