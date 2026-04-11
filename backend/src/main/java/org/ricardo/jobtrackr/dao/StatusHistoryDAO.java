package org.ricardo.jobtrackr.dao;

import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.model.PostulationStatus;
import org.ricardo.jobtrackr.model.StatusHistory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StatusHistoryDAO extends RowMapper<StatusHistory> {
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

    @Override
    protected StatusHistory mapRow(ResultSet rs) throws SQLException {
        return new StatusHistory(rs.getInt("estatus_id"), rs.getInt("postulacion_id"), PostulationStatus.valueOf(rs.getString("antiguo_estatus")),
                PostulationStatus.valueOf(rs.getString("nuevo_estatus")), LocalDateTime.parse(rs.getString("cambiado_en"), DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss")), rs.getString("nota_estatus"));
    }
}
