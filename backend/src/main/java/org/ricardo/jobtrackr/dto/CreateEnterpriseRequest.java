package org.ricardo.jobtrackr.dto;

public class CreateEnterpriseRequest {
    private String nombreEmpresa;
    private String logoEmpresa;

    public CreateEnterpriseRequest() {}

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getLogoEmpresa() {
        return logoEmpresa;
    }

    public void setLogoEmpresa(String logoEmpresa) {
        this.logoEmpresa = logoEmpresa;
    }
}
