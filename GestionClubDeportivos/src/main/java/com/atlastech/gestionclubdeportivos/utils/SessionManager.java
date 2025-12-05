
package com.atlastech.gestionclubdeportivos.utils;

import com.atlastech.gestionclubdeportivos.models.Usuario;

/**
 *
 * @author AtlasTech
 */
public class SessionManager {
    private static SessionManager instance;
    private Usuario usuarioActual;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void iniciarSesion(Usuario usuario) {
        this.usuarioActual = usuario;
        String tipo = usuario.isAdministrador() ? "Administrador" : "Socio";
        System.out.println("Sesión iniciada como: " + tipo);
    }

    public void cerrarSesion() {
        if (usuarioActual != null) {
            System.out.println("Cerrando sesión...");
            this.usuarioActual = null;
        }
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public boolean haySesionActiva() {
        return usuarioActual != null;
    }

    public boolean esAdministrador() {
        return usuarioActual != null && usuarioActual.isAdministrador();
    }

    public boolean esSocio() {
        return usuarioActual != null && usuarioActual.isSocio();
    }

    public Integer getIdSocioActual() {
        return usuarioActual != null ? usuarioActual.getIdSocio() : null;
    }

    public String getTipoUsuarioActual() {
        if (usuarioActual == null) return "ninguno";
        return usuarioActual.getTipoUsuario();
    }

    public String getNombreUsuarioActual() {
        return usuarioActual != null ? usuarioActual.getNombreUsuario() : "Invitado";
    }
}
    

