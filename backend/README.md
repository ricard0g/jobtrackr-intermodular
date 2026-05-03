# JobTrackr - Backend (Java Core)

Backend de **JobTrackr**, una API REST en Java que gestiona usuarios, empresas, candidaturas y etiquetas para el tablero Kanban del proyecto intermodular.


## Objetivo del backend

- Trabajar con **datos reales** en MySQL usando JDBC (no datos simulados).
- Dar servicio al frontend mediante peticiones HTTP (`/api/...`).
- Mostrar un diseño por capas (configuración, modelo, DAO, servicio, controlador) propio de un proyecto profesional sencillo.


## Tecnologías usadas

- **Java Core** (JDK 21+).
- **Servidor HTTP:** `com.sun.net.httpserver.HttpServer` (sin frameworks).
- **JSON:** `Gson`.
- **Base de datos:** MySQL (`JobTrackr`).
- **JDBC:** `mysql-connector-j`.
- **Pool de conexiones:** `HikariCP`.
- **Build:** Maven (`pom.xml` en `/backend`).


## Estructura del backend

Carpeta raíz del backend: `/backend`.

Dentro de `src/main/java/org/ricardo/jobtrackr`:

- `Main.java` → Punto de entrada: configura la base de datos, crea DAOs, Services, Controllers y arranca el servidor.
- `config` → `DatabaseConfig` (pool de conexiones) y `ServerConfig` (puerto, rutas y `HttpServer`).
- `model` → Clases de dominio: `User`, `Enterprise`, `Postulation`, `Tag`, `Interview`, `StatusHistory`, etc.
- `dao` → Acceso a datos con JDBC para cada entidad (`UserDAO`, `PostulationDAO`, etc.).
- `service` → Lógica de negocio (`UserService`, `PostulationService`, `EnterpriseService`, `TagService`, `AllowedFields`).
- `controller` → Endpoints HTTP para `/api/usuario`, `/api/empresas`, `/api/postulaciones`, `/api/etiquetas`.
- `dto`, `exceptions`, `interfaces`, `util` → Clases de apoyo para separar responsabilidades y mejorar la legibilidad.


## Arquitectura por capas

Flujo típico de una petición:

1. El cliente llama a un endpoint (por ejemplo `GET /api/postulaciones`).
2. El **controller** lee la petición, usa `Gson` para JSON y llama al **service** correspondiente.
3. El **service** aplica la lógica de negocio y llama al **DAO**.
4. El **DAO** usa `DatabaseConfig.getConnection()` (HikariCP + JDBC) para consultar o modificar MySQL.
5. La respuesta vuelve en JSON al cliente.

Este patrón cumple con Programación y el MPO: POO real, responsabilidades claras y proyecto mantenible.


## Configuración de base de datos

La clase `DatabaseConfig` lee primero variables de entorno y, si no existen, usa valores por defecto para trabajar en local:

- `DB_URL` → por defecto `jdbc:mysql://localhost:3306/JobTrackr`
- `DB_USER` → por defecto `root`
- `DB_PASSWORD` → por defecto cadena vacía

Se utiliza un `HikariDataSource` para gestionar el pool de conexiones.


## Cómo ejecutar el backend en local

Requisitos:

- JDK 21+
- Maven
- MySQL con la base de datos `JobTrackr` creada y los scripts SQL del proyecto ejecutados (`schema.sql`, `seed.sql`).


Pasos básicos:

```bash
git clone https://github.com/ricard0g/jobtrackr-intermodular.git
cd jobtrackr-intermodular/backend
mvn clean compile
```

1. Asegúrate de que MySQL está arrancado y que la BBDD `JobTrackr` tiene el esquema y los datos de prueba.
2. Ajusta `DB_URL`, `DB_USER`, `DB_PASSWORD` por variables de entorno si tu configuración es distinta (opcional).
3. Ejecuta la clase `Main` desde tu IDE o con Maven (`mvn exec` si lo configuras).
4. El servidor escuchará en el puerto configurado (`PORT` o 8080 por defecto) y expondrá las rutas `/api/...`.


