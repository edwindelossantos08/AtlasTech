
package com.atlastech.gestionclubdeportivos.models;

/**
 *
 * @author Alexander
 */
public class Suscripcion {
    private int id;
    private int idSocio;
    private int idMembresia;
    private boolean renovacionAutomatica;
    
    //Constructor 
    public Suscripcion() {
        this.renovacionAutomatica = false;
    }
    
    //Constructor con parametros principales
    public Suscripcion(int idSocio, int idMembresia, boolean renocacionAutomatica) {
        this();
        this.idSocio = idSocio;
        this.idMembresia = idMembresia;
        this.renovacionAutomatica = renovacionAutomatica;
    }
    
    //Constructor completo
    public Suscripcion(int id, int idSocio, int idMembresia, boolean renovacionAutomatica){
        this.id = id;
        this.idSocio = idSocio;
        this.idMembresia = idMembresia;
        this.renovacionAutomatica = renovacionAutomatica;
    }
    
    //Getters y setters
    public int getId() {return id; }
    public void setId(int id) {this.id = id; }
    
    public int getIdSocio() {return idSocio; }
    public void setIdSocio(int idSocio) {this.idSocio = idSocio; }
    
    public int getIdMembresia() {return idMembresia; }
    public void setIdMembresia(int idMembresia) {this.idMembresia = idMembresia; }
    
    @Override
    public String toString () {
        return String.format("Suscripcion{id=%d, socio=%d, membresía=%d}",
                id, idSocio, idMembresia);
    }
}
