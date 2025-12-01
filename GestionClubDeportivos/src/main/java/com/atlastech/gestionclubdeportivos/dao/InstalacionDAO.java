/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atlastech.gestionclubdeportivos.dao;

import com.atlastech.gestionclubdeportivos.databases.Databases;
import com.atlastech.gestionclubdeportivos.models.Instalacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Atlas_Tech
 */

public class InstalacionDAO {
    private Connection connection;
    
    public InstalacionDAO() {
        this.connection = (Connection) Databases.getInstance();
    }
     
    /* Inserta una nueva instalación */
    public boolean insertarInstalacion(Instalacion instalacion) {
        String sql = "INSERT INTO INSTALACIONES (Nombre, Capacidad_Personas, Estado, Costo_Reserva) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, instalacion.getNombre());
            pstmt.setInt(2, instalacion.getCapacidadPersonas());
            pstmt.setString(3, instalacion.getEstado());
            pstmt.setBigDecimal(4, instalacion.getCostoReserva());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        instalacion.setId(rs.getInt(1));
                    }
                }
                System.out.println("Instalación registrada con ID: " + instalacion.getId());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar instalación: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Obtiene todas las instalaciones */
    public List<Instalacion> obtenerTodasInstalaciones() {
        List<Instalacion> instalaciones = new ArrayList<>();
        String sql = "SELECT * FROM INSTALACIONES ORDER BY Nombre";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                instalaciones.add(mapearResultSetAInstalacion(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener instalaciones: " + e.getMessage());
            e.printStackTrace();
        }
        return instalaciones;
    }
    
    /* Obtiene una instalación por ID */
    public Instalacion obtenerInstalacionPorId(int id) {
        String sql = "SELECT * FROM INSTALACIONES WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAInstalacion(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar instalación: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /* Actualiza una instalación */
    public boolean actualizarInstalacion(Instalacion instalacion) {
        String sql = "UPDATE INSTALACIONES SET Nombre = ?, Capacidad_Personas = ?, " +
                     "Estado = ?, Costo_Reserva = ? WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, instalacion.getNombre());
            pstmt.setInt(2, instalacion.getCapacidadPersonas());
            pstmt.setString(3, instalacion.getEstado());
            pstmt.setBigDecimal(4, instalacion.getCostoReserva());
            pstmt.setInt(5, instalacion.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Instalación actualizada correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar instalación: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Cambia el estado de una instalación */
    public boolean cambiarEstado(int id, String estado) {
        String sql = "UPDATE INSTALACIONES SET Estado = ? WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, estado);
            pstmt.setInt(2, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Estado de instalación actualizado a: " + estado);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Obtiene instalaciones disponibles */
    public List<Instalacion> obtenerInstalacionesDisponibles() {
        List<Instalacion> instalaciones = new ArrayList<>();
        String sql = "SELECT * FROM INSTALACIONES WHERE Estado = 'Disponible' ORDER BY Nombre";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                instalaciones.add(mapearResultSetAInstalacion(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener instalaciones disponibles: " + e.getMessage());
            e.printStackTrace();
        }
        return instalaciones;
    }
    
    /* Busca instalación por nombre */
    public Instalacion obtenerInstalacionPorNombre(String nombre) {
        String sql = "SELECT * FROM INSTALACIONES WHERE Nombre = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAInstalacion(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar por nombre: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /* Cuenta instalaciones activas */
    public int contarInstalacionesActivas() {
        String sql = "SELECT COUNT(*) FROM INSTALACIONES WHERE Estado = 'Disponible'";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar instalaciones: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    private Instalacion mapearResultSetAInstalacion(ResultSet rs) throws SQLException {
        Instalacion instalacion = new Instalacion();
        instalacion.setId(rs.getInt("Id"));
        instalacion.setNombre(rs.getString("Nombre"));
        instalacion.setCapacidadPersonas(rs.getInt("Capacidad_Personas"));
        instalacion.setEstado(rs.getString("Estado"));
        instalacion.setCostoReserva(rs.getBigDecimal("Costo_Reserva"));
        return instalacion;
    }
}
