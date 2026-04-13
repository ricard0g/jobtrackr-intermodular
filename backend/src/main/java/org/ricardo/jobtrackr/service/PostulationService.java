package org.ricardo.jobtrackr.service;

import org.ricardo.jobtrackr.dao.InterviewDAO;
import org.ricardo.jobtrackr.dao.StatusHistoryDAO;
import org.ricardo.jobtrackr.dto.CreateInterviewRequest;
import org.ricardo.jobtrackr.dto.CreatePostulationRequest;
import org.ricardo.jobtrackr.dto.UpdateStatusRequest;
import org.ricardo.jobtrackr.exceptions.DatabaseOperationException;
import org.ricardo.jobtrackr.exceptions.NotFoundException;
import org.ricardo.jobtrackr.exceptions.ValidationException;
import org.ricardo.jobtrackr.interfaces.DtoMapper;
import org.ricardo.jobtrackr.model.*;
import org.ricardo.jobtrackr.dao.PostulationDAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PostulationService implements DtoMapper<Postulation, CreatePostulationRequest> {
    private static final Logger logger = Logger.getLogger(PostulationService.class.getName());

    private static final Set<String> ALLOWED_PATCH_FIELDS = Set.of("empresaId", "rol", "salarioMinimo", "salarioMaximo", "ubicacion", "esTelematico",
            "ofertaUrl", "notaPostulacion", "fechaPostulacion");

    private final PostulationDAO postulationDAO = new PostulationDAO();
    private final StatusHistoryDAO statusHistoryDAO = new StatusHistoryDAO();
    private final InterviewDAO interviewDAO = new InterviewDAO();

    public List<Postulation> getAllPostulations() throws SQLException {
        return postulationDAO.getAllPostulations().orElseThrow(() -> new NotFoundException("No hay postulaciones en la Base de Datos"));
    }

    public Postulation findPostulationById(int postulationId) throws SQLException {
        return postulationDAO.findPostulationById(postulationId).orElseThrow(() -> new NotFoundException("No se ha encontrado Postulacion con" + " ID: " + postulationId));
    }

    public int deletePostulation(int postulationId) throws SQLException {
        return postulationDAO.deletePostulation(postulationId);
    }

    public int createPostulation(CreatePostulationRequest postulationReq) throws SQLException, DatabaseOperationException, IllegalArgumentException,
            ValidationException {
        try {
            Postulation newPostulation = toModel(postulationReq);

            int newPostulationId = postulationDAO.createPostulation(newPostulation);

            statusHistoryDAO.createStatusHistory(newPostulationId, newPostulation.getEstatus().name());

            return newPostulationId;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valor del campo 'estatus' es invalido. Revisalo e intentalo de nuevo. Valor recibido: '" + postulationReq.getEstatus() + "'.");
        } catch (NullPointerException e) {
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

            statusHistoryDAO.createStatusHistory(postulationId, statusValue);

            return postulationDAO.patchStatus(postulationId, statusValue);
        } catch (NullPointerException e) {
            throw new ValidationException("El campo 'estatus' no esta presente, peticion invalida. Revisalo e intentalo de nuevo.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valor del campo 'estatus' es invalido. Revisalo e intentalo de nuevo. Valor recibido: '" + statusUpdateReq.getEstatus() + "'.");
        }
    }

    public int patchOrder(int postulationId, Map<String, Double> updateOrderReq) throws SQLException, ValidationException, ClassCastException {
        if (!updateOrderReq.containsKey("ordenKanban"))
            throw new ValidationException("El campo 'ordenKanban' no esta presente, peticion invalida. Reivsalo e " + "intentalo de nuevo");

        int orderValue = updateOrderReq.get("ordenKanban").intValue();

        System.out.println(orderValue);

        return postulationDAO.patchOrder(postulationId, orderValue);
    }

    public List<StatusHistory> getAllStatusHistory(int postulationId) throws SQLException {
        return statusHistoryDAO.getAllStatusHistory(postulationId);
    }

    public List<Interview> getAllInterviews(int postulationId) throws SQLException {
        return interviewDAO.getAllInterviews(postulationId);
    }

    public int createInterview(int postulationId, CreateInterviewRequest interviewRequest) throws SQLException, DatabaseOperationException,
            IllegalArgumentException {
        if (interviewRequest.getPostulacionId() == 0 || interviewRequest.getNumeroRonda() == 0 || interviewRequest.getTipoEntrevista() == null || interviewRequest.getFechaEntrevista().isBlank() || interviewRequest.getEntrevistador().isBlank() || interviewRequest.getResultadoEntrevista() == null) {
            logger.log(Level.WARNING, "⚠️ Fields not valid on Client request.");
            throw new ValidationException("Los Campos a modificar no son válidos. Revisa los campos e inténtalo de nuevo.");
        }

        try {
            Interview newInterview = toInterview(interviewRequest);

            return interviewDAO.createInterview(postulationId, newInterview);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Los valores de uno o varios de los campos son invalidos. Revisalos e intentalo de nuevo.");
        }
    }

    public Interview findInterviewById(int interviewId) throws SQLException {
        return interviewDAO.findInterviewById(interviewId).orElseThrow(() -> new NotFoundException("No se ha encontrado ninguna entrevista con el ID: " + interviewId));
    }

    public int deleteInterview(int interviewId) throws SQLException {
        return interviewDAO.deleteInterview(interviewId);
    }

    public int updateInterview(int interviewId, CreateInterviewRequest interviewRequest) throws SQLException, DatabaseOperationException,ValidationException {
        if (interviewRequest.getPostulacionId() == 0 || interviewRequest.getNumeroRonda() == 0 || interviewRequest.getTipoEntrevista() == null || interviewRequest.getFechaEntrevista().isBlank() || interviewRequest.getEntrevistador().isBlank() || interviewRequest.getResultadoEntrevista() == null) {
            logger.log(Level.WARNING, "⚠️ Fields not valid on Client request.");
            throw new ValidationException("Los Campos a modificar no son válidos. Revisa los campos y valores que has pasado e inténtalo de nuevo.");
        }

        Interview updatedInterview = toInterview(interviewRequest);
        updatedInterview.setEntrevistaId(interviewId);

        return interviewDAO.updateInterview(updatedInterview);
    }

    public Interview toInterview(CreateInterviewRequest interviewRequest) {
        return new Interview(interviewRequest.getPostulacionId(), interviewRequest.getNumeroRonda(),
                InterviewType.valueOf(interviewRequest.getTipoEntrevista().name()), LocalDateTime.parse(interviewRequest.getFechaEntrevista(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), interviewRequest.getEntrevistador(),
                InterviewResult.valueOf(interviewRequest.getResultadoEntrevista().name()));
    }

    public Postulation toModel(CreatePostulationRequest postulationReq) {
        return new Postulation(postulationReq.getUsuarioId(), postulationReq.getEmpresaId(), postulationReq.getRol(),
                PostulationStatus.valueOf(postulationReq.getEstatus()), postulationReq.getOrdenKanban(), postulationReq.getSalarioMinimo(),
                postulationReq.getSalarioMaximo(), postulationReq.getUbicacion(), postulationReq.isEsTelematico(), postulationReq.getOfertaUrl(),
                postulationReq.getNotaPostulacion(), postulationReq.getFechaPostulacion());
    }
}
