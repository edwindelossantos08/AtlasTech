/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atlastech.gestionclubdeportivos.dao;
import com.atlastech.gestionclubdeportivos.databases.Databases;
import com.atlastech.gestionclubdeportivos.models.Socio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Atlas_Tech
 */

public class SocioDAO {
     private Connection connection;
    
    public SocioDAO() {
        this.connection = (Connection) Databases.getInstance();
    }

    /* Inserta un nuevo socio en la base de datos */
    public boolean insertarSocio(Socio socio) {
        String sql = "INSERT INTO SOCIO (Nombres, Apellidos, Email, Telefono, " +
                     "Fecha_Nacimiento, Fecha_Registro, Estado, Direccion, Foto_URL) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, socio.getNombres());
            pstmt.setString(2, socio.getApellidos());
            pstmt.setString(3, socio.getEmail());
            pstmt.setString(4, socio.getTelefono());
            pstmt.setDate(5, Date.valueOf(socio.getFechaNacimiento()));
            pstmt.setDate(6, Date.valueOf(socio.getFechaRegistro()));
            pstmt.setBoolean(7, socio.isEstado());
            pstmt.setString(8, socio.getDireccion());
            pstmt.setString(9, socio.getFotoUrl());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        socio.setId(rs.getInt(1));
                    }
                }
                System.out.println("Socio insertado con ID: " + socio.getId());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar socio: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Obtiene todos los socios */
    public List<Socio> obtenerTodosSocios() {
        List<Socio> socios = new ArrayList<>();
        String sql = "SELECT * FROM SOCIO ORDER BY Apellidos, Nombres";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                socios.add(mapearResultSetASocio(rs));
            }
            
            System.out.println("Se obtuvieron " + socios.size() + " socios");
            
        } catch (SQLException e) {
            System.err.println("Error al obtener socios: " + e.getMessage());
            e.printStackTrace();
        }
        return socios;
    }
    
    /* Obtiene un socio por su ID */
    public Socio obtenerSocioPorId(int id) {
        String sql = "SELECT * FROM SOCIO WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetASocio(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar socio: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /* Actualiza los datos de un socio */
    public boolean actualizarSocio(Socio socio) {
        String sql = "UPDATE SOCIO SET Nombres = ?, Apellidos = ?, Email = ?, " +
                     "Telefono = ?, Estado = ?, Direccion = ?, Foto_URL = ? WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, socio.getNombres());
            pstmt.setString(2, socio.getApellidos());
            pstmt.setString(3, socio.getEmail());
            pstmt.setString(4, socio.getTelefono());
            pstmt.setBoolean(5, socio.isEstado());
            pstmt.setString(6, socio.getDireccion());
            pstmt.setString(7, socio.getFotoUrl());
            pstmt.setInt(8, socio.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Socio actualizado correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar socio: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Cambia el estado de un socio (activo/inactivo) */
    public boolean cambiarEstadoSocio(int id, boolean estado) {
        String sql = "UPDATE SOCIO SET Estado = ? WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, estado);
            pstmt.setInt(2, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("✅ Estado del socio cambiado a: " + (estado ? "ACTIVO" : "INACTIVO"));
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al cambiar estado: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Elimina físicamente un socio (NO RECOMENDADO - mejor usar cambiarEstadoSocio) */
    public boolean eliminarSocio(int id) {
        String sql = "DELETE FROM SOCIO WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("✅ Socio eliminado correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar socio: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // MÉTODOS DE BÚSQUEDA
    
    /* Busca socios por nombre o apellido */
    public List<Socio> buscarSociosPorNombre(String nombre) {
        List<Socio> socios = new ArrayList<>();
        String sql = "SELECT * FROM SOCIO WHERE Nombres LIKE ? OR Apellidos LIKE ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String busqueda = "%" + nombre + "%";
            pstmt.setString(1, busqueda);
            pstmt.setString(2, busqueda);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    socios.add(mapearResultSetASocio(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error en búsqueda: " + e.getMessage());
            e.printStackTrace();
        }
        return socios;
    }
    
    /* Obtiene solo los socios activos */
    public List<Socio> obtenerSociosActivos() {
        List<Socio> socios = new ArrayList<>();
        String sql = "SELECT * FROM SOCIO WHERE Estado = 1 ORDER BY Apellidos, Nombres";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                socios.add(mapearResultSetASocio(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener socios activos: " + e.getMessage());
            e.printStackTrace();
        }
        return socios;
    }
    
    // MÉTODOS ESPECIALIZADOS
    
    /* Verifica si un email ya está registrado */
    public boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM SOCIO WHERE Email = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar email: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Cuenta el total de socios activos */
    public int contarSociosActivos() {
        String sql = "SELECT COUNT(*) FROM SOCIO WHERE Estado = 1";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al contar socios: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    /* Obtiene socios con suscripción vigente */
    public List<Socio> obtenerSociosConSuscripcionVigente() {
        List<Socio> socios = new ArrayList<>();
        String sql = "SELECT s.* FROM SOCIO s " +
                     "INNER JOIN SUSCRIPCION su ON s.Id = su.Id_Socio " +
                     "WHERE su.Renovacion_Automatica = 1 " +
                     "AND s.Estado = 1";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                socios.add(mapearResultSetASocio(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener socios con suscripción: " + e.getMessage());
            e.printStackTrace();
        }
        return socios;
    }
    
    // MÉTODO AUXILIAR
    
    /* Mapea un ResultSet a un objeto Socio */
    private Socio mapearResultSetASocio(ResultSet rs) throws SQLException {
        Socio socio = new Socio();
        socio.setId(rs.getInt("Id"));
        socio.setNombres(rs.getString("Nombres"));
        socio.setApellidos(rs.getString("Apellidos"));
        socio.setEmail(rs.getString("Email"));
        socio.setTelefono(rs.getString("Telefono"));
        
        Date fechaNac = rs.getDate("Fecha_Nacimiento");
        if (fechaNac != null) {
            socio.setFechaNacimiento(fechaNac.toLocalDate());
        }
        
        Date fechaReg = rs.getDate("Fecha_Registro");
        if (fechaReg != null) {
            socio.setFechaRegistro(fechaReg.toLocalDate());
        }
        
        socio.setEstado(rs.getBoolean("Estado"));
        socio.setDireccion(rs.getString("Direccion"));
        socio.setFotoUrl(rs.getString("Foto_URL"));
        
        return socio;
    }
}
