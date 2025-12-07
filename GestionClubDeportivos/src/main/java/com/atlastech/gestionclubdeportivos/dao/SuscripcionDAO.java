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
  /*private Connection connection;
    
    public SuscripcionDAO() {
        this.connection = Databases.getConection();
    }
    
    public boolean registrarSuscripcion(Suscripcion suscripcion) {
        String sql = "INSERT INTO PAGO (Id_Socio, Id_Membresia, Renovacion_Automatica" +
                     "VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setObject(1, suscripcion.getIdSocio());
            pstmt.setObject(2, suscripcion.getIdMembresia());
            pstmt.setObject(3, suscripcion.getIdRenovacion_Automatica());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        pago.setId(rs.getInt(1));
                    }
                }
                System.out.println("Pago registrado con ID: " + pago.getId());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al registrar pago: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /*Obtiene todos los pagos de la base de datos
     * @return lista de todos los pagos */
   /* public List<Pago> obtenerTodosPagos() {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM PAGO ORDER BY Id DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                pagos.add(mapearResultSetAPago(rs));
            }
            
            System.out.println("Se obtuvieron " + pagos.size() + " pagos");
            
        } catch (SQLException e) {
            System.err.println("Error al obtener pagos: " + e.getMessage());
            e.printStackTrace();
        }
        return pagos;
    }
    
    /*Obtiene un pago por su ID
     * @param id identificador del pago
     * @return objeto Pago o null si no existe*/
    /*public Pago obtenerPagoPorId(int id) {
        String sql = "SELECT * FROM PAGO WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAPago(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar pago: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /*Actualiza un pago existente
     * @param pago objeto Pago con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario*/
    /*public boolean actualizarPago(Pago pago) {
        String sql = "UPDATE PAGO SET Concepto = ?, Monto = ?, Metodo_Pago = ?, " +
                     "Estado = ?, Comprobante_URL = ? WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, pago.getConcepto());
            pstmt.setBigDecimal(2, pago.getMonto());
            pstmt.setString(3, pago.getMetodoPago());
            pstmt.setString(4, pago.getEstado());
            pstmt.setString(5, pago.getComprobanteUrl());
            pstmt.setInt(6, pago.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Pago actualizado correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar pago: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /*Elimina un pago de la base de datos (usar con precaución)
     * @param id identificador del pago
     * @return true si se eliminó correctamente, false en caso contrario*/
    /*public boolean eliminarPago(int id) {
        String sql = "DELETE FROM PAGO WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Pago eliminado correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar pago: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    //MÉTODOS DE BÚSQUEDA
    
    /*Obtiene todos los pagos asociados a una suscripción
     * @param idSuscripcion identificador de la suscripción
     * @return lista de pagos de esa suscripción*/
    /*public List<Pago> obtenerPagosPorSuscripcion(int idSuscripcion) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM PAGO WHERE Id_Suscripcion = ? ORDER BY Id DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idSuscripcion);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pagos.add(mapearResultSetAPago(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener pagos por suscripción: " + e.getMessage());
            e.printStackTrace();
        }
        return pagos;
    }
    
    /*Obtiene todos los pagos asociados a una reserva
     * @param idReserva identificador de la reserva
     * @return lista de pagos de esa reserva*/
    /*public List<Pago> obtenerPagosPorReserva(int idReserva) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM PAGO WHERE Id_Reserva = ? ORDER BY Id DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idReserva);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pagos.add(mapearResultSetAPago(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener pagos por reserva: " + e.getMessage());
            e.printStackTrace();
        }
        return pagos;
    }
    
    /*Obtiene todos los pagos asociados a un torneo
     * @param idTorneo identificador del torneo
     * @return lista de pagos de ese torneo*/
    /*public List<Pago> obtenerPagosPorTorneo(int idTorneo) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM PAGO WHERE Id_Torneo = ? ORDER BY Id DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idTorneo);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pagos.add(mapearResultSetAPago(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener pagos por torneo: " + e.getMessage());
            e.printStackTrace();
        }
        return pagos;
    }
    
    /*Obtiene todos los pagos pendientes
     * @return lista de pagos con estado "pendiente"*/
    /*public List<Pago> obtenerPagosPendientes() {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM PAGO WHERE Estado = 'pendiente' ORDER BY Id DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                pagos.add(mapearResultSetAPago(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener pagos pendientes: " + e.getMessage());
            e.printStackTrace();
        }
        return pagos;
    }
    
    /*Obtiene todos los pagos por estado
     * @param estado estado del pago ("pendiente", "pagado", "rechazado", "cancelado")
     * @return lista de pagos con ese estado*/
   /* public List<Pago> obtenerPagosPorEstado(String estado) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM PAGO WHERE Estado = ? ORDER BY Id DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, estado);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pagos.add(mapearResultSetAPago(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener pagos por estado: " + e.getMessage());
            e.printStackTrace();
        }
        return pagos;
    }
    
    /*Obtiene pagos por método de pago
     * @param metodoPago método de pago ("efectivo", "tarjeta", "transferencia", etc.)
     * @return lista de pagos con ese método*/
    /*public List<Pago> obtenerPagosPorMetodo(String metodoPago) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM PAGO WHERE Metodo_Pago = ? ORDER BY Id DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, metodoPago);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pagos.add(mapearResultSetAPago(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener pagos por método: " + e.getMessage());
            e.printStackTrace();
        }
        return pagos;
    }
    
    /*Busca pagos por concepto (búsqueda parcial)
     * @param concepto texto a buscar en el concepto
     * @return lista de pagos que coinciden*/
    /*public List<Pago> obtenerPagosPorConcepto(String concepto) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM PAGO WHERE Concepto LIKE ? ORDER BY Id DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + concepto + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pagos.add(mapearResultSetAPago(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener pagos por concepto: " + e.getMessage());
            e.printStackTrace();
        }
        return pagos;
    }
    
    //MÉTODOS DE CAMBIO DE ESTADO
    
    /*Marca un pago como pagado
     * @param id identificador del pago
     * @return true si se actualizó correctamente*/
   /* public boolean marcarComoPagado(int id) {
        return cambiarEstadoPago(id, "pagado");
    }
    
    /*Marca un pago como rechazado
     * @param id identificador del pago
     * @return true si se actualizó correctamente*/
    /*public boolean marcarComoRechazado(int id) {
        return cambiarEstadoPago(id, "rechazado");
    }
    
    /*Marca un pago como cancelado
     * @param id identificador del pago
     * @return true si se actualizó correctamente*/
    /*public boolean marcarComoCancelado(int id) {
        return cambiarEstadoPago(id, "cancelado");
    }
    
    /*Cambia el estado de un pago
     * @param id identificador del pago
     * @param nuevoEstado nuevo estado a asignar
     * @return true si se actualizó correctamente*/
    /*private boolean cambiarEstadoPago(int id, String nuevoEstado) {
        String sql = "UPDATE PAGO SET Estado = ? WHERE Id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Estado del pago actualizado a: " + nuevoEstado);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    //MÉTODOS DE REPORTES Y ESTADÍSTICAS
    
    /*Calcula los ingresos totales de un mes específico
     * @param mes mes (1-12)
     * @param anio año
     * @return monto total de ingresos del mes*/
    /*public double calcularIngresosMensuales(int mes, int anio) {
        String sql = "SELECT SUM(Monto) FROM PAGO " +
                     "WHERE Estado = 'pagado' " +
                     "AND MONTH(Fecha_Pago) = ? " +
                     "AND YEAR(Fecha_Pago) = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, mes);
            pstmt.setInt(2, anio);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al calcular ingresos mensuales: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }
    
    /*Calcula los ingresos totales de un año específico
     * @param anio año
     * @return monto total de ingresos del año*/
   /* public double calcularIngresosAnuales(int anio) {
        String sql = "SELECT SUM(Monto) FROM PAGO " +
                     "WHERE Estado = 'pagado' " +
                     "AND YEAR(Fecha_Pago) = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, anio);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al calcular ingresos anuales: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }
    
    /*Cuenta el total de pagos realizados
     * @return cantidad de pagos con estado "pagado"*/
   /* public int contarPagosRealizados() {
        String sql = "SELECT COUNT(*) FROM PAGO WHERE Estado = 'pagado'";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar pagos realizados: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    /*Cuenta el total de pagos pendientes
     * @return cantidad de pagos con estado "pendiente"*/
   /* public int contarPagosPendientes() {
        String sql = "SELECT COUNT(*) FROM PAGO WHERE Estado = 'pendiente'";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar pagos pendientes: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    /*Obtiene el monto total de pagos pendientes
     * @return suma total de montos pendientes*/
    /*public BigDecimal obtenerMontoPendiente() {
        String sql = "SELECT SUM(Monto) FROM PAGO WHERE Estado = 'pendiente'";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal(1);
                return total != null ? total : BigDecimal.ZERO;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener monto pendiente: " + e.getMessage());
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    
    //MÉTODO AUXILIAR
    
    /*Mapea un ResultSet a un objeto Pago
     * @param rs ResultSet con los datos
     * @return objeto Pago mapeado
     * @throws SQLException si hay error al acceder a los datos*/
   /* private Pago mapearResultSetAPago(ResultSet rs) throws SQLException {
        Pago pago = new Pago();
        pago.setId(rs.getInt("Id"));
        
        // Los campos nullable se manejan con getObject
        Integer idSuscripcion = (Integer) rs.getObject("Id_Suscripcion");
        Integer idReserva = (Integer) rs.getObject("Id_Reserva");
        Integer idTorneo = (Integer) rs.getObject("Id_Torneo");
        
        pago.setIdSuscripcion(idSuscripcion);
        pago.setIdReserva(idReserva);
        pago.setIdTorneo(idTorneo);
        pago.setConcepto(rs.getString("Concepto"));
        pago.setMonto(rs.getBigDecimal("Monto"));
        pago.setMetodoPago(rs.getString("Metodo_Pago"));
        pago.setEstado(rs.getString("Estado"));
        pago.setComprobanteUrl(rs.getString("Comprobante_URL"));
        
        return pago;
    }
*/
}
