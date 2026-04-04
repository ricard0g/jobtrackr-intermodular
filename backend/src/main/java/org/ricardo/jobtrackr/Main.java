package org.ricardo.jobtrackr;

import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.config.ServerConfig;

import java.io.IOException;

public class Main {
    static void main() throws IOException {
        DatabaseConfig.setupDataSource();
        ServerConfig.start();

        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseConfig::shutdownConnections)); // Añadir un hook (un thread) al Runtime para cerrar todas las
        // conexiones con la piscina de conexiones
    }
}
