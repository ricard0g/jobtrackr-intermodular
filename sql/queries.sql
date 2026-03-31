-- Obtener todo sobre el principal Dummy User
SELECT *
FROM usuarios u
WHERE u.usuario_id = (SELECT usuario_id
FROM usuarios
WHERE correo_electronico_usuario = 'carlos.rodriguez@ejemplo.jobtrackr.dev');

-- Obtener todas las postulaciones del usuario por estatus y orden en el kanban
SELECT
    *
FROM postulaciones p
    JOIN usuarios u ON p.usuario_id = u.usuario_id
ORDER BY p.estatus, p.orden_kanban ASC;


-- Obtener todos los datos de una postulacion por ID, enseñando el nombre 
SELECT
    *
FROM postulaciones p
    JOIN usuarios u ON u.usuario_id = p.usuario_id
    JOIN empresas e ON p.empresa_id = e.empresa_id
WHERE p.postulacion_id = 2;

-- Obtener postulacion por un Estatus Concreto
SELECT
    *
FROM postulaciones p
    JOIN usuarios u ON u.usuario_id = p.usuario_id
    JOIN empresas e ON p.empresa_id = e.empresa_id
WHERE p.estatus = 'REVISION'
ORDER BY p.orden_kanban ASC;


-- Obtener solo postulaciones de trabajo telematico
SELECT
    *
FROM postulaciones p
    JOIN usuarios u ON p.usuario_id = u.usuario_id
    JOIN empresas e ON e.empresa_id = p.empresa_id
WHERE p.es_telematico
ORDER BY p.estatus ,p.orden_kanban ASC;


-- Postulaciones dentro de un rango salarial dado por el usuario
SELECT
    p.rol,
    e.nombre_empresa AS Nombre_Empresa,
    p.salario_minimo AS Salario_Minimo,
    p.salario_maximo AS Salario_Maximo
FROM postulaciones p
    JOIN usuarios u ON p.usuario_id = u.usuario_id
    JOIN empresas e ON e.empresa_id = p.empresa_id
WHERE p.salario_minimo BETWEEN 12000.00 AND 21000.00
-- Aqui suponemos que el usuario nos dió este rango
ORDER BY p.salario_minimo ASC;


-- Obtener todas las empresas
SELECT
    *
FROM empresas;


-- Buscar empresas por nombre (simulando que el usuario esta filtrando empresas por nombre)
SELECT
    *
FROM empresas e
WHERE e.nombre_empresa LIKE '%tt%';


-- Obtener empresas a las que el usuario ha postulado por lo menos una vez
SELECT
    e.nombre_empresa,
    COUNT(p.postulacion_id) AS Conteo_Postulaciones
FROM empresas e
    JOIN postulaciones p ON e.empresa_id = p.empresa_id
    JOIN usuarios u ON p.usuario_id = u.usuario_id
GROUP BY e.nombre_empresa
HAVING COUNT(p.postulacion_id) > 0;
-- En el caso de solo disponer de los datos de seed.sql no hay empresas sin postulacion, pero al usar la web app se puede añadir una nueva empresa y se verá como no tiene postulaciones asignadas.


-- Obtener todas las etiquetas disponibles
SELECT
    *
FROM etiquetas;

-- Etiquetas asignadas a una postulacion concreta
SELECT
    p.postulacion_id,
    p.rol,
    e.nombre_empresa,
    et.nombre_etiqueta AS Nombre_Etiqueta,
    et.color_etiqueta AS Color_Etiqueta
FROM etiquetas et
    LEFT JOIN postulaciones_etiquetas pe ON pe.etiqueta_id = et.etiqueta_id
    LEFT JOIN postulaciones p ON p.postulacion_id = pe.postulacion_id
    JOIN usuarios u ON u.usuario_id = p.usuario_id
    JOIN empresas e ON e.empresa_id = p.empresa_id
WHERE p.postulacion_id = 10;


-- Obtener entrevistas de una postulacion
SELECT
    *
FROM entrevistas en
    JOIN postulaciones p ON p.postulacion_id = en.postulacion_id
    JOIN usuarios u ON u.usuario_id = p.usuario_id
WHERE p.postulacion_id = 10;


-- Ver entrevistas pendientes del usuario, ordenadas por proximidad a la fecha actual

-- Todos los datos fueron añadidos en las fechas de marzo, asi que para chequear la validez de esta query se puede correr esta Transacción sencilla estableciendo fechas para estos dos registros por encima de la fecha en la que se esté haciendo esta revision.
START TRANSACTION;

UPDATE entrevistas SET fecha_entrevista = CURRENT_TIMESTAMP + INTERVAL
15 DAY WHERE entrevista_id = 2;
UPDATE entrevistas SET fecha_entrevista = CURRENT_TIMESTAMP + INTERVAL
7 DAY WHERE entrevista_id = 7;

COMMIT;

SELECT
    en.entrevista_id,
    e.nombre_empresa AS Nombre_Empresa,
    p.rol AS Rol,
    en.fecha_entrevista AS Fecha_Entrevista
FROM entrevistas en
    JOIN postulaciones p ON en.postulacion_id = p.postulacion_id
    JOIN usuarios u ON u.usuario_id = p.usuario_id
    JOIN empresas e ON p.empresa_id = e.empresa_id
WHERE en.fecha_entrevista > CURRENT_TIMESTAMP AND en.resultado_entrevista = 'PENDIENTE'
ORDER BY Fecha_Entrevista ASC;



-- Obtener el numero de rondas de entrevista superadas de una postulacion
SELECT
    e.nombre_empresa AS Nombre_Empresa,
    p.rol AS Rol,
    COUNT(en.numero_ronda) AS Numero_Rondas_Superadas
FROM entrevistas en
    JOIN postulaciones p ON p.postulacion_id = en.postulacion_id
    JOIN empresas e ON e.empresa_id = p.empresa_id
    JOIN usuarios u ON u.usuario_id = p.usuario_id
WHERE p.postulacion_id = 4 AND en.resultado_entrevista = 'SUPERADA';



-- Historial completo de cambios de Estado de una postulacion ordenados cronológicamente de más antiguo a más reciente

-- Como antes primero será necesario correr esta Transacción para modificar un poco los valores de los registros de Historial_Estatus ya que las fechas de actualización estan establecidas para tener un DEFAULT CURRENT_TIMESTAMP, por lo tanto todos los registros tendrán la misma fecha, porque todos los registros se insertaron en el mismo instante.

START TRANSACTION;

UPDATE historial_estatus
    SET cambiado_en = '2026-03-01 09:00:00'
    WHERE postulacion_id = 1 AND antiguo_estatus IS NULL;

UPDATE historial_estatus
    SET cambiado_en = '2026-03-05 14:30:00'
    WHERE postulacion_id = 1 AND antiguo_estatus = 'POSTULADA';

UPDATE historial_estatus
    SET cambiado_en = '2026-03-12 11:00:00'
    WHERE postulacion_id = 1 AND antiguo_estatus = 'REVISION';

UPDATE historial_estatus
    SET cambiado_en = '2026-03-01 10:15:00'
    WHERE postulacion_id = 2 AND antiguo_estatus IS NULL;

UPDATE historial_estatus
    SET cambiado_en = '2026-03-18 16:00:00'
    WHERE postulacion_id = 2 AND antiguo_estatus = 'POSTULADA';

UPDATE historial_estatus
    SET cambiado_en = '2026-03-10 09:30:00'
    WHERE postulacion_id = 3 AND antiguo_estatus IS NULL;

UPDATE historial_estatus
    SET cambiado_en = '2026-02-20 08:45:00'
    WHERE postulacion_id = 4 AND antiguo_estatus IS NULL;

UPDATE historial_estatus
    SET cambiado_en = '2026-02-25 10:00:00'
    WHERE postulacion_id = 4 AND antiguo_estatus = 'POSTULADA';

UPDATE historial_estatus
    SET cambiado_en = '2026-02-28 12:30:00'
    WHERE postulacion_id = 4 AND antiguo_estatus = 'REVISION';

UPDATE historial_estatus
    SET cambiado_en = '2026-03-20 09:00:00'
    WHERE postulacion_id = 4 AND antiguo_estatus = 'ENTREVISTA';

UPDATE historial_estatus
    SET cambiado_en = '2026-02-15 11:00:00'
    WHERE postulacion_id = 5 AND antiguo_estatus IS NULL;

UPDATE historial_estatus
    SET cambiado_en = '2026-02-22 15:00:00'
    WHERE postulacion_id = 5 AND antiguo_estatus = 'POSTULADA';

UPDATE historial_estatus
    SET cambiado_en = '2026-03-08 10:00:00'
    WHERE postulacion_id = 6 AND antiguo_estatus IS NULL;

UPDATE historial_estatus
    SET cambiado_en = '2026-03-20 17:00:00'
    WHERE postulacion_id = 6 AND antiguo_estatus = 'POSTULADA';

COMMIT;

SELECT
    he.estatus_id,
    he.antiguo_estatus AS 'Antiguo Estatus',
    he.nuevo_estatus AS 'Nuevo Estatus',
    he.cambiado_en AS 'Fecha de Cambio',
    he.nota_estatus AS 'Nota Estatus'
FROM historial_estatus he
    JOIN postulaciones p ON p.postulacion_id = he.postulacion_id
    JOIN usuarios u ON u.usuario_id = p.usuario_id
WHERE p.postulacion_id = 4
ORDER BY he.cambiado_en ASC;



-- Obtener el total de postulaciones del usuario agrupadas por estatus
SELECT
    CONCAT(u.primer_nombre_usuario, ' ', u.primer_apellido_usuario) AS Nombre_Usuario,
    p.estatus AS Estatus,
    COUNT(p.estatus) AS Numero_Postulaciones_Por_Estatus
FROM postulaciones p
    JOIN usuarios u ON p.usuario_id = u.usuario_id
GROUP BY p.estatus, u.primer_nombre_usuario, u.primer_apellido_usuario;


-- Obtener salario medio ofertado por Estatus (media entre salario_minimo y salario_maximo), solo contando en donde los dos valores esten presentes claro.
SELECT
    p.estatus AS Postulacion_Estatus,
    ROUND(AVG((p.salario_minimo + p.salario_maximo)/2), 2) AS Salario_Medio
FROM postulaciones p
WHERE p.salario_minimo IS NOT NULL AND p.salario_maximo IS NOT NULL
GROUP BY p.estatus;

-- Lista de las etiquetas mas usadas por un usuario (solo etiquetas teconologicas)
SELECT
    et.nombre_etiqueta AS Etiqueta,
    COUNT(et.etiqueta_id) AS Apariciones_Totales
FROM etiquetas et
    JOIN postulaciones_etiquetas pe ON pe.etiqueta_id = et.etiqueta_id
    JOIN postulaciones p ON p.postulacion_id = pe.postulacion_id
    JOIN usuarios u ON u.usuario_id = p.usuario_id
WHERE u.usuario_id = 1 AND et.nombre_etiqueta NOT IN ('Junior', 'Gran Empresa', 'Presencial', 'Remoto', 'Practicas', 'Startup', 'Hibrido')
GROUP BY et.nombre_etiqueta
ORDER BY  Apariciones_Totales DESC;

-- Mostrar datos sobre solo postulaciones activas de un usuario (que no este ni DESCARTADA ni RETIRADA), enseñando el numero total de entrevistas registradas
SELECT
    e.nombre_empresa AS Empresa,
    p.rol AS Rol,
    p.estatus AS Estatus,
    COUNT(en.entrevista_id) AS Numero_Entrevistas
FROM postulaciones p
    LEFT JOIN entrevistas en ON en.postulacion_id = p.postulacion_id
    JOIN empresas e ON e.empresa_id = p.empresa_id
    JOIN usuarios u ON p.usuario_id = u.usuario_id
WHERE u.usuario_id = 1 AND p.estatus NOT IN ('DESCARTADA', 'RETIRADA')
GROUP BY p.postulacion_id, e.nombre_empresa, p.rol, p.estatus;


-- Obtener cuantas postulaciones de un usuario alcanzaron cada fase (puede ser muy interesante para porcentajes de paso de fase, una especie de porcentaje de conversion)
SELECT
    he.nuevo_estatus,
    COUNT(he.nuevo_estatus) AS Postulaciones_Fase
FROM postulaciones p
    JOIN usuarios u ON u.usuario_id = p.usuario_id
    JOIN historial_estatus he ON he.postulacion_id = p.postulacion_id
WHERE u.usuario_id = 1
GROUP BY he.nuevo_estatus;


-- Listar empresas a las que el usuario envió más de una postulación
SELECT
    e.nombre_empresa,
    COUNT(p.postulacion_id) AS Postulaciones_Enviadas
FROM postulaciones p
    JOIN usuarios u ON u.usuario_id = p.usuario_id
    JOIN empresas e ON p.empresa_id = e.empresa_id
WHERE u.usuario_id = 1
GROUP BY e.empresa_id
HAVING COUNT(p.postulacion_id) > 1
ORDER BY Postulaciones_Enviadas DESC;