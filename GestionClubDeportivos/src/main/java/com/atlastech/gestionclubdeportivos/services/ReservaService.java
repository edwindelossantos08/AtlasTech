/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atlastech.gestionclubdeportivos.services;

import com.atlastech.gestionclubdeportivos.dao.ReservaDAO;
import com.atlastech.gestionclubdeportivos.dao.SocioDAO;
import com.atlastech.gestionclubdeportivos.dao.InstalacionDAO;
import com.atlastech.gestionclubdeportivos.dao.SuscripcionDAO;
import com.atlastech.gestionclubdeportivos.models.Reserva;
import com.atlastech.gestionclubdeportivos.models.Socio;
import com.atlastech.gestionclubdeportivos.models.Instalacion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.util.List;

/**
 *
 * @author Atlas_Tech
 */
public class ReservaService {
    private ReservaDAO reservaDAO;
    private SocioDAO socioDAO;
    private InstalacionDAO instalacionDAO;
    private SuscripcionDAO suscripcionDAO;
    
    public ReservaService() {
        this.reservaDAO = new ReservaDAO();
        this.socioDAO = new SocioDAO();
        this.instalacionDAO = new InstalacionDAO();
        this.suscripcionDAO = new SuscripcionDAO();
    }
    
    // OPERACIONES CRUD CON VALIDACIONES
    
    /* Crea una reserva con validaciones completas */
    public boolean crearReserva(Reserva reserva) {
        // Validar datos obligatorios
        if (!validarDatosObligatorios(reserva)) {
            return false;
        }
        
        // Verificar que el socio existe y está activo
        Socio socio = socioDAO.obtenerSocioPorId(reserva.getIdSocio());
        if (socio == null) {
            System.err.println("El socio no existe");
            return false;
        }
        
        if (!socio.isEstado()) {
            System.err.println("El socio está inactivo");
            return false;
        }
        
        /*// Verificar que el socio tiene suscripción vigente
        if (!suscripcionDAO.verificarSuscripcionVigente(reserva.getIdSocio())) {
            System.err.println("El socio no tiene suscripción vigente");
            System.out.println("Debe adquirir una membresía para hacer reservas");
            return false;
        }*/
        
        // Verificar que la instalación existe y está disponible
        Instalacion instalacion = instalacionDAO.obtenerInstalacionPorId(reserva.getIdInstalacion());
        if (instalacion == null) {
            System.err.println("La instalación no existe");
            return false;
        }
        
        if (!instalacion.getEstado().equalsIgnoreCase("Disponible")) {
            System.err.println("La instalación no está disponible");
            return false;
        }
        
        // Validar fecha (no puede ser en el pasado)
        if (!validarFecha(reserva.getFecha())) {
            System.err.println("La fecha debe ser hoy o en el futuro");
            return false;
        }
        
        // Validar horarios
        if (!validarHorarios(reserva.getHoraInicio(), reserva.getHoraFin())) {
            System.err.println("Horario inválido. La hora de fin debe ser posterior a la hora de inicio");
            return false;
        }
        
        // Validar duración (mínimo 30 minutos, máximo 4 horas)
        if (!validarDuracion(reserva.getHoraInicio(), reserva.getHoraFin())) {
            System.err.println("Duración inválida. Mínimo 30 minutos, máximo 4 horas");
            return false;
        }
        
        // Verificar disponibilidad
        if (!reservaDAO.verificarDisponibilidad(
                reserva.getIdInstalacion(), 
                reserva.getFecha(), 
                reserva.getHoraInicio(), 
                reserva.getHoraFin())) {
            System.err.println("La instalación no está disponible en ese horario");
            System.out.println("Intente con otro horario o fecha");
            return false;
        }
        
        // Calcular costo automáticamente
        BigDecimal costo = calcularCostoReserva(instalacion, reserva.getHoraInicio(), reserva.getHoraFin());
        reserva.setCosto(costo);
        
        // Estado por defecto
        if (reserva.getEstado() == null || reserva.getEstado().isEmpty()) {
            reserva.setEstado("Confirmada");
        }
        
        // Tipo por defecto
        if (reserva.getTipoReserva() == null || reserva.getTipoReserva().isEmpty()) {
            reserva.setTipoReserva("Individual");
        }
        
        boolean resultado = reservaDAO.crearReserva(reserva);
        
        if (resultado) {
            System.out.println("Reserva creada exitosamente");
            System.out.println("Detalles:");
            System.out.println("   - Socio: " + socio.getNombres() + " " + socio.getApellidos());
            System.out.println("   - Instalación: " + instalacion.getNombre());
            System.out.println("   - Fecha: " + reserva.getFecha());
            System.out.println("   - Horario: " + reserva.getHoraInicio() + " - " + reserva.getHoraFin());
            System.out.println("   - Costo: $" + costo);
        }
        
        return resultado;
    }
    
    /* Obtiene todas las reservas */
    public List<Reserva> listarTodasReservas() {
        return reservaDAO.obtenerTodasReservas();
    }
    
    /* Busca una reserva por ID */
    public Reserva buscarReservaPorId(int id) {
        if (id <= 0) {
            System.err.println("ID inválido");
            return null;
        }
        
        Reserva reserva = reservaDAO.obtenerReservaPorId(id);
        
        if (reserva == null) {
            System.err.println("No se encontró reserva con ID: " + id);
        }
        
        return reserva;
    }
    
    /* Actualiza una reserva */
    public boolean modificarReserva(Reserva reserva) {
        if (reserva.getId() <= 0) {
            System.err.println("ID de reserva inválido");
            return false;
        }
        
        // Verificar que la reserva existe
        Reserva reservaExistente = reservaDAO.obtenerReservaPorId(reserva.getId());
        if (reservaExistente == null) {
            System.err.println("La reserva no existe");
            return false;
        }
        
        // No permitir modificar reservas pasadas
        if (reservaExistente.getFecha().isBefore(LocalDate.now())) {
            System.err.println("No se puede modificar una reserva pasada");
            return false;
        }
        
        // No permitir modificar reservas canceladas
        if (reservaExistente.getEstado().equalsIgnoreCase("Cancelada")) {
            System.err.println("No se puede modificar una reserva cancelada");
            return false;
        }
        
        // Validar horarios
        if (!validarHorarios(reserva.getHoraInicio(), reserva.getHoraFin())) {
            return false;
        }
        
        // Si cambió el horario, verificar disponibilidad
        if (!reserva.getHoraInicio().equals(reservaExistente.getHoraInicio()) ||
            !reserva.getHoraFin().equals(reservaExistente.getHoraFin())) {
            
            if (!reservaDAO.verificarDisponibilidad(
                    reserva.getIdInstalacion(), 
                    reserva.getFecha(), 
                    reserva.getHoraInicio(), 
                    reserva.getHoraFin())) {
                System.err.println("El nuevo horario no está disponible");
                return false;
            }
        }
        
        return reservaDAO.actualizarReserva(reserva);
    }
    
    /* Cancela una reserva */
    public boolean cancelarReserva(int id) {
        if (id <= 0) {
            System.err.println("ID inválido");
            return false;
        }
        
        Reserva reserva = reservaDAO.obtenerReservaPorId(id);
        if (reserva == null) {
            System.err.println("La reserva no existe");
            return false;
        }
        
        // Verificar que no esté ya cancelada
        if (reserva.getEstado().equalsIgnoreCase("Cancelada")) {
            System.out.println("La reserva ya está cancelada");
            return false;
        }
        
        // No permitir cancelar reservas que ya pasaron
        if (reserva.getFecha().isBefore(LocalDate.now())) {
            System.err.println("No se puede cancelar una reserva pasada");
            return false;
        }
        
        boolean resultado = reservaDAO.cancelarReserva(id);
        
        if (resultado) {
            System.out.println("Reserva cancelada exitosamente");
            System.out.println("Si pagó, contacte administración para solicitar reembolso");
        }
        
        return resultado;
    }
    
    // CONSULTAS Y BÚSQUEDAS
    
    /* Obtiene reservas de un socio */
    public List<Reserva> obtenerReservasPorSocio(int idSocio) {
        if (idSocio <= 0) {
            System.err.println("ID de socio inválido");
            return List.of();
        }
        
        return reservaDAO.obtenerReservasPorSocio(idSocio);
    }
    
    /* Obtiene reservas de una instalación en una fecha */
    public List<Reserva> obtenerReservasPorInstalacion(int idInstalacion, LocalDate fecha) {
        if (idInstalacion <= 0) {
            System.err.println("ID de instalación inválido");
            return List.of();
        }
        
        if (fecha == null) {
            fecha = LocalDate.now();
        }
        
        return reservaDAO.obtenerReservasPorInstalacion(idInstalacion, fecha);
    }
    
    /* Obtiene reservas del día actual */
    public List<Reserva> obtenerReservasDelDia() {
        return reservaDAO.obtenerReservasDelDia();
    }
    
    /* Obtiene reservas por estado */
    public List<Reserva> obtenerReservasPorEstado(String estado) {
        if (!validarEstado(estado)) {
            System.err.println("Estado inválido. Use: Confirmada, Cancelada, Completada");
            return List.of();
        }
        
        return reservaDAO.obtenerReservasPorEstado(estado);
    }
    
    /* Verifica disponibilidad de instalación */
    public boolean verificarDisponibilidad(int idInstalacion, LocalDate fecha, 
                                          LocalTime horaInicio, LocalTime horaFin) {
        return reservaDAO.verificarDisponibilidad(idInstalacion, fecha, horaInicio, horaFin);
    }
    
    /* Obtiene horarios disponibles de una instalación para una fecha */
    public String obtenerHorariosDisponibles(int idInstalacion, LocalDate fecha) {
        List<Reserva> reservas = reservaDAO.obtenerReservasPorInstalacion(idInstalacion, fecha);
        
        StringBuilder disponibles = new StringBuilder();
        disponibles.append("HORARIOS DISPONIBLES\n");
        disponibles.append("━━━━━━━━━━━━━━━━━━━━━━━━\n");
        disponibles.append("Instalación ID: ").append(idInstalacion).append("\n");
        disponibles.append("Fecha: ").append(fecha).append("\n\n");
        
        if (reservas.isEmpty()) {
            disponibles.append("Toda la jornada disponible (08:00 - 22:00)");
        } else {
            disponibles.append("Horarios Ocupados:\n");
            for (Reserva r : reservas) {
                disponibles.append("   ").append(r.getHoraInicio())
                          .append(" - ").append(r.getHoraFin()).append("\n");
            }
        }
        
        return disponibles.toString();
    }
    
    // CÁLCULOS Y ESTADÍSTICAS
    
    /* Calcula el costo de una reserva */
    public BigDecimal calcularCostoReserva(Instalacion instalacion, LocalTime horaInicio, LocalTime horaFin) {
        Duration duracion = Duration.between(horaInicio, horaFin);
        long horas = duracion.toHours();
        
        // Redondear hacia arriba si hay minutos adicionales
        if (duracion.toMinutes() % 60 != 0) {
            horas++;
        }
        
        return instalacion.getCostoReserva().multiply(new BigDecimal(horas));
    }
    
    /* Calcula duración de una reserva en horas */
    public double calcularDuracionHoras(Reserva reserva) {
        Duration duracion = Duration.between(reserva.getHoraInicio(), reserva.getHoraFin());
        return duracion.toMinutes() / 60.0;
    }
    
    /* Obtiene estadísticas de reservas */
    public String obtenerEstadisticas() {
        List<Reserva> todas = reservaDAO.obtenerTodasReservas();
        List<Reserva> confirmadas = reservaDAO.obtenerReservasPorEstado("Confirmada");
        List<Reserva> canceladas = reservaDAO.obtenerReservasPorEstado("Cancelada");
        List<Reserva> completadas = reservaDAO.obtenerReservasPorEstado("Completada");
        
        return String.format(
            "ESTADÍSTICAS DE RESERVAS\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "Total: %d\n" +
            "Confirmadas: %d\n" +
            "Completadas: %d\n" +
            "Canceladas: %d",
            todas.size(),
            confirmadas.size(),
            completadas.size(),
            canceladas.size()
        );
    }
    
    // MÉTODOS DE VALIDACIÓN PRIVADOS
    
    private boolean validarDatosObligatorios(Reserva reserva) {
        if (reserva.getIdSocio() <= 0) {
            System.err.println("ID de socio inválido");
            return false;
        }
        
        if (reserva.getIdInstalacion() <= 0) {
            System.err.println("ID de instalación inválido");
            return false;
        }
        
        if (reserva.getFecha() == null) {
            System.err.println("La fecha es obligatoria");
            return false;
        }
        
        if (reserva.getHoraInicio() == null || reserva.getHoraFin() == null) {
            System.err.println("Los horarios son obligatorios");
            return false;
        }
        
        return true;
    }
    
    private boolean validarFecha(LocalDate fecha) {
        return !fecha.isBefore(LocalDate.now());
    }
    
    private boolean validarHorarios(LocalTime horaInicio, LocalTime horaFin) {
        return horaInicio.isBefore(horaFin);
    }
    
    private boolean validarDuracion(LocalTime horaInicio, LocalTime horaFin) {
        Duration duracion = Duration.between(horaInicio, horaFin);
        long minutos = duracion.toMinutes();
        
        // Mínimo 30 minutos, máximo 4 horas (240 minutos)
        return minutos >= 30 && minutos <= 240;
    }
    
    private boolean validarEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return false;
        }
        
        return estado.equalsIgnoreCase("Confirmada") || 
               estado.equalsIgnoreCase("Cancelada") || 
               estado.equalsIgnoreCase("Completada");
    }
}
