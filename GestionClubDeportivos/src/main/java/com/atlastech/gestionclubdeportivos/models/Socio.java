
package com.atlastech.gestionclubdeportivos.models;

import java.time.LocalDate;
import java.time.Period;

/**
 *
 * @author Alexander
 */
public class Socio {
   
    private int id;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private boolean estado;
    private String direccion;
    private String fotoUrl;
    private LocalDate fechaRegistro;
    private LocalDate fechaNacimiento;
    
    
    
    //Constructor vacio
    public Socio() {
        this.fechaRegistro = LocalDate.now();
        this.estado = true;
    }
    
    //Constructor con parametros principales
    public Socio(String nombres, String apellidos, String email, String telefono, LocalDate fechaNacimiento){
        
        this();
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;     
    }
    
    //Constructor completo
    public Socio(int id, String nombres, String apellidos, String email, String telefono, LocalDate fechaNacimiento, LocalDate fechaRegistro, boolean estado, String direccion, String fotoUrl) {
        
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
        this.direccion = direccion;
        this.fotoUrl = fotoUrl;
    }
    
    //Metodos de negocio
    
    /**
     * Calculate the partner's age
     * @return age in years
     */
    public int calcularEdad() {
        if (fechaNacimiento == null) {
            return 0;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
    
    /**
     * Gets the partner's full name
     * @return concatenated first and last names
    */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
    
    /**
     * Determine the category according to age
     * @return  age category
    */
    public String obtenerCategoria() {
        int edad = calcularEdad();
        if (edad < 13) return "Infantil";
        if (edad < 18) return "Juvenil";
        if (edad < 60) return "Adulto";
        return "Senior";
    }
    
    public long getMesesAntiguedad(){
        if (fechaRegistro == null) return 0;
        return Period.between(fechaRegistro, LocalDate.now()).toTotalMonths();
    }
    
    //Getters y Setters 
    public int getId() {return id; }
    public void setId(int id) {this.id = id; }
    
    public String getNombres() {return nombres; }
    public void setNombres(String nombres) {this.nombres = nombres; }
    
    public String getApellidos() {return apellidos; }
    public void setApellidos(String apellidos) {this.apellidos = apellidos; }
    
    public String getEmail() {return email; }
    public void setEmail(String email) {this.email = email; }
    
    public String getTelefono() {return telefono; }
    public void setTelefono(String telefono) {this.telefono = telefono; }
    
    public LocalDate getFechaNacimiento() {return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {this.fechaNacimiento = fechaNacimiento; }
    
    public LocalDate getFechaRegistro() {return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) {this.fechaRegistro = fechaRegistro; }
    
    public boolean isEstado() {return estado; }
    public void setEstado(boolean estado) {this.estado = estado; }
    
    public String getDireccion() {return direccion; }
    public void setDireccion(String direccion) {this.direccion = direccion; }
    
    public String getFotoUrl() {return fotoUrl; }
    public void setFotoUrl(String FotoUrl) {this.fotoUrl = fotoUrl; }
    
    @Override
    public String toString() {
        return String.format("Socio{id=%id, nombre='%s', email='%s', estado=%s}",
                id,
                getNombreCompleto(),
                email,
                estado ? "ACTIVO" : "INACTIVO");
    }
}
