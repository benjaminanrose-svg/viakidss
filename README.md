# ViaKids - Sistema de Transporte Escolar

Sistema full-stack para gestión de transporte escolar con monitoreo en tiempo real, asistencia QR, notificaciones y roles diferenciados.

## Stack Tecnológico

| Capa | Tecnología |
|------|-----------|
| **Frontend** | React 19 + Vite 8 + Tailwind CSS 4 + Lucide Icons |
| **Backend** | Spring Boot 4.0.6 + Java 21 |
| **Base de Datos** | Neon PostgreSQL |
| **Autenticación** | JWT (JJWT 0.12.5) |
| **WebSocket** | STOMP sobre RabbitMQ (broker relay) o SimpleBroker |
| **Mensajería** | RabbitMQ (condicional) |
| **QR** | html5-qrcode + QRCode.react |

## Estructura del Proyecto

```
ViaKidsCompleto/
├── frontend/              # React + Vite
│   ├── src/
│   │   ├── api/           # Axios config + interceptors
│   │   ├── components/    # UI components (organisms, templates, ui)
│   │   ├── context/       # Auth, Toast context providers
│   │   ├── hooks/         # useWebSocket, useGeolocation, etc.
│   │   ├── pages/         # Dashboard pages por rol
│   │   └── services/      # API service layer
│   ├── index.html
│   ├── vite.config.js
│   └── package.json
├── backend/               # Spring Boot
│   ├── src/main/java/com/viakids/backend/
│   │   ├── attendance/    # Asistencia (controller, service, repo, entity)
│   │   ├── auth/          # JWT auth (login, register)
│   │   ├── bus/           # Gestión de buses
│   │   ├── config/        # Security, CORS, WebSocket, JWT, RabbitMQ
│   │   ├── incident/      # Incidencias
│   │   ├── notification/  # Notificaciones + Presets
│   │   ├── route/         # Gestión de rutas
│   │   ├── student/       # Gestión de estudiantes
│   │   ├── user/          # Gestión de usuarios
│   │   └── websocket/     # WS controller
│   └── pom.xml
├── docs/                  # Documentación
│   ├── architecture.md
│   ├── requirements.md
│   ├── flows.md
│   ├── database.md
│   ├── api.md
│   └── agile.md
└── .gitignore
```

## Acceso Demo

| Rol | Email | Password |
|-----|-------|----------|
| Administrador | admin@viakids.cl | 123456 |
| Conductor | conductor@viakids.cl | 123456 |
| Apoderado | apoderado@viakids.cl | 123456 |

## Inicio Rápido

### Backend
```bash
cd backend
./mvnw spring-boot:run
# Servidor en http://localhost:8081
```

### Frontend
```bash
cd frontend
npm install
npm run dev
# Servidor en http://localhost:5173
```

### RabbitMQ (opcional)
```bash
docker run -d --name viakids-rabbitmq -p 5672:5672 -p 15672:15672 -p 61613:61613 rabbitmq:management
# UI: http://localhost:15672 (guest/guest)
```

## Variables de Entorno

| Variable | Descripción | Default |
|----------|-------------|---------|
| `VITE_API_URL` | Base URL del API | `/api` |
| `VITE_WS_URL` | URL WebSocket | `ws://localhost:8081/ws` |
| `SPRING_DATASOURCE_URL` | JDBC PostgreSQL | Neon DB |
| `JWT_SECRET` | Secreto JWT | (configurado) |
| `CORS_ORIGINS` | Orígenes permitidos (separados por coma) | `http://localhost:5173,https://*.vercel.app` |
| `PORT` | Puerto del backend (Railway lo asigna automáticamente) | `8081` |

## Deployment

### Backend en Railway
1. Crear nuevo proyecto y conectar el repositorio de GitHub
2. **Settings → Source → Root Directory:** `/backend`
3. Railway detecta el `pom.xml` y compila automáticamente con Maven
4. Agregar variables de entorno: `DATABASE_URL`, `JWT_SECRET`, `CORS_ORIGINS`
5. Railway expone el servicio en `https://<proyecto>.up.railway.app`

### Frontend en Vercel
1. Importar el repositorio en Vercel
2. **Root Directory:** `frontend`
3. Framework: Vite (detección automática)
4. Variable de entorno: `VITE_API_URL=https://<tu-backend>.up.railway.app/api`
5. Deploy automático en cada push

> El archivo `vercel.json` incluido configura los rewrites necesarios para que React Router funcione sin errores 404 en rutas internas.
