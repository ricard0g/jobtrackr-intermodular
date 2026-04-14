package org.ricardo.jobtrackr.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.ricardo.jobtrackr.controller.base.ControllerBase;
import org.ricardo.jobtrackr.dto.TagResponse;
import org.ricardo.jobtrackr.model.Tag;
import org.ricardo.jobtrackr.service.TagService;
import org.ricardo.jobtrackr.util.JsonUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TagController extends ControllerBase implements HttpHandler {
    private static final Logger logger = Logger.getLogger(TagController.class.getName());

    private final TagService tagService = new TagService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String body = exchange.getRequestBody().toString();
        String path = exchange.getRequestURI().getPath();

        switch (exchange.getRequestMethod()) {
            case "GET" -> {
                if (path.matches("/api/etiquetas")) {
                    getAllTags(exchange);
                } else {
                    sendResponse(exchange, 404, errorString("Este endpoint no existe. Peticion no valida."));
                }
            }
        }
    }

    private void getAllTags(HttpExchange exchange) throws IOException {
        try {
            List<TagResponse> tagResponseList = tagService.getAllTags().stream().map(this::toTagResponse).toList();

            logger.info("✅ List of tags fetched Correctly. Total Number of tags: " + tagResponseList.size());

            sendResponse(exchange, 200, JsonUtil.toJson(tagResponseList));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Fetching of Tags. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error de Base de Datos en el servidor."));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error del servidor, intentalo mas tarde."));
        }
    }

    private TagResponse toTagResponse(Tag tag) {
        return new TagResponse(tag.getEtiquetaId(), tag.getNombreEtiqueta(), tag.getColorEtiqueta());
    }
}
