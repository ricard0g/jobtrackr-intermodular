package org.ricardo.jobtrackr.service;

import org.ricardo.jobtrackr.exceptions.NotFoundException;
import org.ricardo.jobtrackr.model.User;
import org.ricardo.jobtrackr.dao.UserDAO;

import java.sql.SQLException;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public User getUser() throws SQLException {
        return userDAO.getUser().orElseThrow(() -> new NotFoundException("Usuario No Encontrado"));
    }
}
