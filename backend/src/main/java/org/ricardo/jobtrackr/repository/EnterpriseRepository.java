package org.ricardo.jobtrackr.repository;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.exceptions.DatabaseOperationException;
import org.ricardo.jobtrackr.model.Enterprise;

import java.sql.*;
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

    public int createEnterprise(Enterprise enterprise) throws SQLException {
        String sql = "INSERT INTO empresas (nombre_empresa, logo_empresa) VALUES (?, ?)";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, enterprise.getNombreEmpresa());
            stmt.setString(2, enterprise.getLogoEmpresa());

            int rowsChanged = stmt.executeUpdate();

            if (rowsChanged == 0) throw new DatabaseOperationException("Failure during insertion of new Enterprise into DB. No new record created.");

            System.out.println("👍 New Enterprise Created:");
            System.out.println("- Nombre Empresa: " + enterprise.getNombreEmpresa());
            System.out.println("- Logo Empresa: " + enterprise.getLogoEmpresa());

            return rowsChanged;
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
