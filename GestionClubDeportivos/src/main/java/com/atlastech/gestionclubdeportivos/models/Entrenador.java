package com.atlastech.gestionclubdeportivos.models;
import java.time.LocalDate;

/**
 *
 * @author Alexander
 */
public class Entrenador {
    private int id;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private String especialidad;
    private LocalDate fechaContratacion;
    private boolean estado;
    
    //Constructor
    public Entrenador() {
        this.estado = true;
        this.fechaContratacion = LocalDate.now();
    }
    
    //Constructor con parametros principales
    public Entrenador(String nombres, String apellidos, String email, String telefono, String especialidad) {
        this();
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.especialidad = especialidad;
    }
    
    //Constructor completo
     public Entrenador(int id, String nombres, String apellidos, String email, String telefono, String especialidad, LocalDate fechaContratacion, boolean estado) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.especialidad = especialidad;
        this.fechaContratacion = fechaContratacion;
        this.estado = estado;
     }
     
     public String getNombreCompleto() {
         return nombres + " " + apellidos;
     }
     
     //Getters y Setters
     public int getId() { return id; }
     public void setId(int id) { this.id = id; }
     
     public String getNombres() {return nombres; }
     public void setNombres(String nombres) {this.nombres = nombres; }
     
     public String getApellidos() {return apellidos; }
     public void setApellidos(String apellidos) { this.apellidos = apellidos; }
     
     public String getEmail() {return email; }
     public void setEmail(String email) {this.email = email; }
     
     public String getTelefono() {return telefono; }
     public void setTelefono(String telefono) {this.telefono = telefono; }
     
     public String getEspecialidad() {return especialidad; }
     public void setEspecialidad(String especialidad) {this.especialidad = especialidad; }
     
     public LocalDate getFechaContratacion() {return fechaContratacion; }
     public void setFechaContratacion(LocalDate fechaContratacion) {this.fechaContratacion = fechaContratacion; }
     
     public boolean isEstado() {return estado; }
     public void setEstado(boolean estado) {this.estado = estado; }
            
     
    @Override
    public String toString() {
        return String.format("Entrenador{id=%d, nombre='%s', especialidad='%s'}",
                id, getNombreCompleto(), especialidad);
    
    }
}
