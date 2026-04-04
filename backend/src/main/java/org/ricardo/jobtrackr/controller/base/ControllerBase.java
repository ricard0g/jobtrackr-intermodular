package org.ricardo.jobtrackr.controller.base;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ControllerBase {
    // Metodo con el que se enviarán todas las respuestas por HTTP, todos los handlers extenderán esta clase con este método. Solo accesible para las clases
    // dentro handler
    protected void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] response = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.getResponseBody().close();
    }
}
