
package com.atlastech.gestionclubdeportivos.models;

import java.time.LocalDateTime;
/**
 *
 * @author Alexander
 */
public class Usuario {
    private int id;
    private String nombreUsuario;
    private String contraseña;
    private String nombreCompleto;
    private String email;
    private String rol;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    
    //Constructor vacio 
    public Usuario(){
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
    }
    
    //Constructor con parametros principales
    public Usuario(String nombreUsuario, String contraseña, String nombreCompleto, String email, String rol){
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.rol = rol;
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    //Constructor completo
     //Constructor con parametros principales
    public Usuario(int id, String nombreUsuario, String contraseña, String nombreCompleto, String email, String rol, boolean activo, LocalDateTime fechaCreacion){
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.rol = rol;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }
    
    //Getters y Setters
    public int getId() {return id; }
    public void setId(int id) {this.id = id; }
    
    public String getNombreUsuario() {return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) {this.nombreUsuario = nombreUsuario; }
    
    public String getContraseña() {return contraseña; }
    public void setContraseña(String contraseña) {this.contraseña = contraseña; }
    
    public String getNombreCompleto() {return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) {this.nombreCompleto = nombreCompleto; }
    
    public String getEmail() {return email; }
    public void setEmail(String email) {this.email = email; }
    
    public String getRol() {return rol; }
    public void setRol(String rol) {this.rol = rol; }
    
    public boolean isActivo() {return activo; }
    public void setActivo(boolean activo) {this.activo = activo; }
    

    
}
