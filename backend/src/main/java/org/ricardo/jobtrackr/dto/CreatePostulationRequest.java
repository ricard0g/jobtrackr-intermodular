package org.ricardo.jobtrackr.dto;

import org.ricardo.jobtrackr.model.PostulationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreatePostulationRequest {
    private int usuarioId;
    private int empresaId;
    private String rol;
    private PostulationStatus estatus;
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

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(int empresaId) {
        this.empresaId = empresaId;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public PostulationStatus getEstatus() {
        return estatus;
    }

    public void setEstatus(PostulationStatus estatus) {
        this.estatus = estatus;
    }

    public int getOrdenKanban() {
        return ordenKanban;
    }

    public void setOrdenKanban(int ordenKanban) {
        this.ordenKanban = ordenKanban;
    }

    public BigDecimal getSalarioMinimo() {
        return salarioMinimo;
    }

    public void setSalarioMinimo(BigDecimal salarioMinimo) {
        this.salarioMinimo = salarioMinimo;
    }

    public BigDecimal getSalarioMaximo() {
        return salarioMaximo;
    }

    public void setSalarioMaximo(BigDecimal salarioMaximo) {
        this.salarioMaximo = salarioMaximo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean isEsTelematico() {
        return esTelematico;
    }

    public void setEsTelematico(boolean esTelematico) {
        this.esTelematico = esTelematico;
    }

    public String getOfertaUrl() {
        return ofertaUrl;
    }

    public void setOfertaUrl(String ofertaUrl) {
        this.ofertaUrl = ofertaUrl;
    }

    public String getNotaPostulacion() {
        return notaPostulacion;
    }

    public void setNotaPostulacion(String notaPostulacion) {
        this.notaPostulacion = notaPostulacion;
    }

    public LocalDate getFechaPostulacion() {
        return fechaPostulacion;
    }

    public void setFechaPostulacion(LocalDate fechaPostulacion) {
        this.fechaPostulacion = fechaPostulacion;
    }
}
