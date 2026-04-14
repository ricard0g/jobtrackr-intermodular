package org.ricardo.jobtrackr.model;

public class PostulationTag {
    private int postulacionId;
    private int etiquetaId;

    public PostulationTag() {
    }

    public PostulationTag(int postulacionId, int etiquetaId) {
        this.postulacionId = postulacionId;
        this.etiquetaId = etiquetaId;
    }

    public int getPostulacionId() {
        return postulacionId;
    }

    public void setPostulacionId(int postulacionId) {
        this.postulacionId = postulacionId;
    }

    public int getEtiquetaId() {
        return etiquetaId;
    }

    public void setEtiquetaId(int etiquetaId) {
        this.etiquetaId = etiquetaId;
    }
}
