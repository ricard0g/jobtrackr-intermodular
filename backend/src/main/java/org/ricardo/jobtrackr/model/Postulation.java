package org.ricardo.jobtrackr.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Postulation {
    private int postulacionId;
    private int usuarioId;
    private int empresaId;
    private String rol;
    private Enum<PostulationStatus> estatus;
    private int ordenKanban;
    private BigDecimal salarioMinimo;
    private BigDecimal salarioMaximo;
    private String ubicacion;
    private boolean esTelematico;
    private String ofertaUrl;
    private LocalDateTime creadaEn;
    private LocalDateTime actualizadaEn;
    private String notaPostulacion;
    private LocalDate fechaPostulacion;

    public Postulation() {}

    public int getPostulacionId() {
        return postulacionId;
    }

    public void setPostulacionId(int postulacionId) {
        this.postulacionId = postulacionId;
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

    public Enum<PostulationStatus> getEstatus() {
        return estatus;
    }

    public void setEstatus(Enum<PostulationStatus> estatus) {
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

    public LocalDateTime getCreadaEn() {
        return creadaEn;
    }

    public void setCreadaEn(LocalDateTime creadaEn) {
        this.creadaEn = creadaEn;
    }

    public LocalDateTime getActualizadaEn() {
        return actualizadaEn;
    }

    public void setActualizadaEn(LocalDateTime actualizadaEn) {
        this.actualizadaEn = actualizadaEn;
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
