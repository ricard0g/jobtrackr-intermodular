package org.ricardo.jobtrackr.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.ricardo.jobtrackr.controller.base.ControllerBase;
import org.ricardo.jobtrackr.dto.EnterpriseResponse;
import org.ricardo.jobtrackr.exceptions.NotFoundException;
import org.ricardo.jobtrackr.model.Enterprise;
import org.ricardo.jobtrackr.service.EnterpriseService;
import org.ricardo.jobtrackr.util.JsonUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EnterpriseController extends ControllerBase implements HttpHandler {
    private final EnterpriseService enterpriseService = new EnterpriseService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            try {
                List<EnterpriseResponse> enterpriseResponses =
                        enterpriseService.getAllEnterprises().stream().map(this::toEnterpriseResponse).toList();

                System.out.println("✅ Enterprise Response created correctly.");

                sendResponse(exchange, 200, JsonUtil.toJson(enterpriseResponses));
            } catch(NotFoundException e) {
                e.printStackTrace();
                System.out.println("Not Found Exception. Error: " + e.getMessage());
                sendResponse(exchange, NotFoundException.STATUS_CODE, String.format("{\"error\":\"%s\"}", e.getMessage()));
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error during SQL Retrieval. Error: " + e.getMessage());
                sendResponse(exchange, 500, "{\"error\":\"Error durante el acceso a Base de Datos en el servidor.\"}");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Unhandled error. Error: " + e.getMessage());
                sendResponse(exchange, 500, "{\"error\":\"Error del servidor, intentalo mas tarde.\"}");
            }

        }
    }

    private EnterpriseResponse toEnterpriseResponse(Enterprise enterprise) {
        return new EnterpriseResponse(enterprise.getEmpresaId(), enterprise.getNombreEmpresa(), enterprise.getLogoEmpresa());
    }
}
