package org.ricardo.jobtrackr.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.ricardo.jobtrackr.controller.base.ControllerBase;
import org.ricardo.jobtrackr.dto.UserResponse;
import org.ricardo.jobtrackr.exceptions.NotFoundException;
import org.ricardo.jobtrackr.model.User;
import org.ricardo.jobtrackr.service.UserService;
import org.ricardo.jobtrackr.util.JsonUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserController extends ControllerBase implements HttpHandler {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.info("🌐 Request to /api/usuario endpoint received");

        if (!"GET".equals(exchange.getRequestMethod())) {
            logger.log(Level.WARNING, "❌ Invalid Request method to /api/usuario endpoint. Method received: " + exchange.getRequestMethod());
            sendResponse(exchange, 405, "{\"error\":\"Metodo no permitido, solo Peticiones GET para el endpoint /api/usuario.\"}");
            return;
        }

        try {
            UserResponse userResponse = toUserResponse(userService.getUser());

            logger.info("✅ User Fetched Correctly from DB. Name: " + userResponse.primerNombreUsuario() + " - First Last Nam: " + userResponse.primerApellidoUsuario());

            sendResponse(exchange, 200, JsonUtil.toJson(userResponse));
        } catch (NotFoundException e) {
            logger.log(Level.WARNING, "Not Found Exception. Error: " + e.getMessage(), e);
            sendResponse(exchange, NotFoundException.STATUS_CODE, String.format("{\"error\":\"%s\"}", e.getMessage()));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unhandled Error during SQL Extraction of User. DB Connection error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "{\"error\":\"Error de conexion con Base de Datos desde el servidor.\"}");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled error. Error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "{\"error\":\"Error del servidor, intentalo mas tarde.\"}");
        }
    }

    private UserResponse toUserResponse(User userObj) {
        return new UserResponse(userObj.getUsuarioId(), userObj.getPrimerNombreUsuario(), userObj.getSegundoNombreUsuario(),
                userObj.getPrimerApellidoUsuario(), userObj.getSegundoApellidoUsuario(), userObj.getCorreoElectronicoUsuario());
    }
}
