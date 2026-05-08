# Arquitectura del Sistema

## Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────────────────────────┐
│                        CLIENTE (Browser)                            │
│                                                                     │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌───────────────────┐  │
│  │ Splash   │  │  Login   │  │ Dashboard│  │ WebSocket Client  │  │
│  │ Screen   │  │  Form    │  │ (por rol)│  │ (STOMP.js)        │  │
│  └──────────┘  └──────────┘  └──────────┘  └───────────────────┘  │
│                                                                     │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │              Axios (JWT Interceptor)                         │   │
│  └──────────────────────────────────────────────────────────────┘   │
└──────────────────────────┬──────────────────────────────────────────┘
                           │
                    ┌──────┴──────┐
                    │  Vite Proxy │  /api → localhost:8081
                    └──────┬──────┘
                           │
┌──────────────────────────┴──────────────────────────────────────────┐
│                      SERVIDOR (Spring Boot :8081)                   │
│                                                                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────────┐  │
│  │  Security    │  │    CORS      │  │   JWT Auth Filter        │  │
│  │  Config      │  │   Config     │  │   (JwtAuthenticationFilter) │
│  └──────────────┘  └──────────────┘  └──────────────────────────┘  │
│                                                                     │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │              REST Controllers                                │   │
│  │  Auth  │  User  │  Student  │  Bus  │  Route  │  Attendance │   │
│  │  Incident  │  Notification  │  Report  │  WebSocket          │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                                                                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────────┐  │
│  │   Services   │  │  Repositories│  │   Entities (JPA)         │  │
│  └──────┬───────┘  └──────┬───────┘  └──────────┬───────────────┘  │
│         │                 │                      │                  │
│         └─────────────────┼──────────────────────┘                  │
│                           │                                         │
│  ┌────────────────────────┴──────────────────────────────────────┐  │
│  │              WebSocket (STOMP)                                │  │
│  │  ┌──────────────────┐    ┌─────────────────────────────────┐  │  │
│  │  │  SimpleBroker    │ ←→ │  StompBrokerRelay → RabbitMQ   │  │  │
│  │  │  (default)       │    │  (cuando app.rabbitmq.enabled) │  │  │
│  │  └──────────────────┘    └─────────────────────────────────┘  │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           │                                         │
│  ┌────────────────────────┴──────────────────────────────────────┐  │
│  │              Neon PostgreSQL                                   │  │
│  └───────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────┘
```

## Patrón Arquitectónico

**Microservicio Monolítico** con separación por paquetes (feature-based packages):

- Cada entidad vive en su propio paquete: `attendance/`, `bus/`, `incident/`, etc.
- Comunicación síncrona vía REST entre frontend y backend
- Comunicación asíncrona vía STOMP WebSocket para eventos en tiempo real
- Mensajería opcional con RabbitMQ para notificaciones

## Decisiones Técnicas

| Decisión | Justificación |
|----------|---------------|
| **Spring Boot 4.0.6** | Última versión estable con soporte LTS para Java 21 |
| **JWT stateless** | Escalabilidad horizontal sin sesiones compartidas |
| **STOMP sobre WS** | Protocolo maduro para mensajería pub/sub en web |
| **RabbitMQ condicional** | SimpleBroker suficiente para desarrollo; RabbitMQ para producción |
| **Neon PostgreSQL** | Serverless PostgreSQL con escalado automático |
| **Vite proxy** | Evita CORS en desarrollo; Nginx maneja en producción |
| **UUID como PK** | Seguridad (no expone IDs secuenciales) + distribuible |

## Roles y Permisos

| Rol | Acceso | Funcionalidades |
|-----|--------|-----------------|
| **ADMIN** | `/admin/*` | CRUD completo, reportes, tracking general, gestión de flota |
| **DRIVER** | `/driver/*` | Asistencia QR, notificaciones rápidas, incidencias, ruta |
| **PARENT** | `/parent/*` | Seguimiento en vivo, QR del estudiante, notificaciones |

## Flujo de Autenticación

1. Usuario ingresa credenciales → `POST /api/auth/login`
2. Backend valida contra BD → genera JWT con email + rol
3. Frontend almacena token en `localStorage` (`viakids_token_v3`)
4. Axios interceptor adjunta `Authorization: Bearer <token>` a cada request
5. JwtAuthenticationFilter valida token en cada request protegido
6. 401 auto-logout: interceptor de respuesta limpia storage y redirige a `/`
