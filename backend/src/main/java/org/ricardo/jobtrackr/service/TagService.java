package org.ricardo.jobtrackr.service;

import org.ricardo.jobtrackr.dao.TagDAO;
import org.ricardo.jobtrackr.dto.CreateTagRequest;
import org.ricardo.jobtrackr.exceptions.DatabaseOperationException;
import org.ricardo.jobtrackr.exceptions.ValidationException;
import org.ricardo.jobtrackr.interfaces.DtoMapper;
import org.ricardo.jobtrackr.model.Tag;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TagService implements DtoMapper<Tag, CreateTagRequest> {
    private static final Logger logger = Logger.getLogger(TagService.class.getName());

    private final TagDAO tagDAO;

    public TagService(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    public List<Tag> getAllTags() throws SQLException {
        return tagDAO.getAllTags();
    }

    public int createTag(CreateTagRequest tagRequest) throws SQLException, DatabaseOperationException, IllegalArgumentException, ValidationException {
        if (tagRequest.getNombreEtiqueta() == null || tagRequest.getColorEtiqueta() == null) {
            logger.log(Level.WARNING, "⚠️ Fields not valid on Client request.");
            throw new ValidationException("Los Campos a modificar no son válidos. Revisa los campos e inténtalo de nuevo.");
        }

        try {
            Tag newTag = toModel(tagRequest);

            return tagDAO.createTag(newTag);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Los valores de uno o varios de los campos son invalidos. Revisalos e intentalo de nuevo.");
        }
    }

    public Tag toModel(CreateTagRequest tagRequest) {
        return new Tag(tagRequest.getNombreEtiqueta(), tagRequest.getColorEtiqueta());
    }
}
