package com.atlastech.gestionclubdeportivos.models;

import java.time.LocalDate;

/**
 *
 * @author Alexander
 */
public class Torneo {
    private int id;
    private int idDeporte;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean estado;
    private String categoria;
    
    //Constructor
     public Torneo() {
        this.estado = true;
    }
     
     //Constructor con parametros principales
     public Torneo(int idDeporte, String nombre, LocalDate fechaInicio, LocalDate fechaFin, String categoria) {
        this();
        this.idDeporte = idDeporte;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.categoria = categoria;
    }
     
     //Constructor completo
    public Torneo(int id, int idDeporte, String nombre, LocalDate fechaInicio, LocalDate fechaFin, boolean estado, String categoria) {
        this.id = id;
        this.idDeporte = idDeporte;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.categoria = categoria;
    }
    
     public boolean isActivo() {
        LocalDate hoy = LocalDate.now();
        return estado && !hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaFin);
    }
     
    // Getters y Setters
    public int getId() {return id; }
    public void setId(int id) {this.id = id; }
    
    public int getIdDeporte() {return idDeporte; }
    public void setIdDeporte(int idDeporte) {this.idDeporte = idDeporte; }
    
    public String getNombre() {return nombre; }
    public void setNombre(String nombre) {this.nombre = nombre; }
    
    public LocalDate getFechaInicio() {return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) {this.fechaInicio = fechaInicio; }
    
    public LocalDate getFechaFin() {return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) {this.fechaFin = fechaFin; }
    
    public boolean isEstado() {return estado; }
    public void setEstado(boolean estado) {this.estado = estado; }
    
    public String getCategoria() {return categoria; }
    public void setCategoria(String categoria) {this.categoria = categoria; }

    @Override
    public String toString() {
        return String.format("Torneo{id=%d, nombre='%s', categoria='%s'}",
                id, nombre, categoria);
    }
    
}
