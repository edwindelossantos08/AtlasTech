/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atlastech.gestionclubdeportivos.services;

import com.atlastech.gestionclubdeportivos.dao.PagoDAO;
import com.atlastech.gestionclubdeportivos.dao.SuscripcionDAO;
import com.atlastech.gestionclubdeportivos.dao.ReservaDAO;
import com.atlastech.gestionclubdeportivos.models.Pago;
import com.atlastech.gestionclubdeportivos.models.Suscripcion;
import com.atlastech.gestionclubdeportivos.models.Reserva;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Atlas_Tech
 */

public class PagoService {
    private PagoDAO pagoDAO;
    private SuscripcionDAO suscripcionDAO;
    private ReservaDAO reservaDAO;
    
    public PagoService() {
        this.pagoDAO = new PagoDAO();
        this.suscripcionDAO = new SuscripcionDAO();
        this.reservaDAO = new ReservaDAO();
    }
    
    // OPERACIONES CRUD CON VALIDACIONES

    /*  Registra un pago de suscripción con validaciones */
    /*public boolean registrarPagoSuscripcion(int idSuscripcion, BigDecimal monto, 
                                            String metodoPago, String concepto) {
        // Validar que la suscripción existe
        Suscripcion suscripcion = suscripcionDAO.obtenerSuscripcionPorId(idSuscripcion);
        if (suscripcion == null) {
            System.err.println("La suscripción no existe");
            return false;
        }
        
        // Validar monto
        if (!validarMonto(monto)) {
            System.err.println("El monto debe ser mayor a 0");
            return false;
        }
        
        // Validar método de pago
        if (!validarMetodoPago(metodoPago)) {
            System.err.println("Método de pago inválido. Use: Efectivo, Tarjeta, Transferencia");
            return false;
        }
        
        // Crear el pago
        Pago pago = new Pago();
        pago.setIdSuscripcion(idSuscripcion);
        pago.setIdReserva(0); // No aplica
        pago.setIdTorneo(0);  // No aplica
        pago.setConcepto(concepto != null ? concepto : "Pago de Suscripción");
        pago.setMonto(monto);
        pago.setMetodoPago(metodoPago);
        pago.setEstado("Pagado");
        pago.setComprobanteUrl(generarNumeroComprobante());
        
        boolean resultado = pagoDAO.registrarPago(pago);
        
        if (resultado) {
            System.out.println("Pago registrado exitosamente");
            System.out.println("Comprobante: " + pago.getComprobanteUrl());
            System.out.println("Monto: $" + monto);
        }
        
        return resultado;
    }
    
    /* Registra un pago de reserva */
    public boolean registrarPagoReserva(int idReserva, BigDecimal monto, 
                                        String metodoPago, String concepto) {
        // Validar que la reserva existe
        Reserva reserva = reservaDAO.obtenerReservaPorId(idReserva);
        if (reserva == null) {
            System.err.println("La reserva no existe");
            return false;
        }
        
        // Validar monto
        if (!validarMonto(monto)) {
            System.err.println("El monto debe ser mayor a 0");
            return false;
        }
        
        // Validar método de pago
        if (!validarMetodoPago(metodoPago)) {
            System.err.println("Método de pago inválido");
            return false;
        }
        
        // Crear el pago
        Pago pago = new Pago();
        pago.setIdSuscripcion(0); // No aplica
        pago.setIdReserva(idReserva);
        pago.setIdTorneo(0);  // No aplica
        pago.setConcepto(concepto != null ? concepto : "Pago de Reserva");
        pago.setMonto(monto);
        pago.setMetodoPago(metodoPago);
        pago.setEstado("Pagado");
        pago.setComprobanteUrl(generarNumeroComprobante());
        
        boolean resultado = pagoDAO.registrarPago(pago);
        
        if (resultado) {
            System.out.println("Pago de reserva registrado");
            System.out.println("Comprobante: " + pago.getComprobanteUrl());
        }
        
        return resultado;
    }
    
    /* Registra un pago de torneo */
    public boolean registrarPagoTorneo(int idTorneo, BigDecimal monto, 
                                       String metodoPago, String concepto) {
        // Validar monto
        if (!validarMonto(monto)) {
            System.err.println("El monto debe ser mayor a 0");
            return false;
        }
        
        // Validar método de pago
        if (!validarMetodoPago(metodoPago)) {
            System.err.println("Método de pago inválido");
            return false;
        }
        
        // Crear el pago
        Pago pago = new Pago();
        pago.setIdSuscripcion(0);
        pago.setIdReserva(0);
        pago.setIdTorneo(idTorneo);
        pago.setConcepto(concepto != null ? concepto : "Inscripción a Torneo");
        pago.setMonto(monto);
        pago.setMetodoPago(metodoPago);
        pago.setEstado("Pagado");
        pago.setComprobanteUrl(generarNumeroComprobante());
        
        return pagoDAO.registrarPago(pago);
    }
    
    /* Registra un pago genérico */
    public boolean registrarPago(Pago pago) {
        // Validaciones básicas
        if (pago.getConcepto() == null || pago.getConcepto().trim().isEmpty()) {
            System.err.println("El concepto es obligatorio");
            return false;
        }
        
        if (!validarMonto(pago.getMonto())) {
            System.err.println("El monto debe ser mayor a 0");
            return false;
        }
        
        if (!validarMetodoPago(pago.getMetodoPago())) {
            System.err.println("Método de pago inválido");
            return false;
        }
        
        // Generar comprobante si no tiene
        if (pago.getComprobanteUrl() == null || pago.getComprobanteUrl().isEmpty()) {
            pago.setComprobanteUrl(generarNumeroComprobante());
        }
        
        // Estado por defecto
        if (pago.getEstado() == null || pago.getEstado().isEmpty()) {
            pago.setEstado("Pagado");
        }
        
        return pagoDAO.registrarPago(pago);
    }
    
    /* Obtiene todos los pagos */
    public List<Pago> listarTodosPagos() {
        return pagoDAO.obtenerTodosPagos();
    }
    
    /* Busca un pago por ID */
    public Pago buscarPagoPorId(int id) {
        if (id <= 0) {
            System.err.println("ID inválido");
            return null;
        }
        
        Pago pago = pagoDAO.obtenerPagoPorId(id);
        
        if (pago == null) {
            System.err.println("No se encontró pago con ID: " + id);
        }
        
        return pago;
    }
    
    /* Actualiza el estado de un pago */
    public boolean actualizarEstadoPago(int id, String nuevoEstado) {
        if (id <= 0) {
            System.err.println("ID inválido");
            return false;
        }
        
        Pago pago = pagoDAO.obtenerPagoPorId(id);
        if (pago == null) {
            System.err.println("El pago no existe");
            return false;
        }
        
        if (!validarEstadoPago(nuevoEstado)) {
            System.err.println("Estado inválido. Use: Pagado, Pendiente, Cancelado, Rechazado");
            return false;
        }
        
        pago.setEstado(nuevoEstado);
        return pagoDAO.actualizarPago(pago);
    }
    
    // CONSULTAS Y BÚSQUEDAS
    
    /* Obtiene pagos de una suscripción */
    public List<Pago> obtenerPagosPorSuscripcion(int idSuscripcion) {
        if (idSuscripcion <= 0) {
            System.err.println("ID de suscripción inválido");
            return List.of();
        }
        
        return pagoDAO.obtenerPagosPorSuscripcion(idSuscripcion);
    }
    
    /* Obtiene pagos pendientes */
    public List<Pago> obtenerPagosPendientes() {
        return pagoDAO.obtenerPagosPendientes();
    }
    
    /* Obtiene pagos por método de pago */
    public List<Pago> obtenerPagosPorMetodo(String metodoPago) {
        if (!validarMetodoPago(metodoPago)) {
            System.err.println("Método de pago inválido");
            return List.of();
        }
        
        return pagoDAO.obtenerPagosPorMetodo(metodoPago);
    }
    
    /* Obtiene pagos por concepto */
    public List<Pago> buscarPagosPorConcepto(String concepto) {
        if (concepto == null || concepto.trim().isEmpty()) {
            System.err.println("Debe especificar un concepto para buscar");
            return List.of();
        }
        
        return pagoDAO.obtenerPagosPorConcepto(concepto.trim());
    }
    
    // CÁLCULOS Y ESTADÍSTICAS
    
    /* Calcula ingresos mensuales */
    public double calcularIngresosMensuales(int mes, int anio) {
        if (mes < 1 || mes > 12) {
            System.err.println("Mes inválido (debe ser 1-12)");
            return 0.0;
        }
        
        if (anio < 2000 || anio > LocalDate.now().getYear()) {
            System.err.println("Año inválido");
            return 0.0;
        }
        
        return pagoDAO.calcularIngresosMensuales(mes, anio);
    }
    
    /* Calcula ingresos del mes actual */
    public double calcularIngresosMesActual() {
        LocalDate hoy = LocalDate.now();
        return calcularIngresosMensuales(hoy.getMonthValue(), hoy.getYear());
    }
    
    /* Calcula total de pagos pendientes */
    public BigDecimal calcularTotalPendiente() {
        List<Pago> pendientes = pagoDAO.obtenerPagosPendientes();
        
        BigDecimal total = BigDecimal.ZERO;
        for (Pago pago : pendientes) {
            total = total.add(pago.getMonto());
        }
        
        return total;
    }
    
    /* Obtiene estadísticas de pagos por método */
    public String obtenerEstadisticasPorMetodo() {
        List<Pago> efectivo = pagoDAO.obtenerPagosPorMetodo("Efectivo");
        List<Pago> tarjeta = pagoDAO.obtenerPagosPorMetodo("Tarjeta");
        List<Pago> transferencia = pagoDAO.obtenerPagosPorMetodo("Transferencia");
        
        BigDecimal totalEfectivo = calcularTotalLista(efectivo);
        BigDecimal totalTarjeta = calcularTotalLista(tarjeta);
        BigDecimal totalTransferencia = calcularTotalLista(transferencia);
        BigDecimal totalGeneral = totalEfectivo.add(totalTarjeta).add(totalTransferencia);
        
        return String.format(
            "ESTADÍSTICAS DE PAGOS POR MÉTODO\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "Efectivo: %d pagos - $%.2f\n" +
            "Tarjeta: %d pagos - $%.2f\n" +
            "Transferencia: %d pagos - $%.2f\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "TOTAL: %d pagos - $%.2f",
            efectivo.size(), totalEfectivo,
            tarjeta.size(), totalTarjeta,
            transferencia.size(), totalTransferencia,
            efectivo.size() + tarjeta.size() + transferencia.size(),
            totalGeneral
        );
    }
    
    /* Obtiene reporte financiero del mes */
    public String obtenerReporteFinancieroMes(int mes, int anio) {
        double ingresos = calcularIngresosMensuales(mes, anio);
        
        return String.format(
            "REPORTE FINANCIERO - %02d/%d\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "Ingresos Totales: $%.2f\n" +
            "Promedio Diario: $%.2f",
            mes, anio,
            ingresos,
            ingresos / 30
        );
    }
    
    // MÉTODOS AUXILIARES PRIVADOS
    
    /* Valida que el monto sea positivo */
    private boolean validarMonto(BigDecimal monto) {
        return monto != null && monto.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /* Valida método de pago */
    private boolean validarMetodoPago(String metodoPago) {
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            return false;
        }
        
        String metodo = metodoPago.trim();
        return metodo.equalsIgnoreCase("Efectivo") || 
               metodo.equalsIgnoreCase("Tarjeta") || 
               metodo.equalsIgnoreCase("Transferencia");
    }
    
    /* Valida estado de pago */
    private boolean validarEstadoPago(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return false;
        }
        
        return estado.equalsIgnoreCase("Pagado") || 
               estado.equalsIgnoreCase("Pendiente") || 
               estado.equalsIgnoreCase("Cancelado") || 
               estado.equalsIgnoreCase("Rechazado");
    }
    
    /* Genera un número de comprobante único */
    private String generarNumeroComprobante() {
        long timestamp = System.currentTimeMillis();
        return "COMP-" + timestamp;
    }
    
    /* Calcula el total de una lista de pagos */
    private BigDecimal calcularTotalLista(List<Pago> pagos) {
        BigDecimal total = BigDecimal.ZERO;
        for (Pago pago : pagos) {
            total = total.add(pago.getMonto());
        }
        return total;
    }
   
}
