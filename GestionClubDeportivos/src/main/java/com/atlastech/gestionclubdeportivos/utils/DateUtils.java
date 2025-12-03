package com.atlastech.gestionclubdeportivos.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

// Utilidades para manejo de fechas
public class DateUtils {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    //Formatea una fecha a String
    public static String formatear(LocalDate fecha) {
        if (fecha == null) return "N/A";
        return fecha.format(DATE_FORMATTER);
    }

    //Formatea una fecha y hora a String
    public static String formatear(LocalDateTime fechaHora) {
        if (fechaHora == null) return "N/A";
        return fechaHora.format(DATETIME_FORMATTER);
    }

    // Parsea un String a LocalDate
    public static LocalDate parsearFecha(String fechaStr) throws DateTimeParseException {
        return LocalDate.parse(fechaStr, DATE_FORMATTER);
    }

    //Parsea un String a LocalDateTime
    public static LocalDateTime parsearFechaHora(String fechaHoraStr) throws DateTimeParseException {
        return LocalDateTime.parse(fechaHoraStr, DATETIME_FORMATTER);
    }

    //Calcula la diferencia en días entre dos fechas
    public static long diferenciaEnDias(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) return 0;
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }

    //Calcula la edad en años
    public static int calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return 0;
        return (int) ChronoUnit.YEARS.between(fechaNacimiento, LocalDate.now());
    }

    // Verifica si una fecha es futura
    public static boolean esFutura(LocalDate fecha) {
        if (fecha == null) return false;
        return fecha.isAfter(LocalDate.now());
    }

    //Verifica si una fecha es pasada
    public static boolean esPasada(LocalDate fecha) {
        if (fecha == null) return false;
        return fecha.isBefore(LocalDate.now());
    }

    //Verifica si una fecha es hoy
    public static boolean esHoy(LocalDate fecha) {
        if (fecha == null) return false;
        return fecha.isEqual(LocalDate.now());
    }

    //Agrega días a una fecha
    public static LocalDate agregarDias(LocalDate fecha, int dias) {
        if (fecha == null) return null;
        return fecha.plusDays(dias);
    }

    //Agrega meses a una fecha
    public static LocalDate agregarMeses(LocalDate fecha, int meses) {
        if (fecha == null) return null;
        return fecha.plusMonths(meses);
    }

    // Obtiene la fecha actual formateada
    public static String obtenerFechaActualFormateada() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    //Obtiene la fecha y hora actual formateada
    public static String obtenerFechaHoraActualFormateada() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }

    //Valida si un String es una fecha válida
    public static boolean esFechaValida(String fechaStr) {
        try {
            parsearFecha(fechaStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Obtiene el primer día del me
    public static LocalDate primerDiaDelMes(LocalDate fecha) {
        if (fecha == null) return null;
        return fecha.withDayOfMonth(1);
    }

    //Obtiene el último día del mes
    public static LocalDate ultimoDiaDelMes(LocalDate fecha) {
        if (fecha == null) return null;
        return fecha.withDayOfMonth(fecha.lengthOfMonth());
    }
}
