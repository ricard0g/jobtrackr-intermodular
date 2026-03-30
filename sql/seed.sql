-- Unico Usuario de la App, es de Ejemplo - Dummy User
INSERT INTO usuarios
    (primer_nombre_usuario, primer_apellido_usuario, segundo_apellido_usuario, correo_electronico_usuario)
VALUES
    ("Carlos", "Rodriguez", "Perez", "carlos.rodriguez@ejemplo.jobtrackr.dev");


-- Insertar Algunas Empresas por Defecto
INSERT INTO empresas
    (nombre_empresa, logo_empresa)
VALUES
    ('Inditex', 'https://logos.hunter.io/inditex.com'),
    ('Telefónica', 'https://logos.hunter.io/telefonica.com'),
    ('Santander', 'https://logos.hunter.io/santander.com'),
    ('Cabify', 'https://logos.hunter.io/cabify.com'),
    ('Wallapop', 'https://logos.hunter.io/wallapop.com'),
    ('Glovo', 'https://logos.hunter.io/glovoapp.com'),
    ('BBVA', 'https://logos.hunter.io/bbva.com'),
    ('Accenture', 'https://logos.hunter.io/accenture.com'),
    ('Capgemini', 'https://logos.hunter.io/capgemini.com'),
    ('Indra', 'https://logos.hunter.io/indracompany.com'),
    ('NTT Data', 'https://logos.hunter.io/nttdata.com'),
    ('Google', 'https://logos.hunter.io/google.com'),
    ('Microsoft', 'https://logos.hunter.io/microsoft.com'),
    ('Spotify', 'https://logos.hunter.io/spotify.com'),
    ('Shopify', 'https://logos.hunter.io/shopify.com'),
    ('Atlassian', 'https://logos.hunter.io/atlassian.com'),
    ('GitHub', 'https://logos.hunter.io/github.com');


-- Etiquetas con colores para etiquetar postulaciones

INSERT INTO etiquetas
    (nombre_etiqueta, color_etiqueta)
VALUES
    ("Java", "#F89820"),
    ("JavaScript", "#F7DF1E"),
    ("Python", "#3776AB"),
    ("SQL", "#336791"),
    ('MySQL', '#00758F'),
    ('PostgreSQL', '#336791'),
    ('SQL Server', '#CC2927'),
    ("Docker", "#2496ED"),
    ("Git", "#F05032"),
    ("React", "#61DAFB"),
    ("Node.js", "#339933"),
    ("AWS", "#FF9900"),
    ("Linux", "#FCC624"),
    ("Practicas", "#A78BFA"),
    ("Junior", "#34D399"),
    ("Mid-level", "#1a664a"),
    ("Senior", "#092319"),
    ("Media Jornada", "#F472B6"),
    ("Remoto", "#22D3EE"),
    ("Hibrido", "#818CF8"),
    ("Presencial", "#94A3B8"),
    ("Startup", "#F43F5E"),
    ("Gran Empresa", "#64748B");


-- Postulaciones
INSERT INTO postulaciones
    (usuario_id, empresa_id, rol, estatus, orden_kanban, salario_minimo, salario_maximo, ubicacion, es_telematico, oferta_url, nota_postulacion, fecha_postulacion)
VALUES
    (1, 8, 'Desarrollador Java Junior', 'ENTREVISTA', 0, 18000.00, 22000.00, 'Madrid, España', FALSE, 'https://www.accenture.com/es-es/careers/jobdetails?id=java-junior-001', 'Buscan Spring Boot y eso me gusta. Posición para consultora.', '2026-03-01'),
    (1, 9, 'Desarrollador Full Stack Junior', 'REVISION', 0, 20000.00, 25000.00, 'Barcelona, España', TRUE, 'https://www.capgemini.com/es-es/careers/job-listings/full-stack-junior/', 'Me dijero que es proceso de tres fases: test técnico, entrevista técnica y entrevista con RRHH. Envié test técnico el dia 18.', '2026-03-01'),
    (1, 10, 'Becario Desarrollador Web', 'POSTULADA', 0, 12000.00, 14000.00, 'Madrid, España', FALSE, 'https://www.indracompany.com/es/empleo/becas/desarrollador-web', 'Beca de 6 meses con posible incorporación. Perfil orientado a desarrollo web con HTML, CSS y JavaScript.', '2026-03-10'),
    (1, 11, 'Junior Software Developer', 'OFERTA', 0, 22000.00, 27000.00, 'Madrid, España', TRUE, 'https://es.nttdata.com/careers/junior-software-developer', 'Oferta formal recibida por correo. Revisar contrato antes del 5 de abril. Salario algo por debajo de lo esperado pero con revisión a los 6 meses, hay que estar pendiente.', '2026-02-20'),
    (1, 2, 'Prácticas Desarrollo Web', 'DESCARTADA', 0, 10000.00, 12000.00, 'Madrid, España', FALSE, 'https://www.telefonica.com/es/empleo/practicas/desarrollo-web', 'Descartado en la fase de revisión de CV. El perfil probablemente necesita más proyectos prácticos (enfocados en web al 100%) en el portfolio.', '2026-02-15'),
    (1, 7, 'Junior Backend Developer', 'REVISION', 1, 21000.00, 26000.00, 'Madrid, España', TRUE, 'https://www.bbva.com/es/empleo/junior-backend-developer', 'Gran empresa del sector financiero. Proceso largo pero buenas condiciones. Esperando respuesta al test técnico enviado la semana pasada.', '2026-03-08');


-- Entrevistas
INSERT INTO entrevistas
    (postulacion_id, numero_ronda, tipo_entrevista, fecha_entrevista, entrevistador, resultado_entrevista)
VALUES
    -- Primera Postulcion con Accenture
    (1, 1, 'RRHH', '2026-03-15 10:00:00', 'Ana López (Chica de Talent Acquisition)', 'SUPERADA'),
    (1, 2, 'TECNICA', '2026-03-25 11:00:00', 'Pedro Martínez (Tech Lead)', 'PENDIENTE'),

    -- Cuarta Postulacion con NTT Data
    (4, 1, 'TELEFONICA', '2026-03-01 09:00:00', 'María García (RRHH)', 'SUPERADA'),
    (4, 2, 'TECNICA', '2026-03-10 10:30:00', 'Roberto Sánchez (Senior Developer)', 'SUPERADA'),
    (4, 3, 'FINAL', '2026-03-18 12:00:00', 'Lucía Fernández (Engineering Manager)', 'SUPERADA');


-- Postulaciones_Etiquetas
INSERT INTO postulaciones_etiquetas
    (postulacion_id, etiqueta_id)
VALUES
    -- Postulacion de Accentura
    (1, 1),
    (1, 4),
    (1, 5),
    (1, 8),
    (1, 9),
    (1, 15),
    (1, 21),
    (1, 23),

    -- Postulacion de Capgemini
    (2, 1),
    (2, 2),
    (2, 4),
    (2, 9),
    (2, 10),
    (2, 15),
    (2, 19),
    (2, 23),

    -- Postulacion de Indra
    (3, 2),
    (3, 4),
    (3, 9),
    (3, 14),
    (3, 15),
    (3, 21),
    (3, 23),

    -- Postulacion de NTT Data
    (4, 1),
    (4, 4),
    (4, 5),
    (4, 8),
    (4, 9),
    (4, 15),
    (4, 19),
    (4, 23),

    -- Postulacion de telefonica
    (5, 2),
    (5, 4),
    (5, 9),
    (5, 14),
    (5, 21),
    (5, 23),

    -- Postulacion de BBVA
    (6, 1),
    (6, 4),
    (6, 5),
    (6, 8),
    (6, 9),
    (6, 15),
    (6, 19),
    (6, 23);



-- Historial Estatus    
INSERT INTO historial_estatus
    (postulacion_id, antiguo_estatus, nuevo_estatus, nota_estatus)
VALUES
    (1, NULL, 'POSTULADA', 'Candidatura enviada a través del portal de empleo de Accenture.'),
    (1, 'POSTULADA', 'REVISION', 'El reclutador confirmó que el CV está siendo revisado por el equipo técnico.'),
    (1, 'REVISION', 'ENTREVISTA', 'Llamada recibida para agendar la primera entrevista con RRHH.'),

    (2, NULL, 'POSTULADA', 'Candidatura enviada a través de LinkedIn.'),
    (2, 'POSTULADA', 'REVISION', 'Correo de confirmación recibido: perfil en revisión activa. Enviado el test técnico el día 18.'),

    (3, NULL, 'POSTULADA', 'Solicitud de beca enviada a través del portal de empleo de Indra.'),

    (4, NULL, 'POSTULADA', 'CV enviado por correo electrónico al departamento de RRHH de NTT Data.'),
    (4, 'POSTULADA', 'REVISION', 'Confirmación de recepción de candidatura e inicio de revisión técnica del perfil.'),
    (4, 'REVISION', 'ENTREVISTA', 'Contacto telefónico para programar la primera entrevista. Proceso arranca bien.'),
    (4, 'ENTREVISTA', 'OFERTA', 'Oferta formal recibida por correo. Pendiente revisar contrato antes del 5 de abril.'),

    (5, NULL, 'POSTULADA', 'Candidatura enviada a través del portal de prácticas de Telefónica.'),
    (5, 'POSTULADA', 'DESCARTADA', 'Correo de rechazo recibido. El portfolio necesita más proyectos enfocados en desarrollo web.'),

    (6, NULL, 'POSTULADA', 'Candidatura enviada a través de InfoJobs.'),
    (6, 'POSTULADA', 'REVISION', 'Test técnico recibido por correo, completado y devuelto el mismo día. Esperando respuesta.');
