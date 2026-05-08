# Flujos del Sistema

## 1. Flujo de Autenticación

```
Usuario              Frontend               Backend               DB
  │                     │                     │                   │
  │   Ingresa creds     │                     │                   │
  ├────────────────────>│                     │                   │
  │                     │  POST /auth/login   │                   │
  │                     ├────────────────────>│                   │
  │                     │                     │  Query user       │
  │                     │                     ├──────────────────>│
  │                     │                     │  <── user data ──┤
  │                     │                     │                   │
  │                     │                     │  Validate password│
  │                     │                     │  (BCrypt)         │
  │                     │                     │                   │
  │                     │                     │  Generate JWT     │
  │                     │  <── {token,role} ──┤                   │
  │                     │                     │                   │
  │                     │  Store token in     │                   │
  │                     │  localStorage       │                   │
  │                     │                     │                   │
  │                     │  Navigate to        │                   │
  │                     │  /admin | /driver   │                   │
  │                     │  | /parent          │                   │
  │  <── Dashboard ─────┤                     │                   │
```

## 2. Flujo de Asistencia QR

```
Conductor              Frontend               Backend               DB           Apoderado (WS)
  │                     │                     │                   │                │
  │  Inicia ruta        │                     │                   │                │
  ├────────────────────>│                     │                   │                │
  │                     │                     │                   │                │
  │  Escanea QR         │                     │                   │                │
  │  (cámara o archivo) │                     │                   │                │
  ├────────────────────>│                     │                   │                │
  │                     │  POST /attendance/scan                  │                │
  │                     │  {qrData, action}   │                   │                │
  │                     ├────────────────────>│                   │                │
  │                     │                     │  Save record      │                │
  │                     │                     ├──────────────────>│                │
  │                     │                     │  Update Student   │                │
  │                     │                     │  status           │                │
  │                     │                     ├──────────────────>│                │
  │                     │                     │                   │                │
  │                     │                     │  Send WS event    │                │
  │                     │                     │  to /queue/       │                │
  │                     │                     │  notifications    │                │
  │                     │                     ├───────────────────────────────────>│
  │                     │                     │                   │                │
  │  <── ✓ Abordó ─────┤                     │                   │          ┌─────┴─────┐
  │                     │                     │                   │          │  "Mateo   │
  │                     │                     │                   │          │  abordó   │
  │                     │                     │                   │          │  el bus"  │
  │                     │                     │                   │          └───────────┘
```

## 3. Flujo de Notificaciones Rápidas (Presets)

```
Conductor              Frontend               Backend               DB           Destinatarios (WS)
  │                     │                     │                   │                │
  │  Click preset       │                     │                   │                │
  │  (ej: "Retraso")    │                     │                   │                │
  ├────────────────────>│                     │                   │                │
  │                     │  POST /notifications/preset             │                │
  │                     │  {presetKey,         │                   │                │
  │                     │   targetRoles,       │                   │                │
  │                     │   mensaje, etc}      │                   │                │
  │                     ├────────────────────>│                   │                │
  │                     │                     │  Save notification │               │
  │                     │                     │  + sender info    │               │
  │                     │                     ├──────────────────>│                │
  │                     │                     │                   │                │
  │                     │                     │  Publishes to     │                │
  │                     │                     │  /topic/notify    │               │
  │                     │                     │  (STOMP)          │               │
  │  <── ✓ Enviado ─────┤                     ├──────────────────────────────────>│
  │                     │                     │                   │          ┌─────┴─────┐
  │                     │                     │                   │          │  Apoderados │
  │                     │                     │                   │          │  Admin      │
  │                     │                     │                   │          └───────────┘
```

## 4. Flujo de Tracking GPS

```
Conductor              Frontend               Backend               DB           Apoderado (WS)
  │                     │                     │                   │                │
  │  Inicia ruta        │                     │                   │                │
  │  + GPS activado     │                     │                   │                │
  ├────────────────────>│                     │                   │                │
  │                     │                     │                   │                │
  │  (Cada 5-10s)       │                     │                   │                │
  │  Geolocation API    │                     │                   │                │
  │  -> lat, lng        │                     │                   │                │
  │                     │  PUT /buses/:id     │                   │                │
  │  (Mock en frontend) │  {lat, lng}         │                   │                │
  │                     ├────────────────────>│                   │                │
  │                     │                     │  Update bus       │                │
  │                     │                     │  coordinates      │                │
  │                     │                     ├──────────────────>│                │
  │                     │                     │                   │                │
  │                     │                     │                   │                │
  │                     │                     │                   │   (Polling)    │
  │                     │  GET /students      │                   │                │
  │                     │  -> find student    │                   │                │
  │                     │                     │                   │                │
  │                     │  GET /buses/:id     │                   │                │
  │                     │  (cada 10s)         │                   │                │
  │                     ├────────────────────>│                   │                │
  │                     │                     │  Query bus        │                │
  │                     │                     ├──────────────────>│                │
  │                     │                     │  <── lat, lng ───┤                │
  │                     │  <── {lat, lng} ────┤                   │                │
  │                     │                     │                   │                │
  │                     │  Render BusMap      │                   │                │
  │                     │  con marcador       │                   │                │
```

## 5. Flujo de Reporte de Incidencias

```
Conductor              Frontend               Backend               DB
  │                     │                     │                   │
  │  Click "Reportar"   │                     │                   │
  ├────────────────────>│                     │                   │
  │                     │  Modal: tipo + desc │                   │
  │                     │                     │                   │
  │  Enviar reporte     │                     │                   │
  ├────────────────────>│                     │                   │
  │                     │  POST /incidents    │                   │
  │                     │  {tipo, desc, bus } │                   │
  │                     ├────────────────────>│                   │
  │                     │                     │  Save incident    │
  │                     │                     ├──────────────────>│
  │                     │                     │                   │
  │  <── ✓ Exitoso ─────┤                     │                   │
```

## 6. Flujo de Datos Iniciales (Seed)

```
Inicio App              DataInitializer          DB
  │                        │                    │
  │  onApplicationEvent    │                    │
  ├───────────────────────>│                    │
  │                        │  ¿User vacía?      │
  │                        ├───────────────────>│
  │                        │  <── count=0 ──────┤
  │                        │                    │
  │                        │  Crear 3 usuarios  │
  │                        │  (admin, driver,   │
  │                        │   parent)          │
  │                        ├───────────────────>│
  │                        │                    │
  │                        │  Crear 3 buses     │
  │                        ├───────────────────>│
  │                        │                    │
  │                        │  Crear 2 rutas     │
  │                        ├───────────────────>│
  │                        │                    │
  │                        │  Crear 3 estudiantes│
  │                        ├───────────────────>│
  │                        │                    │
  │                        │  Crear notificaciones│
  │                        ├───────────────────>│
  │                        │                    │
  │                        │  Crear incidencias  │
  │                        ├───────────────────>│
  │  <── Ready ───────────┤                    │
```
