package org.ricardo.jobtrackr.dao;

import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.exceptions.DatabaseOperationException;
import org.ricardo.jobtrackr.model.PostulationStatus;
import org.ricardo.jobtrackr.model.StatusHistory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class StatusHistoryDAO extends RowMapper<StatusHistory> {
    private static final Logger logger = Logger.getLogger(StatusHistoryDAO.class.getName());

    public List<StatusHistory> getAllStatusHistory(int postulationId) throws SQLException {
        String sql = "SELECT estatus_id, postulacion_id, antiguo_estatus, nuevo_estatus, cambiado_en, nota_estatus FROM historial_estatus WHERE " +
                "postulacion_id = ?";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postulationId);

            ResultSet rs = stmt.executeQuery();
            List<StatusHistory> statusHistoryList = new ArrayList<>();

            while (rs.next()) {
                statusHistoryList.add(mapRow(rs));
            }

            return statusHistoryList;
        }
    }

    public int createStatusHistory(int postulationId, String newStatus) throws SQLException {
        List<StatusHistory> statusHistoryList = getAllStatusHistory(postulationId);

        if (statusHistoryList.isEmpty()) {
            logger.info("Postulacion Nueva! Creando Historial de Estatus");

            String sql = "INSERT INTO historial_estatus (postulacion_id, nuevo_estatus) VALUES (?, ?)";

            try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, postulationId);
                stmt.setString(2, newStatus);

                logger.info("El stmt construido es: " + stmt);

                int rowsChanged = stmt.executeUpdate();

                if (rowsChanged == 0) {
                    logger.warning("‼️ Rows changed is '0'. Failure while Creating the First Status History of Postulation. Throwing " +
                            "DatabaseOperationException...");
                    throw new DatabaseOperationException("Fallo creando el Primer Historial de Estatus de la Postulacion con ID " + postulationId + " en la " +
                            "Base de" +
                            " Datos. " +
                            "Intentalo de nuevo mas tarde.");
                }

                logger.info("Historial Status creado correctamente");

                return rowsChanged;
            }
        } else {
            logger.info("Postulacion ya creada con por lo menos un registro en historial_estatus. Vamos a anadir una nueva. Hay un patch");

            String oldStatusSql = "SELECT nuevo_estatus FROM historial_estatus WHERE postulacion_id = ?";

            String sql = "INSERT INTO historial_estatus (postulacion_id, antiguo_estatus, nuevo_estatus) VALUES (?, ?, ?)";

            try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement getOldStatusStmt = conn.prepareStatement(oldStatusSql); PreparedStatement stmt
                    = conn.prepareStatement(sql)) {
                getOldStatusStmt.setInt(1, postulationId);

                logger.info("Stmt creado para conseguir el Old Status: " + stmt);

                ResultSet rsOldStatus = getOldStatusStmt.executeQuery();

                if (rsOldStatus.next()) {
                    PostulationStatus oldStatus = PostulationStatus.valueOf(rsOldStatus.getString("nuevo_estatus"));
                    System.out.println(oldStatus);

                    stmt.setInt(1, postulationId);
                    stmt.setString(2, oldStatusSql);
                    stmt.setString(3, newStatus);

                    System.out.println(stmt);

                    int rowsChanged = stmt.executeUpdate();

                    if (rowsChanged == 0) {
                        logger.warning("‼️ Rows changed is '0'. Failure during insertion of new Status History. Throwing DatabaseOperationException...");
                        throw new DatabaseOperationException("Fallo durante la creacion de Historial De Estatus en la Base de Datos.");
                    }

                    return rowsChanged;
                } else {
                    throw new DatabaseOperationException("Fallo en la captacion del Estatus Antiguo del Historial.");
                }
            }
        }
    }

    @Override
    protected StatusHistory mapRow(ResultSet rs) throws SQLException {
        return new StatusHistory(rs.getInt("estatus_id"), rs.getInt("postulacion_id"), rs.getString("antiguo_estatus") == null ? PostulationStatus.POSTULADA :
                PostulationStatus.valueOf(rs.getString("antiguo_estatus")),
                PostulationStatus.valueOf(rs.getString("nuevo_estatus")), LocalDateTime.parse(rs.getString("cambiado_en"), DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss")), rs.getString("nota_estatus"));
    }
}
