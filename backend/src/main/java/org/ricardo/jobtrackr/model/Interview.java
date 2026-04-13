package org.ricardo.jobtrackr.model;

import java.time.LocalDateTime;

public class Interview {
    private int entrevistaId;
    private int postulacionId;
    private int numeroRonda;
    private InterviewType tipoEntrevista;
    private LocalDateTime fechaEntrevista;
    private String entrevistador;
    private InterviewResult resultadoEntrevista;

    public Interview() {
    }

    public Interview(int postulacionId, int numeroRonda, InterviewType tipoEntrevista, LocalDateTime fechaEntrevista, String entrevistador, InterviewResult resultadoEntrevista) {
        this.postulacionId = postulacionId;
        this.numeroRonda = numeroRonda;
        this.tipoEntrevista = tipoEntrevista;
        this.fechaEntrevista = fechaEntrevista;
        this.entrevistador = entrevistador;
        this.resultadoEntrevista = resultadoEntrevista;
    }

    public Interview(int entrevistaId, int postulacionId, int numeroRonda, InterviewType tipoEntrevista, LocalDateTime fechaEntrevista, String entrevistador, InterviewResult resultadoEntrevista) {
        this.entrevistaId = entrevistaId;
        this.postulacionId = postulacionId;
        this.numeroRonda = numeroRonda;
        this.tipoEntrevista = tipoEntrevista;
        this.fechaEntrevista = fechaEntrevista;
        this.entrevistador = entrevistador;
        this.resultadoEntrevista = resultadoEntrevista;
    }

    public int getEntrevistaId() {
        return entrevistaId;
    }

    public int getPostulacionId() {
        return postulacionId;
    }

    public int getNumeroRonda() {
        return numeroRonda;
    }

    public InterviewType getTipoEntrevista() {
        return tipoEntrevista;
    }

    public LocalDateTime getFechaEntrevista() {
        return fechaEntrevista;
    }

    public String getEntrevistador() {
        return entrevistador;
    }

    public InterviewResult getResultadoEntrevista() {
        return resultadoEntrevista;
    }

    public void setEntrevistaId(int entrevistaId) {
        this.entrevistaId = entrevistaId;
    }
}
