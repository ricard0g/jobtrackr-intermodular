package org.ricardo.jobtrackr;

import org.ricardo.jobtrackr.config.DatabaseConfig;
import org.ricardo.jobtrackr.config.ServerConfig;
import org.ricardo.jobtrackr.controller.EnterpriseController;
import org.ricardo.jobtrackr.controller.PostulationController;
import org.ricardo.jobtrackr.controller.TagController;
import org.ricardo.jobtrackr.controller.UserController;
import org.ricardo.jobtrackr.dao.*;
import org.ricardo.jobtrackr.service.EnterpriseService;
import org.ricardo.jobtrackr.service.PostulationService;
import org.ricardo.jobtrackr.service.TagService;
import org.ricardo.jobtrackr.service.UserService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        DatabaseConfig.setupDataSource();

        // Es mejor modularizar y desacoplar componentes para mejor manejo de dependencias. Aqui inyectamos las dependencias de cada componente
        // Instanciar DAOS
        UserDAO userDAO = new UserDAO();
        PostulationDAO postulationDAO = new PostulationDAO();
        InterviewDAO interviewDAO = new InterviewDAO();
        StatusHistoryDAO statusHistoryDAO = new StatusHistoryDAO();
        TagDAO tagDAO = new TagDAO();
        EnterpriseDAO enterpriseDAO = new EnterpriseDAO();

        //Instanciar Servicios que dependen de los DAOs
        UserService userService = new UserService(userDAO);
        PostulationService postulationService = new PostulationService(postulationDAO, statusHistoryDAO, interviewDAO);
        EnterpriseService enterpriseService = new EnterpriseService(enterpriseDAO);
        TagService tagService = new TagService(tagDAO);

        //Instanciar los Controllers que dependen de los servicios
        UserController userController = new UserController(userService);
        PostulationController postulationController = new PostulationController(postulationService);
        EnterpriseController enterpriseController = new EnterpriseController(enterpriseService);
        TagController tagController = new TagController(tagService);

        ServerConfig.start(userController, postulationController, enterpriseController, tagController);

        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseConfig::shutdownConnections)); // Añadir un hook (un thread) al Runtime para cerrar todas las
        // conexiones con la piscina de conexiones
    }
}
