package org.ricardo.jobtrackr.service;

import org.ricardo.jobtrackr.exceptions.NotFoundException;
import org.ricardo.jobtrackr.model.User;
import org.ricardo.jobtrackr.repository.UserRepository;

import java.sql.SQLException;

public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public User getUser() throws SQLException {
        return userRepository.getUser().orElseThrow(() -> new NotFoundException("Usuario No Encontrado"));
    }
}
