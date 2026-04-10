package org.ricardo.jobtrackr.dto;

import org.ricardo.jobtrackr.model.PostulationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreatePostulationRequest {
    private int usuarioId;
    private int empresaId;
    private String rol;
    private String estatus;
    private int ordenKanban;
    private BigDecimal salarioMinimo;
    private BigDecimal salarioMaximo;
    private String ubicacion;
    private boolean esTelematico;
    private String ofertaUrl;
    private String notaPostulacion;
    private LocalDate fechaPostulacion;

    public CreatePostulationRequest() {
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public int getEmpresaId() {
        return empresaId;
    }

    public String getRol() {
        return rol;
    }

    public String getEstatus() {
        return estatus;
    }

    public int getOrdenKanban() {
        return ordenKanban;
    }

    public BigDecimal getSalarioMinimo() {
        return salarioMinimo;
    }

    public BigDecimal getSalarioMaximo() {
        return salarioMaximo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public boolean isEsTelematico() {
        return esTelematico;
    }

    public String getOfertaUrl() {
        return ofertaUrl;
    }

    public String getNotaPostulacion() {
        return notaPostulacion;
    }

    public LocalDate getFechaPostulacion() {
        return fechaPostulacion;
    }

}
