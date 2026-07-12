#!/usr/bin/env bash
# ============================================================================
#  runbook-ops-heredado.sh  —  "runbook del equipo de ops de TaskFlow"
# ----------------------------------------------------------------------------
#  MATERIAL DE LECTURA, NO STARTER. Este archivo es el INSUMO de MP-1 (AM-1):
#  lo heredaste del equipo de operaciones y NADIE recuerda del todo qué hace
#  cada línea. Tu trabajo NO es ejecutarlo — es DESCIFRARLO con:
#
#      gh copilot explain "<pega aquí el comando>"
#
#  y luego contrastar la explicación con `man`/`--help`: ¿te dijo el PORQUÉ,
#  o solo parafraseó los flags? Un comando de ops mal entendido borra logs de
#  producción; por eso hoy la regla de oro es: ningún comando se ejecuta sin
#  leerlo y entenderlo. Los rm/prune/gzip -exec NO perdonan.
#
#  ⚠ Las rutas (/var/log/taskflow, access.log) y nombres de servicio son de un
#  entorno de ops ficticio: NO existen en tu máquina. No corras nada de aquí.
# ============================================================================

set -euo pipefail   # <- también explícalo: ¿qué hace cada letra? (e, u, pipefail)

# ----------------------------------------------------------------------------
# [1] ROTACIÓN DE LOGS — el find críptico del cron nocturno.
#     Comprime los logs viejos y grandes de la API para liberar disco.
#     Pregúntale a `gh copilot explain` qué significa CADA predicado y por qué
#     el terminador es `+` y no `\;` (pista: cuántas veces invoca a gzip).
# ----------------------------------------------------------------------------
find /var/log/taskflow -name '*.log*' -mtime +14 -size +1M -exec gzip {} +

# ----------------------------------------------------------------------------
# [2] TOP ENDPOINTS QUE ROMPEN — el awk del post-mortem.
#     Sobre el access log (formato combined) de tu API desplegada, saca los
#     endpoints que más 5xx devolvieron. $9 = código de estado, $7 = ruta.
#     Preguntas para explain: ¿por qué `^5` y no `== 500`? ¿qué hace el bloque
#     END? ¿por qué el `sort -rn` va DESPUÉS del awk y no dentro?
# ----------------------------------------------------------------------------
awk '$9 ~ /^5/ { count[$7]++ } END { for (ruta in count) print count[ruta], ruta }' \
    /var/log/taskflow/access.log | sort -rn | head -10

# ----------------------------------------------------------------------------
# [3] (afín) LIMPIEZA DE .tar.gz HUÉRFANOS — el xargs con -print0.
#     Borra backups comprimidos de más de 30 días. Explica por qué `-print0`
#     con `xargs -0` (pista: nombres de archivo con espacios) y qué pasaría
#     con un `rm -rf` mal parametrizado aquí. Este SÍ borra: entiéndelo entero.
# ----------------------------------------------------------------------------
find /var/backups/taskflow -name '*.tar.gz' -mtime +30 -print0 | xargs -0 rm -f

# ----------------------------------------------------------------------------
# [4] (afín) LATENCIA p95 A OJO — el pipe de sort/awk sobre el tiempo de
#     respuesta (último campo del log). Explica qué calcula el índice
#     0.95*NR y por qué es una aproximación, no el p95 exacto.
# ----------------------------------------------------------------------------
awk '{ print $NF }' /var/log/taskflow/timing.log | sort -n \
    | awk '{ a[NR]=$1 } END { print "p95 ~", a[int(NR*0.95)], "ms" }'

# ----------------------------------------------------------------------------
#  Cierre de MP-1: por cada comando que explicaste, anota en el diario UNA
#  cosa que la explicación te aclaró y UNA que tuviste que verificar aparte
#  (man/--help) porque `explain` la parafraseó sin decir el porqué.
# ----------------------------------------------------------------------------
