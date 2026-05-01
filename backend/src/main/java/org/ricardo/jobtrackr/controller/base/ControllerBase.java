package org.ricardo.jobtrackr.controller.base;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ControllerBase {
    // Metodo con el que se enviarán todas las respuestas por HTTP, todos los handlers extenderán esta clase con este método. Solo accesible para las clases
    // dentro handler
    protected void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] response = body.getBytes(StandardCharsets.UTF_8);
        setCorsHeaders(exchange);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.getResponseBody().close();
    }

    protected void sendNoContentResponse(HttpExchange exchange, int statusCode) throws IOException {
        setCorsHeaders(exchange);
        exchange.sendResponseHeaders(statusCode, -1);
        exchange.getResponseBody().close();
    }

    private void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    protected String errorString(String errorStr) {
        return String.format("{\"error\":\"%s\"}", errorStr);
    }

    protected String successMessage(String message) {
        return String.format("{\"message\":\"%s\"}", message);
    }
}
