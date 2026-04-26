package org.ricardo.jobtrackr.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Postulation {
    private int postulacionId;
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
    private LocalDateTime creadaEn;
    private LocalDateTime actualizadaEn;
    private String notaPostulacion;
    private LocalDate fechaPostulacion;
    private List<Tag> listaEtiquetas;
    private Enterprise empresa;

    public Postulation() {}

    public Postulation(int postulacionId, int usuarioId, int empresaId, String rol, PostulationStatus estatus, int ordenKanban, BigDecimal salarioMinimo, BigDecimal salarioMaximo, String ubicacion, boolean esTelematico, String ofertaUrl, LocalDateTime creadaEn, LocalDateTime actualizadaEn, String notaPostulacion, LocalDate fechaPostulacion) {
        this.postulacionId = postulacionId;
        this.usuarioId = usuarioId;
        this.empresaId = empresaId;
        this.rol = rol;
        this.estatus = estatus;
        this.ordenKanban = ordenKanban;
        this.salarioMinimo = salarioMinimo;
        this.salarioMaximo = salarioMaximo;
        this.ubicacion = ubicacion;
        this.esTelematico = esTelematico;
        this.ofertaUrl = ofertaUrl;
        this.creadaEn = creadaEn;
        this.actualizadaEn = actualizadaEn;
        this.notaPostulacion = notaPostulacion;
        this.fechaPostulacion = fechaPostulacion;
    }

    public Postulation(int usuarioId, int empresaId, String rol, PostulationStatus estatus, int ordenKanban, BigDecimal salarioMinimo, BigDecimal salarioMaximo, String ubicacion, boolean esTelematico, String ofertaUrl, String notaPostulacion, LocalDate fechaPostulacion) {
        this.usuarioId = usuarioId;
        this.empresaId = empresaId;
        this.rol = rol;
        this.estatus = estatus;
        this.ordenKanban = ordenKanban;
        this.salarioMinimo = salarioMinimo;
        this.salarioMaximo = salarioMaximo;
        this.ubicacion = ubicacion;
        this.esTelematico = esTelematico;
        this.ofertaUrl = ofertaUrl;
        this.notaPostulacion = notaPostulacion;
        this.fechaPostulacion = fechaPostulacion;
    }

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

    public List<Tag> getListaEtiquetas() {
        return listaEtiquetas;
    }

    public void setListaEtiquetas(List<Tag> listaEtiquetas) {
        this.listaEtiquetas = listaEtiquetas;
    }

    public Enterprise getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Enterprise empresa) {
        this.empresa = empresa;
    }
}
