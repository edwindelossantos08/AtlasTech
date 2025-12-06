
package com.atlastech.gestionclubdeportivos.models;

import java.math.BigDecimal;
/**
 *
 * @author Alexander
 */
public class Instalacion {
    private int id;
    private String nombre;
    private int capacidadPersonas;
    private String estado;
    private BigDecimal costoReserva;
    
    //Constructor vacio
    public Instalacion() {
        this.estado = "disponible";
    }
    
    //Constructor con parametros principales
    public Instalacion(String nombre, int capacidadPersonas, BigDecimal costoReserva) {
        this();
        this.nombre = nombre;
        this.capacidadPersonas = capacidadPersonas;
        this.costoReserva = costoReserva;
    }
    
    //Constructor completo
    public Instalacion(int id, String nombre, int capacidadPersonas, String esatdo, BigDecimal costoReserva) {
        this.id = id;
        this.nombre = nombre;
        this.capacidadPersonas = capacidadPersonas;
        this.costoReserva = costoReserva;
    }
    
    //Metodos de negocio
    
    public boolean isDisponible() {
        return "disponible".equalsIgnoreCase(estado);
    }
    
    public String getEstadoFormatted() {
        switch (estado.toLowerCase()) {
            case "disponible": return "DISPONIBLE";
            case "mantenimiento": return "EN MANTENIMINETO";
            case "ocupada": return "OCUPADA";
            case "cerrada": return "CERRADA";
            default: return estado.toUpperCase();
        }
    }
    
    //Getters y Setters
    public int getId() {return id; }
    public void setId(int id) {this.id = id; }
    
    public String getNombre() {return nombre; }
    public void setNombre(String nombre) {this.nombre = nombre; }
    
    public int getCapacidadPersonas() {return capacidadPersonas; }
    public void setCapacidadPersonas(int capacidadPersonas) {this.capacidadPersonas = capacidadPersonas; }
    
    public BigDecimal getCostoReserva() {return costoReserva; }
    public void setCostoReserva(BigDecimal costoReserva) {this.costoReserva = costoReserva; }
    
    @Override
    public String toString() {
        return String.format("Instalacion{id=%d, nombre='%s', capacidad=%d, estado=%s}",
                id, nombre, capacidadPersonas, getEstadoFormatted());
    } 

    public String getEstado() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setEstado(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
