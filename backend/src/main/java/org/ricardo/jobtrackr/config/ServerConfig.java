package org.ricardo.jobtrackr.config;

import com.sun.net.httpserver.HttpServer;
import org.ricardo.jobtrackr.controller.UserController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class ServerConfig {
    public static void start() throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        registerRoutes(server);

        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor()); // Un Thread Virtual para cada peticion, Virtual Threads son manejados por la JVM y
        // mucho mas ligeros que los threads del SO, eso si, solo a partir del JDK 21
        server.start();
        System.out.println("SERVER RUNNING IN PORT -> " + port);
    }

    private static void registerRoutes(HttpServer server) {
        server.createContext("/api/usuario", new UserController());
    }
}
