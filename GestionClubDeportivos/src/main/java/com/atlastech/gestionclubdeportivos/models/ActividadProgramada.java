package com.atlastech.gestionclubdeportivos.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Alexander
 */
public class ActividadProgramada {
    private int id;
    private int idInstalacion;
    private int idEntrenador;
    private int idDeporte;
    private String nombreActividad;
    private String nivel;
    private int cupoMax;
    private BigDecimal costoAdicional;
    private String estado;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    
    //Constructor
    public ActividadProgramada() {
        this.estado = "activa";
        this.costoAdicional = BigDecimal.ZERO;
    }
    
    //Constructor con parametros principales
    public ActividadProgramada(int idInstalacion, int idEntrenador, int idDeporte, String nombreActividad, String nivel, int cupoMax) {
        this();
        this.idInstalacion = idInstalacion;
        this.idEntrenador = idEntrenador;
        this.idDeporte = idDeporte;
        this.nombreActividad = nombreActividad;
        this.nivel = nivel;
        this.cupoMax = cupoMax;
    }
    
    //Constructor completo
     public ActividadProgramada(int id, int idInstalacion, int idEntrenador, int idDeporte, String nombreActividad, String nivel, int cupoMax, BigDecimal costoAdicional, String estado, String diaSemana, LocalTime horaInicio, LocalTime horaFin, LocalDate fechaInicio, LocalDate fechaFin) {
         
        this.id = id;
        this.idInstalacion = idInstalacion;
        this.idEntrenador = idEntrenador;
        this.idDeporte = idDeporte;
        this.nombreActividad = nombreActividad;
        this.nivel = nivel;
        this.cupoMax = cupoMax;
        this.costoAdicional = costoAdicional;
        this.estado = estado;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
     }
     
      public boolean isActiva() {
        return "activa".equalsIgnoreCase(estado);
    }
      
    // Getters y Setters
    public int getId() {return id; }
    public void setId(int id) {this.id = id; }
    
    public int getIdInstalacion() {return idInstalacion; }
    public void setIdInstalacion(int idInstalacion) {this.idInstalacion = idInstalacion; }
    
    public int getIdEntrenador() {return idEntrenador; }
    public void setIdEntrenador(int idEntrenador) {this.idEntrenador = idEntrenador; }
    
    public int getIdDeporte() {return idDeporte; }
    public void setIdDeporte(int idDeporte) {this.idDeporte = idDeporte; }
    
    public String getNombreActividad() {return nombreActividad; }
    public void setNombreActividad(String nombreActividad) {this.nombreActividad = nombreActividad; }
    
    public String getNivel() {return nivel; }
    public void setNivel(String nivel) {this.nivel = nivel; }
    
    public int getCupoMax() {return cupoMax; }
    public void setCupoMax(int cupoMax) {this.cupoMax = cupoMax; }
    
    public BigDecimal getCostoAdicional() {return costoAdicional; }
    public void setCostoAdicional(BigDecimal costoAdicional) {this.costoAdicional = costoAdicional; }
    
    public String getEstado() {return estado; }
    public void setEstado(String estado) {this.estado = estado; }
    
    public String getDiaSemana() {return diaSemana; }
    public void setDiaSemana(String diaSemana) {this.diaSemana = diaSemana; }
    
    public LocalTime getHoraInicio() {return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) {this.horaInicio = horaInicio; }
    
    public LocalTime getHoraFin() {return horaFin; }
    public void setHoraFin(LocalTime horaFin) {this.horaFin = horaFin; }
    
    public LocalDate getFechaInicio() {return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) {this.fechaInicio = fechaInicio; }
    
    public LocalDate getFechaFin() {return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) {this.fechaFin = fechaFin; }

    @Override
    public String toString() {
        return String.format("ActividadProgramada{id=%d, nombre='%s', nivel='%s', cupo=%d}",
                id, nombreActividad, nivel, cupoMax);
    }
}
