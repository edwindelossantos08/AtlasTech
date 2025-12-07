
package com.atlastech.gestionclubdeportivos.models;
import java.time.LocalDate;

/**
 *
 * @author Alexander
 */
public class Suscripcion {
    private int id;
    private int idSocio;
    private int idMembresia;
    private boolean renovacionAutomatica;
    private LocalDate fechaInicio;
    private Integer duracionMeses;
    public Suscripcion() {
        this.renovacionAutomatica = false;
    }

    public Suscripcion(int idSocio, int idMembresia, boolean renovacionAutomatica) {
        this.idSocio = idSocio;
        this.idMembresia = idMembresia;
        this.renovacionAutomatica = renovacionAutomatica;
    }

    public Suscripcion(int id, int idSocio, int idMembresia, boolean renovacionAutomatica){
        this.id = id;
        this.idSocio = idSocio;
        this.idMembresia = idMembresia;
        this.renovacionAutomatica = renovacionAutomatica;
    }
    
public LocalDate calcularFechaFin() {
    if (this.fechaInicio == null) return null;
    Integer meses = this.duracionMeses; // puede ser null
    if (meses == null || meses <= 0) return null;
    return this.fechaInicio.plusMonths(meses);
}

public LocalDate getFechaInicio() { return fechaInicio; }
public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

public Integer getDuracionMeses() { return duracionMeses; }
public void setDuracionMeses(Integer duracionMeses) { this.duracionMeses = duracionMeses; }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdSocio() { return idSocio; }
    public void setIdSocio(int idSocio) { this.idSocio = idSocio; }

    public int getIdMembresia() { return idMembresia; }
    public void setIdMembresia(int idMembresia) { this.idMembresia = idMembresia; }

    public boolean isRenovacionAutomatica() { return renovacionAutomatica; }
    public void setRenovacionAutomatica(boolean renovacionAutomatica) {
        this.renovacionAutomatica = renovacionAutomatica;
    }

    @Override
    public String toString () {
        return String.format("Suscripcion{id=%d, socio=%d, membresía=%d, renovacion=%s}",
                id, idSocio, idMembresia, renovacionAutomatica);
    }
}