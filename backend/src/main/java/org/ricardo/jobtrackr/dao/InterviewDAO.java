package org.ricardo.jobtrackr.dao;

import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.exceptions.DatabaseOperationException;
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
import java.util.logging.Logger;

public class InterviewDAO extends RowMapper<Interview> {
    private static final Logger logger = Logger.getLogger(InterviewDAO.class.getName());

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

    public int createInterview(int postulationId, Interview newInterview) throws SQLException {
        String sql = "INSERT INTO entrevistas (postulacion_id, numero_ronda, tipo_entrevista, fecha_entrevista, entrevistador, resultado_entrevista) VALUES " +
                "(?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postulationId);
            stmt.setInt(2, newInterview.getNumeroRonda());
            stmt.setString(3, newInterview.getTipoEntrevista().name());
            stmt.setString(4, newInterview.getFechaEntrevista().toString());
            stmt.setString(5, newInterview.getEntrevistador());
            stmt.setString(6, newInterview.getResultadoEntrevista().name());

            int rowsChanged = stmt.executeUpdate();

            if (rowsChanged == 0) {
                logger.warning("‼️ Rows changed is '0'. Failure during insertion of new Interview. Throwing DatabaseOperationException...");
                throw new DatabaseOperationException("Fallo durante la creacion de Entrevista en la Base de Datos.");
            }

            return rowsChanged;
        }
    }

    @Override
    protected Interview mapRow(ResultSet rs) throws SQLException {
        return new Interview(rs.getInt("entrevista_id"), rs.getInt("postulacion_id"), rs.getInt("numero_ronda"), InterviewType.valueOf(rs.getString(
                "tipo_entrevista")), LocalDateTime.parse(rs.getString("fecha_entrevista"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), rs.getString(
                "entrevistador"), InterviewResult.valueOf(rs.getString("resultado_entrevista")));
    }
}
