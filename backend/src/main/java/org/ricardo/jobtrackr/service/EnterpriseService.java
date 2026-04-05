package org.ricardo.jobtrackr.service;

import org.ricardo.jobtrackr.dto.CreateEnterpriseRequest;
import org.ricardo.jobtrackr.exceptions.NotFoundException;
import org.ricardo.jobtrackr.model.Enterprise;
import org.ricardo.jobtrackr.repository.EnterpriseRepository;

import java.sql.SQLException;
import java.util.List;

public class EnterpriseService {
    private final EnterpriseRepository enterpriseRepository = new EnterpriseRepository();

    public List<Enterprise> getAllEnterprises() throws SQLException {
        return enterpriseRepository.getAllEnterprises().orElseThrow(() -> new NotFoundException("No se han encontrado Empresas."));
    }

    public int createEnterprise(CreateEnterpriseRequest createEnterpriseRequest) throws SQLException {
        Enterprise newEnterprise = toEnterprise(createEnterpriseRequest);

        return enterpriseRepository.createEnterprise(newEnterprise);
    }

    private Enterprise toEnterprise(CreateEnterpriseRequest req) {
        Enterprise enterprise = new Enterprise();
        enterprise.setNombreEmpresa(req.getNombreEmpresa());
        enterprise.setLogoEmpresa(req.getLogoEmpresa());

        return enterprise;
    }
}
