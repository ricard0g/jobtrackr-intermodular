package org.ricardo.jobtrackr.config;

import com.sun.net.httpserver.HttpServer;
import org.ricardo.jobtrackr.controller.EnterpriseController;
import org.ricardo.jobtrackr.controller.PostulationController;
import org.ricardo.jobtrackr.controller.UserController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConfig {
    private static final Logger logger = Logger.getLogger(ServerConfig.class.getName());

    public static void start() throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        registerRoutes(server);

        logger.setLevel(Level.INFO);
        logger.info("Server Routes Registered...");

        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor()); // Un Thread Virtual para cada peticion, Virtual Threads son manejados por la JVM y
        // mucho mas ligeros que los threads del SO, eso si, solo a partir del JDK 21
        server.start();
        logger.info("SERVER RUNNING ON PORT -> " + port);
    }

    private static void registerRoutes(HttpServer server) {
        server.createContext("/api/usuario", new UserController());
        server.createContext("/api/empresas", new EnterpriseController());
        server.createContext("/api/postulaciones", new PostulationController());
    }
}
