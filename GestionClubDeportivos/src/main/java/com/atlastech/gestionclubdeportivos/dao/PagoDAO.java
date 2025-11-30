/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atlastech.gestionclubdeportivos.dao;

import com.atlastech.gestionclubdeportivos.databases.Databases;
import com.atlastech.gestionclubdeportivos.models.Pago;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 *
 * @author Atlas_Tech
 */

public class PagoDAO {
    private int id;
    private Integer idSuscripcion;
    private Integer idReserva;
    private Integer idTorneo;
    private String concepto;
    private BigDecimal monto;
    private String metodoPago;
    private String estado;
    private String comprobanteUrl;
    private LocalDateTime fechaPago;
    
    public PagoDAO() {
        this.estado = "pendiente";
        this.fechaPago = LocalDateTime.now();
    }
    
    public PagoDAO(String concepto, BigDecimal monto, String metodoPago) {
        this();
        this.concepto = concepto;
        this.monto = monto;
        this.metodoPago = metodoPago;
    }
    
    public PagoDAO(int id, Integer idSuscripcion, Integer idReserva, Integer idTorneo,
                String concepto, BigDecimal monto, String metodoPago, String estado,
                String comprobanteUrl, LocalDateTime fechaPago) {
        this.id = id;
        this.idSuscripcion = idSuscripcion;
        this.idReserva = idReserva;
        this.idTorneo = idTorneo;
        this.concepto = concepto;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.estado = estado;
        this.comprobanteUrl = comprobanteUrl;
        this.fechaPago = fechaPago;
    }
    
    public boolean isPagado() {
        return "pagado".equalsIgnoreCase(estado);
    }

    public String getEstadoFormatted() {
        return switch (estado.toLowerCase()) {
            case "pagado" -> "PAGADO";
            case "pendiente" -> "PENDIENTE";
            case "rechazado" -> "RECHAZADO";
            case "cancelado" -> "CANCELADO";
            default -> estado.toUpperCase();
        };
    }
    
    public String getTipoPago() {
        if (idSuscripcion != null) return "Suscripción";
        if (idReserva != null) return "Reserva";
        if (idTorneo != null) return "Torneo";
        return "Otro";
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdSuscripcion() {
        return idSuscripcion;
    }

    public void setIdSuscripcion(Integer idSuscripcion) {
        this.idSuscripcion = idSuscripcion;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Integer getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(Integer idTorneo) {
        this.idTorneo = idTorneo;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComprobanteUrl() {
        return comprobanteUrl;
    }

    public void setComprobanteUrl(String comprobanteUrl) {
        this.comprobanteUrl = comprobanteUrl;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    @Override
    public String toString() {
        return String.format("Pago{id=%d, concepto='%s', monto=$%.2f, estado=%s}",
            id, concepto, monto, getEstadoFormatted());
    }

    public boolean registrarPago(Pago pago) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Pago> obtenerTodosPagos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Pago obtenerPagoPorId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean actualizarPago(Pago pago) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Pago> obtenerPagosPorSuscripcion(int idSuscripcion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Pago> obtenerPagosPendientes() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Pago> obtenerPagosPorMetodo(String metodoPago) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Pago> obtenerPagosPorConcepto(String trim) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public double calcularIngresosMensuales(int mes, int anio) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
