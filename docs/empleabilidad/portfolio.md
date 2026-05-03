# Portfolio básico - Proyecto JobTrackr

## 1. Descripción breve

- **Nombre:** JobTrackr  
- **Tipo:** Aplicación web tipo Kanban  
- **Objetivo:** Organizar y seguir candidaturas de empleo.

JobTrackr centraliza las ofertas a las que aplica el usuario y permite ver en qué fase está cada proceso de selección.

---

## 2. Funcionalidades principales

- Registro de candidaturas con datos como:
  - Empresa.
  - Puesto.
  - Fecha de solicitud.
  - Canal de aplicación.
  - Notas importantes.
- Tablero Kanban con columnas que representan las fases del proceso (por ejemplo):
  - “Por enviar”.
  - “CV enviado”.
  - “Entrevista técnica”.
  - “Oferta”.
  - “Descartado”.
- Movimiento de tarjetas entre columnas mediante drag & drop.
- Persistencia de los datos en MySQL.
- Comunicación entre frontend y backend a través de una API REST.

---

## 3. Enlaces

- **Repositorio GitHub:**  
  `https://github.com/ricard0g/jobtrackr-intermodular`

---

## 4. Capturas de pantalla (pendiente)

Cuando el proyecto esté más avanzado, añadiré capturas, por ejemplo:

- Landing page explicando qué es JobTrackr.
- Tablero Kanban con varias candidaturas en diferentes columnas.
- Formulario de creación/edición de candidatura.

Las imágenes se almacenarán en la carpeta de assets del proyecto y se referenciarán desde aquí.

---

## 5. Tecnologías utilizadas

### Frontend

- HTML, CSS y JavaScript (landing).
- React + React Router (SPA del tablero).
- Tailwind CSS.
- `dnd-kit` para drag & drop.

### Backend

- Java Core con `com.sun.net.httpserver.HttpServer`.
- JDBC (`JdbcConnectionPool`) para MySQL.
- `Gson` para JSON.

### Base de datos

- MySQL con:

  - `schema.sql` (creación de tablas).
  - `seed.sql` (datos de ejemplo).
  - `queries.sql` (consultas con JOIN y casos reales).

### Control de versiones e infraestructura

- Git + GitHub con monorepo organizado.
- Preparado para Docker, Vercel y Railway.

---

## 6. Aprendizajes clave

Con este proyecto he aprendido a:

- Conectar varios módulos de 1.º de DAW en un mismo producto.
- Diseñar y documentar una base de datos coherente con el negocio.
- Implementar una API REST en Java Core con acceso a MySQL.
- Crear una interfaz que refleja el estado real de los datos.
- Mantener un repositorio limpio, estructurado y entendible.

---

## 7. Posibles mejoras futuras

Algunas ideas de evolución:

- Filtros y búsquedas por empresa, estado o fecha.
- Métricas básicas:
  - Número de candidaturas por mes.
  - Ratio de avance entre fases.
- Recordatorios o alertas cuando una candidatura lleve mucho tiempo sin cambios.
- Integraciones con APIs de empleo o redes profesionales.
- Uso de IA para sugerir prioridades o próximas acciones.