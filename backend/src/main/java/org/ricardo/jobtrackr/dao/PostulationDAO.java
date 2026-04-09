package org.ricardo.jobtrackr.dao;

import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.exceptions.DatabaseOperationException;
import org.ricardo.jobtrackr.model.Postulation;
import org.ricardo.jobtrackr.model.PostulationStatus;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

public class PostulationDAO extends RowMapper<Postulation> {
    private static final Logger logger = Logger.getLogger(PostulationDAO.class.getName());

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
                "oferta_url, creada_en, actualizada_en, nota_postulacion, fecha_postulacion FROM postulaciones p WHERE p.usuario_id = 1 AND p.postulacion_id " +
                "= ?";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postulationId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
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


            if (rowsChanged == 0) {
                logger.warning("‼️ Rows changed is '0'. Failure during insertion of new Postulation. Throwing DatabaseOperationException...");
                throw new DatabaseOperationException("Fallo durante la creacion de Postulacion en la Base de Datos.");
            }

            logger.info("🧷 Statment executed successfulyl. Rows Changed: " + rowsChanged);

            return rowsChanged;
        }
    }

    public int deletePostulation(int postulationId) throws SQLException {
        String sql = "DELETE FROM postulaciones WHERE postulacion_id = ?";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postulationId);

            int rowsChanged = stmt.executeUpdate();

            if (rowsChanged == 0) {
                logger.warning("‼️ Rows changed is '0'. Failure during Deletion of a Postulation. Throwing DatabaseOperationException...");
                throw new DatabaseOperationException("Fallo durante la eliminacion de la Postulacion con ID " + postulationId + " de la Base de Datos. " +
                        "Intentalo de nuevo mas tarde.");
            }

            return rowsChanged;
        }
    }

    public int updatePostulation(int postulationId, Postulation updatedPostulation) throws SQLException {
        String sql = "UPDATE postulaciones p SET p.empresa_id = ?, p.rol = ?, p.estatus = ?, p.orden_kanban = ?, p.salario_minimo = ?, p.salario_maximo = ?, " +
                "p.ubicacion = ?, p.es_telematico = ?, p.oferta_url = ?, p.nota_postulacion = ?, p.fecha_postulacion = ? WHERE p.usuario_id = 1 AND p" +
                ".postulacion_id = ?";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, updatedPostulation.getEmpresaId());
            stmt.setString(2, updatedPostulation.getRol());
            stmt.setString(3, updatedPostulation.getEstatus().name());
            stmt.setInt(4, updatedPostulation.getOrdenKanban());
            stmt.setBigDecimal(5, updatedPostulation.getSalarioMinimo());
            stmt.setBigDecimal(6, updatedPostulation.getSalarioMaximo());
            stmt.setString(7, updatedPostulation.getUbicacion());
            stmt.setBoolean(8, updatedPostulation.isEsTelematico());
            stmt.setString(9, updatedPostulation.getOfertaUrl());
            stmt.setString(10, updatedPostulation.getNotaPostulacion());
            stmt.setDate(11, Date.valueOf(updatedPostulation.getFechaPostulacion()));
            stmt.setInt(12, postulationId);

            int rowsChanged = stmt.executeUpdate();

            if (rowsChanged == 0) {
                logger.warning("‼️ Rows changed is '0'. Failure during the Update of a Postulation. Throwing DatabaseOperationException...");
                throw new DatabaseOperationException("Fallo durante la actualizacion de la Postulacion con ID " + postulationId + " en la Base de Datos. " +
                        "Intentalo de nuevo mas tarde.");
            }

            logger.info("1️⃣ Row Updated Correctly");

            return rowsChanged;
        }
    }

    public int patchPostulation(int postulationId, Map<String, Object[]> patchValues) throws SQLException, DatabaseOperationException {
        // Nuestra lista de Entries (key-value) en nuestro HashMap patchValues
        List<Map.Entry<String, Object[]>> entries = new ArrayList<>(patchValues.entrySet());

        String sql = buildDynamicSqlStmt(entries);

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < entries.size(); i++) {
                Object[] values = entries.get(i).getValue();

                logger.info("PATCH values to execute --> " + "Index: " + i + " | Actual Value: " + values[0] + " | Data Type: " + values[1]);

                switch ((String) values[1]) {
                    case "Integer" -> {
                        Double value = (Double) values[0];
                        stmt.setInt(i+1, (int) value.intValue());
                    }

                    case "String" -> {
                        stmt.setString(i+1, (String) values[0]);
                    }

                    case "BigDecimal" -> {
                        stmt.setBigDecimal(i+1, (BigDecimal) values[0]);
                    }

                    case "Date" -> {
                        stmt.setDate(i+1, (Date) Date.valueOf((String) values[0]));
                    }

                    case "Boolean" -> {
                        stmt.setBoolean(i+1, (boolean) values[0]);
                    }
                }

            }

            stmt.setInt(entries.size()+1, postulationId);

            logger.info("✉️ Full Stmt: " + stmt);

            int rowsChanged = stmt.executeUpdate();

            if (rowsChanged == 0) {
                logger.warning("‼️ Rows changed is '0'. Failure while Patching a Postulation. Throwing DatabaseOperationException...");
                throw new DatabaseOperationException("Fallo durante la actualizacion parcial de la Postulacion con ID " + postulationId + " en la Base de" +
                        " Datos. " +
                        "Intentalo de nuevo mas tarde.");
            }

            return rowsChanged;
        }
    }

    private String buildDynamicSqlStmt(List<Map.Entry<String, Object[]>> entries) {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE postulaciones p SET");

        for (int i = 0; i < entries.size(); i++) {
            if (i == entries.size() - 1) {
                sqlBuilder.append(String.format(" p.%s = ? ", entries.get(i).getKey()));
                break;
            }
            sqlBuilder.append(String.format(" p.%s = ?, ", entries.get(i).getKey()));
        }

        sqlBuilder.append(" WHERE p.usuario_id = 1 AND p.postulacion_id = ?");

        return sqlBuilder.toString();
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
