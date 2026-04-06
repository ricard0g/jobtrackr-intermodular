package org.ricardo.jobtrackr.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.ricardo.jobtrackr.controller.base.ControllerBase;
import org.ricardo.jobtrackr.dto.PostulationResponse;
import org.ricardo.jobtrackr.exceptions.NotFoundException;
import org.ricardo.jobtrackr.model.Postulation;
import org.ricardo.jobtrackr.service.PostulationService;
import org.ricardo.jobtrackr.util.JsonUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PostulationController extends ControllerBase implements HttpHandler {
    private final PostulationService postulationService = new PostulationService();

    @Override
    public void handle (HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());

        if ("GET".equals(exchange.getRequestMethod())) {
            try {
                List<PostulationResponse> postulations = postulationService.getAllPostulations().stream().map(this::toPostulationResponse).toList();

                System.out.println("✅ All postulations obtained");

                for (PostulationResponse postulation : postulations) {
                    System.out.println("\n👉 Postulation " + postulation.postulacionId() + "\n");
                    System.out.println("- Rol: " + postulation.rol());
                    System.out.println("- Salario Minimo: " + postulation.salarioMinimo());
                    System.out.println("- Salario Maximo: " + postulation.salarioMaximo());
                    System.out.println("- Estatus: " + postulation.estatus());
                    System.out.println("- Creada En: " + postulation.creadaEn());
                    System.out.println("- Actualizada En: " + postulation.actualizadaEn() + "\n");
                }

                sendResponse(exchange, 200, JsonUtil.toJson(postulations));
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

    private PostulationResponse toPostulationResponse(Postulation postulation) {
        return new PostulationResponse(postulation.getPostulacionId(), postulation.getUsuarioId(), postulation.getEmpresaId(), postulation.getRol(),
                postulation.getEstatus(), postulation.getOrdenKanban(), postulation.getSalarioMinimo(), postulation.getSalarioMaximo(),
                postulation.getUbicacion(), postulation.isEsTelematico(), postulation.getOfertaUrl(), postulation.getCreadaEn(),
                postulation.getActualizadaEn(), postulation.getNotaPostulacion(), postulation.getFechaPostulacion());
    }
}
