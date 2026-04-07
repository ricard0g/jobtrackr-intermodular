package org.ricardo.jobtrackr.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.ricardo.jobtrackr.controller.base.ControllerBase;
import org.ricardo.jobtrackr.dto.CreatePostulationRequest;
import org.ricardo.jobtrackr.dto.PostulationResponse;
import org.ricardo.jobtrackr.exceptions.DatabaseOperationException;
import org.ricardo.jobtrackr.exceptions.NotFoundException;
import org.ricardo.jobtrackr.model.Postulation;
import org.ricardo.jobtrackr.service.PostulationService;
import org.ricardo.jobtrackr.util.JsonUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostulationController extends ControllerBase implements HttpHandler {
    private static final Logger logger = Logger.getLogger(PostulationController.class.getName());

    private final PostulationService postulationService = new PostulationService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.info(String.format("Received Request at --> /api/postulaciones. Request Method: %s", exchange.getRequestMethod()));

        String body = new String(exchange.getRequestBody().readAllBytes());
        String path = exchange.getRequestURI().getPath();

        switch (exchange.getRequestMethod()) {
            case "GET" -> {
                if (path.matches("/api/postulaciones/[0-9]+")) {
                    findPostulationById(exchange, path.split("/"));
                } else if (path.matches("/api/postulaciones")) {
                    getAllPostulactions(exchange);
                } else {
                    sendResponse(exchange, 404, "{\"error\":\"Este endpoint no existe. Peticion no valida.\"}");
                }
            }

            case "POST" -> {
                logger.info("🌐 POST Request to /api/postulaciones endpoint received...");

                if (body.isBlank()) {
                    sendResponse(exchange, 400, "{\"error\":\"El Body de la peticion no puede estar vacio!\"}");
                    return;
                }

                if (path.matches("/api/postulaciones")) {
                    CreatePostulationRequest postulationRequest = JsonUtil.fromJson(body, CreatePostulationRequest.class);

                    createPostulation(exchange, postulationRequest);
                } else {
                    sendResponse(exchange, 404, "{\"error\":\"Este endpoint no existe. Peticion no valida.\"}");
                }
            }

            case "DELETE" -> {
                logger.info("🌐 POST Request to /api/postulaciones endpoint received...");
                if (path.matches("/api/postulaciones/[0-9]+")) {
                    deletePostulation(exchange, path.split("/"));
                } else {
                    sendResponse(exchange, 404, "{\"error\":\"Este endpoint no existe. Peticion no valida.\"}");
                }
            }

            default -> {
                logger.log(Level.WARNING, "❌ Invalid Request method to /api/postulaciones endpoint. Method received: " + exchange.getRequestMethod());
                sendResponse(exchange, 405, "{\"error\":\"Metodo no permitido, solo Peticiones GET y POST para el endpoint /api/postulaciones.\"}");
            }
        }
    }

    private void getAllPostulactions(HttpExchange exchange) throws IOException {
        try {
            logger.info("🌐 GET Request to /api/postulaciones endpoint received...");

            List<PostulationResponse> postulations = postulationService.getAllPostulations().stream().map(this::toPostulationResponse).toList();

            logger.info(String.format("✅ Postulations obtained. Total of: %s", postulations.size()));

            sendResponse(exchange, 200, JsonUtil.toJson(postulations));
        } catch (NotFoundException e) {
            logger.log(Level.WARNING, "Not Found Exception. Error: " + e.getMessage(), e);
            sendResponse(exchange, NotFoundException.STATUS_CODE, String.format("{\"error\":\"%s\"}", e.getMessage()));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Creation of new Postulation. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "{\"error\":\"Error durante el acceso a Base de Datos en el servidor.\"}");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "{\"error\":\"Error del servidor, intentalo mas tarde.\"}");
        }
    }

    private void findPostulationById(HttpExchange exchange, String[] requestPathSplit) throws IOException {
        try {
            int postulationId = Integer.parseInt(requestPathSplit[requestPathSplit.length - 1]);

            logger.info("🌐 GET Request to /api/postulaciones/{id} endpoint received...");

            PostulationResponse postulation = toPostulationResponse(postulationService.findPostulationById(postulationId));

            logger.info("✅ Postulation obtained with ID --> " + postulation.postulacionId());

            sendResponse(exchange, 200, JsonUtil.toJson(postulation));
        } catch (NotFoundException e) {
            logger.log(Level.WARNING, "Not Found Exception. Error: " + e.getMessage(), e);
            sendResponse(exchange, NotFoundException.STATUS_CODE, String.format("{\"error\":\"%s\"}", e.getMessage()));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL the Extraction of Postulation. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "{\"error\":\"Error de conexion con Base de Datos desde el servidor.\"}");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "{\"error\":\"Error del servidor, intentalo mas tarde.\"}");
        }
    }

    private void createPostulation(HttpExchange exchange, CreatePostulationRequest postulationRequest) throws IOException {
        try {
            int rowsChanged = postulationService.createPostulation(postulationRequest);

            logger.info("✅ New Postulation Created Successfully. Number of rows changed: " + rowsChanged);

            sendResponse(exchange, 201, JsonUtil.toJson(rowsChanged));
        } catch (DatabaseOperationException e) {
            logger.log(Level.WARNING, "Error during SQL Creation of new Postulation. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "{\"error\":\"Error durante la insercion de datos en la BBDD del servidor.\"}");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Creation of new Postulation. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "{\"error\":\"Error de conexion con Base de Datos desde el servidor.\"}");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "{\"error\":\"Error del servidor, intentalo mas tarde.\"}");
        }
    }

    private void deletePostulation(HttpExchange exchange, String[] requestPathSplit) throws IOException {
        try {
            int postulationId = Integer.parseInt(requestPathSplit[requestPathSplit.length - 1]);

            Postulation existingPostulation = postulationService.findPostulationById(postulationId);

            logger.info("✅ An existing Postulation was found with ID: " + existingPostulation.getPostulacionId());

            int postulationDeleted = postulationService.deletePostulation(existingPostulation.getPostulacionId());

            sendResponse(exchange, 204, JsonUtil.toJson(postulationDeleted));
        } catch (NotFoundException e) {
            logger.log(Level.WARNING, "Not Found Exception. Error: " + e.getMessage(), e);
            sendResponse(exchange, NotFoundException.STATUS_CODE, String.format("{\"error\":\"%s\"}", e.getMessage()));
        } catch (DatabaseOperationException e) {
            logger.log(Level.WARNING, "Error during SQL Deletion of Postulation. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "{\"error\":\"Error durante la eliminacion de datos en la BBDD del servidor.\"}");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Creation of new Postulation. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "{\"error\":\"Error de conexion con Base de Datos desde el servidor.\"}");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "{\"error\":\"Error del servidor, intentalo mas tarde.\"}");
        }
    }

    private PostulationResponse toPostulationResponse(Postulation postulation) {
        return new PostulationResponse(postulation.getPostulacionId(), postulation.getUsuarioId(), postulation.getEmpresaId(), postulation.getRol(),
                postulation.getEstatus(), postulation.getOrdenKanban(), postulation.getSalarioMinimo(), postulation.getSalarioMaximo(),
                postulation.getUbicacion(), postulation.isEsTelematico(), postulation.getOfertaUrl(), postulation.getCreadaEn(),
                postulation.getActualizadaEn(), postulation.getNotaPostulacion(), postulation.getFechaPostulacion());
    }
}
