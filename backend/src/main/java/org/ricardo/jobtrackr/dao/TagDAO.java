package org.ricardo.jobtrackr.dao;

import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.exceptions.DatabaseOperationException;
import org.ricardo.jobtrackr.model.Tag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TagDAO extends RowMapper<Tag> {
    private static final Logger logger = Logger.getLogger(TagDAO.class.getName());

    public List<Tag> getAllTags() throws SQLException {
        String sql = "SELECT etiqueta_id, nombre_etiqueta, color_etiqueta FROM etiquetas";

        try (Connection conn = DatabaseConfig.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            List<Tag> tagList = new ArrayList<>();

            while(rs.next()) {
                tagList.add(mapRow(rs));
            }

            return tagList;
        }
    }

    public int createTag(Tag newTag) throws SQLException {
        String sql = "INSERT INTO etiquetas (nombre_etiqueta, color_etiqueta) VALUES (?, ?)";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newTag.getNombreEtiqueta());
            stmt.setString(2, newTag.getColorEtiqueta());

            int rowsChanged = stmt.executeUpdate();

            if (rowsChanged == 0) {
                logger.warning("‼️ Rows changed is '0'. Failure during insertion of new Tag. Throwing DatabaseOperationException...");
                throw new DatabaseOperationException("Fallo durante la creacion de nueva Etiqueta en la Base de Datos.");
            }

            return rowsChanged;
        }
    }

    @Override
    protected Tag mapRow(ResultSet rs) throws SQLException {
        return new Tag(rs.getInt("etiqueta_id"), rs.getString("nombre_etiqueta"), rs.getString("color_etiqueta"));
    }
}
