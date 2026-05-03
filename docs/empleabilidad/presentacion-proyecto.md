# Presentación profesional del proyecto - JobTrackr

## Resumen del proyecto

**Nombre:** JobTrackr  
**Tipo:** Aplicación web tipo Kanban  
**Objetivo:** Gestionar y visualizar candidaturas de empleo de forma clara y estructurada.

JobTrackr permite registrar ofertas de trabajo y seguir su evolución a través de distintas fases, moviendo tarjetas en un tablero Kanban.

---

## Problema que resuelve

En una búsqueda activa de empleo es fácil:

- Olvidar dónde se ha enviado el CV.
- Perder el hilo de en qué fase está cada proceso.
- Acumular información en sitios dispersos (notas, hojas de cálculo, correos).

JobTrackr soluciona esto con:

- Un tablero Kanban donde cada columna representa una fase del proceso.
- Tarjetas con información clave de cada candidatura.
- Un modelo de datos pensado específicamente para procesos de selección.

---

## Público objetivo

El proyecto está pensado para:

- Estudiantes de FP y perfiles junior que empiezan a buscar su primer trabajo.
- Personas en transición profesional o búsqueda activa.
- Cualquiera que quiera gestionar su búsqueda de empleo como un “proyecto” organizado.

Aunque está adaptado al nivel de 1.º de DAW, la idea es lo suficientemente realista como para evolucionar a un producto más completo.

---

## Tecnologías utilizadas

### Frontend

- Landing corporativa:
  - HTML, CSS y JavaScript.
- Web App (SPA):
  - React + React Router.
  - Tailwind CSS para estilos.
  - `dnd-kit` para drag & drop en el tablero Kanban.

### Backend

- Java Core (JDK).
- Servidor HTTP con `com.sun.net.httpserver.HttpServer` (API REST).
- JDBC (`JdbcConnectionPool`) para acceso a MySQL.
- `Gson` para serializar y deserializar JSON.

### Base de datos

- MySQL con diseño relacional.
- Scripts separados:
  - `schema.sql` para el esquema.
  - `seed.sql` para datos de ejemplo.
  - `queries.sql` para consultas relevantes.

### Infraestructura

- Ejecución garantizada en local.
- Preparado para:
  - Docker.
  - Vercel (frontend).
  - Railway (backend + base de datos).

---

## Qué demuestro con este proyecto

Con JobTrackr demuestro que soy capaz de:

- Analizar una necesidad real y traducirla a un modelo relacional coherente.
- Desarrollar un backend en Java Core conectado a MySQL mediante JDBC.
- Exponer una API REST consumida por un frontend moderno.
- Construir una interfaz Kanban funcional con drag & drop y estados persistentes.
- Organizar un monorepo profesional (`/web`, `/backend`, `/sql`, `/docs`, etc.).
- Documentar el proyecto con enfoque tanto técnico como de empleabilidad.

En resumen, JobTrackr es una pieza de portfolio donde se ve no solo que “sé programar”, sino que entiendo el problema, el usuario y el producto.