# Requisitos del Sistema

## Requisitos Funcionales

### Módulo de Autenticación
| ID | Requisito | Prioridad |
|----|-----------|-----------|
| RF-01 | El sistema debe permitir inicio de sesión con email y contraseña | Alta |
| RF-02 | El sistema debe validar credenciales contra la base de datos | Alta |
| RF-03 | El sistema debe generar un token JWT válido por 24 horas | Alta |
| RF-04 | El sistema debe redirigir según el rol del usuario (admin, driver, parent) | Alta |
| RF-05 | El sistema debe cerrar sesión y limpiar el token al presionar "Salir" | Alta |
| RF-06 | El sistema debe mostrar pantalla de transición animada durante el login | Media |

### Módulo de Dashboard por Rol
| ID | Requisito | Prioridad |
|----|-----------|-----------|
| RF-07 | El administrador debe ver resumen operativo con estadísticas | Alta |
| RF-08 | El conductor debe ver su ruta activa, estudiantes asignados y asistencia | Alta |
| RF-09 | El apoderado debe ver el estado de su hijo y ubicación del bus | Alta |
| RF-10 | Cada dashboard debe mostrar indicadores en tiempo real | Alta |

### Módulo de Asistencia QR
| ID | Requisito | Prioridad |
|----|-----------|-----------|
| RF-11 | El sistema debe generar un código QR único por estudiante | Alta |
| RF-12 | El sistema debe permitir escanear QR con cámara o subir imagen | Alta |
| RF-13 | El sistema debe registrar si el estudiante abordó o bajó del bus | Alta |
| RF-14 | El sistema debe actualizar el estado del estudiante EN_BUS / ENTREGADO | Alta |
| RF-15 | El sistema debe notificar al apoderado cuando el estudiante aborde/descienda | Media |

### Módulo de Notificaciones
| ID | Requisito | Prioridad |
|----|-----------|-----------|
| RF-16 | El conductor debe poder enviar notificaciones predefinidas con 1 clic | Alta |
| RF-17 | Las notificaciones deben tener roles destino (apoderados, admin, ambos) | Alta |
| RF-18 | El sistema debe entregar notificaciones en tiempo real vía WebSocket | Alta |
| RF-19 | El sistema debe persistir el historial de notificaciones en BD | Media |
| RF-20 | El administrador debe poder enviar notificaciones manuales a rutas | Media |

### Módulo de Incidencias
| ID | Requisito | Prioridad |
|----|-----------|-----------|
| RF-21 | El conductor debe reportar incidencias (mecánicas, tráfico, clima, estudiante) | Alta |
| RF-22 | El sistema debe registrar tipo, descripción, bus y fecha de la incidencia | Alta |
| RF-23 | El sistema debe marcar incidencias como resuelto/pendiente | Media |

### Módulo de Tracking GPS
| ID | Requisito | Prioridad |
|----|-----------|-----------|
| RF-24 | El sistema debe capturar ubicación GPS del conductor | Alta |
| RF-25 | El sistema debe almacenar latitud/longitud del bus en BD | Alta |
| RF-26 | El apoderado debe ver la ubicación del bus en un mapa | Alta |
| RF-27 | El administrador debe ver todas las rutas en un mapa general | Media |

### Módulo de Gestión (Admin)
| ID | Requisito | Prioridad |
|----|-----------|-----------|
| RF-28 | El admin debe poder CRUD de usuarios | Alta |
| RF-29 | El admin debe poder CRUD de buses | Alta |
| RF-30 | El admin debe poder CRUD de estudiantes | Alta |
| RF-31 | El admin debe poder CRUD de rutas | Alta |
| RF-32 | El admin debe poder ver reportes de asistencia | Media |

## Requisitos No Funcionales

| ID | Requisito | Descripción |
|----|-----------|-------------|
| RNF-01 | Rendimiento | Tiempo de respuesta < 500ms para APIs críticas |
| RNF-02 | Seguridad | Autenticación JWT, contraseñas hasheadas con BCrypt |
| RNF-03 | Disponibilidad | Sistema debe funcionar con o sin RabbitMQ (degradación graceful) |
| RNF-04 | Compatibilidad | Responsive design: mobile-first, tablets y desktop |
| RNF-05 | Persistencia | Datos almacenados en PostgreSQL (Neon serverless) |
| RNF-06 | Tiempo Real | WebSocket con reconexión automática cada 5 segundos |
| RNF-07 | Escalabilidad | API stateless permite escalado horizontal |
| RNF-08 | UX | Splash screen inicial, transiciones animadas, feedback visual |

## Criterios de Aceptación

1. Un apoderado puede ver en tiempo real cuando su hijo aborda el bus
2. Un conductor puede registrar asistencia escaneando QR en < 3 segundos
3. Un administrador puede gestionar toda la flota desde una sola pantalla
4. Las notificaciones del conductor llegan al apoderado en < 2 segundos
5. El sistema funciona correctamente sin RabbitMQ (modo degradado)
6. La interfaz es usable desde un dispositivo móvil
