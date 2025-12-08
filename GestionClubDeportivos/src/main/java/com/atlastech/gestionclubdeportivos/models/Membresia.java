
package com.atlastech.gestionclubdeportivos.models;

import java.math.BigDecimal;
/**
 *
 * @author Alexander
 */
public class Membresia {
    
    private int id;
    private String tipo;
    private BigDecimal precio;
    private int duracionDias;
    private String descripcion;
    
    //Constructor vacio
    public Membresia() {}
    
    //Constructor con parametros principales
    public Membresia(String tipo, BigDecimal precio, int duracionDias) {
        this.tipo = tipo;
        this.precio = precio;
        this.duracionDias = duracionDias;
    }
    
    //Constructor completo
    public Membresia(int id, String tipo, BigDecimal precio, int duracionDias) {
        this.id = id;
        this.tipo = tipo;
        this.precio = precio;
        this.duracionDias = duracionDias;
    }
    
    //Metodos de negocio
     
    public BigDecimal getPrecioPorDia() {
        if (duracionDias == 0) return BigDecimal.ZERO;
        return precio.divide(BigDecimal.valueOf(duracionDias), 2, BigDecimal.ROUND_HALF_UP);
    }
    
    public String getTipoPreciodo() {
        if (duracionDias <= 30) return "Mensual";
        if (duracionDias <= 90) return "Trimestral";
        if (duracionDias <= 180) return "Semestral";
        return "Anual";      
    }
    
    //Getters y Setters
    public int getId() {return id; }
    public void setId(int id) {this.id = id; }
    
    public String getTipo() {return tipo; }
    public void setTipo(String tipo) {this.tipo = tipo; }
    
    public BigDecimal getPrecio() {return precio; }
    public void setPrecio(BigDecimal precio) {this.precio = precio; }
    
    public int getDuracionDias() {return duracionDias; }
    public void setDuracionDias(int duracionDias) {this.duracionDias = duracionDias; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return "Membresia{" + 
                "id=" + id + 
                ", tipo='" + tipo + '\'' +
                ", precio=" + precio +
                ", duracionDias=" + duracionDias +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
    
}

