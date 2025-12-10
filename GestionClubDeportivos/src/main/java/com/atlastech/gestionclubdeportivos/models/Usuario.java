
package com.atlastech.gestionclubdeportivos.models;
import java.time.LocalDateTime;

/**
 *
 * @author Alexander
 */
public class Usuario {
    private int id;
    private String nombreUsuario;  // Username
    private String email;          // Email
    private String contraseña;     // Contrasena
    private String tipoUsuario;    // TipoUsuario
    private Integer idSocio;       // Id_Socio
    private boolean estado;        // Estado
    private LocalDateTime fechaCreacion; // Fecha_Creacion
    private LocalDateTime ultimoAcceso;  // Ultimo_Acceso


    // CONSTRUCTORES
    public Usuario() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = true; // activo por defecto
    }

    public Usuario(String nombreUsuario, String email, String contraseña, String tipoUsuario, Integer idSocio) {
        this();
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.contraseña = contraseña;
        this.tipoUsuario = tipoUsuario;
        this.idSocio = idSocio;
    }

    
    // MÉTODOS DE NEGOCIO
    public boolean isAdministrador() {
        return "admin".equalsIgnoreCase(tipoUsuario)
            || "administrador".equalsIgnoreCase(tipoUsuario);
    }

    public boolean isSocio() {
        return "socio".equalsIgnoreCase(tipoUsuario);
    }

    
    // GETTERS Y SETTERS
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

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
        return String.format(
                "Usuario{id=%d, usuario='%s', email='%s', tipo='%s', estado=%s}",
                id, nombreUsuario, email, tipoUsuario, estado ? "Activo" : "Inactivo"
        );
    }
}
