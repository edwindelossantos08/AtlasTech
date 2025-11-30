/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atlastech.gestionclubdeportivos.dao;

import com.atlastech.gestionclubdeportivos.databases.Databases;
import com.atlastech.gestionclubdeportivos.models.Entrenador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Atlas_Tech
 */

public class EntrenadorDAO {
    private Connection connection;
    
    public EntrenadorDAO() {
        this.connection = (Connection) Databases.getInstance();
    }
    
    public boolean insertarEntrenador(Entrenador entrenador) {
        String sql = "INSERT INTO ENTRENADOR (Nombres, Apellidos, Email, Telefono, " +
                     "Especialidad, Fecha_Contratacion, Estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, entrenador.getNombres());
            pstmt.setString(2, entrenador.getApellidos());
            pstmt.setString(3, entrenador.getEmail());
            pstmt.setString(4, entrenador.getTelefono());
            pstmt.setString(5, entrenador.getEspecialidad());
            pstmt.setDate(6, Date.valueOf(entrenador.getFechaContratacion()));
            pstmt.setBoolean(7, entrenador.isEstado());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) entrenador.setId(rs.getInt(1));
                System.out.println("Entrenador registrado: " + entrenador.getNombres());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }
    
    public List<Entrenador> obtenerTodosEntrenadores() {
        List<Entrenador> entrenadores = new ArrayList<>();
        String sql = "SELECT * FROM ENTRENADOR ORDER BY Apellidos, Nombres";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                entrenadores.add(mapearResultSetAEntrenador(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return entrenadores;
    }
    
    public Entrenador obtenerEntrenadorPorId(int id) {
        String sql = "SELECT * FROM ENTRENADOR WHERE Id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return mapearResultSetAEntrenador(rs);
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }
    
    public boolean actualizarEntrenador(Entrenador entrenador) {
        String sql = "UPDATE ENTRENADOR SET Nombres = ?, Apellidos = ?, Email = ?, " +
                     "Telefono = ?, Especialidad = ?, Estado = ? WHERE Id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, entrenador.getNombres());
            pstmt.setString(2, entrenador.getApellidos());
            pstmt.setString(3, entrenador.getEmail());
            pstmt.setString(4, entrenador.getTelefono());
            pstmt.setString(5, entrenador.getEspecialidad());
            pstmt.setBoolean(6, entrenador.isEstado());
            pstmt.setInt(7, entrenador.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }
    
    public List<Entrenador> obtenerEntrenadoresActivos() {
        List<Entrenador> entrenadores = new ArrayList<>();
        String sql = "SELECT * FROM ENTRENADOR WHERE Estado = 1 ORDER BY Apellidos";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                entrenadores.add(mapearResultSetAEntrenador(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return entrenadores;
    }
    
    public List<Entrenador> obtenerEntrenadoresPorEspecialidad(String especialidad) {
        List<Entrenador> entrenadores = new ArrayList<>();
        String sql = "SELECT * FROM ENTRENADOR WHERE Especialidad LIKE ? AND Estado = 1";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + especialidad + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                entrenadores.add(mapearResultSetAEntrenador(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return entrenadores;
    }
    
    public boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM ENTRENADOR WHERE Email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }
    
    public boolean cambiarEstado(int id, boolean estado) {
        String sql = "UPDATE ENTRENADOR SET Estado = ? WHERE Id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, estado);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }
    
    private Entrenador mapearResultSetAEntrenador(ResultSet rs) throws SQLException {
        Entrenador entrenador = new Entrenador();
        entrenador.setId(rs.getInt("Id"));
        entrenador.setNombres(rs.getString("Nombres"));
        entrenador.setApellidos(rs.getString("Apellidos"));
        entrenador.setEmail(rs.getString("Email"));
        entrenador.setTelefono(rs.getString("Telefono"));
        entrenador.setEspecialidad(rs.getString("Especialidad"));
        entrenador.setFechaContratacion(rs.getDate("Fecha_Contratacion").toLocalDate());
        entrenador.setEstado(rs.getBoolean("Estado"));
        return entrenador;
    }
}
