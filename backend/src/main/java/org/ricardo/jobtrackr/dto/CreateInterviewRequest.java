package org.ricardo.jobtrackr.dto;

import org.ricardo.jobtrackr.model.InterviewResult;
import org.ricardo.jobtrackr.model.InterviewType;

public class CreateInterviewRequest {
    private int postulacionId;
    private int numeroRonda;
    private InterviewType tipoEntrevista;
    private String fechaEntrevista;
    private String entrevistador;
    private InterviewResult resultadoEntrevista;

    public CreateInterviewRequest() {
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

    public String getFechaEntrevista() {
        return fechaEntrevista;
    }

    public String getEntrevistador() {
        return entrevistador;
    }

    public InterviewResult getResultadoEntrevista() {
        return resultadoEntrevista;
    }
}
