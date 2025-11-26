package com.atlastech.gestionclubdeportivos.models;

import java.time.LocalDate;
/**
 *
 * @author Alexander
 */
public class InscripcionActividad {
    private int id;
    private int idSocio;
    private int idActividad;
    private LocalDate fechaInscripcion;
    private String estado;
    
    //Constructor
    public InscripcionActividad() {
        this.fechaInscripcion = LocalDate.now();
        this.estado = "activa";
    }
    
    //Constructor con parametros principales
      public InscripcionActividad(int idSocio, int idActividad) {
        this();
        this.idSocio = idSocio;
        this.idActividad = idActividad;
    }
      
    //Constructor completo
       public InscripcionActividad(int id, int idSocio, int idActividad, LocalDate fechaInscripcion, String estado) {
        this.id = id;
        this.idSocio = idSocio;
        this.idActividad = idActividad;
        this.fechaInscripcion = fechaInscripcion;
        this.estado = estado;
    }
       public boolean isActiva() {
        return "activa".equalsIgnoreCase(estado);
    }

    // Getters y Setters
    public int getId() {return id; }
    public void setId(int id) {this.id = id; }
    
    public int getIdSocio() {return idSocio; }
    public void setIdSocio(int idSocio) {this.idSocio = idSocio; }
    
    public int getIdActividad() {return idActividad; }
    public void setIdActividad(int idActividad) {this.idActividad = idActividad; }
    
    public LocalDate getFechaInscripcion() {return fechaInscripcion; }
    public void setFechaInscripcion(LocalDate fechaInscripcion) {this.fechaInscripcion = fechaInscripcion; }
    
    public String getEstado() {return estado; }
    public void setEstado(String estado) {this.estado = estado; }

    @Override
    public String toString() {
        return String.format("InscripcionActividad{id=%d, socio=%d, actividad=%d, estado=%s}",
                id, idSocio, idActividad, estado);
    }
    
}
