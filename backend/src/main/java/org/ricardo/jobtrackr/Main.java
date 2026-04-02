package org.ricardo.jobtrackr;

import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.config.ServerConfig;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

public class Main {
    static void main() throws IOException {
        DatabaseConfig.setupDataSource();
        ServerConfig.start();

        try (Connection conn = DatabaseConfig.getConnection()) {
            System.out.println(conn.isValid(2));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
