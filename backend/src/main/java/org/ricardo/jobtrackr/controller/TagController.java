package org.ricardo.jobtrackr.controller;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.ricardo.jobtrackr.controller.base.ControllerBase;
import org.ricardo.jobtrackr.dto.CreateTagRequest;
import org.ricardo.jobtrackr.dto.TagResponse;
import org.ricardo.jobtrackr.exceptions.DatabaseOperationException;
import org.ricardo.jobtrackr.exceptions.ValidationException;
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

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());
        String path = exchange.getRequestURI().getPath();

        switch (exchange.getRequestMethod()) {
            case "GET" -> {
                logger.info("🌐 GET Request to /api/etiquetas endpoint received...");
                if (path.matches("/api/etiquetas")) {
                    getAllTags(exchange);
                } else {
                    sendResponse(exchange, 404, errorString("Este endpoint no existe. Peticion no valida."));
                }
            }

            case "POST" -> {
                logger.info("🌐 POST Request to /api/etiquetas endpoint received...");
                if (path.matches("/api/etiquetas")) {
                    try {
                        CreateTagRequest tagRequest = JsonUtil.fromJson(body, CreateTagRequest.class);

                        createTag(exchange, tagRequest);
                    } catch (JsonSyntaxException e) {
                        logger.log(Level.WARNING, "❌ Invalid input data fields on the Request Body, deserialization/parsing error. Error: " + e.getMessage(),
                                e);
                        sendResponse(exchange, 400, errorString("Los datos enviados no son válidos. Revisa los campos e inténtalo de nuevo."));
                    }
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

    private void createTag(HttpExchange exchange, CreateTagRequest tagRequest) throws IOException {
        try {
            int tagsCreated = tagService.createTag(tagRequest);

            logger.info("✅ New Tag Created Successfully. Tags created: " + tagsCreated);

            sendResponse(exchange, 201, successMessage("Nueva Etiqueta Creada Correctamente."));
        } catch (IllegalArgumentException | ValidationException e) {
            logger.log(Level.WARNING, "Field Validation Error. Error: " + e.getMessage(), e);
            sendResponse(exchange, ValidationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (DatabaseOperationException e) {
            logger.log(Level.WARNING, "Error during SQL Creation of new Tag. Error: " + e.getMessage(), e);
            sendResponse(exchange, DatabaseOperationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Creation of new Tag. DB Connection error. Error: " + e.getMessage(), e);
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
