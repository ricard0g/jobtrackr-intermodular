package org.ricardo.jobtrackr.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.ricardo.jobtrackr.controller.base.ControllerBase;
import org.ricardo.jobtrackr.dto.CreateEnterpriseRequest;
import org.ricardo.jobtrackr.dto.EnterpriseResponse;
import org.ricardo.jobtrackr.exceptions.DatabaseOperationException;
import org.ricardo.jobtrackr.exceptions.NotFoundException;
import org.ricardo.jobtrackr.model.Enterprise;
import org.ricardo.jobtrackr.service.EnterpriseService;
import org.ricardo.jobtrackr.util.JsonUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnterpriseController extends ControllerBase implements HttpHandler {
    private static final Logger logger = Logger.getLogger(EnterpriseController.class.getName());

    private final EnterpriseService enterpriseService = new EnterpriseService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());

        switch (exchange.getRequestMethod()) {
            case "GET" -> getAllEnterprises(exchange);

            case "POST" -> {
                if (body.isBlank()) {
                    sendResponse(exchange, 400, "{\"error\":\"El Body de la peticion no puede estar vacio!\"}");
                    return;
                }

                CreateEnterpriseRequest newEnterprise = JsonUtil.fromJson(body, CreateEnterpriseRequest.class);

                createEnterprise(exchange, newEnterprise);
            }

            default -> {
                logger.log(Level.WARNING, "❌ Invalid Request method to /api/empresas endpoint. Method received: " + exchange.getRequestMethod());
                sendResponse(exchange, 405, "{\"error\":\"Metodo no permitido, solo Peticiones GET y POST para el endpoint /api/empresas.\"}");
            }
        }
    }

    private void getAllEnterprises(HttpExchange exchange) throws IOException {
        try {
            List<EnterpriseResponse> enterpriseResponses =
                    enterpriseService.getAllEnterprises().stream().map(this::toEnterpriseResponse).toList();

            logger.info("✅ List of enterpises fetched. Total: " + enterpriseResponses.size());

            sendResponse(exchange, 200, JsonUtil.toJson(enterpriseResponses));
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

    private void createEnterprise(HttpExchange exchange, CreateEnterpriseRequest newEnterprise) throws IOException {
        try {
            int rowsChanged = enterpriseService.createEnterprise(newEnterprise);

            logger.info("✅ New Enterprise Created Successfully. Number of rows changed: " + rowsChanged);

            sendResponse(exchange, 201, JsonUtil.toJson(rowsChanged));
        } catch (DatabaseOperationException e) {
            logger.log(Level.WARNING, "Error during SQL Creation of new Enterprise. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "{\"error\":\"Error durante la insercion de datos en la BBDD del servidor.\"}");
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Unhandled Error during SQL Creation of new Enterprise. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "{\"error\":\"Error de conexion con Base de Datos desde el servidor.\"}");
        }
    }

    private EnterpriseResponse toEnterpriseResponse(Enterprise enterprise) {
        return new EnterpriseResponse(enterprise.getEmpresaId(), enterprise.getNombreEmpresa(), enterprise.getLogoEmpresa());
    }
}
