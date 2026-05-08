# Documentación de la API REST

Base URL: `/api` (proxy Vite en desarrollo → `http://localhost:8081`)

Autenticación: `Authorization: Bearer <token>` (JWT en todas las rutas excepto `/auth/**`)

---

## Autenticación

### `POST /api/auth/login`

Inicio de sesión.

**Body:**
```json
{
  "email": "admin@viakids.cl",
  "password": "123456"
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "role": "admin",
  "name": "Admin ViaKids"
}
```

**Errors:** 401 Unauthorized

### `POST /api/auth/register`

Registro de nuevo usuario (rol ADMIN por defecto).

### `GET /api/auth/me`

Obtener perfil del usuario autenticado.

---

## Usuarios

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/users` | Listar todos los usuarios |
| `POST` | `/api/users` | Crear usuario |
| `PUT` | `/api/users/{id}` | Actualizar usuario |
| `DELETE` | `/api/users/{id}` | Eliminar usuario |

---

## Buses

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/buses` | Listar todos los buses |
| `GET` | `/api/buses/{id}` | Obtener bus por ID |
| `GET` | `/api/buses/{id}/location` | Obtener ubicación GPS |
| `POST` | `/api/buses` | Crear bus |
| `PUT` | `/api/buses/{id}` | Actualizar bus |
| `DELETE` | `/api/buses/{id}` | Eliminar bus |

**Bus entity:**
```json
{
  "id": "uuid",
  "patente": "AB-1234",
  "conductor": "Juan Pérez",
  "capacidad": 40,
  "estado": "EN_RUTA",
  "tiempoEstimado": "15 min",
  "lat": -33.4489,
  "lng": -70.6693
}
```

---

## Estudiantes

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/students` | Listar todos los estudiantes |
| `GET` | `/api/students/{id}` | Obtener estudiante por ID |
| `GET` | `/api/students/{id}/qr` | Datos para QR del estudiante |
| `POST` | `/api/students` | Crear estudiante |
| `PUT` | `/api/students/{id}` | Actualizar estudiante |
| `DELETE` | `/api/students/{id}` | Eliminar estudiante |

**Student entity:**
```json
{
  "id": "uuid",
  "nombre": "Mateo García",
  "curso": "4to B",
  "rut": "20.123.456-7",
  "apoderado": "Carlos García",
  "telefono": "+56912345678",
  "colegio": "Colegio Los Andes",
  "estado": "EN_ESPERA",
  "busId": "uuid",
  "routeId": "uuid",
  "busPatente": "AB-1234",
  "ruta": "Ruta Norte"
}
```

---

## Rutas

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/routes` | Listar rutas |
| `GET` | `/api/routes/{id}` | Obtener ruta por ID |
| `POST` | `/api/routes` | Crear ruta |
| `PUT` | `/api/routes/{id}` | Actualizar ruta |
| `DELETE` | `/api/routes/{id}` | Eliminar ruta |

---

## Asistencia

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/attendance` | Listar registros (filtro opcional: `fecha`, `busId`) |
| `GET` | `/api/attendance/student/{studentId}` | Historial de un estudiante |
| `GET` | `/api/attendance/summary/today` | Resumen de hoy (abordaron, entregados, ausentes) |
| `POST` | `/api/attendance/scan` | Registrar escaneo QR |

**POST /api/attendance/scan Body:**
```json
{
  "qrData": {
    "id": "uuid-del-estudiante",
    "nombre": "Mateo García"
  },
  "action": "BOARDED"
}
```

**Actions:** `BOARDED`, `DISEMBARKED`, `ABSENT`

**Response:**
```json
{
  "success": true,
  "data": { "id": "uuid", "studentId": "uuid", "studentName": "Mateo García", "action": "BOARDED", "timestamp": "2026-05-08T12:00:00Z" },
  "studentStatus": "BOARDED"
}
```

---

## Notificaciones

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/notifications` | Listar todas las notificaciones |
| `POST` | `/api/notifications` | Crear notificación manual |
| `PUT` | `/api/notifications/{id}/read` | Marcar como leída |
| `GET` | `/api/notifications/presets` | Listar mensajes predefinidos |
| `POST` | `/api/notifications/preset` | Enviar notificación predefinida |

**POST /api/notifications/preset Body:**
```json
{
  "presetKey": "retraso_10",
  "presetLabel": "Retraso 10 min",
  "mensaje": "Estimados apoderados, presentaremos un retraso...",
  "tipo": "ALERTA",
  "ruta": "Ruta Norte",
  "senderName": "Juan Pérez",
  "senderRole": "DRIVER",
  "targetRoles": "PARENT,ADMIN"
}
```

**Presets disponibles:** `ruta_iniciada`, `llegando_colegio`, `ruta_completada`, `retraso_5`, `retraso_10`, `retraso_15`, `emergencia`, `clima_adverso`, `averia_menor`, `todo_ok`

---

## Incidencias

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/incidents` | Listar incidencias |
| `POST` | `/api/incidents` | Crear incidencia |

**POST /api/incidents Body:**
```json
{
  "tipo": "MECANICO",
  "descripcion": "Falla menor en motor",
  "busPatente": "AB-1234"
}
```

**Tipos:** `MECANICO`, `TRAFICO`, `CLIMA`, `ESTUDIANTE`, `OTRO`

---

## Reportes

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/reports/attendance` | Reporte de asistencia (filtro: `fecha`, `busId`) |

---

## WebSocket (STOMP)

**Endpoint:** `ws://localhost:8081/ws`

| Cola/Tópico | Evento | Payload |
|-------------|--------|---------|
| `/queue/notifications` | Nueva notificación | Objeto `Notification` completo |
| `/queue/attendance` | Nuevo registro asistencia | Objeto `AttendanceRecord` |
| `/queue/locations` | Actualización de ubicación | `{busId, lat, lng}` |

**Conexión:**
```javascript
const client = new Client({
  brokerURL: 'ws://localhost:8081/ws',
  reconnectDelay: 5000,
  onConnect: () => {
    client.subscribe('/queue/notifications', msg => {
      console.log(JSON.parse(msg.body));
    });
  }
});
client.activate();
```
