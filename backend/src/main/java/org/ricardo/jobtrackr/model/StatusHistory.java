package org.ricardo.jobtrackr.model;

import java.time.LocalDateTime;

public class StatusHistory {
    private int estatusId;
    private int postulacionId;
    private PostulationStatus antiguoEstatus;
    private PostulationStatus nuevoEstatus;
    private LocalDateTime cambiadoEn;
    private String notaPostulacion;

    public StatusHistory() {
    }

    public StatusHistory(int estatusId, int postulacionId, PostulationStatus antiguoEstatus, PostulationStatus nuevoEstatus, LocalDateTime cambiadoEn, String notaPostulacion) {
        this.estatusId = estatusId;
        this.postulacionId = postulacionId;
        this.antiguoEstatus = antiguoEstatus;
        this.nuevoEstatus = nuevoEstatus;
        this.cambiadoEn = cambiadoEn;
        this.notaPostulacion = notaPostulacion;
    }

    public int getEstatusId() {
        return estatusId;
    }

    public void setEstatusId(int estatusId) {
        this.estatusId = estatusId;
    }

    public int getPostulacionId() {
        return postulacionId;
    }

    public void setPostulacionId(int postulacionId) {
        this.postulacionId = postulacionId;
    }

    public PostulationStatus getAntiguoEstatus() {
        return antiguoEstatus;
    }

    public void setAntiguoEstatus(PostulationStatus antiguoEstatus) {
        this.antiguoEstatus = antiguoEstatus;
    }

    public PostulationStatus getNuevoEstatus() {
        return nuevoEstatus;
    }

    public void setNuevoEstatus(PostulationStatus nuevoEstatus) {
        this.nuevoEstatus = nuevoEstatus;
    }

    public LocalDateTime getCambiadoEn() {
        return cambiadoEn;
    }

    public void setCambiadoEn(LocalDateTime cambiadoEn) {
        this.cambiadoEn = cambiadoEn;
    }

    public String getNotaPostulacion() {
        return notaPostulacion;
    }

    public void setNotaPostulacion(String notaPostulacion) {
        this.notaPostulacion = notaPostulacion;
    }
}
