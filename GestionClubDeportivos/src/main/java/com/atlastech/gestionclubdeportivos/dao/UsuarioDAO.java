
package com.atlastech.gestionclubdeportivos.dao;

import com.atlastech.gestionclubdeportivos.databases.Databases;
import com.atlastech.gestionclubdeportivos.models.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

 // DAO (Data Access Object) para la entidad {@link Usuario}.
 
 // Esta clase se encarga de gestionar todas las operaciones relacionadas con
 // Usuario en la base de datos, tales como insertar, actualizar, eliminar,
 // autenticar y obtener registros.
 //
 // Utiliza JDBC como tecnología de persistencia.
 
 // @author AtlasTech
 
public class UsuarioDAO {
    // Conexión activa hacia la base de datos. 
private Connection connection;

//Constructor encargado de obtener la conexión a la base de datos.
    public UsuarioDAO() {
        this.connection = Databases.getConection();
    }

    // Inserta un nuevo usuario en la base de datos.
     
     //@param usuario Objeto {@link Usuario} con los datos a guardar.
     //@return true si la operación fue exitosa; false en caso contrario.
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

   // Autentica un usuario validando username y contraseña.
    // @param username Nombre de usuario
    // @param contraseña Contraseña
    // @return Usuario si existe, null si no coincide
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

    // Obtiene la lista de todos los usuarios registrados.
    // @return Lista con objetos Usuario
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

    
    // Busca un usuario utilizando su ID.
    // @param id Identificador del usuario
    // @return Usuario si existe, null si no se encontró
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

    // Actualiza un usuario existente.
    // @param usuario Datos actualizados
    // @return true si se actualizó correctamente, false si falló
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

    // Elimina un usuario utilizando su ID.
    // @param id Identificador del usuario
    // @return true si se eliminó correctamente, false si no
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

    // Verifica si un username ya está registrado.
    // @param username Nombre de usuario
    // @return true si ya existe, false si está disponible
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

    // Actualiza la fecha y hora del último acceso del usuario.
    // @param idUsuario ID del usuario autenticado
    private void actualizarUltimoAcceso(int idUsuario) {

        String sql = "UPDATE USUARIO SET Ultimo_Acceso = NOW() WHERE Id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar último acceso: " + e.getMessage());
        }
    }
    
    // Convierte una fila de resultado (ResultSet) en un objeto Usuario.
    // @param rs ResultSet con los datos
    // @return Usuario construido
    // @throws SQLException si ocurre un error leyendo los datos
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

