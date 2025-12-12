/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atlastech.gestionclubdeportivos.dao;
import com.atlastech.gestionclubdeportivos.databases.Databases;
import com.atlastech.gestionclubdeportivos.models.Reserva;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/*
 *
 * @author Atlas_Tech
 */

public class ReservaDAO {
       private final Connection connection;
    
    public ReservaDAO() {
        this.connection = Databases.getConection();
    }
    
    
    /* Crea una nueva reserva*/
    public boolean crearReserva(Reserva reserva) {
        String sql = "INSERT INTO RESERVA (Id_Socio, Id_Instalacion, Fecha, " +
                     "Hora_Inicio, Hora_Fin, Estado, Tipo_de_Reserva, Costo, Notas) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, reserva.getIdSocio());
            pstmt.setInt(2, reserva.getIdInstalacion());
            pstmt.setDate(3, Date.valueOf(reserva.getFecha()));
            pstmt.setTime(4, Time.valueOf(reserva.getHoraInicio()));
            pstmt.setTime(5, Time.valueOf(reserva.getHoraFin()));
            pstmt.setString(6, reserva.getEstado());
            pstmt.setString(7, reserva.getTipoReserva());
            pstmt.setBigDecimal(8, reserva.getCosto());
            pstmt.setString(9, reserva.getNotas());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        reserva.setId(rs.getInt(1));
                    }
                }
                System.out.println("Reserva creada con ID: " + reserva.getId());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear reserva: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Obtiene todas las reservas */
    public List<Reserva> obtenerTodasReservas() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM RESERVA ORDER BY Fecha DESC, Hora_Inicio";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reservas.add(mapearResultSetAReserva(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener reservas: " + e.getMessage());
            e.printStackTrace();
        }
        return reservas;
    }
    
    /* Obtiene una reserva por ID */
    public Reserva obtenerReservaPorId(int id) {
        String sql = "SELECT * FROM RESERVA WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAReserva(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar reserva: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /* Actualiza una reserva */
    public boolean actualizarReserva(Reserva reserva) {
        String sql = "UPDATE RESERVA SET Fecha = ?, Hora_Inicio = ?, Hora_Fin = ?, " +
                     "Estado = ?, Notas = ? WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(reserva.getFecha()));
            pstmt.setTime(2, Time.valueOf(reserva.getHoraInicio()));
            pstmt.setTime(3, Time.valueOf(reserva.getHoraFin()));
            pstmt.setString(4, reserva.getEstado());
            pstmt.setString(5, reserva.getNotas());
            pstmt.setInt(6, reserva.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Reserva actualizada correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar reserva: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Cancela una reserva (cambia estado a "Cancelada") */
    public boolean cancelarReserva(int id) {
        String sql = "UPDATE RESERVA SET Estado = 'Cancelada' WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Reserva cancelada correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al cancelar reserva: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Obtiene reservas de un socio */
    public List<Reserva> obtenerReservasPorSocio(int idSocio) {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM RESERVA WHERE Id_Socio = ? ORDER BY Fecha DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idSocio);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearResultSetAReserva(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener reservas del socio: " + e.getMessage());
            e.printStackTrace();
        }
        return reservas;
    }
    
    /* Obtiene reservas de una instalación en una fecha */
    public List<Reserva> obtenerReservasPorInstalacion(int idInstalacion, LocalDate fecha) {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM RESERVA WHERE Id_Instalacion = ? AND Fecha = ? " +
                     "ORDER BY Hora_Inicio";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idInstalacion);
            pstmt.setDate(2, Date.valueOf(fecha));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearResultSetAReserva(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener reservas de instalación: " + e.getMessage());
            e.printStackTrace();
        }
        return reservas;
    }
    
    /* Verifica disponibilidad de una instalación en un horario */
    public boolean verificarDisponibilidad(int idInstalacion, LocalDate fecha, 
                                          LocalTime horaInicio, LocalTime horaFin) {
        String sql = "SELECT COUNT(*) FROM RESERVA " +
                     "WHERE Id_Instalacion = ? AND Fecha = ? " +
                     "AND Estado != 'Cancelada' " +
                     "AND ((Hora_Inicio < ? AND Hora_Fin > ?) " +
                     "OR (Hora_Inicio < ? AND Hora_Fin > ?) " +
                     "OR (Hora_Inicio >= ? AND Hora_Fin <= ?))";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idInstalacion);
            pstmt.setDate(2, Date.valueOf(fecha));
            pstmt.setTime(3, Time.valueOf(horaFin));
            pstmt.setTime(4, Time.valueOf(horaInicio));
            pstmt.setTime(5, Time.valueOf(horaFin));
            pstmt.setTime(6, Time.valueOf(horaFin));
            pstmt.setTime(7, Time.valueOf(horaInicio));
            pstmt.setTime(8, Time.valueOf(horaFin));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Disponible si no hay reservas en ese horario
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar disponibilidad: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /* Obtiene reservas del día actual */
    public List<Reserva> obtenerReservasDelDia() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM RESERVA WHERE Fecha = CURDATE() " +
                     "AND Estado != 'Cancelada' ORDER BY Hora_Inicio";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reservas.add(mapearResultSetAReserva(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener reservas del día: " + e.getMessage());
            e.printStackTrace();
        }
        return reservas;
    }
    
    /* Obtiene reservas por estado */
    public List<Reserva> obtenerReservasPorEstado(String estado) {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM RESERVA WHERE Estado = ? ORDER BY Fecha DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, estado);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearResultSetAReserva(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener reservas por estado: " + e.getMessage());
            e.printStackTrace();
        }
        return reservas;
    }
    
    // MÉTODO AUXILIAR
    
    private Reserva mapearResultSetAReserva(ResultSet rs) throws SQLException {
        Reserva reserva = new Reserva();
        reserva.setId(rs.getInt("Id"));
        reserva.setIdSocio(rs.getInt("Id_Socio"));
        reserva.setIdInstalacion(rs.getInt("Id_Instalacion"));
        reserva.setFecha(rs.getDate("Fecha").toLocalDate());
        reserva.setHoraInicio(rs.getTime("Hora_Inicio").toLocalTime());
        reserva.setHoraFin(rs.getTime("Hora_Fin").toLocalTime());
        reserva.setEstado(rs.getString("Estado"));
        reserva.setTipoReserva(rs.getString("Tipo_de_Reserva"));
        reserva.setCosto(rs.getBigDecimal("Costo"));
        reserva.setNotas(rs.getString("Notas"));
        return reserva;
    }
    public int contarReservas() {
        String sql = "SELECT COUNT(*) FROM RESERVA";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error al contar reservas: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

}
