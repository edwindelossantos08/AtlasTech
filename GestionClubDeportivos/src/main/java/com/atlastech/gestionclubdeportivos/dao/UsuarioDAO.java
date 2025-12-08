
package com.atlastech.gestionclubdeportivos.dao;

import com.atlastech.gestionclubdeportivos.databases.Databases;
import com.atlastech.gestionclubdeportivos.models.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AtlasTech
 */
public class UsuarioDAO {
private Connection connection;

    public UsuarioDAO() {
        this.connection = Databases.getConection();
    }

    // ============================================================
    // INSERTAR
    // ============================================================
    public boolean insertar(Usuario usuario) {

        String sql = "INSERT INTO USUARIO (Username, Email, Contrasena, TipoUsuario, Id_Socio, Estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, usuario.getNombreUsuario());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getContraseña());
            pstmt.setString(4, usuario.getTipoUsuario());

            if (usuario.getIdSocio() == null) {
                pstmt.setNull(5, Types.INTEGER);
            } else {
                pstmt.setInt(5, usuario.getIdSocio());
            }

            pstmt.setBoolean(6, usuario.isEstado());

            int filas = pstmt.executeUpdate();

            if (filas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) usuario.setId(rs.getInt(1));
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
        }

        return false;
    }

    // ============================================================
    // AUTENTICAR
    // ============================================================
    public Usuario autenticar(String username, String contraseña) {

        String sql = "SELECT * FROM USUARIO WHERE Username = ? AND Contrasena = ? AND Estado = 1";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, contraseña);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = mapearUsuario(rs);
                actualizarUltimoAcceso(usuario.getId());
                return usuario;
            }

        } catch (SQLException e) {
            System.err.println("Error al autenticar: " + e.getMessage());
        }

        return null;
    }

    // ============================================================
    // OBTENER TODOS
    // ============================================================
    public List<Usuario> obtenerTodos() {

        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT * FROM USUARIO ORDER BY Username";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) lista.add(mapearUsuario(rs));

        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }

        return lista;
    }

    // ============================================================
    // OBTENER POR ID
    // ============================================================
    public Usuario obtenerPorId(int id) {

        String sql = "SELECT * FROM USUARIO WHERE Id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) return mapearUsuario(rs);

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
        }

        return null;
    }

    // ============================================================
    // ACTUALIZAR
    // ============================================================
    public boolean actualizar(Usuario usuario) {

        String sql = "UPDATE USUARIO SET Username=?, Email=?, Contrasena=?, TipoUsuario=?, Id_Socio=?, Estado=? "
                   + "WHERE Id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNombreUsuario());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getContraseña());
            pstmt.setString(4, usuario.getTipoUsuario());

            if (usuario.getIdSocio() == null) {
                pstmt.setNull(5, Types.INTEGER);
            } else {
                pstmt.setInt(5, usuario.getIdSocio());
            }

            pstmt.setBoolean(6, usuario.isEstado());
            pstmt.setInt(7, usuario.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
        }

        return false;
    }

    // ============================================================
    // ELIMINAR
    // ============================================================
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

    // ============================================================
    // VERIFICAR SI EXISTE USERNAME
    // ============================================================
    public boolean existeNombreUsuario(String username) {

        String sql = "SELECT COUNT(*) FROM USUARIO WHERE Username = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.err.println("Error al verificar usuario: " + e.getMessage());
        }

        return false;
    }

    // ============================================================
    // ACTUALIZAR ÚLTIMO ACCESO
    // ============================================================
    private void actualizarUltimoAcceso(int idUsuario) {

        String sql = "UPDATE USUARIO SET Ultimo_Acceso = NOW() WHERE Id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar último acceso: " + e.getMessage());
        }
    }

    // ============================================================
    // MAPEO DE RESULTSET → OBJETO
    // ============================================================
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {

        Usuario u = new Usuario();

        u.setId(rs.getInt("Id"));
        u.setNombreUsuario(rs.getString("Username"));
        u.setEmail(rs.getString("Email"));
        u.setContraseña(rs.getString("Contrasena"));
        u.setTipoUsuario(rs.getString("TipoUsuario"));
        u.setIdSocio(rs.getObject("Id_Socio", Integer.class));
        u.setEstado(rs.getBoolean("Estado"));

        Timestamp fc = rs.getTimestamp("Fecha_Creacion");
        if (fc != null) u.setFechaCreacion(fc.toLocalDateTime());

        Timestamp ua = rs.getTimestamp("Ultimo_Acceso");
        if (ua != null) u.setUltimoAcceso(ua.toLocalDateTime());

        return u;
    }

}

