package com.atlastech.gestionclubdeportivos.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

// Utilidades para validación de datos
public class ValidationUtils {
    
    // Patrones de validación
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern TELEFONO_PATTERN = Pattern.compile(
        "^[0-9]{3}-[0-9]{3}-[0-9]{4}$"
    );

    // Valida que una cadena no sea nula ni vacía
    public static boolean esTextoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    // Valida que una cadena tenga una longitud mínima
    public static boolean tieneLongitudMinima(String texto, int longitudMinima) {
        return esTextoValido(texto) && texto.trim().length() >= longitudMinima;
    }

    // Valida que una cadena tenga una longitud máxima
    public static boolean tieneLongitudMaxima(String texto, int longitudMaxima) {
        return esTextoValido(texto) && texto.trim().length() <= longitudMaxima;
    }

    //Valida formato de email
    public static boolean esEmailValido(String email) {
        if (!esTextoValido(email)) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // Valida formato de teléfono (formato: 809-555-1234)
    public static boolean esTelefonoValido(String telefono) {
        if (!esTextoValido(telefono)) return false;
        return TELEFONO_PATTERN.matcher(telefono).matches();
    }

    // Valida que un número esté dentro de un rango
    public static boolean estaEnRango(int numero, int min, int max) {
        return numero >= min && numero <= max;
    }

    //Valida que un decimal sea positivo
    public static boolean esDecimalPositivo(BigDecimal valor) {
        return valor != null && valor.compareTo(BigDecimal.ZERO) > 0;
    }

    //Valida que un decimal sea mayor o igual a cero
    public static boolean esDecimalNoNegativo(BigDecimal valor) {
        return valor != null && valor.compareTo(BigDecimal.ZERO) >= 0;
    }

    //Valida que un entero sea positivo
    public static boolean esEnteroPositivo(int valor) {
        return valor > 0;
    }

    //Valida que una fecha no sea nula
    public static boolean esFechaValida(LocalDate fecha) {
        return fecha != null;
    }

    //Valida que una fecha de nacimiento corresponda a una edad válida
    public static boolean esEdadValida(LocalDate fechaNacimiento, int edadMinima, int edadMaxima) {
        if (fechaNacimiento == null) return false;
        int edad = DateUtils.calcularEdad(fechaNacimiento);
        return edad >= edadMinima && edad <= edadMaxima;
    }

    // Valida que una fecha esté en el futuro
    public static boolean esFechaFutura(LocalDate fecha) {
        if (fecha == null) return false;
        return fecha.isAfter(LocalDate.now());
    }

    // Valida que una fecha esté en el pasado
    public static boolean esFechaPasada(LocalDate fecha) {
        if (fecha == null) return false;
        return fecha.isBefore(LocalDate.now());
    }

    //Valida un nombre de usuario
    public static boolean esNombreUsuarioValido(String nombreUsuario) {
        if (!esTextoValido(nombreUsuario)) return false;
        // Mínimo 3 caracteres, máximo 20, solo letras y números
        return nombreUsuario.matches("^[a-zA-Z0-9]{3,20}$");
    }

    //Valida una contraseña
    public static boolean esContrasenaValida(String contraseña) {
        if (!esTextoValido(contraseña)) return false;
        // Mínimo 4 caracteres
        return contraseña.length() >= 4;
    }

    //Valida que dos contraseñas coincidan
    public static boolean contrasenasCoinciden(String contraseña1, String contraseña2) {
        if (!esTextoValido(contraseña1) || !esTextoValido(contraseña2)) return false;
        return contraseña1.equals(contraseña2);
    }

    //Mensaje de error para campo vacío
    public static String mensajeErrorCampoVacio(String nombreCampo) {
        return "❌ El campo '" + nombreCampo + "' es obligatorio";
    }

    // Mensaje de error para formato inválido
    public static String mensajeErrorFormatoInvalido(String nombreCampo) {
        return "❌ El formato del campo '" + nombreCampo + "' es inválido";
    }

    //Mensaje de error para rango inválido
    public static String mensajeErrorRango(String nombreCampo, int min, int max) {
        return "❌El campo '" + nombreCampo + "' debe estar entre " + min + " y " + max;
    }
}