package org.ricardo.jobtrackr.model;

public class Tag {
    private int etiquetaId;
    private String nombreEtiqueta;
    private String colorEtiqueta;

    public Tag() {
    }

    public Tag(String nombreEtiqueta, String colorEtiqueta) {
        this.nombreEtiqueta = nombreEtiqueta;
        this.colorEtiqueta = colorEtiqueta;
    }

    public Tag(int etiquetaId, String nombreEtiqueta, String colorEtiqueta) {
        this.etiquetaId = etiquetaId;
        this.nombreEtiqueta = nombreEtiqueta;
        this.colorEtiqueta = colorEtiqueta;
    }

    public int getEtiquetaId() {
        return etiquetaId;
    }

    public void setEtiquetaId(int etiquetaId) {
        this.etiquetaId = etiquetaId;
    }

    public String getNombreEtiqueta() {
        return nombreEtiqueta;
    }

    public void setNombreEtiqueta(String nombreEtiqueta) {
        this.nombreEtiqueta = nombreEtiqueta;
    }

    public String getColorEtiqueta() {
        return colorEtiqueta;
    }

    public void setColorEtiqueta(String colorEtiqueta) {
        this.colorEtiqueta = colorEtiqueta;
    }
}
