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
// Clase DAO para manejar operaciones CRUD relacionadas a la tabla SUSCRIPCION.
// Permite registrar, consultar, verificar y eliminar suscripciones.
public class SuscripcionDAO {
    // Conexión activa a la base de datos
    private final Connection connection;

    // Constructor: obtiene la conexión desde Databases
    public SuscripcionDAO() {
        this.connection = Databases.getConection();
    }

    // Registra una nueva suscripción en la base de datos.
    // @param suscripcion Objeto con los datos de la suscripción.
    // @return true si fue registrada correctamente, false en caso contrario.
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

    // Obtiene todas las suscripciones registradas.
    // @return Lista con objetos Suscripcion.
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

    // Obtiene una suscripción específica por su ID.
    // @param id Identificador de la suscripción.
    // @return Objeto Suscripcion si existe, null si no se encuentra.
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

            // Cargar Fecha_Inicio
            Date fechaInicioSQL = rs.getDate("Fecha_Inicio");
            if (fechaInicioSQL != null) {
                s.setFechaInicio(fechaInicioSQL.toLocalDate());
            }

            // Cargar Duracion_Meses
            Integer duracionMeses = rs.getObject("Duracion_Meses", Integer.class);
            s.setDuracionMeses(duracionMeses);

            return s;
        }

    } catch (SQLException e) {
        System.err.println("Error al obtener suscripción: " + e.getMessage());
    }

    return null;
}

    

    // Verifica si un socio actualmente tiene una suscripción activa.
    // @param idSocio Identificador del socio.
    // @return true si tiene suscripción vigente, false si no.
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
    
   // Verifica si existe una suscripción utilizando su ID.
    // @param idSuscripcion Identificador de la suscripción.
    // @return true si existe, false si no.
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


    // Elimina una suscripción por su ID.
    // @param id Identificador de la suscripción.
    // @return true si fue eliminada correctamente, false en caso contrario.
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
