package com.atlastech.gestionclubdeportivos.models;

/**
 *
 * @author Alexander
 */
public class Deporte {
    private int id;
    private String nombre;
    private String descripcion; 


//Constructor vacio
public Deporte() {}

//Constructor con parametros principales
public Deporte(String nombre, String descripcion) {
    this.nombre = nombre;
    this.descripcion = descripcion;
}

//Constructor completo
public Deporte(int id,String nombre, String descripcion) {
    this.id = id;
    this.nombre = nombre;
    this.descripcion = descripcion;
}

//Getters y Setters
public int getId() {return id; }
public void setId(int id) {this.id = id; }

public String getNombre() { return nombre; }
public void setNombre(String nombre) { this.nombre = nombre; }

public String getDescripcion() { return descripcion; }
public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

@Override
    public String toString() {
        return String.format("Deporte{id=%d, nombre='%s'}", id, nombre);
    }

}