# Metodología Ágil — ViaKids

## Enfoque: Scrum adaptado para equipo pequeño

**Ritmo:** Sprints de 1 semana
**Ceremonias:** Daily standup (5 min), Sprint Planning (lunes), Sprint Review + Retro (viernes)
**Herramientas:** GitHub Projects, Issues, Milestones

## Épicas (Epics)

| Épica | Descripción | Sprint |
|-------|-------------|--------|
| **E-01** | Autenticación y Roles | Sprint 1 |
| **E-02** | Dashboard Admin | Sprint 1 |
| **E-03** | Dashboard Conductor | Sprint 2 |
| **E-04** | Dashboard Apoderado | Sprint 3 |
| **E-05** | Asistencia QR | Sprint 2 |
| **E-06** | Notificaciones en Tiempo Real | Sprint 3 |
| **E-07** | Tracking GPS | Sprint 4 |
| **E-08** | Incidencias | Sprint 2 |
| **E-09** | Reportes y Analytics | Sprint 4 |
| **E-10** | Infraestructura y DevOps | Sprint 1 |

## Sprint Backlog

### Sprint 1: Fundación
| Item | Tipo | Horas | Estado |
|------|------|-------|--------|
| Configurar proyecto Spring Boot + Neon PostgreSQL | Técnica | 4 | ✅ |
| Configurar proyecto React + Vite + Tailwind | Técnica | 3 | ✅ |
| Implementar JWT auth (login/register) | Funcional | 6 | ✅ |
| Crear splash screen animada | Funcional | 3 | ✅ |
| Crear layout responsivo + navegación | Funcional | 4 | ✅ |
| Seed data (usuarios demo) | Funcional | 2 | ✅ |
| Configurar WebSocket (STOMP) | Técnica | 4 | ✅ |
| Configurar Docker RabbitMQ | Técnica | 2 | ✅ |

### Sprint 2: Conductor + Asistencia
| Item | Tipo | Horas | Estado |
|------|------|-------|--------|
| Admin Dashboard con stats | Funcional | 6 | ✅ |
| Driver Dashboard con tabs | Funcional | 6 | ✅ |
| QR Scanner (cámara + archivo) | Funcional | 5 | ✅ |
| Generación de QR por estudiante | Funcional | 3 | ✅ |
| Registro de asistencia (scan → backend) | Funcional | 6 | ✅ |
| Reporte de incidencias | Funcional | 4 | ✅ |
| CRUD usuarios/buses/estudiantes | Funcional | 8 | ✅ |

### Sprint 3: Notificaciones + Apoderado
| Item | Tipo | Horas | Estado |
|------|------|-------|--------|
| Parent Dashboard con seguimiento | Funcional | 6 | ✅ |
| Presets de notificaciones (10 mensajes) | Funcional | 4 | ✅ |
| Envío de notificaciones vía API | Funcional | 4 | ✅ |
| WebSocket en frontend (STOMP) | Funcional | 4 | ✅ |
| Filtro de notificaciones por rol target | Funcional | 2 | ✅ |
| Historial de notificaciones | Funcional | 2 | ✅ |
| Mapa con ubicación del bus | Funcional | 5 | ✅ |

### Sprint 4: Tracking + Reportes
| Item | Tipo | Horas | Estado |
|------|------|-------|--------|
| Tracking GPS con geolocalización | Funcional | 4 | ✅ |
| Actualización de ubicación en BD | Funcional | 3 | ✅ |
| LiveTracking page (admin) | Funcional | 4 | ✅ |
| Reportes de asistencia | Funcional | 4 | ✅ |
| Route timeline en Parent Dashboard | Funcional | 2 | ✅ |
| Pull request final + documentación | Técnica | 4 | ✅ |

## User Stories

### US-01: Inicio de Sesión
> **Como** usuario del sistema,
> **Quiero** iniciar sesión con mi email y contraseña,
> **Para** acceder a mi panel según mi rol.

**Criterios de Aceptación:**
- Ingreso con credenciales válidas redirige al dashboard correcto
- Credenciales inválidas muestran mensaje de error
- La sesión persiste al recargar la página (token en localStorage)
- El splash screen se reproduce antes del login

### US-02: Registro de Asistencia QR
> **Como** conductor,
> **Quiero** escanear el QR del estudiante al subir y bajar del bus,
> **Para** llevar un registro digital de asistencia.

**Criterios de Aceptación:**
- Puedo escanear con la cámara del dispositivo
- Puedo subir una foto del QR como alternativa
- Se registra si el estudiante abordó o descendió
- El apoderado recibe notificación en tiempo real
- El estado del estudiante se actualiza automáticamente

### US-03: Notificación Rápida
> **Como** conductor,
> **Quiero** enviar mensajes predefinidos con un solo clic,
> **Para** informar rápidamente a los apoderados sobre novedades de la ruta.

**Criterios de Aceptación:**
- 10 mensajes predefinidos disponibles
- Los mensajes tienen destinatarios preconfigurados (apoderados, admin)
- El envío es instantáneo y confirma visualmente
- El historial muestra los mensajes enviados

### US-04: Seguimiento en Vivo
> **Como** apoderado,
> **Quiero** ver la ubicación del bus en un mapa,
> **Para** saber cuándo llegará mi hijo.

**Criterios de Aceptación:**
- El mapa muestra la posición actual del bus asignado
- La ubicación se actualiza periódicamente
- Veo el estado actual de mi hijo (en bus/entregado/ausente)
- Recibo notificaciones cuando mi hijo aborda o desciende

### US-05: Dashboard Administrativo
> **Como** administrador,
> **Quiero** ver un resumen operativo con estadísticas,
> **Para** monitorear el estado de la flota y la asistencia.

**Criterios de Aceptación:**
- Veo cantidad de buses en ruta y en espera
- Veo total de estudiantes y asistencia del día
- Puedo acceder a gestión de usuarios, buses, estudiantes y rutas
- Los gráficos muestran tendencias semanales

## Definición de Done (DoD)

- [ ] Código implementado y funcional
- [ ] Compilación/build exitoso (sin errores)
- [ ] Integrated con backend real (no mocks)
- [ ] Responsive design verificado
- [ ] Manejo de errores implementado (try/catch + feedback visual)
- [ ] Sin regresiones en otras funcionalidades

## Pipeline de Trabajo (GitHub Flow)

1. `main` — rama estable, siempre deployable
2. `feature/<nombre>` — ramas por funcionalidad
3. Pull Request → revisión → merge a `main`
4. Tags semánticos para releases (`v1.0.0`, `v1.1.0`)
