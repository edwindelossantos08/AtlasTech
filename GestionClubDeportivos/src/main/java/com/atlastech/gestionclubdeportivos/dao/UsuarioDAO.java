
package com.atlastech.gestionclubdeportivos.dao;

import com.atlastech.gestionclubdeportivos.databases.Databases;
import com.atlastech.gestionclubdeportivos.models.Usuario;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AtlasTech
 */
public class UsuarioDAO {
    private Connection connection;

    public UsuarioDAO() {
        this.connection = Databases.getInstance().getConnection();
    }

    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO USUARIO (Nombre_Usuario, Contraseña, Tipo_Usuario, Id_Socio, Estado) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, usuario.getNombreUsuario());
            pstmt.setString(2, usuario.getContraseña());
            pstmt.setString(3, usuario.getTipoUsuario());
            pstmt.setString(4, usuario.getIdSocio(), Types.INTEGER);
            pstmt.setBoolean(5, usuario.isEstado());
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
        }
        return false;
    }

    public Usuario autenticar(String nombreUsuario, String contrasena) {
        String sql = "SELECT * FROM USUARIO WHERE Nombre_Usuario = ? AND Contraseña = ? AND Estado = 1";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombreUsuario);
            pstmt.setString(2, contraseña);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = mapearUsuario(rs);
                    // Actualizar último acceso
                    actualizarUltimoAcceso(usuario.getId());
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al autenticar: " + e.getMessage());
        }
        return null;
    }

    public List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO ORDER BY Nombre_Completo";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public Usuario obtenerPorId(int id) {
        String sql = "SELECT * FROM USUARIO WHERE Id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }
    
    public Usuario obtenerPorIdSocio(int idSocio) {
        String sql = "SELECT * FROM USUARIO WHERE Id_Socio = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idSocio);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por socio: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE USUARIO SET Nombre_Usuario = ?, Contraseña = ?, " +
                     "Tipo_Usuario = ?, Id_Socio = ?, Estado = ? WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombreUsuario());
            pstmt.setString(2, usuario.getContraseña());
            pstmt.setString(3, usuario.getTipoUSuario());
            pstmt.setString(4, usuario.getIdSocio(), Types.INTEGER);
            pstmt.setBoolean(5, usuario.isEstado());
            pstmt.setInt(6, usuario.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM USUARIO WHERE Id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
        return false;
    }

    public boolean existeNombreUsuario(String nombreUsuario) {
        String sql = "SELECT COUNT(*) FROM USUARIO WHERE Nombre_Usuario = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombreUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar usuario: " + e.getMessage());
        }
        return false;
    }

    private void actualizarUltimoAcceso(int idUsuario) {
        String sql = "UPDATE USUARIO SET Ultimo_Acceso = GETDATE() WHERE Id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar último acceso: " + e.getMessage());
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("Id"));
        usuario.setNombreUsuario(rs.getString("Nombre_Usuario"));
        usuario.setContraseña(rs.getString("Contraseña"));
        usuario.setTipoUsuario(rs.getString("Tipo_Usuario"));
        
        Integer idSocio = rs.getObject("Id_Socio", Integer.class);
        usuario.setIdSocio(idSocio);
        
        usuario.setEstado(rs.getBoolean("Estado"));
        
        Timestamp fechaCreacion = rs.getTimestamp("Fecha_Creacion");
        if (fechaCreacion != null) {
            usuario.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        
        Timestamp ultimoAcceso = rs.getTimestamp("Ultimo_Acceso");
        if (ultimoAcceso != null) {
            usuario.setUltimoAcceso(ultimoAcceso.toLocalDateTime());
        }
        
        return usuario;
    }
}

