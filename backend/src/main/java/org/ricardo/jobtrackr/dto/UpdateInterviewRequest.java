package org.ricardo.jobtrackr.dto;

import org.ricardo.jobtrackr.model.InterviewResult;
import org.ricardo.jobtrackr.model.InterviewType;

import java.time.LocalDateTime;

public class UpdateInterviewRequest {
    private int numeroRonda;
    private InterviewType tipoEntrevista;
    private LocalDateTime fechaEntrevista;
    private String entrevistador;
    private InterviewResult resultadoEntrevista;

    public UpdateInterviewRequest() {
    }

    public UpdateInterviewRequest(int numeroRonda, InterviewType tipoEntrevista, LocalDateTime fechaEntrevista, String entrevistador, InterviewResult resultadoEntrevista) {
        this.numeroRonda = numeroRonda;
        this.tipoEntrevista = tipoEntrevista;
        this.fechaEntrevista = fechaEntrevista;
        this.entrevistador = entrevistador;
        this.resultadoEntrevista = resultadoEntrevista;
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
}
