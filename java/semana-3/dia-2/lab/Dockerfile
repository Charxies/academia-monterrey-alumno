# ============================================================================
# Dockerfile de TaskFlow — ESQUELETO (S3D2). Lo construyes en clase (AM-2).
# En 'solucion/' está la versión final si te atoras — pero primero intenta tú.
# ============================================================================
#
# ---------------------------------------------------------------------------
# REFERENCIA MP-4 — la v1 INGENUA (queda comentada como contraste; NO la uses de
# entrega). Compila FUERA (mvn en tu laptop) y empaca sobre un JDK completo:
#
#   FROM eclipse-temurin:21
#   WORKDIR /app
#   COPY target/taskflow-api-*.jar app.jar
#   ENTRYPOINT ["java", "-jar", "app.jar"]
#
# Crítica guiada (por qué NO es la buena):
#   (1) pesa de más — carga un JDK ENTERO solo para EJECUTAR el jar.
#   (2) el build depende de TU Maven y TU JDK locales: si quien construye no tiene
#       JDK 21, volvió "en mi máquina sí funciona"... ahora en el build.
# ---------------------------------------------------------------------------
#
# ===========================  TU TAREA: v2 MULTI-STAGE (MP-5)  ===============
# Dos FROM = dos etapas. 'build' compila con Maven+JDK; runtime corre con SOLO JRE.

# ---------- Stage 1: build (Maven + JDK 21) ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# TODO MP-5 (1): copiar SOLO el pom primero y bajar dependencias offline, para que
#   esa capa se cachee mientras el pom no cambie (lo que cambia poco, arriba).
#   Pistas:  COPY pom.xml .
#            RUN mvn -q dependency:go-offline

# TODO MP-5 (2): ahora sí copiar el código y empaquetar (los tests corren en 'mvn
#   test' local y en el pipeline de D4, no aquí: por eso -DskipTests).
#   Pistas:  COPY src ./src
#            RUN mvn -q package -DskipTests

# ---------- Stage 2: runtime (solo JRE) ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# TODO MP-5 (3): copiar el jar DESDE la etapa build (nota el --from=build); ni
#   Maven ni el JDK deben viajar a esta imagen.
#   Pista:   COPY --from=build /app/target/taskflow-api-*.jar app.jar

# TODO MP-5 (4): documentar el puerto y definir el proceso del contenedor.
#   Pistas:  EXPOSE 8080
#            ENTRYPOINT ["java", "-jar", "app.jar"]
