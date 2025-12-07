
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
    private String tipoUsuario; 
    private Integer idSocio;
    private boolean estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimoAcceso;

    public Usuario() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = true;
    }

    public Usuario(String nombreUsuario, String contraseña, String tipoUsuario, Integer idSocio) {
        this();
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.tipoUsuario = tipoUsuario;
        this.idSocio = idSocio;
    }

    // Métodos de negocio
    public boolean isAdministrador() {
        return "administrador".equalsIgnoreCase(tipoUsuario);
    }

    public boolean isSocio() {
        return "socio".equalsIgnoreCase(tipoUsuario);
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public Integer getIdSocio() { return idSocio; }
    public void setIdSocio(Integer idSocio) { this.idSocio = idSocio; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }

    @Override
    public String toString() {
        return String.format("Usuario{id=%d, usuario='%s', tipo='%s'}",
                id, nombreUsuario, tipoUsuario);
    }
    
}
