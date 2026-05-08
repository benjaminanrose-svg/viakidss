# Modelo de Base de Datos

## Esquema Relacional

```
┌─────────────────────────────────────────────────────────────┐
│                         users                                │
├──────────────┬────────────────┬──────────────────────────────┤
│ PK id (UUID) │ nombre (varchar)│ email (varchar, unique)     │
│ password (varchar)            │ rol (enum: ADMIN/DRIVER/PARENT)│
│ telefono (varchar)            │ estado (enum: ACTIVO/SUSPENDIDO)│
│ created_at (timestamp)        │                              │
└──────────────────────────────────────────────────────────────┘
        │
        │ (no FK directa — relación por nombre de apoderado)
        │
┌─────────────────────────────────────────────────────────────┐
│                        students                              │
├──────────────┬────────────────┬──────────────────────────────┤
│ PK id (UUID) │ nombre (varchar)│ curso (varchar)             │
│ rut (varchar) │ apoderado (varchar)│ telefono (varchar)       │
│ colegio (varchar)             │ estado (enum: EN_ESPERA/     │
│ bus_id (UUID) ────────────────┤         EN_BUS/ENTREGADO/    │
│ route_id (UUID) ──────────────┤         AUSENTE)             │
└───────────────────────────────┴──────────────────────────────┘
        │                        │
        │                        │
┌───────┴──────────────┐  ┌─────┴──────────────────┐
│       buses           │  │       routes           │
├────────┬─────────────┤  ├────────┬───────────────┤
│ PK id  │ patente     │  │ PK id  │ nombre         │
│ (UUID) │ (varchar)   │  │ (UUID) │ colegio        │
│        │ conductor   │  │        │ bus_id (FK)    │
│        │ capacidad   │  │        │ horario        │
│        │ estado      │  │        │ paradas        │
│        │ (EN_RUTA/   │  └────────┴───────────────┘
│        │  EN_ESPERA) │
│        │ lat         │
│        │ lng         │
│        │ tiempo_     │
│        │ estimado    │
└────────┴─────────────┘

┌─────────────────────────────────────────────────────────────┐
│                     attendance                               │
├──────────────┬────────────────┬──────────────────────────────┤
│ PK id (UUID) │ student_id (FK)│ student_name (varchar)      │
│ bus_id (UUID) │ bus_patente   │ route (varchar)             │
│ timestamp (instant)            │ action (enum: BOARDED/     │
│ trip_type (enum: MORNING/      │          DISEMBARKED/      │
│            AFTERNOON)          │          ABSENT)            │
└──────────────────────────────────────────────────────────────┘
        │
        │ FK: student_id → students.id

┌─────────────────────────────────────────────────────────────┐
│                    notifications                             │
├──────────────┬────────────────┬──────────────────────────────┤
│ PK id (UUID) │ fecha (varchar)│ tipo (enum: ALERTA/INFO/    │
│ mensaje (text)                │          URGENTE)            │
│ ruta (varchar)│ leido (bool)  │ sender_name (varchar)       │
│ sender_role (varchar)         │ target_roles (varchar)      │
│ preset_key (varchar)          │ preset_label (varchar)       │
│ created_at (timestamp)        │                              │
└──────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                      incidents                               │
├──────────────┬────────────────┬──────────────────────────────┤
│ PK id (UUID) │ fecha (varchar)│ tipo (enum: MECANICO/       │
│ descripcion (text)            │          TRAFICO/CLIMA/     │
│ bus_patente (varchar)         │          ESTUDIANTE/OTRO)   │
│ resuelto (bool)               │                              │
└──────────────────────────────────────────────────────────────┘
```

## Diagrama Entidad-Relación (textual)

```
users 1 ──── * students   (un apoderado puede tener varios estudiantes)
buses 1 ──── * students   (un bus transporta varios estudiantes)
buses 1 ──── 1 routes    (una ruta es operada por un bus)
students 1 ──── * attendance (un estudiante tiene múltiples registros)
```

## Tablas y Propósitos

| Tabla | Propósito |
|-------|-----------|
| `users` | Cuentas de acceso con roles (ADMIN, DRIVER, PARENT) |
| `buses` | Flota de vehículos con ubicación GPS y estado |
| `routes` | Rutas programadas asociadas a buses |
| `students` | Estudiantes con datos del apoderado y asignación |
| `attendance` | Registro diario de abordaje/descenso de estudiantes |
| `notifications` | Mensajes enviados desde conductores o admin |
| `incidents` | Reportes de incidencias durante las rutas |

## Estrategia de IDs

- **UUID v4** como Primary Key en todas las tablas
- Ventajas: seguridad (no expone crecimiento), distribución, no conflicto en merges
- Desventaja: más espacio en índices (pero irrelevante para el volumen)

## Consideraciones

- `busPatente` y `ruta` en Student son campos `@Transient` (calculados por el service)
- `student_name` en Attendance es desnormalizado (evita JOIN para el timeline)
- `target_roles` usa formato "PARENT,ADMIN" (string CSV) por simplicidad
- `timestamp` en Attendance usa `Instant` para zona horaria UTC
- Seed data se carga solo cuando `users` está vacío (idempotente)
