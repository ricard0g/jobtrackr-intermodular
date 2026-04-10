package org.ricardo.jobtrackr.service;

import org.ricardo.jobtrackr.dto.CreatePostulationRequest;
import org.ricardo.jobtrackr.dto.UpdateStatusRequest;
import org.ricardo.jobtrackr.exceptions.DatabaseOperationException;
import org.ricardo.jobtrackr.exceptions.NotFoundException;
import org.ricardo.jobtrackr.exceptions.ValidationException;
import org.ricardo.jobtrackr.interfaces.DtoMapper;
import org.ricardo.jobtrackr.model.Postulation;
import org.ricardo.jobtrackr.dao.PostulationDAO;
import org.ricardo.jobtrackr.model.PostulationStatus;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PostulationService implements DtoMapper<Postulation, CreatePostulationRequest> {
    private static final Logger logger = Logger.getLogger(PostulationService.class.getName());

    private static final Set<String> ALLOWED_PATCH_FIELDS = Set.of("empresaId", "rol", "estatus", "ordenKanban", "salarioMinimo",
            "salarioMaximo", "ubicacion", "esTelematico", "ofertaUrl", "notaPostulacion", "fechaPostulacion");

    private final PostulationDAO postulationDAO = new PostulationDAO();

    public List<Postulation> getAllPostulations() throws SQLException {
        return postulationDAO.getAllPostulations().orElseThrow(() -> new NotFoundException("No hay postulaciones en la Base de Datos"));
    }

    public Postulation findPostulationById(int postulationId) throws SQLException {
        return postulationDAO.findPostulationById(postulationId).orElseThrow(() -> new NotFoundException("No se ha encontrado Postulacion con" +
                " ID: " + postulationId));
    }

    public int deletePostulation(int postulationId) throws SQLException {
        return postulationDAO.deletePostulation(postulationId);
    }

    public int createPostulation(CreatePostulationRequest postulationReq) throws SQLException, DatabaseOperationException, IllegalArgumentException, ValidationException {
        try {
            Postulation newPostulation = toModel(postulationReq);

            return postulationDAO.createPostulation(newPostulation);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valor del campo 'estatus' es invalido. Revisalo e intentalo de nuevo. Valor recibido: '" + postulationReq.getEstatus() +
                    "'.");
        } catch(NullPointerException e) {
            throw new ValidationException("El campo 'estatus' no esta presente, peticion invalida. Revisalo e intentalo de nuevo.");
        }
    }

    public int updatePostulation(int postulationId, CreatePostulationRequest postulationReq) throws SQLException {
        Postulation updatedPostulation = toModel(postulationReq);

        return postulationDAO.updatePostulation(postulationId, updatedPostulation);
    }

    public int patchPostulation(int postulationId, Map<String, Object> patchValues) throws SQLException, ValidationException, DatabaseOperationException {
        if (!validPatchFields(patchValues.keySet())) {
            logger.log(Level.WARNING, "⚠️ Fields not valid on Client request. Fields received: " + patchValues.keySet());
            throw new ValidationException("Los Campos a modificar no son válidos. Revisa los campos e inténtalo de nuevo. Campos recibidos: " + patchValues.keySet());
        }

        // Necesito que los valores de tipo BigDecimal se mapeen como BigDecimal y los Double como Int, no manejamos decimales en este caso
        patchValues.replaceAll((key, value) -> {
            if (value instanceof Double && (key.equals("salarioMinimo") || key.equals("salarioMaximo"))) {
                return BigDecimal.valueOf((double) value);
            }
            return value;
        });

        Map<String, Object[]> formattedPatchValues = formatKeys(patchValues);

        return postulationDAO.patchPostulation(postulationId, formattedPatchValues);
    }

    private boolean validPatchFields(Set<String> keySet) {
        return ALLOWED_PATCH_FIELDS.containsAll(keySet);
    }

    private Map<String, Object[]> formatKeys(Map<String, Object> patchValues) {
        Map<String, Object[]> formattedPatchValues = new HashMap<>();
        for (Map.Entry<String, Object> entrySet : patchValues.entrySet()) {
            String formattedKey =
                    Arrays.stream(entrySet.getKey().splitWithDelimiters("[A-Z][a-z]*", -1)).map(String::toLowerCase).collect(Collectors.joining("_")).replaceFirst("_$", "");

            AllowedFields field = AllowedFields.fromSqlField(formattedKey);

            if (field != null) {
                formattedPatchValues.put(field.getSqlField(), new Object[]{entrySet.getValue(), field.getFieldType().getSimpleName()});
            } else {
                throw new IllegalArgumentException("Este campo es invalido. Revisa campos e intentalo de nuevo.");
            }
        }

        return formattedPatchValues;
    }

    public int patchStatus(int postulationId, UpdateStatusRequest statusUpdateReq) throws SQLException, IllegalArgumentException, ValidationException,
            DatabaseOperationException {
        try {
            String statusValue = PostulationStatus.valueOf(statusUpdateReq.getEstatus()).name();

            return postulationDAO.patchStatus(postulationId, statusValue);
        } catch (NullPointerException e) {
            throw new ValidationException("El campo 'estatus' no esta presente, peticion invalida. Revisalo e intentalo de nuevo.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valor del campo 'estatus' es invalido. Revisalo e intentalo de nuevo. Valor recibido: '" + statusUpdateReq.getEstatus() + "'.");
        }
    }

    public int patchOrder(int postulationId, Map<String, Double> updateOrderReq) throws SQLException, ValidationException, ClassCastException {
        if(!updateOrderReq.containsKey("ordenKanban")) throw new ValidationException("El campo 'ordenKanban' no esta presente, peticion invalida. Reivsalo e " +
                "intentalo de nuevo");

        int orderValue = updateOrderReq.get("ordenKanban").intValue();

        System.out.println(orderValue);

        return postulationDAO.patchOrder(postulationId, orderValue);
    }

    public Postulation toModel(CreatePostulationRequest postulationReq) {
        return new Postulation(postulationReq.getUsuarioId(), postulationReq.getEmpresaId(), postulationReq.getRol(),
                PostulationStatus.valueOf(postulationReq.getEstatus()), postulationReq.getOrdenKanban(), postulationReq.getSalarioMinimo(),
                postulationReq.getSalarioMaximo(),
                postulationReq.getUbicacion(), postulationReq.isEsTelematico(), postulationReq.getOfertaUrl(), postulationReq.getNotaPostulacion(),
                postulationReq.getFechaPostulacion());
    }


}
