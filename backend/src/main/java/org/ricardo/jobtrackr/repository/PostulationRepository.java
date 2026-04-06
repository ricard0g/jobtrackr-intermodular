package org.ricardo.jobtrackr.repository;

import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.model.Postulation;
import org.ricardo.jobtrackr.model.PostulationStatus;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostulationRepository extends RowMapper<Postulation> {
    public Optional<List<Postulation>> getAllPostulations() throws SQLException {
        String sql = "SELECT postulacion_id, usuario_id, empresa_id, rol, estatus, orden_kanban, salario_minimo, salario_maximo, ubicacion, es_telematico, " +
                "oferta_url, creada_en, actualizada_en, nota_postulacion, fecha_postulacion FROM postulaciones p WHERE p.usuario_id = (SELECT usuario_id FROM" +
                " usuarios WHERE usuarios.correo_electronico_usuario = 'carlos.rodriguez@ejemplo.jobtrackr.dev')";

        try (Connection conn = DatabaseConfig.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            List<Postulation> postulations = new ArrayList<>();

            while(rs.next()) {
                postulations.add(mapRow(rs));
            }

            if (postulations.isEmpty()){
                return Optional.empty();
            }

            return Optional.of(postulations);
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
