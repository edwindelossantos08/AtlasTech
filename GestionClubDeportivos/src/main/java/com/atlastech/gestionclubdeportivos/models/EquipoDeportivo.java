package com.atlastech.gestionclubdeportivos.models;

/**
 *
 * @author Alexander
 */
public class EquipoDeportivo {
    private int id;
    private int idInstalacion;
    private String nombre;
    private int cantidadTotal;
    private int cantidadDisponible;
    private boolean estado;
    
    //Constructor 
    public EquipoDeportivo() {
        this.estado = true;
    }
    
    //Constructor con parametros principales
    public EquipoDeportivo(int idInstalacion, String nombre, int cantidadTotal, int cantidadDisponible) {
        this();
        this.idInstalacion = idInstalacion;
        this.nombre = nombre;
        this.cantidadTotal = cantidadTotal;
        this.cantidadDisponible = cantidadTotal;
    }
    
    //Constructor completo
    public EquipoDeportivo(int id, int idInstalacion, String nombre, int cantidadTotal, int cantidadDisponible, boolean estado) {
        this.id = id;
        this.idInstalacion = idInstalacion;
        this.nombre = nombre;
        this.cantidadTotal = cantidadTotal;
        this.cantidadDisponible = cantidadDisponible;
        this.estado = estado;
    }
    
    //Metodo de negocio
    public boolean hayDisponibilidad() {
        return cantidadDisponible > 0 && estado;
    }
    
    public int getCantidadPrestada() {
        return cantidadTotal - cantidadDisponible;
    }
    
    // Getters y Setters
    public int getId() {return id; }
    public void setId(int id) {this.id = id; }
    
    public int getIdInstalacion() {return idInstalacion; }
    public void setIdInstalacion(int idInstalacion) {this.idInstalacion = idInstalacion; }
    
    public String getNombre() {return nombre; }
    public void setNombre(String nombre) {this.nombre = nombre; }
    
    public int getCantidadTotal() {return cantidadTotal; }
    public void setCantidadTotal(int cantidadTotal) {this.cantidadTotal = cantidadTotal; }
    
    public int getCantidadDisponible() {return cantidadDisponible; }
    public void setCantidadDisponible(int cantidadDisponible) {this.cantidadDisponible = cantidadDisponible; }
    
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    @Override
    public String toString() {
        return String.format("EquipoDeportivo{id=%d, nombre='%s', disponible=%d/%d}",
                id, nombre, cantidadDisponible, cantidadTotal);
    }
    
}
