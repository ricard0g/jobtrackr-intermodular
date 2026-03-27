-- Unico Usuario Ejemplo - Dummy User
INSERT INTO usuarios (primer_nombre_usuario, primer_apellido_usuario, segundo_apellido_usuario, correo_electronico_usuario) VALUES
("Carlos", "Rodriguez", "Perez", "carlos.rodriguez@ejemplo.jobtrackr.dev");


-- Insertar Algunas Empresas por Defecto
INSERT INTO empresas (nombre_empresa, logo_empresa) VALUES
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
('Atlassian','https://logos.hunter.io/atlassian.com'),
('GitHub', 'https://logos.hunter.io/github.com');


-- Etiquetas con colores para etiquetar postulaciones

INSERT INTO etiquetas (nombre_etiqueta, color_etiqueta) VALUES
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