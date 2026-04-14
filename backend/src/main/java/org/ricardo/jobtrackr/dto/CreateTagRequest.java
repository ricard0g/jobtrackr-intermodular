package org.ricardo.jobtrackr.dto;

public class CreateTagRequest {
    private String nombreEtiqueta;
    private String colorEtiqueta;

    public CreateTagRequest() {
    }

    public CreateTagRequest(String nombreEtiqueta, String colorEtiqueta) {
        this.nombreEtiqueta = nombreEtiqueta;
        this.colorEtiqueta = colorEtiqueta;
    }

    public String getNombreEtiqueta() {
        return nombreEtiqueta;
    }

    public String getColorEtiqueta() {
        return colorEtiqueta;
    }
}
