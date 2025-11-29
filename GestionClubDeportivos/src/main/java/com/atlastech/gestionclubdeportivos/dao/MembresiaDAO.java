/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atlastech.gestionclubdeportivos.dao;
import com.atlastech.gestionclubdeportivos.databases.Databases;
import com.atlastech.gestionclubdeportivos.models.Membresia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bryanpeguerocamilo
 */
public class MembresiaDAO {
    private Connection connection;
    
    public MembresiaDAO() {
        this.connection = (Connection) Databases.getInstance();
    }
    
    // CRUD BÁSICO
    
    /* Inserta una nueva membresía */
    public boolean insertarMembresia(Membresia membresia) {
        String sql = "INSERT INTO MEMBRESIA (Tipo, Precio, Duracion_Dias) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, membresia.getTipo());
            pstmt.setBigDecimal(2, membresia.getPrecio());
            pstmt.setInt(3, membresia.getDuracionDias());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        membresia.setId(rs.getInt(1));
                    }
                }
                System.out.println("Membresía registrada con ID: " + membresia.getId());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar membresía: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Obtiene todas las membresías */
    public List<Membresia> obtenerTodasMembresias() {
        List<Membresia> membresias = new ArrayList<>();
        String sql = "SELECT * FROM MEMBRESIA ORDER BY Precio";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                membresias.add(mapearResultSetAMembresia(rs));
            }
            
            System.out.println("Se obtuvieron " + membresias.size() + " membresías");
            
        } catch (SQLException e) {
            System.err.println("Error al obtener membresías: " + e.getMessage());
            e.printStackTrace();
        }
        return membresias;
    }
    
    /* Obtiene una membresía por su ID */
    public Membresia obtenerMembresiaPorId(int id) {
        String sql = "SELECT * FROM MEMBRESIA WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAMembresia(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar membresía: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /* Actualiza una membresía */
    public boolean actualizarMembresia(Membresia membresia) {
        String sql = "UPDATE MEMBRESIA SET Tipo = ?, Precio = ?, Duracion_Dias = ? WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, membresia.getTipo());
            pstmt.setBigDecimal(2, membresia.getPrecio());
            pstmt.setInt(3, membresia.getDuracionDias());
            pstmt.setInt(4, membresia.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Membresía actualizada correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar membresía: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Elimina una membresía (usar con precaución - puede afectar suscripciones) */
    public boolean eliminarMembresia(int id) {
        String sql = "DELETE FROM MEMBRESIA WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Membresía eliminada correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar membresía: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // MÉTODOS DE BÚSQUEDA Y CONSULTA
    
    /*Obtiene membresías disponibles (todas en este caso, */
    /* podrías agregar lógica de disponibilidad si la necesitas) */
    public List<Membresia> obtenerMembresiasDisponibles() {
        return obtenerTodasMembresias();
    }
    
    /* Busca una membresía por tipo */
    public Membresia obtenerMembresiaPorTipo(String tipo) {
        String sql = "SELECT * FROM MEMBRESIA WHERE Tipo = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, tipo);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAMembresia(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar membresía por tipo: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /* Obtiene membresías ordenadas por precio (de menor a mayor) */
    public List<Membresia> obtenerMembresiasOrdenadasPorPrecio() {
        List<Membresia> membresias = new ArrayList<>();
        String sql = "SELECT * FROM MEMBRESIA ORDER BY Precio ASC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                membresias.add(mapearResultSetAMembresia(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener membresías ordenadas: " + e.getMessage());
            e.printStackTrace();
        }
        return membresias;
    }
    
    /* Obtiene membresías por duración */
    public List<Membresia> obtenerMembresiasPorDuracion(int duracionDias) {
        List<Membresia> membresias = new ArrayList<>();
        String sql = "SELECT * FROM MEMBRESIA WHERE Duracion_Dias = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, duracionDias);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    membresias.add(mapearResultSetAMembresia(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener membresías por duración: " + e.getMessage());
            e.printStackTrace();
        }
        return membresias;
    }
    
    /* Cuenta el total de membresías registradas */
    public int contarMembresias() {
        String sql = "SELECT COUNT(*) FROM MEMBRESIA";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar membresías: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    /* Verifica si existe una membresía con un tipo específico */
    public boolean existeTipoMembresia(String tipo) {
        String sql = "SELECT COUNT(*) FROM MEMBRESIA WHERE Tipo = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, tipo);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar tipo de membresía: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // MÉTODO AUXILIAR
    
    /* Mapea un ResultSet a un objeto Membresia */
    private Membresia mapearResultSetAMembresia(ResultSet rs) throws SQLException {
        Membresia membresia = new Membresia();
        membresia.setId(rs.getInt("Id"));
        membresia.setTipo(rs.getString("Tipo"));
        membresia.setPrecio(rs.getBigDecimal("Precio"));
        membresia.setDuracionDias(rs.getInt("Duracion_Dias"));
        return membresia;
    }
}
