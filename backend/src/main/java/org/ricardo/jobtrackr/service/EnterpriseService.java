package org.ricardo.jobtrackr.service;

import org.ricardo.jobtrackr.dto.CreateEnterpriseRequest;
import org.ricardo.jobtrackr.exceptions.NotFoundException;
import org.ricardo.jobtrackr.exceptions.ValidationException;
import org.ricardo.jobtrackr.interfaces.DtoMapper;
import org.ricardo.jobtrackr.model.Enterprise;
import org.ricardo.jobtrackr.dao.EnterpriseDAO;

import java.sql.SQLException;
import java.util.List;

public class EnterpriseService implements DtoMapper<Enterprise, CreateEnterpriseRequest> {
    private final EnterpriseDAO enterpriseDAO;

    public EnterpriseService(EnterpriseDAO enterpriseDAO) {
        this.enterpriseDAO = enterpriseDAO;
    }

    public List<Enterprise> getAllEnterprises() throws SQLException {
        return enterpriseDAO.getAllEnterprises().orElseThrow(() -> new NotFoundException("No se han encontrado Empresas."));
    }

    public int createEnterprise(CreateEnterpriseRequest createEnterpriseRequest) throws SQLException {
        if (createEnterpriseRequest.getNombreEmpresa() == null || createEnterpriseRequest.getLogoEmpresa() == null)
            throw new ValidationException("Los campos " +
                    "enviados son invalidos. Revisalo e intentalo de nuevo.");

        Enterprise newEnterprise = toModel(createEnterpriseRequest);

        return enterpriseDAO.createEnterprise(newEnterprise);
    }

    public Enterprise toModel(CreateEnterpriseRequest req) {
        return new Enterprise(req.getNombreEmpresa(), req.getLogoEmpresa());
    }
}
