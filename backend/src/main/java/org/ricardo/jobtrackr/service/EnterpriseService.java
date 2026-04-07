package org.ricardo.jobtrackr.service;

import org.ricardo.jobtrackr.dto.CreateEnterpriseRequest;
import org.ricardo.jobtrackr.exceptions.NotFoundException;
import org.ricardo.jobtrackr.interfaces.DtoMapper;
import org.ricardo.jobtrackr.model.Enterprise;
import org.ricardo.jobtrackr.repository.EnterpriseRepository;

import java.sql.SQLException;
import java.util.List;

public class EnterpriseService implements DtoMapper<Enterprise, CreateEnterpriseRequest> {
    private final EnterpriseRepository enterpriseRepository = new EnterpriseRepository();

    public List<Enterprise> getAllEnterprises() throws SQLException {
        return enterpriseRepository.getAllEnterprises().orElseThrow(() -> new NotFoundException("No se han encontrado Empresas."));
    }

    public int createEnterprise(CreateEnterpriseRequest createEnterpriseRequest) throws SQLException {
        Enterprise newEnterprise = toModel(createEnterpriseRequest);

        return enterpriseRepository.createEnterprise(newEnterprise);
    }

    public Enterprise toModel(CreateEnterpriseRequest req) {
        Enterprise enterprise = new Enterprise();
        enterprise.setNombreEmpresa(req.getNombreEmpresa());
        enterprise.setLogoEmpresa(req.getLogoEmpresa());

        return enterprise;
    }
}
