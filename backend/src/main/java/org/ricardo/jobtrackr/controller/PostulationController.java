package org.ricardo.jobtrackr.controller;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.ricardo.jobtrackr.controller.base.ControllerBase;
import org.ricardo.jobtrackr.dto.*;
import org.ricardo.jobtrackr.exceptions.DatabaseOperationException;
import org.ricardo.jobtrackr.exceptions.NotFoundException;
import org.ricardo.jobtrackr.exceptions.ValidationException;
import org.ricardo.jobtrackr.model.Interview;
import org.ricardo.jobtrackr.model.Postulation;
import org.ricardo.jobtrackr.model.StatusHistory;
import org.ricardo.jobtrackr.service.PostulationService;
import org.ricardo.jobtrackr.util.JsonUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
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
                logger.info("🌐 GET Request to /api/postulaciones endpoint received...");

                if (path.matches("/api/postulaciones/[0-9]+/entrevistas")) {
                    getAllInterviews(exchange, path.split("/"));
                } else if (path.matches("/api/postulaciones/[0-9]+/historial")) {
                    getStatusHistory(exchange, path.split("/"));
                } else if (path.matches("/api/postulaciones/[0-9]+")) {
                    findPostulationById(exchange, path.split("/"));
                } else if (path.matches("/api/postulaciones")) {
                    getAllPostulations(exchange);
                } else {
                    sendResponse(exchange, 404, errorString("Este endpoint no existe. Peticion no valida."));
                }
            }

            case "POST" -> {
                logger.info("🌐 POST Request to /api/postulaciones endpoint received...");

                if (body.isBlank()) {
                    sendResponse(exchange, 400, errorString("El Body de la peticion no puede estar vacio!"));
                    return;
                }

                if (path.matches("/api/postulaciones/[0-9]+/entrevistas")) {
                    try {
                        CreateInterviewRequest interviewRequest = JsonUtil.fromJson(body, CreateInterviewRequest.class);

                        createInterview(exchange, interviewRequest, path.split("/"));
                    } catch (JsonSyntaxException e) {
                        logger.log(Level.WARNING, "❌ Invalid input data fields on the Request Body, deserialization/parsing error. Error: " + e.getMessage(),
                                e);
                        sendResponse(exchange, 400, errorString("Los datos enviados no son válidos. Revisa los campos e inténtalo de nuevo."));
                    }
                } else if (path.matches("/api/postulaciones")) {
                    try {
                        CreatePostulationRequest postulationRequest = JsonUtil.fromJson(body, CreatePostulationRequest.class);

                        createPostulation(exchange, postulationRequest);
                    } catch (JsonSyntaxException e) {
                        logger.log(Level.WARNING, "❌ Invalid input data fields on the Request Body, deserialization/parsing error. Error: " + e.getMessage(),
                                e);
                        sendResponse(exchange, 400, errorString("Los datos enviados no son válidos. Revisa los campos e inténtalo de nuevo."));
                    }
                } else {
                    sendResponse(exchange, 404, errorString("Este endpoint no existe. Peticion no valida."));
                }
            }

            case "DELETE" -> {
                logger.info("🌐 DELETE Request to /api/postulaciones/{id} endpoint received...");
                if (path.matches("/api/postulaciones/[0-9]+")) {
                    deletePostulation(exchange, path.split("/"));
                } else if (path.matches("/api/postulaciones/[0-9]+/entrevistas/[0-9]+")) {
                    deleteInterview(exchange, path.split("/"));
                } else {
                    sendResponse(exchange, 404, errorString("Este endpoint no existe. Peticion no valida."));
                }
            }

            case "PUT" -> {
                logger.info("🌐 PUT Request to /api/postulaciones/{id} endpoint received...");

                if (body.isBlank()) {
                    sendResponse(exchange, 400, errorString("El Body de la peticion no puede estar vacio!"));
                    return;
                }

                if (path.matches("/api/postulaciones/[0-9]+")) {
                    try {
                        CreatePostulationRequest postulationRequest = JsonUtil.fromJson(body, CreatePostulationRequest.class);

                        updatePostulation(exchange, path.split("/"), postulationRequest);
                    } catch (JsonSyntaxException e) {
                        logger.log(Level.WARNING, "❌ Invalid input data fields on the Request Body, deserialization/parsing error. Error: " + e.getMessage(),
                                e);
                        sendResponse(exchange, 400, errorString("Los datos enviados no son válidos. Revisa los campos e inténtalo de nuevo."));
                    }
                } else if (path.matches("/api/postulaciones/[0-9]+/entrevistas/[0-9]+")) {
                    try {
                        CreateInterviewRequest interviewRequest = JsonUtil.fromJson(body, CreateInterviewRequest.class);

                        updateInterview(exchange, interviewRequest, path.split("/"));
                    } catch (JsonSyntaxException e) {
                        logger.log(Level.WARNING, "❌ Invalid input data fields on the Request Body, deserialization/parsing error. Error: " + e.getMessage(),
                                e);
                        sendResponse(exchange, 400, errorString("Los datos enviados no son válidos. Revisa los campos e inténtalo de nuevo."));
                    }
                } else {
                    sendResponse(exchange, 404, errorString("Este endpoint no existe. Peticion no valida."));
                }

            }

            case "PATCH" -> {
                logger.info("🌐 PATCH Request to /api/postulaciones/{id} endpoint received...");

                if (body.isBlank()) {
                    sendResponse(exchange, 400, errorString("El Body de la peticion no puede estar vacio!"));
                    return;
                }

                if (path.matches("/api/postulaciones/[0-9]+/estatus")) {
                    try {
                        UpdateStatusRequest statusUpdateReq = JsonUtil.fromJson(body, UpdateStatusRequest.class);

                        patchStatus(exchange, statusUpdateReq, path.split("/"));
                    } catch (JsonSyntaxException e) {
                        logger.log(Level.WARNING, "❌ Invalid input data fields on the Request Body, deserialization/parsing error. Error: " + e.getMessage(),
                                e);
                        sendResponse(exchange, 400, errorString("Los datos enviados no son válidos. Revisa los campos e inténtalo de nuevo."));
                    }
                } else if (path.matches("/api/postulaciones/[0-9]+/orden")) {
                    try {
                        Map<String, Double> orderUpdateReq = JsonUtil.fromJson(body, HashMap.class);

                        patchOrder(exchange, orderUpdateReq, path.split("/"));
                    } catch (JsonSyntaxException e) {
                        logger.log(Level.WARNING, "❌ Invalid input data fields on the Request Body, deserialization/parsing error. Error: " + e.getMessage(),
                                e);
                        sendResponse(exchange, 400, errorString("Los datos enviados no son válidos. Revisa los campos e inténtalo de nuevo."));
                    }
                } else if (path.matches("/api/postulaciones/[0-9]+")) {
                    try {
                        Map<String, Object> patchValues = JsonUtil.fromJson(body, HashMap.class);

                        patchPostulation(exchange, patchValues, path.split("/"));
                    } catch (ClassCastException e) {
                        logger.log(Level.WARNING, "❌ Invalid input data fields on the Request Body, deserialization/parsing error. Error: " + e.getMessage(),
                                e);
                        sendResponse(exchange, 400, errorString("Los datos enviados no son válidos. Revisa los campos e inténtalo de nuevo."));
                    }
                } else {
                    sendResponse(exchange, 404, errorString("Este endpoint no existe. Peticion no valida."));
                }
            }

            default -> {
                logger.log(Level.WARNING, "❌ Invalid Request method to /api/postulaciones endpoint. Method received: " + exchange.getRequestMethod());
                sendResponse(exchange, 405,
                        errorString("Metodo no permitido, solo Peticiones GET/DELETE/POST/PUT/PATCH para el endpoint /api/postulaciones."));
            }
        }
    }

    private void getAllPostulations(HttpExchange exchange) throws IOException {
        try {
            List<PostulationResponse> postulations = postulationService.getAllPostulations().stream().map(this::toPostulationResponse).toList();

            logger.info(String.format("✅ Postulations obtained. Total of: %s", postulations.size()));

            sendResponse(exchange, 200, JsonUtil.toJson(postulations));
        } catch (NotFoundException e) {
            logger.log(Level.WARNING, "Not Found Exception. Error: " + e.getMessage(), e);
            sendResponse(exchange, NotFoundException.STATUS_CODE, errorString(e.getMessage()));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Creation of new Postulation. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error de Base de Datos en el servidor."));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error del servidor, intentalo mas tarde."));
        }
    }

    private void findPostulationById(HttpExchange exchange, String[] requestPathSplit) throws IOException {
        try {
            int postulationId = Integer.parseInt(requestPathSplit[requestPathSplit.length - 1]);

            PostulationResponse postulation = toPostulationResponse(postulationService.findPostulationById(postulationId));

            logger.info("✅ Postulation obtained with ID --> " + postulation.postulacionId());

            sendResponse(exchange, 200, JsonUtil.toJson(postulation));
        } catch (NotFoundException e) {
            logger.log(Level.WARNING, "Not Found Exception. Error: " + e.getMessage(), e);
            sendResponse(exchange, NotFoundException.STATUS_CODE, errorString(e.getMessage()));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL the Extraction of Postulation. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error de Base de Datos en el servidor."));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error del servidor, intentalo mas tarde."));
        }
    }

    private void createPostulation(HttpExchange exchange, CreatePostulationRequest postulationRequest) throws IOException {
        try {
            int postulationsCreated = postulationService.createPostulation(postulationRequest);

            logger.info("✅ New Postulation Created Successfully. Number of rows changed: " + postulationsCreated);

            sendResponse(exchange, 201, successMessage("Nueva Postulacion Creada Correctamente."));
        } catch (IllegalArgumentException | ValidationException e) {
            logger.log(Level.WARNING, "Field Validation Error. Error: " + e.getMessage(), e);
            sendResponse(exchange, ValidationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (DatabaseOperationException e) {
            logger.log(Level.WARNING, "Error during SQL Creation of new Postulation. Error: " + e.getMessage(), e);
            sendResponse(exchange, DatabaseOperationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Creation of new Postulation. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error de Base de Datos en el servidor."));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error del servidor, intentalo mas tarde."));
        }
    }

    private void deletePostulation(HttpExchange exchange, String[] requestPathSplit) throws IOException {
        try {
            int postulationId = Integer.parseInt(requestPathSplit[requestPathSplit.length - 1]);

            Postulation existingPostulation = postulationService.findPostulationById(postulationId);

            logger.info("✅ An existing Postulation was found with ID: " + existingPostulation.getPostulacionId());

            postulationService.deletePostulation(existingPostulation.getPostulacionId());

            // Deberia ser un 204 sin contenido en el body, pero con el 200 puedo enviar el mensaje al frontend lo cual lo hace un poco mas facil de manejar
            sendResponse(exchange, 200, successMessage("Postulacion con ID " + postulationId + " eliminada correctamente."));
        } catch (NotFoundException e) {
            logger.log(Level.WARNING, "Not Found Exception. Error: " + e.getMessage(), e);
            sendResponse(exchange, NotFoundException.STATUS_CODE, errorString(e.getMessage()));
        } catch (DatabaseOperationException e) {
            logger.log(Level.WARNING, "Error during SQL Deletion of Postulation. Error: " + e.getMessage(), e);
            sendResponse(exchange, DatabaseOperationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Deletion of new Postulation. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error de Base de Datos en el servidor."));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error del servidor, intentalo mas tarde."));
        }
    }

    private void updatePostulation(HttpExchange exchange, String[] requestPathSplit, CreatePostulationRequest updatedPostulation) throws IOException {
        try {
            int postulationId = Integer.parseInt(requestPathSplit[requestPathSplit.length - 1]);

            Postulation existingPostulation = postulationService.findPostulationById(postulationId);

            logger.info("✅ An existing Postulation was found to make the Update");

            postulationService.updatePostulation(existingPostulation.getPostulacionId(), updatedPostulation);

            sendResponse(exchange, 201, successMessage("Postulacion con ID " + postulationId + " Actualizada Correctamente."));
        } catch (DatabaseOperationException e) {
            logger.log(Level.WARNING, "Error during SQL Deletion of Postulation. Error: " + e.getMessage(), e);
            sendResponse(exchange, DatabaseOperationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (NotFoundException e) {
            logger.log(Level.WARNING, "Not Found Exception. Error: " + e.getMessage(), e);
            sendResponse(exchange, NotFoundException.STATUS_CODE, errorString(e.getMessage()));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Update of Postulation. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error de Base de Datos en el servidor."));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error del servidor, intentalo mas tarde."));
        }
    }

    private void patchPostulation(HttpExchange exchange, Map<String, Object> patchValues, String[] requestPathSplit) throws IOException {
        try {
            int postulationId = Integer.parseInt(requestPathSplit[requestPathSplit.length - 1]);

            postulationService.patchPostulation(postulationId, patchValues);

            sendResponse(exchange, 200, successMessage("Postulacion con ID " + postulationId + " Actualización Parcial ejecutada Correctamente."));
        } catch (ClassCastException e) {
            logger.log(Level.WARNING, "❌ Invalid input data fields on the Request Body, deserialization/parsing error. Error: " + e.getMessage(),
                    e);
            sendResponse(exchange, 400, errorString("Los datos enviados no son válidos. Revisa los campos e inténtalo de nuevo."));
        } catch (ValidationException e) {
            logger.log(Level.WARNING, "Field Validation Error. Error: " + e.getMessage(), e);
            sendResponse(exchange, ValidationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (DatabaseOperationException e) {
            logger.log(Level.WARNING, "Error during SQL Patch of Postulation. Error: " + e.getMessage(), e);
            sendResponse(exchange, DatabaseOperationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (NotFoundException e) {
            logger.log(Level.WARNING, "Not Found Exception. Error: " + e.getMessage(), e);
            sendResponse(exchange, NotFoundException.STATUS_CODE, errorString(e.getMessage()));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Patch of Postulation. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error de Base de Datos en el servidor."));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error del servidor, intentalo mas tarde."));
        }
    }

    private void patchOrder(HttpExchange exchange, Map<String, Double> updateOrderRequest, String[] requestPathSplit) throws IOException {
        try {
            int postulationId = Integer.parseInt(requestPathSplit[3]);

            postulationService.patchOrder(postulationId, updateOrderRequest);

            sendResponse(exchange, 200, successMessage("Orden Kanban Actualizado para Postulacion con ID " + postulationId));
        } catch (ValidationException e) {
            logger.log(Level.WARNING, "Field Validation Error. Error: " + e.getMessage(), e);
            sendResponse(exchange, ValidationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (ClassCastException e) {
            logger.log(Level.WARNING, "❌ Invalid input data fields on the Request Body, deserialization/parsing error. Error: " + e.getMessage(),
                    e);
            sendResponse(exchange, 400, errorString("Los datos enviados no son válidos, Orden Kanban debe ser un numero. Revisa los campos e inténtalo de " +
                    "nuevo."));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Patch of Order of Postulation. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error de Base de Datos en el servidor."));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error del servidor, intentalo mas tarde."));
        }
    }

    private void patchStatus(HttpExchange exchange, UpdateStatusRequest statusUpdateReq, String[] requestPathSplit) throws IOException {
        try {
            int postulationId = Integer.parseInt(requestPathSplit[3]);

            postulationService.patchStatus(postulationId, statusUpdateReq);

            sendResponse(exchange, 200, successMessage("Estatus Actualizada para Postulacion con ID " + postulationId));
        } catch (IllegalArgumentException | ValidationException e) {
            logger.log(Level.WARNING, "Field Validation Error. Error: " + e.getMessage(), e);
            sendResponse(exchange, ValidationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (DatabaseOperationException e) {
            logger.log(Level.WARNING, "Error during SQL Patch of Status. Error: " + e.getMessage(), e);
            sendResponse(exchange, DatabaseOperationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (NotFoundException e) {
            logger.log(Level.WARNING, "Not Found Exception. Error: " + e.getMessage(), e);
            sendResponse(exchange, NotFoundException.STATUS_CODE, errorString(e.getMessage()));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Patch of Status of a Postulation. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error de Base de Datos en el servidor."));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error del servidor, intentalo mas tarde."));
        }
    }

    private void getStatusHistory(HttpExchange exchange, String[] requestPathSplit) throws IOException {
        try {
            int postulationId = Integer.parseInt(requestPathSplit[3]);

            List<StatusHistoryResponse> statusHistoryResponseList =
                    postulationService.getAllStatusHistory(postulationId).stream().map(this::toStatusHistoryResponse).toList();

            logger.info("✅ " + statusHistoryResponseList.size() + " Status histories found");

            sendResponse(exchange, 200, JsonUtil.toJson(statusHistoryResponseList));
        } catch (NotFoundException e) {
            logger.log(Level.WARNING, "Not Found Exception. Error: " + e.getMessage(), e);
            sendResponse(exchange, NotFoundException.STATUS_CODE, errorString(e.getMessage()));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL fetching of Status History. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error de Base de Datos en el servidor."));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error del servidor, intentalo mas tarde."));
        }
    }

    private void getAllInterviews(HttpExchange exchange, String[] requestPathSplit) throws IOException {
        try {
            int postulationId = Integer.parseInt(requestPathSplit[3]);

            List<InterviewResponse> interviewResponseList = postulationService.getAllInterviews(postulationId).stream().map(this::toInterviewResponse).toList();

            logger.info("✅ List of interview fetched successfully. Number of Interviews Fetched: " + interviewResponseList.size());

            sendResponse(exchange, 200, JsonUtil.toJson(interviewResponseList));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Fetching of Interviews. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error de Base de Datos en el servidor."));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error del servidor, intentalo mas tarde."));
        }
    }

    private void createInterview(HttpExchange exchange, CreateInterviewRequest interviewRequest, String[] requestPathSplit) throws IOException {
        try {
            int postulationId = Integer.parseInt(requestPathSplit[3]);

            int interviewsCreated = postulationService.createInterview(postulationId, interviewRequest);

            logger.info("✅ New Interview Created Successfully. Number of rows changed: " + interviewsCreated);

            sendResponse(exchange, 201, successMessage("Nueva entrevista creada correctamente"));
        } catch (IllegalArgumentException | ValidationException e) {
            logger.log(Level.WARNING, "Field Validation Error. Error: " + e.getMessage(), e);
            sendResponse(exchange, ValidationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (DatabaseOperationException e) {
            logger.log(Level.WARNING, "Error during SQL Creation of new Interview. Error: " + e.getMessage(), e);
            sendResponse(exchange, DatabaseOperationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Creation of new Interview. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error de Base de Datos en el servidor."));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error del servidor, intentalo mas tarde."));
        }
    }

    private void deleteInterview(HttpExchange exchange, String[] requestPathSplit) throws IOException {
        try {
            int interviewId = Integer.parseInt(requestPathSplit[5]);

            Interview existingInterview = postulationService.findInterviewById(interviewId);

            int interviewsRemoved = postulationService.deleteInterview(existingInterview.getEntrevistaId());

            logger.info("✅ Interview with ID " + interviewId + " found and " + interviewsRemoved + " Interview was removed");

            sendResponse(exchange, 201, successMessage("Entrevista Eliminada correctamente."));
        } catch (NotFoundException e) {
            logger.log(Level.WARNING, "Not Found Exception. Error: " + e.getMessage(), e);
            sendResponse(exchange, NotFoundException.STATUS_CODE, errorString(e.getMessage()));
        } catch (DatabaseOperationException e) {
            logger.log(Level.WARNING, "Error during SQL Deletion of Postulation. Error: " + e.getMessage(), e);
            sendResponse(exchange, DatabaseOperationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Deletion of new Postulation. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error de Base de Datos en el servidor."));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error del servidor, intentalo mas tarde."));
        }
    }

    private void updateInterview(HttpExchange exchange, CreateInterviewRequest interviewRequest, String[] requestPathSplit) throws IOException {
        try {
            int interviewId = Integer.parseInt(requestPathSplit[5]);

            Interview existingInterview = postulationService.findInterviewById(interviewId);

            int interviewsUpdated = postulationService.updateInterview(existingInterview.getEntrevistaId(), interviewRequest);

            logger.info("✅ " + interviewsUpdated + " Interviews Updated Successfully");

            sendResponse(exchange, 201, successMessage("Entrevista con ID " + existingInterview.getEntrevistaId() + " Actualizada correctamente."));
        } catch (ValidationException e) {
            logger.log(Level.WARNING, "Field Validation Error. Error: " + e.getMessage(), e);
            sendResponse(exchange, ValidationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (DatabaseOperationException e) {
            logger.log(Level.WARNING, "Error during SQL Update of Postulation. Error: " + e.getMessage(), e);
            sendResponse(exchange, DatabaseOperationException.STATUS_CODE, errorString(e.getMessage()));
        } catch (NotFoundException e) {
            logger.log(Level.WARNING, "Not Found Exception. Error: " + e.getMessage(), e);
            sendResponse(exchange, NotFoundException.STATUS_CODE, errorString(e.getMessage()));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Update of Postulation. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error de Base de Datos en el servidor."));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, errorString("Error del servidor, intentalo mas tarde."));
        }
    }

    private InterviewResponse toInterviewResponse(Interview interview) {
        return new InterviewResponse(interview.getEntrevistaId(), interview.getPostulacionId(), interview.getNumeroRonda(),
                interview.getTipoEntrevista(), interview.getFechaEntrevista(), interview.getEntrevistador(), interview.getResultadoEntrevista());
    }

    private StatusHistoryResponse toStatusHistoryResponse(StatusHistory statusHistory) {
        return new StatusHistoryResponse(statusHistory.getEstatusId(), statusHistory.getPostulacionId(), statusHistory.getAntiguoEstatus(),
                statusHistory.getNuevoEstatus(), statusHistory.getCambiadoEn(), statusHistory.getNotaPostulacion());
    }

    private PostulationResponse toPostulationResponse(Postulation postulation) {
        return new PostulationResponse(postulation.getPostulacionId(), postulation.getUsuarioId(), postulation.getEmpresaId(), postulation.getRol(),
                postulation.getEstatus(), postulation.getOrdenKanban(), postulation.getSalarioMinimo(), postulation.getSalarioMaximo(),
                postulation.getUbicacion(), postulation.isEsTelematico(), postulation.getOfertaUrl(), postulation.getCreadaEn(),
                postulation.getActualizadaEn(), postulation.getNotaPostulacion(), postulation.getFechaPostulacion(), postulation.getListaEtiquetas());
    }
}
