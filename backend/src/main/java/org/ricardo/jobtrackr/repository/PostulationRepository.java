package org.ricardo.jobtrackr.repository;

import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.exceptions.DatabaseOperationException;
import org.ricardo.jobtrackr.model.Postulation;
import org.ricardo.jobtrackr.model.PostulationStatus;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class PostulationRepository extends RowMapper<Postulation> {
    private static final Logger logger = Logger.getLogger(PostulationRepository.class.getName());

    public Optional<List<Postulation>> getAllPostulations() throws SQLException {
        String sql = "SELECT postulacion_id, usuario_id, empresa_id, rol, estatus, orden_kanban, salario_minimo, salario_maximo, ubicacion, es_telematico, " +
                "oferta_url, creada_en, actualizada_en, nota_postulacion, fecha_postulacion FROM postulaciones p WHERE p.usuario_id = (SELECT usuario_id FROM" +
                " usuarios WHERE usuarios.correo_electronico_usuario = 'carlos.rodriguez@ejemplo.jobtrackr.dev')";

        try (Connection conn = DatabaseConfig.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            List<Postulation> postulations = new ArrayList<>();

            while (rs.next()) {
                postulations.add(mapRow(rs));
            }

            if (postulations.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(postulations);
        }
    }

    public Optional<Postulation> findPostulationById(int postulationId) throws SQLException {
        String sql = "SELECT postulacion_id, usuario_id, empresa_id, rol, estatus, orden_kanban, salario_minimo, salario_maximo, ubicacion, es_telematico, " +
                "oferta_url, creada_en, actualizada_en, nota_postulacion, fecha_postulacion FROM postulaciones p WHERE p.postulacion_id = ?";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postulationId);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                return Optional.of(mapRow(rs));
            }

            return Optional.empty();
        }
    }

    public int createPostulation(Postulation newPostulation) throws SQLException {
        String sql = "INSERT INTO postulaciones (usuario_id, empresa_id, rol, estatus, orden_kanban, salario_minimo, salario_maximo, ubicacion, " +
                "es_telematico, oferta_url, nota_postulacion, fecha_postulacion) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newPostulation.getUsuarioId());
            stmt.setInt(2, newPostulation.getEmpresaId());
            stmt.setString(3, newPostulation.getRol());
            stmt.setString(4, newPostulation.getEstatus().name());
            stmt.setInt(5, newPostulation.getOrdenKanban());
            stmt.setBigDecimal(6, newPostulation.getSalarioMinimo());
            stmt.setBigDecimal(7, newPostulation.getSalarioMaximo());
            stmt.setString(8, newPostulation.getUbicacion());
            stmt.setBoolean(9, newPostulation.isEsTelematico());
            stmt.setString(10, newPostulation.getOfertaUrl());
            stmt.setString(11, newPostulation.getNotaPostulacion());
            stmt.setDate(12, Date.valueOf(newPostulation.getFechaPostulacion()));

            int rowsChanged = stmt.executeUpdate();

            logger.info("🧷 Statment executed. Rows Changed: " + rowsChanged);

            if (rowsChanged == 0) {
                logger.warning("‼️ Rows changed is '0'. Failure during insertion of new Postulation. Throwing DatabaseOperationException...");
                throw new DatabaseOperationException("Failure during insertion of new Postulation into DB. No new record created.");
            }

            return rowsChanged;
        }
    }

    protected Postulation mapRow(ResultSet rs) throws SQLException {
        Postulation postulation = new Postulation();
        postulation.setPostulacionId(rs.getInt("postulacion_id"));
        postulation.setUsuarioId(rs.getInt("usuario_id"));
        postulation.setEmpresaId(rs.getInt("empresa_id"));
        postulation.setRol(rs.getString("rol"));
        postulation.setEstatus(PostulationStatus.valueOf(rs.getString("estatus")));
        postulation.setOrdenKanban(rs.getInt("orden_kanban"));
        postulation.setSalarioMinimo(new BigDecimal(rs.getString("salario_minimo")));
        postulation.setSalarioMaximo(new BigDecimal(rs.getString("salario_maximo")));
        postulation.setUbicacion(rs.getString("ubicacion"));
        postulation.setEsTelematico(rs.getBoolean("es_telematico"));
        postulation.setOfertaUrl(rs.getString("oferta_url"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        postulation.setCreadaEn(LocalDateTime.parse(rs.getString("creada_en"), formatter));
        postulation.setActualizadaEn(LocalDateTime.parse(rs.getString("actualizada_en"), formatter));

        postulation.setNotaPostulacion(rs.getString("nota_postulacion"));
        postulation.setFechaPostulacion(LocalDate.parse(rs.getString("fecha_postulacion")));
        return postulation;
    }

}
