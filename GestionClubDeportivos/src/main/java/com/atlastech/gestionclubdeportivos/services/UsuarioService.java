
package com.atlastech.gestionclubdeportivos.services;

import com.atlastech.gestionclubdeportivos.dao.UsuarioDAO;
import com.atlastech.gestionclubdeportivos.models.Usuario;


import java.util.List;

/**
 *
 * @author AtlasTech
 */
public class UsuarioService {
    /*private UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }*/

    /*public Usuario autenticar(String nombreUsuario, String contraseña) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            System.err.println("El nombre de usuario es obligatorio");
            return null;
        }

        if (contraseña == null || contraseña.trim().isEmpty()) {
            System.err.println("La contraseña es obligatoria");
            return null;
        }

        /*Usuario usuario = usuarioDAO.autenticar(nombreUsuario, contraseña);
        
        if (usuario != null) {
            System.out.println("Autenticación exitosa: " + usuario.getNombreCompleto());
        } else {
            System.err.println("Usuario o contraseña incorrectos");
        }
        
        return usuario;
    }

    public boolean registrarUsuario(Usuario usuario) {
        // Validaciones
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isEmpty()) {
            System.err.println("El nombre de usuario es obligatorio");
            return false;
        }

        if (usuario.getContraseña() == null || usuario.getContraseña().length() < 4) {
            System.err.println("La contraseña debe tener al menos 4 caracteres");
            return false;
        }

        if (usuarioDAO.existeNombreUsuario(usuario.getNombreUsuario())) {
            System.err.println("El nombre de usuario ya existe");
            return false;
        }

        return usuarioDAO.insertar(usuario);
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioDAO.obtenerTodos();
    }

    public Usuario buscarPorId(int id) {
        return usuarioDAO.obtenerPorId(id);
    }

    public boolean actualizarUsuario(Usuario usuario) {
        if (usuario.getId() <= 0) {
            System.err.println("ID de usuario inválido");
            return false;
        }
        return usuarioDAO.actualizar(usuario);
    }

    public boolean eliminarUsuario(int id) {
        // No permitir eliminar el admin principal
        Usuario usuario = usuarioDAO.obtenerPorId(id);
        if (usuario != null && "admin".equals(usuario.getNombreUsuario())) {
            System.err.println("No se puede eliminar el administrador principal");
            return false;
        }
        return usuarioDAO.eliminar(id);
    }*/
}


