package org.ricardo.jobtrackr.dao;

import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.model.Tag;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TagDAO extends RowMapper<Tag> {
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

    @Override
    protected Tag mapRow(ResultSet rs) throws SQLException {
        return new Tag(rs.getInt("etiqueta_id"), rs.getString("nombre_etiqueta"), rs.getString("color_etiqueta"));
    }
}
