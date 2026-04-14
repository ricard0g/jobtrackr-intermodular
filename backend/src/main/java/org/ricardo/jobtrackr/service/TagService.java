package org.ricardo.jobtrackr.service;

import org.ricardo.jobtrackr.dao.TagDAO;
import org.ricardo.jobtrackr.model.Tag;

import java.sql.SQLException;
import java.util.List;

public class TagService {
    private final TagDAO tagDAO = new TagDAO();

    public List<Tag> getAllTags() throws SQLException {
        return tagDAO.getAllTags();
    }
}
