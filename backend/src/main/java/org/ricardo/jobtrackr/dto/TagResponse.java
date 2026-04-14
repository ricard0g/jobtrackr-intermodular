package org.ricardo.jobtrackr.dto;

public record TagResponse(
        int etiquetaId,
        String nombreEtiqueta,
        String colorEtiqueta
) {
}
