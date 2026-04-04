package org.ricardo.jobtrackr.model;

public enum PostulationStatus {
    POSTULADA("POSTULADA"), REVISION("REVISION"), ENTREVISTA("ENTREVISTA"), OFERTA("OFERTA"), DESCARTADA("DESCARTADA"), RETIRADA("RETIRADA");

    private final String status;

    PostulationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
