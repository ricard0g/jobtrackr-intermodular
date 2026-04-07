package org.ricardo.jobtrackr.service;

import org.ricardo.jobtrackr.controller.PostulationController;
import org.ricardo.jobtrackr.dto.CreatePostulationRequest;
import org.ricardo.jobtrackr.exceptions.DatabaseOperationException;
import org.ricardo.jobtrackr.exceptions.NotFoundException;
import org.ricardo.jobtrackr.interfaces.DtoMapper;
import org.ricardo.jobtrackr.model.Postulation;
import org.ricardo.jobtrackr.repository.PostulationRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class PostulationService implements DtoMapper<Postulation, CreatePostulationRequest> {
    private static final Logger logger = Logger.getLogger(PostulationService.class.getName());

    private final PostulationRepository postulationRepository = new PostulationRepository();

    public List<Postulation> getAllPostulations() throws SQLException {
        return postulationRepository.getAllPostulations().orElseThrow(() -> new NotFoundException("No hay postulaciones en la Base de Datos"));
    }

    public Postulation findPostulationById(int postulationId) throws SQLException {
        return postulationRepository.findPostulationById(postulationId).orElseThrow(() -> new NotFoundException("No se ha encontrado Postulacion con" +
                " ID: " + postulationId));
    }

    public int createPostulation(CreatePostulationRequest postulationReq) throws SQLException, DatabaseOperationException {
        Postulation newPostulation = toModel(postulationReq);

        logger.info("👉 New Postulation mapped: " + newPostulation);

        return postulationRepository.createPostulation(newPostulation);
    }

    public Postulation toModel(CreatePostulationRequest postulationReq) {
        return new Postulation(postulationReq.getUsuarioId(), postulationReq.getEmpresaId(), postulationReq.getRol(),
                postulationReq.getEstatus(), postulationReq.getOrdenKanban(), postulationReq.getSalarioMinimo(), postulationReq.getSalarioMaximo(),
                postulationReq.getUbicacion(), postulationReq.isEsTelematico(), postulationReq.getOfertaUrl(),  postulationReq.getNotaPostulacion(), postulationReq.getFechaPostulacion());
    }


}
