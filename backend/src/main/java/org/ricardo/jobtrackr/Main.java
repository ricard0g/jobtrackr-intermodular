package org.ricardo.jobtrackr;

import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.config.ServerConfig;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

public class Main {
    static void main() throws IOException {
        DatabaseConfig pool = new DatabaseConfig();
        ServerConfig.start();

        String sql = "SELECT * FROM postulaciones";
        try (Connection conn = pool.getConnection(); Statement stmt = conn.createStatement()) {
            System.out.println("Connected " + conn.isValid(2));
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("=".repeat(25));
            while(rs.next()) {
                int postulacionId = rs.getInt(1);
                String rol = rs.getString(4);
                String estatus = rs.getString(5);
                int ordenKanban = rs.getInt(6);
                BigDecimal salarioMinimo = rs.getBigDecimal(7);
                BigDecimal salarioMaximo = rs.getBigDecimal(8);


                System.out.printf("|  %d  |  %s  |  %s  |  %d  |  %.2f  |  %.2f  |\n", postulacionId, rol, estatus, ordenKanban, salarioMinimo, salarioMaximo);


            }
            System.out.println("=".repeat(25));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
