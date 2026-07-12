# 05 — Una ejecución verde de punta a punta

**Qué se ve:** una **ejecución** del pipeline con los 3 stages en **verde** (Succeeded), los tiempos
por stage (Source ~5s, Build ~2-3 min, Deploy ~30s) y el commit/SHA que la disparó.

**Qué decir:** "Esto es un **run verde** de su pestaña Actions, con otra piel. Fíjense en los tiempos
por stage: el Build se lleva casi todo (compilar + suite + imagen) — igual que su job `test`+`build`
domina el run. Y como los stages están encadenados, si el Build hubiera fallado, el Deploy **ni
arranca**: es su `needs:` otra vez. Verde de punta a punta = lo que ustedes entregan hoy."

**Equivale a:** un run verde de `test → build-and-push → deploy` en la pestaña Actions.
