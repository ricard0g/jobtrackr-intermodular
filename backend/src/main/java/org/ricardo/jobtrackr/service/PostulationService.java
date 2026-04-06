package org.ricardo.jobtrackr.service;

import org.ricardo.jobtrackr.controller.PostulationController;
import org.ricardo.jobtrackr.exceptions.NotFoundException;
import org.ricardo.jobtrackr.model.Postulation;
import org.ricardo.jobtrackr.repository.PostulationRepository;

import java.sql.SQLException;
import java.util.List;

public class PostulationService {
    private final PostulationRepository postulationRepository = new PostulationRepository();

    public List<Postulation> getAllPostulations() throws SQLException {
        return postulationRepository.getAllPostulations().orElseThrow(() -> new NotFoundException("No hay postulaciones en la Base de Datos"));
    }
}
