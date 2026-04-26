package org.ricardo.jobtrackr.dto;

import org.ricardo.jobtrackr.model.Enterprise;
import org.ricardo.jobtrackr.model.PostulationStatus;
import org.ricardo.jobtrackr.model.Tag;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
        LocalDate fechaPostulacion,
        List<Tag> tagList,
        Enterprise empresa
        ) {
}
