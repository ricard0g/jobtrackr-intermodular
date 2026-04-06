package org.ricardo.jobtrackr.dto;

import org.ricardo.jobtrackr.model.PostulationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PostulationResponse(
        int postulacionId,
        int usuarioId,
        int empresaId,
        String rol,
        PostulationStatus estatus,
        int ordenKanban,
        BigDecimal salarioMinimo,
        BigDecimal salarioMaximo,
        String ubicacion,
        boolean esTelematico,
        String ofertaUrl,
        LocalDateTime creadaEn,
        LocalDateTime actualizadaEn,
        String notaPostulacion,
        LocalDate fechaPostulacion
) {
}
