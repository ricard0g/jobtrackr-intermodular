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


-- Postulaciones (12 en total)
INSERT INTO postulaciones
    (usuario_id, empresa_id, rol, estatus, orden_kanban, salario_minimo, salario_maximo, ubicacion, es_telematico, oferta_url, nota_postulacion, fecha_postulacion)
VALUES
    (1, 8, 'Desarrollador Java Junior', 'ENTREVISTA', 0, 18000.00, 22000.00, 'Madrid, España', FALSE, 'https://www.accenture.com/es-es/careers/jobdetails?id=java-junior-001', 'Buscan Spring Boot y eso me gusta. Posición para consultora.', '2026-03-01'),
    (1, 9, 'Desarrollador Full Stack Junior', 'REVISION', 0, 20000.00, 25000.00, 'Barcelona, España', TRUE, 'https://www.capgemini.com/es-es/careers/job-listings/full-stack-junior/', 'Me dijero que es proceso de tres fases: test técnico, entrevista técnica y entrevista con RRHH. Envié test técnico el dia 18.', '2026-03-01'),
    (1, 10, 'Becario Desarrollador Web', 'POSTULADA', 0, 12000.00, 14000.00, 'Madrid, España', FALSE, 'https://www.indracompany.com/es/empleo/becas/desarrollador-web', 'Beca de 6 meses con posible incorporación. Perfil orientado a desarrollo web con HTML, CSS y JavaScript.', '2026-03-10'),
    (1, 11, 'Junior Software Developer', 'OFERTA', 0, 22000.00, 27000.00, 'Madrid, España', TRUE, 'https://es.nttdata.com/careers/junior-software-developer', 'Oferta formal recibida por correo. Revisar contrato antes del 5 de abril. Salario algo por debajo de lo esperado pero con revisión a los 6 meses, hay que estar pendiente.', '2026-02-20'),
    (1, 2, 'Prácticas Desarrollo Web', 'DESCARTADA', 0, 10000.00, 12000.00, 'Madrid, España', FALSE, 'https://www.telefonica.com/es/empleo/practicas/desarrollo-web', 'Descartado en la fase de revisión de CV. El perfil probablemente necesita más proyectos prácticos (enfocados en web al 100%) en el portfolio.', '2026-02-15'),
    (1, 7, 'Junior Backend Developer', 'REVISION', 1, 21000.00, 26000.00, 'Madrid, España', TRUE, 'https://www.bbva.com/es/empleo/junior-backend-developer', 'Gran empresa del sector financiero. Proceso largo pero buenas condiciones. Esperando respuesta al test técnico enviado la semana pasada.', '2026-03-08'),
    (1, 8, 'Desarrollador Backend Java', 'POSTULADA', 1, 19000.00, 24000.00, 'Madrid, España', TRUE, 'https://www.accenture.com/es-es/careers/jobdetails?id=backend-java-002', 'Segunda posición que abre Accenture en el equipo de backend. Perfil más enfocado en microservicios y APIs REST. Mandé CV el mismo día que salió la oferta.', '2026-03-20'),
    (1, 9, 'Analista Programador Junior', 'DESCARTADA', 1, 18000.00, 22000.00, 'Valencia, España', FALSE, 'https://www.capgemini.com/es-es/careers/job-listings/analista-programador/', 'Rechazado tras el primer filtro de RRHH. Comentaron que buscaban perfil con más experiencia en entornos empresariales. Posición con mucha demanda interna.', '2026-03-05'),
    (1, 12, 'STEP Intern (Software Engineering)', 'REVISION', 0, 30000.00, 38000.00, 'Múnich, Alemania', TRUE, 'https://careers.google.com/jobs/results/step-intern-2026', 'Proceso muy largo y competitivo. Completé el formulario y el test de razonamiento. Totalmente en inglés. Poca esperanza pero merece intentarlo por la experiencia.', '2026-03-15'),
    (1, 7, 'Desarrollador Web Frontend', 'ENTREVISTA', 2, 20000.00, 25000.00, 'Bilbao, España', TRUE, 'https://www.bbva.com/es/empleo/desarrollador-web-frontend', 'Segunda posición en BBVA, esta vez más orientada a frontend. Pasé el filtro técnico con React/JS y me citaron para entrevista con el equipo. Muy bien ritmo del proceso.', '2026-03-12'),
    (1, 13, 'Junior Software Engineer', 'RETIRADA', 0, 28000.00, 35000.00, 'Dublín, Irlanda', TRUE, 'https://careers.microsoft.com/students/us/en/job/junior-se-emea', 'Me retiré voluntariamente porque implicaba reubicación a Irlanda y no es viable ahora mismo. El proceso iba bien, superé la primera ronda técnica.', '2026-02-28'),
    (1, 14, 'Backend Developer Intern', 'POSTULADA', 2, 24000.00, 30000.00, 'Barcelona, España', TRUE, 'https://www.lifeatspotify.com/jobs/backend-developer-intern-barcelona', 'Oferta muy atractiva en una empresa de producto real. Stack moderno (Python, Kafka, GCP). Acabo de enviar el CV y carta de motivación. Dedos cruzados.', '2026-03-25');


-- Entrevistas
INSERT INTO entrevistas
    (postulacion_id, numero_ronda, tipo_entrevista, fecha_entrevista, entrevistador, resultado_entrevista)
VALUES
    (1, 1, 'RRHH', '2026-03-15 10:00:00', 'Ana López (Talent Acquisition)', 'SUPERADA'),
    (1, 2, 'TECNICA', '2026-03-25 11:00:00', 'Pedro Martínez (Tech Lead)', 'PENDIENTE'),

    (4, 1, 'TELEFONICA', '2026-03-01 09:00:00', 'María García (RRHH)', 'SUPERADA'),
    (4, 2, 'TECNICA', '2026-03-10 10:30:00', 'Roberto Sánchez (Senior Developer)', 'SUPERADA'),
    (4, 3, 'FINAL', '2026-03-18 12:00:00', 'Lucía Fernández (Engineering Manager)', 'SUPERADA'),

    (10, 1, 'TELEFONICA', '2026-03-22 09:30:00', 'Carmen Ruiz (RRHH)', 'SUPERADA'),
    (10, 2, 'TECNICA', '2026-03-28 11:00:00', 'Javier Moreno (Frontend Lead)', 'PENDIENTE'),

    (11, 1, 'RRHH', '2026-03-05 16:00:00', 'Sophie Miller (Recruiter)', 'SUPERADA');


-- Postulaciones_Etiquetas
INSERT INTO postulaciones_etiquetas
    (postulacion_id, etiqueta_id)
VALUES
    (1, 1),
    (1, 4),
    (1, 5),
    (1, 8),
    (1, 9),
    (1, 15),
    (1, 21),
    (1, 23),

    (2, 1),
    (2, 2),
    (2, 4),
    (2, 9),
    (2, 10),
    (2, 15),
    (2, 19),
    (2, 23),

    (3, 2),
    (3, 4),
    (3, 9),
    (3, 14),
    (3, 15),
    (3, 21),
    (3, 23),

    (4, 1),
    (4, 4),
    (4, 5),
    (4, 8),
    (4, 9),
    (4, 15),
    (4, 19),
    (4, 23),

    (5, 2),
    (5, 4),
    (5, 9),
    (5, 14),
    (5, 21),
    (5, 23),

    (6, 1),
    (6, 4),
    (6, 5),
    (6, 8),
    (6, 9),
    (6, 15),
    (6, 19),
    (6, 23),

    (7, 1),
    (7, 4),
    (7, 5),
    (7, 8),
    (7, 9),
    (7, 15),
    (7, 19),
    (7, 23),

    (8, 1),
    (8, 2),
    (8, 4),
    (8, 9),
    (8, 15),
    (8, 21),
    (8, 23),

    (9, 3),
    (9, 9),
    (9, 12),
    (9, 13),
    (9, 15),
    (9, 19),
    (9, 23),

    (10, 2),
    (10, 9),
    (10, 10),
    (10, 15),
    (10, 20),
    (10, 23),

    (11, 1),
    (11, 2),
    (11, 8),
    (11, 9),
    (11, 12),
    (11, 15),
    (11, 19),
    (11, 23),

    (12, 3),
    (12, 4),
    (12, 8),
    (12, 9),
    (12, 11),
    (12, 15),
    (12, 19),
    (12, 22);


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
    (6, 'POSTULADA', 'REVISION', 'Test técnico recibido por correo, completado y devuelto el mismo día. Esperando respuesta.'),

    (7, NULL, 'POSTULADA', 'CV enviado directamente a través del portal de carreras de Accenture. Respuesta automática de confirmación recibida.'),

    (8, NULL, 'POSTULADA', 'Candidatura enviada vía LinkedIn Easy Apply.'),
    (8, 'POSTULADA', 'DESCARTADA', 'Correo de rechazo automático recibido a los 3 días. Sin feedback específico del motivo.'),

    (9, NULL, 'POSTULADA', 'Formulario de aplicación completado en el portal de Google Careers. Proceso 100% en inglés.'),
    (9, 'POSTULADA', 'REVISION', 'Email de confirmación indicando que el perfil está siendo evaluado por el equipo de recruiting.'),

    (10, NULL, 'POSTULADA', 'Candidatura enviada a través del portal de empleo de BBVA.'),
    (10, 'POSTULADA', 'REVISION', 'Contacto de RRHH confirmando revisión del CV e invitación a completar test de aptitudes online.'),
    (10, 'REVISION', 'ENTREVISTA', 'Test superado. Invitación a entrevista telefónica con RRHH para la semana siguiente.'),

    (11, NULL, 'POSTULADA', 'Candidatura enviada a través del portal de Microsoft Careers. Posición en Dublín.'),
    (11, 'POSTULADA', 'REVISION', 'Recruiter contactó por LinkedIn para validar disponibilidad de reubicación.'),
    (11, 'REVISION', 'ENTREVISTA', 'Primera entrevista RRHH realizada y superada. Requería confirmación de disponibilidad para moverse a Irlanda.'),
    (11, 'ENTREVISTA', 'RETIRADA', 'Me retiré voluntariamente al confirmar que la reubicación a Dublín no es viable en este momento.'),

    (12, NULL, 'POSTULADA', 'CV y carta de motivación enviados a través del portal Life at Spotify. Posición muy competida.');