/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atlastech.gestionclubdeportivos.dao;
import com.atlastech.gestionclubdeportivos.databases.Databases;
import com.atlastech.gestionclubdeportivos.models.Suscripcion;

import java.sql.*;  
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Atlas_Tech
 */
public class SuscripcionDAO {
    private Connection connection;
    
    public SuscripcionDAO() {
        this.connection = (Connection) Databases.getInstance();
    }
    
    // CRUD BÁSICO
    
    /* Inserta una nueva suscripción */
    public boolean insertarSuscripcion(Suscripcion suscripcion) {
        String sql = "INSERT INTO SUSCRIPCION (Id_Socio, Id_Membresia, Renovacion_Automatica) " +
                     "VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, suscripcion.getIdSocio());
            pstmt.setInt(2, suscripcion.getIdMembresia());
            pstmt.setBoolean(3, suscripcion.isRenovacionAutomatica());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        suscripcion.setId(rs.getInt(1));
                    }
                }
                System.out.println("Suscripción registrada con ID: " + suscripcion.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar suscripción: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Obtiene todas las suscripciones */
    public List<Suscripcion> obtenerTodasSuscripciones() {
        List<Suscripcion> suscripciones = new ArrayList<>();
        String sql = "SELECT * FROM SUSCRIPCION ORDER BY Id DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                suscripciones.add(mapearResultSetASuscripcion(rs));
            }
            
            System.out.println("Se obtuvieron " + suscripciones.size() + " suscripciones");
            
        } catch (SQLException e) {
            System.err.println("Error al obtener suscripciones: " + e.getMessage());
            e.printStackTrace();
        }
        return suscripciones;
    }
    
    /* Obtiene una suscripción por su ID */
    public Suscripcion obtenerSuscripcionPorId(int id) {
        String sql = "SELECT * FROM SUSCRIPCION WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetASuscripcion(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar suscripción: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /* Actualiza una suscripción */
    public boolean actualizarSuscripcion(Suscripcion suscripcion) {
        String sql = "UPDATE SUSCRIPCION SET Id_Socio = ?, Id_Membresia = ?, " +
                     "Renovacion_Automatica = ? WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, suscripcion.getIdSocio());
            pstmt.setInt(2, suscripcion.getIdMembresia());
            pstmt.setBoolean(3, suscripcion.isRenovacionAutomatica());
            pstmt.setInt(4, suscripcion.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Suscripción actualizada correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar suscripción: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Elimina una suscripción */
    public boolean eliminarSuscripcion(int id) {
        String sql = "DELETE FROM SUSCRIPCION WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Suscripción eliminada correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar suscripción: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // MÉTODOS DE CONSULTA ESPECÍFICOS
    
    /* Obtiene todas las suscripciones activas */
    public List<Suscripcion> obtenerSuscripcionesActivas() {
        List<Suscripcion> suscripciones = new ArrayList<>();
        String sql = "SELECT * FROM SUSCRIPCION WHERE Renovacion_Automatica = 1 ORDER BY Id DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                suscripciones.add(mapearResultSetASuscripcion(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener suscripciones activas: " + e.getMessage());
            e.printStackTrace();
        }
        return suscripciones;
    }
    
    /* Obtiene suscripciones de un socio específico */
    public List<Suscripcion> obtenerSuscripcionesPorSocio(int idSocio) {
        List<Suscripcion> suscripciones = new ArrayList<>();
        String sql = "SELECT * FROM SUSCRIPCION WHERE Id_Socio = ? ORDER BY Id DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idSocio);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    suscripciones.add(mapearResultSetASuscripcion(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener suscripciones del socio: " + e.getMessage());
            e.printStackTrace();
        }
        return suscripciones;
    }
    
    /* Verifica si un socio tiene suscripción vigente */
    public boolean verificarSuscripcionVigente(int idSocio) {
        String sql = "SELECT COUNT(*) FROM SUSCRIPCION WHERE Id_Socio = ? AND Renovacion_Automatica = 1";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idSocio);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar suscripción vigente: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Renueva una suscripción (cambia estado de renovación automática) */
    public boolean renovarSuscripcion(int idSuscripcion) {
        String sql = "UPDATE SUSCRIPCION SET Renovacion_Automatica = 1 WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idSuscripcion);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Suscripción renovada correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al renovar suscripción: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Cancela la renovación automática de una suscripción */
    public boolean cancelarRenovacion(int idSuscripcion) {
        String sql = "UPDATE SUSCRIPCION SET Renovacion_Automatica = 0 WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idSuscripcion);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Renovación automática cancelada");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al cancelar renovación: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Obtiene suscripciones por tipo de membresía */
    public List<Suscripcion> obtenerSuscripcionesPorMembresia(int idMembresia) {
        List<Suscripcion> suscripciones = new ArrayList<>();
        String sql = "SELECT * FROM SUSCRIPCION WHERE Id_Membresia = ? ORDER BY Id DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idMembresia);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    suscripciones.add(mapearResultSetASuscripcion(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener suscripciones por membresía: " + e.getMessage());
            e.printStackTrace();
        }
        return suscripciones;
    }
    
    /* Cuenta suscripciones activas */
    public int contarSuscripcionesActivas() {
        String sql = "SELECT COUNT(*) FROM SUSCRIPCION WHERE Renovacion_Automatica = 1";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al contar suscripciones activas: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    /* Obtiene la suscripción activa de un socio */
    public Suscripcion obtenerSuscripcionActivaDeSocio(int idSocio) {
        String sql = "SELECT * FROM SUSCRIPCION WHERE Id_Socio = ? AND Renovacion_Automatica = 1 " +
                     "ORDER BY Id DESC LIMIT 1";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idSocio);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetASuscripcion(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener suscripción activa: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // MÉTODO AUXILIAR
    
    /* Mapea un ResultSet a un objeto Suscripcion */
    private Suscripcion mapearResultSetASuscripcion(ResultSet rs) throws SQLException {
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setId(rs.getInt("Id"));
        suscripcion.setIdSocio(rs.getInt("Id_Socio"));
        suscripcion.setIdMembresia(rs.getInt("Id_Membresia"));
        suscripcion.setRenovacionAutomatica(rs.getBoolean("Renovacion_Automatica"));
        return suscripcion;
    }
}
