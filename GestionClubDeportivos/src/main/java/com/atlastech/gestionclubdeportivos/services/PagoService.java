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

public class PagoService {

    private final PagoDAO pagoDAO;
    private final SuscripcionDAO suscripcionDAO;
    private final ReservaDAO reservaDAO;

    public PagoService() {
        this.pagoDAO = new PagoDAO();
        this.suscripcionDAO = new SuscripcionDAO();
        this.reservaDAO = new ReservaDAO();
    }

    // ============================================================
    // REGISTRAR PAGO DE SUSCRIPCIÓN
    // ============================================================

    public boolean registrarPagoSuscripcion(int idSuscripcion, BigDecimal monto,
                                            String metodoPago, String concepto) {

        // Obtener suscripción con el método real del DAO
        Suscripcion sus = suscripcionDAO.obtenerPorId(idSuscripcion);
        if (sus == null) {
            System.err.println("La suscripción no existe");
            return false;
        }

        // Calcular fecha fin correctamente
        LocalDate fechaFin = sus.getFechaInicio().plusMonths(sus.getDuracionMeses());
        boolean vigente = !fechaFin.isBefore(LocalDate.now());

        if (!vigente) {
            System.err.println("La suscripción NO está vigente.");
            return false;
        }

        if (!validarMonto(monto)) {
            System.err.println("El monto debe ser mayor a 0");
            return false;
        }

        if (!validarMetodoPago(metodoPago)) {
            System.err.println("Método de pago inválido");
            return false;
        }

        // Crear pago
        Pago pago = new Pago();

        // Pago NO tiene idSocio → NO SE USA
        pago.setIdSuscripcion(idSuscripcion);
        pago.setIdReserva(0);
        pago.setIdTorneo(0);

        pago.setConcepto(
                concepto != null && !concepto.trim().isEmpty()
                ? concepto.trim()
                : "Pago de Suscripción"
        );

        pago.setMonto(monto);
        pago.setMetodoPago(metodoPago.trim());
        pago.setEstado("Pagado");
        pago.setComprobanteUrl(generarNumeroComprobante());

        boolean resultado = pagoDAO.registrarPago(pago);

        if (resultado) {
            System.out.println("Pago de suscripción registrado. Comprobante: " + pago.getComprobanteUrl());
        }

        return resultado;
    }

    // ============================================================
    // REGISTRAR PAGO DE RESERVA
    // ============================================================

    public boolean registrarPagoReserva(int idReserva, BigDecimal monto,
                                        String metodoPago, String concepto) {

        Reserva reserva = reservaDAO.obtenerReservaPorId(idReserva);
        if (reserva == null) {
            System.err.println("La reserva no existe");
            return false;
        }

        if (!validarMonto(monto)) {
            System.err.println("El monto debe ser mayor a 0");
            return false;
        }

        if (!validarMetodoPago(metodoPago)) {
            System.err.println("Método de pago inválido");
            return false;
        }

        Pago pago = new Pago();
        pago.setIdSuscripcion(0);
        pago.setIdReserva(idReserva);
        pago.setIdTorneo(0);

        pago.setConcepto(
                concepto != null && !concepto.trim().isEmpty()
                ? concepto.trim()
                : "Pago de Reserva"
        );

        pago.setMonto(monto);
        pago.setMetodoPago(metodoPago.trim());
        pago.setEstado("Pagado");
        pago.setComprobanteUrl(generarNumeroComprobante());

        boolean resultado = pagoDAO.registrarPago(pago);

        if (resultado) {
            System.out.println("Pago de reserva registrado. Comprobante: " + pago.getComprobanteUrl());
        }

        return resultado;
    }

    // ============================================================
    // REGISTRAR PAGO DE TORNEO
    // ============================================================

    public boolean registrarPagoTorneo(int idTorneo, BigDecimal monto,
                                       String metodoPago, String concepto) {

        if (!validarMonto(monto)) {
            System.err.println("El monto debe ser mayor a 0");
            return false;
        }

        if (!validarMetodoPago(metodoPago)) {
            System.err.println("Método de pago inválido");
            return false;
        }

        Pago pago = new Pago();
        pago.setIdSuscripcion(0);
        pago.setIdReserva(0);
        pago.setIdTorneo(idTorneo);

        pago.setConcepto(
                concepto != null && !concepto.trim().isEmpty()
                ? concepto.trim()
                : "Inscripción a Torneo"
        );

        pago.setMonto(monto);
        pago.setMetodoPago(metodoPago.trim());
        pago.setEstado("Pagado");
        pago.setComprobanteUrl(generarNumeroComprobante());

        boolean resultado = pagoDAO.registrarPago(pago);

        if (resultado) {
            System.out.println("Pago de torneo registrado. Comprobante: " + pago.getComprobanteUrl());
        }

        return resultado;
    }

    // ============================================================
    // REGISTRO GENÉRICO
    // ============================================================

    public boolean registrarPago(Pago pago) {

        if (pago == null) {
            System.err.println("El objeto pago es nulo");
            return false;
        }

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

        if (pago.getComprobanteUrl() == null) {
            pago.setComprobanteUrl(generarNumeroComprobante());
        }

        if (pago.getEstado() == null) {
            pago.setEstado("Pagado");
        }

        boolean resultado = pagoDAO.registrarPago(pago);

        if (resultado) {
            System.out.println("Pago registrado. Comprobante: " + pago.getComprobanteUrl());
        }

        return resultado;
    }

    // ============================================================
    // CONSULTAS
    // ============================================================

    public List<Pago> listarTodosPagos() {
        return pagoDAO.obtenerTodosPagos();
    }

    public Pago buscarPagoPorId(int id) {
        if (id <= 0) return null;
        return pagoDAO.obtenerPagoPorId(id);
    }

    public boolean actualizarEstadoPago(int id, String nuevoEstado) {

        if (id <= 0) return false;

        Pago pago = pagoDAO.obtenerPagoPorId(id);
        if (pago == null) return false;

        if (!validarEstadoPago(nuevoEstado)) {
            System.err.println("Estado inválido");
            return false;
        }

        pago.setEstado(nuevoEstado.trim());
        return pagoDAO.actualizarPago(pago);
    }

    public List<Pago> obtenerPagosPorSuscripcion(int idSuscripcion) {
        if (idSuscripcion <= 0) return List.of();
        return pagoDAO.obtenerPagosPorSuscripcion(idSuscripcion);
    }

    public List<Pago> obtenerPagosPendientes() {
        return pagoDAO.obtenerPagosPendientes();
    }

    public List<Pago> obtenerPagosPorMetodo(String metodoPago) {
        if (!validarMetodoPago(metodoPago)) return List.of();
        return pagoDAO.obtenerPagosPorMetodo(metodoPago.trim());
    }

    public List<Pago> buscarPagosPorConcepto(String concepto) {
        if (concepto == null || concepto.trim().isEmpty()) return List.of();
        return pagoDAO.obtenerPagosPorConcepto(concepto.trim());
    }

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    public double calcularIngresosMensuales(int mes, int anio) {
        try {
            return pagoDAO.calcularIngresosMensuales(mes, anio);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public double calcularIngresosMesActual() {
        LocalDate hoy = LocalDate.now();
        return calcularIngresosMensuales(hoy.getMonthValue(), hoy.getYear());
    }

    public BigDecimal calcularTotalPendiente() {
        BigDecimal total = BigDecimal.ZERO;
        List<Pago> pendientes = pagoDAO.obtenerPagosPendientes();

        if (pendientes != null) {
            for (Pago p : pendientes) {
                if (p.getMonto() != null) {
                    total = total.add(p.getMonto());
                }
            }
        }
        return total;
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    private boolean validarMonto(BigDecimal monto) {
        return monto != null && monto.compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean validarMetodoPago(String metodoPago) {
        if (metodoPago == null) return false;
        String m = metodoPago.trim().toLowerCase();
        return m.equals("efectivo") || m.equals("tarjeta") || m.equals("transferencia");
    }

    private boolean validarEstadoPago(String estado) {
        if (estado == null) return false;
        String e = estado.trim().toLowerCase();
        return e.equals("pagado") || e.equals("pendiente") || e.equals("cancelado") || e.equals("rechazado");
    }

    private String generarNumeroComprobante() {
        return "COMP-" + System.currentTimeMillis();
    }
}
