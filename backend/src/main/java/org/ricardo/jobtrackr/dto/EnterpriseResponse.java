package org.ricardo.jobtrackr.dto;

public record EnterpriseResponse(
        int empresaId,
        String nombreEmpresa,
        String logoEmpresa
) {}
