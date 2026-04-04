package org.ricardo.jobtrackr.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.ricardo.jobtrackr.controller.base.ControllerBase;
import org.ricardo.jobtrackr.dto.UserResponse;
import org.ricardo.jobtrackr.exceptions.UserNotFoundException;
import org.ricardo.jobtrackr.model.User;
import org.ricardo.jobtrackr.service.UserService;
import org.ricardo.jobtrackr.util.JsonUtil;

import java.io.IOException;
import java.sql.SQLException;

public class UserController extends ControllerBase implements HttpHandler {
    private final UserService userService = new UserService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("=== Request to /api/usuario endpoint received ===");

        if (!"GET".equals(exchange.getRequestMethod())) {
            System.out.println("❌ Invalid Request method to /api/usuario endpoint. Method received: " + exchange.getRequestMethod());
            sendResponse(exchange, 405, "{\"error\":\"Metodo no permitido, solo Peticiones GET para el endpoint /api/usuario.\"}");
            return;
        }

        System.out.println("👍 Valid GET Request. Fetching User...");

        try {
            UserResponse userResponse = toUserResponse(userService.getUser());

            System.out.println("✅ UserResponse created correctly. User fetched:");
            System.out.println("User First Name -> " + userResponse.primerNombreUsuario());
            System.out.println("User Last Name -> " + userResponse.primerApellidoUsuario());
            System.out.println("User's Email -> " + userResponse.correoElectronicoUsuario());

            sendResponse(exchange, 200, JsonUtil.toJson(userResponse));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            System.out.println("Runtime Error. Error: " + e.getMessage());
            sendResponse(exchange, UserNotFoundException.STATUS_CODE, String.format("{\"error\":\"%s\"}", e.getMessage()));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error during SQL Retrieval. Error: " + e.getMessage());
            sendResponse(exchange, 500, "{\"error\":\"Error durante el acceso a Base de Datos en el servidor.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during SQL Retrieval. Error: " + e.getMessage());
            sendResponse(exchange, 500, "{\"error\":\"Error del servidor, intentalo mas tarde.\"}");
        }
    }

    private UserResponse toUserResponse(User userObj) {
        return new UserResponse(userObj.getUsuarioId(), userObj.getPrimerNombreUsuario(), userObj.getSegundoNombreUsuario(),
                userObj.getPrimerApellidoUsuario(), userObj.getSegundoApellidoUsuario(), userObj.getCorreoElectronicoUsuario());
    }
}
