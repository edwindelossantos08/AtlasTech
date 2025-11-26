package com.atlastech.gestionclubdeportivos.models;

import java.time.LocalDate;
/**
 *
 * @author Alexander
 */
public class PrestamoEquipo {
     private int id;
     private int idSocio;
     private int idEquipo;
     private LocalDate fechaPrestamo;
     private LocalDate fechaDevolucionEsperada;
     private LocalDate fechaDevolucionReal;
     private boolean estado;
     private String notas;
     
    //Constructor
    public PrestamoEquipo() {
        this.fechaPrestamo = LocalDate.now();
        this.estado = true;
    }

    //Constructor con parametros principales
    public PrestamoEquipo(int idSocio, int idEquipo, LocalDate fechaDevolucionEsperada) {
        this();
        this.idSocio = idSocio;
        this.idEquipo = idEquipo;
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
    }

    //Constructor completo
    public PrestamoEquipo(int id, int idSocio, int idEquipo, LocalDate fechaPrestamo,
                         LocalDate fechaDevolucionEsperada, LocalDate fechaDevolucionReal,
                         boolean estado, String notas) {
        this.id = id;
        this.idSocio = idSocio;
        this.idEquipo = idEquipo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.estado = estado;
        this.notas = notas;
    }

    public boolean isVencido() {
        if (fechaDevolucionReal != null) return false;
        return LocalDate.now().isAfter(fechaDevolucionEsperada);
    }

    public boolean isDevuelto() {
        return fechaDevolucionReal != null;
    }

    // Getters y Setters
    public int getId() {return id; }
    public void setId(int id) {this.id = id; }
    
    public int getIdSocio() {return idSocio; }
    public void setIdSocio(int idSocio) {this.idSocio = idSocio; }
    
    public int getIdEquipo() {return idEquipo; }
    public void setIdEquipo(int idEquipo) {this.idEquipo = idEquipo; }
    
    public LocalDate getFechaPrestamo() {return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) {this.fechaPrestamo = fechaPrestamo; }
    
    public LocalDate getFechaDevolucionEsperada() {return fechaDevolucionEsperada; }
    public void setFechaDevolucionEsperada(LocalDate fechaDevolucionEsperada) {this.fechaDevolucionEsperada = fechaDevolucionEsperada; }
    
    public LocalDate getFechaDevolucionReal() {return fechaDevolucionReal; }
    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal) {this.fechaDevolucionReal = fechaDevolucionReal; }
    
    public boolean isEstado() {return estado; }
    public void setEstado(boolean estado) {this.estado = estado; }
    
    public String getNotas() {return notas; }
    public void setNotas(String notas) {this.notas = notas; }

    @Override
    public String toString() {
        return String.format("PrestamoEquipo{id=%d, socio=%d, equipo=%d, vencido=%s}",
                id, idSocio, idEquipo, isVencido() ? "SÍ" : "NO");
    }
    
}
