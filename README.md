# API tareas

[![Estado del Flujo de Trabajo de GitHub Actions](https://github.com/JuanLiz/proteccion-test/actions/workflows/deployment.yml/badge.svg)](https://github.com/JuanLiz/proteccion-test/actions/workflows/deployment.yml)

API de tareas creada como parte de la prueba de postulación para la Protección.

## Tecnologías

- Java
- Spring Boot
- Spring Security con autenticación JWT y filtro de seguridad
- PostgreSQL (hospedado en Neon)
- Flyway para migraciones de base de datos

## Funcionalidades

- **Autenticación:**
  - Registro de nuevos usuarios.
  - Inicio de sesión de usuarios para obtener un token JWT.
- **Gestión de Usuarios:**
  - Crear, leer, actualizar y eliminar usuarios (CRUD).
- **Gestión de Tareas:**
  - Crear, leer, actualizar y eliminar tareas (CRUD).
  - Asignar tareas a uno o más usuarios.
  - Ver tareas por usuario según su rol, autenticación y estado (creado por el usuario o asignado a él).

- **Despliegue e integración continua:**
  - Despliegue automático en servidor privado mediante SSH usando pipelines de GitHub Actions.
