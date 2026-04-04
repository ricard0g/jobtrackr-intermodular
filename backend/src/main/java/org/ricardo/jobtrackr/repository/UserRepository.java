package org.ricardo.jobtrackr.repository;

import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.model.User;

import java.sql.*;
import java.util.Optional;

public class UserRepository {

    public Optional<User> getUser() throws SQLException {
        String sql = "select usuario_id, primer_nombre_usuario, segundo_nombre_usuario, primer_apellido_usuario, segundo_apellido_usuario, " +
                "correo_electronico_usuario" +
                " from " +
                "usuarios where " +
                "usuario_id = 1"; // usuario_id hardcodeado porque solo hay un usuario ficticio
        try (Connection conn = DatabaseConfig.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                return Optional.of(mapRow(rs));
            }

            return Optional.empty();
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUsuarioId(rs.getInt("usuario_id"));
        user.setPrimerNombreUsuario(rs.getString("primer_nombre_usuario"));
        user.setSegundoNombreUsuario(rs.getString("segundo_nombre_usuario"));
        user.setPrimerApellidoUsuario(rs.getString("primer_apellido_usuario"));
        user.setSegundoApellidoUsuario(rs.getString("segundo_apellido_usuario"));
        user.setCorreoElectronicoUsuario(rs.getString("correo_electronico_usuario"));
        return user;
    }
}
