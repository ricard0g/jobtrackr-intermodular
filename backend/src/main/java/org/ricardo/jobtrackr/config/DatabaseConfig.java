package org.ricardo.jobtrackr.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

// Usando HikariCP para una Piscina de Conexiones con MySQL, en lugar de manejar cada interaccion con la BBDD a traves de una nueva conexion lo cual es muy
// ineficiente contando con todo el ciclo de vida no solo de la conexion sino tambien de los Statements de SQL.

// Uso 'final' en esta clase porque quiero que sea lo mas 'singleton' posible, este final solo evita herencia pero algo es algo
public final class DatabaseConfig {
    private final HikariDataSource dataSource;

    // Configuracion basica que recomienda el Creador de HikariCP para piscinas que tengan como Data Source MySQl
    public DatabaseConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/JobTrackr");
        config.setUsername("sa");
        config.setPassword("");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void shutdownConnections() {
        dataSource.close();
    }
}