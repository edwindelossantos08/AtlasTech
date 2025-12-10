/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atlastech.gestionclubdeportivos.dao;

import com.atlastech.gestionclubdeportivos.databases.Databases;
import com.atlastech.gestionclubdeportivos.models.Deporte;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Atlas_Tech
 */

public class DeporteDAO {
    private Connection connection;
    
    public DeporteDAO() {
        this.connection = Databases.getConection();
    }
    
      // Inserta un nuevo deporte en la tabla DEPORTE
    public boolean insertarDeporte(Deporte deporte) {
        String sql = "INSERT INTO DEPORTE (Nombre, Descripcion) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, deporte.getNombre());
            pstmt.setString(2, deporte.getDescripcion());
            int affectedRows = pstmt.executeUpdate();
            
            // Si se insertó correctamente, se obtiene el ID generado
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) deporte.setId(rs.getInt(1));
                System.out.println("Deporte registrado: " + deporte.getNombre());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar deporte: " + e.getMessage());
        }
        return false;
    }
    
      // Retorna lista con todos los deportes registrados
    public List<Deporte> obtenerTodosDeportes() {
        List<Deporte> deportes = new ArrayList<>();
        String sql = "SELECT * FROM DEPORTE ORDER BY Nombre";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Mapeo fila por fila a objeto Deporte
            while (rs.next()) {
                deportes.add(mapearResultSetADeporte(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return deportes;
    }
    
    // Busca deporte basado en su Id    
    public Deporte obtenerDeportePorId(int id) {
        String sql = "SELECT * FROM DEPORTE WHERE Id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
             // Si existe el deporte, se retorna
            if (rs.next()) return mapearResultSetADeporte(rs);
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }
    
    // Actualiza los datos del deporte    
    public boolean actualizarDeporte(Deporte deporte) {
        String sql = "UPDATE DEPORTE SET Nombre = ?, Descripcion = ? WHERE Id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, deporte.getNombre());
            pstmt.setString(2, deporte.getDescripcion());
            pstmt.setInt(3, deporte.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }
    
     // Busca deporte que coincida parcialmente con el nombre
    
    public Deporte buscarDeportePorNombre(String nombre) {
        String sql = "SELECT * FROM DEPORTE WHERE Nombre LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nombre + "%");
            ResultSet rs = pstmt.executeQuery();
            
             // Si encuentra coincidencia, retorna el deporte
            if (rs.next()) return mapearResultSetADeporte(rs);
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }
    
     // Convierte los datos del ResultSet en un objeto Deporte
    private Deporte mapearResultSetADeporte(ResultSet rs) throws SQLException {
        Deporte deporte = new Deporte();
        deporte.setId(rs.getInt("Id"));
        deporte.setNombre(rs.getString("Nombre"));
        deporte.setDescripcion(rs.getString("Descripcion"));
        return deporte;
    }
}
