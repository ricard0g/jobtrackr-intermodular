package org.ricardo.jobtrackr.dto;

import org.ricardo.jobtrackr.model.PostulationStatus;

import java.time.LocalDateTime;

public record StatusHistoryResponse(
        int estatusId,
        int postulacionId,
        PostulationStatus antiguoEstatus,
        PostulationStatus nuevoEstatus,
        LocalDateTime cambiadoEn,
        String notaEstatus
) {
}
