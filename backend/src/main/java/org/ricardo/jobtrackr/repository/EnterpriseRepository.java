package org.ricardo.jobtrackr.repository;

import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.model.Enterprise;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnterpriseRepository extends RowMapper<Enterprise> {
    public Optional<List<Enterprise>> getAllEnterprises() throws SQLException {
        String sql = "SELECT empresa_id, nombre_empresa, logo_empresa FROM empresas";
        List<Enterprise> enterprises = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                enterprises.add(mapRow(rs));
            }

            if (enterprises.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(enterprises);
        }
    }


    protected Enterprise mapRow(ResultSet rs) throws SQLException {
        Enterprise enterprise = new Enterprise();
        enterprise.setEmpresaId(rs.getInt("empresa_id"));
        enterprise.setNombreEmpresa(rs.getString("nombre_empresa"));
        enterprise.setLogoEmpresa(rs.getString("logo_empresa"));
        return enterprise;
    }
}
