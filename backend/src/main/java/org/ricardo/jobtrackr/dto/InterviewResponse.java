package org.ricardo.jobtrackr.dto;

import org.ricardo.jobtrackr.model.InterviewResult;
import org.ricardo.jobtrackr.model.InterviewType;

import java.time.LocalDateTime;

public record InterviewResponse(
        int entrevistaId,
        int postulacionId,
        int numeroRonda,
        InterviewType tipoEntrevista,
        LocalDateTime fechaEntrevista,
        String entrevistador,
        InterviewResult resultadoEntrevista
) {
}
