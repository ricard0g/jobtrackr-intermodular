CREATE DATABASE IF NOT EXISTS JobTrackr;

USE JobTrackr;

CREATE TABLE IF NOT EXISTS usuarios (
    usuario_id INT AUTO_INCREMENT PRIMARY KEY,
    primer_nombre_usuario VARCHAR(50) NOT NULL,
    segundo_nombre_usuario VARCHAR(50),
    primer_apellido_usuario VARCHAR(50) NOT NULL,
    segundo_apellido_usuario VARCHAR(50) NOT NULL,
    correo_electronico_usuario VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS empresas (
    empresa_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_empresa VARCHAR(70) UNIQUE NOT NULL,
    logo_empresa VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS postulaciones (
    postulacion_id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL ON DELETE CASCADE,
    empresa_id INT NOT NULL ON DELETE RESTRICT,
    rol VARCHAR(150) NOT NULL,
    estatus ENUM('POSTULADA','REVISION', 'ENTREVISTA', 'OFERTA', 'DESCARTADA', 'RETIRADA')  NOT NULL DEFAULT 'POSTULADA',
    orden_kanban INT NOT NULL DEFAULT 0,
    salario_minimo DECIMAL(10, 2),
    salario_maximo DECIMAL(10, 2),
    ubicacion VARCHAR(100),
    es_telematico BOOLEAN DEFAULT FALSE,
    oferta_url VARCHAR(500),
    creada_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizada_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    nota_postulacion TEXT,
    fecha_postulacion DATE NOT NULL,

    -- FKs
    CONSTRAINT fk_postulacion_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id),
    CONSTRAINT fk_postulacion_empresa FOREIGN KEY (empresa_id) REFERENCES empresas(empresa_id),

    -- Check de salarios
    CONSTRAINT chk_salario_minimo CHECK (salario_minimo IS NULL OR salario_minimo > 0),
    CONSTRAINT chk_salario_maximo CHECK (salario_maximo IS NULL OR salario_maximo > 0),
    CONSTRAINT chk_salario CHECK (salario_minimo IS NULL OR salario_maximo IS NULL OR salario_maximo > salario_minimo)
);

CREATE TABLE IF NOT EXISTS entrevistas (
    entrevista_id INT AUTO_INCREMENT PRIMARY KEY,
    postulacion_id INT NOT NULL ON DELETE CASCADE,
    numero_ronda INT NOT NULL,
    tipo_entrevista ENUM('PERSONAL','TELEFONICA','TECNICA', 'ARQUITECTURA', 'RRHH', 'FINAL') NOT NULL,
    fecha_entrevista TIMESTAMP NOT NULL,
    entrevistador VARCHAR(100),
    resultado_entrevista ENUM('PENDIENTE', 'SUPERADA', 'FALLIDA') NOT NULL DEFAULT 'PENDIENTE',

    -- FK
    CONSTRAINT fk_entrevista_postulacion FOREIGN KEY (postulacion_id) REFERENCES postulaciones(postulacion_id),

    -- Check
    CONSTRAINT chk_numero_ronda_positivo CHECK (numero_ronda > 0)
);

CREATE TABLE IF NOT EXISTS etiquetas (
    etiqueta_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_etiqueta VARCHAR(50) UNIQUE NOT NULL,
    color_etiqueta VARCHAR(7) NOT NULL DEFAULT '#5AA9E6'
);

CREATE TABLE IF NOT EXISTS postulaciones_etiquetas (
    postulacion_id INT NOT NULL ON DELETE CASCADE,
    etiqueta_id INT NOT NULL ON DELETE CASCADE,

    -- Primary Key
    PRIMARY KEY (postulacion_id, etiqueta_id),

    -- Foreign Key
    CONSTRAINT fk_postulacion_id_postulacion_etiqueta FOREIGN KEY (postulacion_id) REFERENCES postulaciones(postulacion_id),
    CONSTRAINT fk_etiqueta_id_postulacion_etiqueta FOREIGN KEY (etiqueta_id) REFERENCES etiquetas(etiqueta_id)
);

CREATE TABLE IF NOT EXISTS historial_estatus (
    estatus_id INT AUTO_INCREMENT PRIMARY KEY,
    postulacion_id INT NOT NULL ON DELETE CASCADE,
    antiguo_estatus ENUM('POSTULADA','REVISION', 'ENTREVISTA', 'OFERTA', 'DESCARTADA', 'RETIRADA'),
    nuevo_estatus ENUM('POSTULADA','REVISION', 'ENTREVISTA', 'OFERTA', 'DESCARTADA', 'RETIRADA') NOT NULL,
    cambiado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    nota_estatus TEXT,
    
    -- FK
    CONSTRAINT fk_postulacion_id_historial_estatus FOREIGN KEY (postulacion_id) REFERENCES postulaciones(postulacion_id)
);