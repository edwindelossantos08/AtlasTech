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
    private final Connection connection;

    public SuscripcionDAO() {
        this.connection = Databases.getConection();
    }

    // =====================================================
    // INSERTAR
    // =====================================================
    public boolean registrarSuscripcion(Suscripcion suscripcion) {

        String sql = "INSERT INTO SUSCRIPCION (Id_Socio, Id_Membresia, Renovacion_Automatica) " +
                     "VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, suscripcion.getIdSocio());
            pstmt.setInt(2, suscripcion.getIdMembresia());
            pstmt.setBoolean(3, suscripcion.isRenovacionAutomatica());

            int filas = pstmt.executeUpdate();

            if (filas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) suscripcion.setId(rs.getInt(1));
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar suscripción: " + e.getMessage());
        }

        return false;
    }

    // =====================================================
    // OBTENER TODAS
    // =====================================================
    public List<Suscripcion> obtenerTodas() {

        List<Suscripcion> lista = new ArrayList<>();
        String sql = "SELECT * FROM SUSCRIPCION";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Suscripcion s = new Suscripcion();

                s.setId(rs.getInt("Id"));
                s.setIdSocio(rs.getInt("Id_Socio"));
                s.setIdMembresia(rs.getInt("Id_Membresia"));
                s.setRenovacionAutomatica(rs.getBoolean("Renovacion_Automatica"));

                lista.add(s);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener suscripciones: " + e.getMessage());
        }

        return lista;
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================
public Suscripcion obtenerPorId(int id) {

    String sql = "SELECT * FROM SUSCRIPCION WHERE Id = ?";

    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            Suscripcion s = new Suscripcion();

            s.setId(rs.getInt("Id"));
            s.setIdSocio(rs.getInt("Id_Socio"));
            s.setIdMembresia(rs.getInt("Id_Membresia"));
            s.setRenovacionAutomatica(rs.getBoolean("Renovacion_Automatica"));

            // ================================
            // Cargar Fecha_Inicio
            // ================================
            Date fechaInicioSQL = rs.getDate("Fecha_Inicio");
            if (fechaInicioSQL != null) {
                s.setFechaInicio(fechaInicioSQL.toLocalDate());
            }

            // ================================
            // Cargar Duracion_Meses
            // ================================
            Integer duracionMeses = rs.getObject("Duracion_Meses", Integer.class);
            s.setDuracionMeses(duracionMeses);

            return s;
        }

    } catch (SQLException e) {
        System.err.println("Error al obtener suscripción: " + e.getMessage());
    }

    return null;
}

    
    // =====================================================
    // VERIFICAR LA SUSCRIPCION VIGENTE
    // =====================================================
    
    public boolean verificarSuscripcionVigente(int idSocio) {
    String sql = "SELECT COUNT(*) "
               + "FROM SUSCRIPCION "
               + "WHERE Id_Socio = ? "
               + "AND Fecha_Fin >= CURDATE()"; // Sigue vigente

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {

        stmt.setInt(1, idSocio);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0; // Tiene suscripción activa
            }
        }

    } catch (SQLException e) {
        System.err.println("Error al verificar suscripción vigente: " + e.getMessage());
    }

    return false;
}
    // =====================================================
    // OBTENER SUSCRIPCION POR ID
    // =====================================================
    
    public boolean existeSuscripcion(int idSuscripcion) {
    String sql = "SELECT COUNT(*) FROM SUSCRIPCION WHERE Id_Suscripcion = ?";
    
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, idSuscripcion);
        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al verificar existencia de suscripción: " + e.getMessage());
    }
    return false;
}


    // =====================================================
    // ELIMINAR
    // =====================================================
    public boolean eliminar(int id) {

        String sql = "DELETE FROM SUSCRIPCION WHERE Id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar suscripción: " + e.getMessage());
        }

        return false;
    }
}
