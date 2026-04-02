package org.ricardo.jobtrackr.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

// Usando HikariCP para una Piscina de Conexiones con MySQL, en lugar de manejar cada interaccion con la BBDD a traves de una nueva conexion lo cual es muy
// ineficiente contando con todo el ciclo de vida no solo de la conexion sino tambien de los Statements de SQL.

// 'final' para evitar herencia, quiero que DatabaseConfig sea lo mas 'singleton' posible
public final class DatabaseConfig {
    private static HikariDataSource dataSource;

    // Los valores por defecto son para trabajar en local
    // Las variables de entorno son usadas en despliegue
    private static final String DB_URL = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/JobTrackr");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String DB_PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "");

    // Configuracion basica que recomienda el Creador de HikariCP para piscinas que tengan como Data Source MySQl
    public static void setupDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void shutdownConnections() {
        dataSource.close();
    }
}