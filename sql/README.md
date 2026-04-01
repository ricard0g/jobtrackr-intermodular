# Base de Datos - JobTrackr

Este directorio contiene todo el diseño y la implementación de la base de datos relacional de **JobTrackr**, una aplicación web tipo Kanban para el seguimiento de candidaturas de empleo.

---

## Análisis de la empresa ficticia

**JobTrackr** es una plataforma SaaS de gestión de búsqueda de empleo. El negocio gestiona la siguiente información:

- **Usuarios** que buscan empleo activamente.
- **Empresas** a las que los usuarios aplican.
- **Postulaciones** (candidaturas) a ofertas de trabajo concretas.
- **Entrevistas** asociadas a cada candidatura, con múltiples rondas.
- **Etiquetas** (tags tecnológicos y de modalidad) para clasificar las candidaturas.
- **Historial de cambios de estado** de cada candidatura a lo largo del proceso.

---

## Diagrama Entidad-Relación (ER)

El diagrama ER identifica las entidades principales del sistema, sus atributos y las relaciones entre ellas.

👉 [`diagramas/JobTrackr - Diagrama ER.drawio.png`](./diagramas/JobTrackr-JobTrackr%20-%20Diagrama%20ER.drawio.png)

**Entidades principales:**

| Entidad | Descripción |
|---|---|
| `usuarios` | Persona que usa la plataforma para gestionar sus postulaciones |
| `empresas` | Compañía a la que el usuario ha enviado o considera enviar una postulacion |
| `postulaciones` | Candidatura concreta de un usuario a un rol en una empresa |
| `entrevistas` | Rondas de entrevista asociadas a una postulación |
| `etiquetas` | Tags reutilizables (tecnologías, modalidad) para clasificar postulaciones |
| `historial_estatus` | Registro cronológico de los cambios de estado de cada postulación |

---

## Modelo Relacional

A partir del diagrama ER, se obtiene el modelo relacional con las tablas, tipos de datos, claves primarias, claves foráneas y restricciones.

👉 [`diagramas/JobTrackr - Modelo Relacional.drawio.png`](./diagramas/JobTrackr-JobTrackr%20-%20Modelo%20Relacional.drawio.png)

**Relaciones más importantes:**
- Un `usuario` tiene muchas `postulaciones` → relación **1:N**
- Una `empresa` puede aparecer en muchas `postulaciones` → relación **1:N**
- Una `postulacion` puede tener muchas `entrevistas` → relación **1:N**
- Una `postulacion` puede tener muchas `etiquetas` y una `etiqueta` puede estar en muchas `postulaciones` → relación **N:M** (resuelta con la tabla intermedia `postulaciones_etiquetas`)
- Una `postulacion` tiene muchos registros en `historial_estatus` → relación **1:N**

---

## Scripts SQL

### [`schema.sql`](./schema.sql) - Creación de tablas

Contiene la creación completa de la base de datos `JobTrackr` con todas sus tablas, tipos de datos, claves primarias, claves foráneas y restricciones (`CHECK`, `UNIQUE`, `ENUM`).

**Aspectos destacados del diseño:**
- `ENUM` para el campo `estatus` de las postulaciones: `POSTULADA`, `REVISION`, `ENTREVISTA`, `OFERTA`, `DESCARTADA`, `RETIRADA`
- `ENUM` para el tipo de entrevista: `PERSONAL`, `TELEFONICA`, `TECNICA`, `ARQUITECTURA`, `RRHH`, `FINAL`
- `CHECK` constraints para validar rangos salariales (`salario_maximo > salario_minimo`)
- `ON DELETE CASCADE` en postulaciones → entrevistas, etiquetas e historial (si se borra una candidatura, se borra todo lo asociado para mantener la integridad referencial)
- `ON DELETE RESTRICT` en empresas → postulaciones (no se puede borrar una empresa si tiene candidaturas activas)
- `TIMESTAMP` con `ON UPDATE CURRENT_TIMESTAMP` para auditoría automática

> He usado `ENUM` por simplicidad para el proyecto, pero considero que atributos como `tipo_entrevista` se podrían normalizar a una entidad aparte para cumplir cumplir al 100% con la 2NF.

### [`seed.sql`](./seed.sql) - Inserción de datos de ejemplo

Contiene datos de prueba realistas para poder trabajar y jugar con la base de datos:
- 1 usuario dummy principal (`carlos.rodriguez@ejemplo.jobtrackr.dev`)
- Empresas tecnológicas españolas reales (Bankinter, BBVA, Glovo, Cabify, etc.)
- 10+ postulaciones en distintos estados del Kanban
- Entrevistas en distintas fases y resultados
- Etiquetas tecnológicas (Java, JavaScript, React, SQL…) y de modalidad (Remoto, Híbrido…)
- Historial de cambios de estado para demostrar el flujo completo de una candidatura

### [`queries.sql`](./queries.sql) - Consultas útiles para el negocio

Contiene consultas SQL reales y significativas para el negocio de JobTrackr.

> Algunas queries del fichero incluyen `TRANSACTION` previas para ajustar fechas de los datos seed y poder validar los resultados correctamente. Los comentarios dentro del archivo lo explican.

---

## Cómo ejecutar la base de datos en local

**Requisitos previos:**
- MySQL 8.0 o superior instalado
- Un cliente SQL: [MySQL Workbench](https://www.mysql.com/products/workbench/), [DBeaver](https://dbeaver.io/) o la extensión de base de datos de VS Code

**Pasos:**

1. Abre tu cliente SQL y conéctate a tu servidor MySQL local (`localhost:3306`)
2. Ejecuta el script de creación del esquema:
   ```sql
   source /ruta/al/proyecto/sql/schema.sql
   ```
3. Ejecuta el script de datos de prueba:
   ```sql
   source /ruta/al/proyecto/sql/seed.sql
   ```
4. Ya puedes ejecutar las consultas de `queries.sql` para explorar los datos

> También puedes abrir cada archivo directamente desde tu cliente SQL y ejecutarlo con el botón de "Run".

---

## Relación con el proyecto web

La base de datos está diseñada específicamente para el funcionamiento interno de JobTrackr. La web representa la empresa y sus funcionalidades, mientras que esta base de datos modela exactamente cómo funciona la plataforma por dentro:

- Cada columna del tablero Kanban (`POSTULADA`, `REVISION`, `ENTREVISTA`…) corresponde a los valores del `ENUM` de `estatus` en la tabla `postulaciones`
- El campo `orden_kanban` de `postulaciones` permite mantener el orden de las tarjetas dentro de cada columna
- La tabla `historial_estatus` registra automáticamente cada movimiento de tarjeta, dando trazabilidad completa al proceso de búsqueda de empleo

