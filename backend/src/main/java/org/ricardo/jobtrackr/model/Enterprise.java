package org.ricardo.jobtrackr.model;

public class Enterprise {
    private int empresaId;
    private String nombreEmpresa;
    private String logoEmpresa;

    public Enterprise() {}

    public Enterprise(int empresaId, String nombreEmpresa, String logoEmpresa) {
        this.empresaId = empresaId;
        this.nombreEmpresa = nombreEmpresa;
        this.logoEmpresa = logoEmpresa;
    }

    public Enterprise(String nombreEmpresa, String logoEmpresa) {
        this.empresaId = empresaId;
        this.nombreEmpresa = nombreEmpresa;
        this.logoEmpresa = logoEmpresa;
    }

    public int getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(int empresaId) {
        this.empresaId = empresaId;
    }

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
