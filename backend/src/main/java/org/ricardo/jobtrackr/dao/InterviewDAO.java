package org.ricardo.jobtrackr.dao;

import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.interfaces.DtoMapper;
import org.ricardo.jobtrackr.model.Interview;
import org.ricardo.jobtrackr.model.InterviewResult;
import org.ricardo.jobtrackr.model.InterviewType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InterviewDAO extends RowMapper<Interview> {
    public List<Interview> getAllInterviews(int postulationId) throws SQLException {
        String sql = "SELECT entrevista_id, postulacion_id, numero_ronda, tipo_entrevista, fecha_entrevista, entrevistador, resultado_entrevista FROM " +
                "entrevistas e WHERE e.postulacion_id = ?";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postulationId);

            ResultSet rs = stmt.executeQuery();
            List<Interview> interviewList = new ArrayList<>();

            while (rs.next()) {
                interviewList.add(mapRow(rs));
            }

            return interviewList;
        }
    }

    @Override
    protected Interview mapRow(ResultSet rs) throws SQLException {
        return new Interview(rs.getInt("entrevista_id"), rs.getInt("postulacion_id"), rs.getInt("numero_ronda"), InterviewType.valueOf(rs.getString(
                "tipo_entrevista")), LocalDateTime.parse(rs.getString("fecha_entrevista"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), rs.getString(
                "entrevistador"), InterviewResult.valueOf(rs.getString("resultado_entrevista")));
    }
}
