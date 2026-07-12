<!-- ============================================================================
     ERR-2 — "El changelog inflado" (MP-4). Un CHANGELOG con features que el repo
     NO tiene y métricas inventadas. El instructor lo contrasta con
     `git log --oneline --reverse`: NADA de esto está en la historia.

     Regla que se instala: cada línea del changelog debe ser rastreable a un commit
     real. Curar (agrupar, traducir a valor) SÍ; inventar NO.

     NO copies este archivo: es el ANTIejemplo. El bueno está en
     ../plantillas/CHANGELOG-plantilla.md y ../../solucion/CHANGELOG-ejemplo.md
     ============================================================================ -->

# Changelog

## [3.0.0] - 2026-07-11

### Added
- **Arquitectura multi-tenant** con aislamiento por organización.        <!-- ▲ MENTIRA: no hay tenants; User/Project/Task no tienen orgId. -->
- **Caché distribuido con Redis** para acelerar los listados.            <!-- ▲ MENTIRA: no hay dependencia de Redis en el pom, ni @Cacheable. -->
- **Notificaciones en tiempo real por WebSocket.**                        <!-- ▲ MENTIRA: no hay WebSocket ni STOMP; la API es REST pura. -->
- Panel de administración web con métricas.                              <!-- ▲ MENTIRA: no hay frontend; el capstone es "sin frontend". -->
- Soporte para GraphQL además de REST.                                   <!-- ▲ MENTIRA: no existe ningún schema GraphQL. -->

### Performance
- **99.9 % de uptime garantizado.**                                      <!-- ▲ MENTIRA: no hay monitoreo ni SLA; un uptime no se "declara". -->
- Latencia reducida un **300 %.**                                        <!-- ▲ ABSURDO: no se puede reducir 300 % (ni se midió). -->
- Escala a **1 millón de usuarios concurrentes.**                        <!-- ▲ MENTIRA: nunca se probó carga; el pool de Tomcat es el default. -->

### Security
- Cumplimiento **SOC 2 e ISO 27001.**                                    <!-- ▲ MENTIRA: no hay auditoría ni certificación alguna. -->
- Cifrado de extremo a extremo.                                          <!-- ▲ VAGO/FALSO: hay HTTPS/JWT, no "E2E encryption". -->

<!-- ============================================================================
     Qué SÍ diría la historia real (git log --oneline --reverse):
       - test: suite en slices + gate JaCoCo 70%
       - feat: Dockerfile multi-stage + compose + perfil docker
       - ci: pipeline test/build-and-push/deploy a GHCR
       - feat: endpoint /info
       - chore: barrida de deuda
       - docs: README + CHANGELOG
       - chore: release taskflow api v3.0
     Eso es lo que hay. Lo demás es humo — y en la demo, el punto 5 enseña el repo
     REAL y el humo se desmorona en pantalla.
     ============================================================================ -->
