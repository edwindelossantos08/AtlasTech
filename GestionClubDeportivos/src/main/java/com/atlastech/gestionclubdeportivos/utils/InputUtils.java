package com.atlastech.gestionclubdeportivos.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

//Utilidades para entrada de datos del usuario
 
public class InputUtils {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    //Lee un entero con validación
    public static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor ingrese un número válido");
            }
        }
    }

    //Lee un entero dentro de un rango
    public static int leerEntero(String mensaje, int min, int max) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.println("❌ El valor debe estar entre " + min + " y " + max);
        }
    }

    // Lee un decimal con validación
    public static BigDecimal leerDecimal(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine().trim();
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor ingrese un número decimal válido");
            }
        }
    }

    // Lee una cadena no vacía
    public static String leerCadena(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("❌ Este campo no puede estar vacío");
        }
    }

    //Lee una cadena con opción de dejarla vacía
    public static String leerCadenaOpcional(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    //Lee una fecha con validació
    public static LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + " (dd/MM/yyyy): ");
                String input = scanner.nextLine().trim();
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato de fecha inválido. Use dd/MM/yyyy");
            }
        }
    }

    //Lee un email con validación
    public static String leerEmail(String mensaje) {
        while (true) {
            String email = leerCadena(mensaje);
            if (ValidationUtils.esEmailValido(email)) {
                return email;
            }
            System.out.println("❌ Email inválido. Debe contener @ y un dominio válido");
        }
    }

    // Lee un teléfono con validación
    public static String leerTelefono(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (formato: 809-555-1234): ");
            String telefono = scanner.nextLine().trim();
            if (ValidationUtils.esTelefonoValido(telefono)) {
                return telefono;
            }
            System.out.println("❌ Formato inválido. Use: 809-555-1234");
        }
    }

    //Lee una confirmación S/N
    public static boolean leerConfirmacion(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (S/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("S") || input.equals("SI") || input.equals("SÍ")) {
                return true;
            }
            if (input.equals("N") || input.equals("NO")) {
                return false;
            }
            System.out.println("❌ Responda S o N");
        }
    }

    //Lee una contraseña (no se oculta en consola estándar)
    public static String leerContrasena(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    //Pausa hasta que el usuario presione ENTER
    public static void pausa() {
        System.out.println("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }

    //Pausa con mensaje personalizado
    public static void pausa(String mensaje) {
        System.out.println("\n" + mensaje);
        scanner.nextLine();
    }

    //Limpia la pantalla
    public static void limpiarPantalla() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    //Muestra un mensaje de error
    public static void mostrarError(String mensaje) {
        System.out.println("\n❌ ERROR: " + mensaje);
    }

    // Muestra un mensaje de éxito
    public static void mostrarExito(String mensaje) {
        System.out.println("\n✅ " + mensaje);
    }

    //Muestra un mensaje de advertencia
    public static void mostrarAdvertencia(String mensaje) {
        System.out.println("\n⚠️  " + mensaje);
    }

    //Muestra un mensaje informativo
    public static void mostrarInfo(String mensaje) {
        System.out.println("\nℹ️  " + mensaje);
    }

    // Dibuja una línea separadora
    public static void dibujarLinea() {
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    // Muestra un encabezado con formato
    public static void mostrarEncabezado(String titulo) {
        limpiarPantalla();
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║  " + centrarTexto(titulo, 60) + "  ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
    }

    //Centra un texto en un ancho específico
    private static String centrarTexto(String texto, int ancho) {
        if (texto.length() >= ancho) {
            return texto.substring(0, ancho);
        }
        int espaciosIzq = (ancho - texto.length()) / 2;
        int espaciosDer = ancho - texto.length() - espaciosIzq;
        return " ".repeat(espaciosIzq) + texto + " ".repeat(espaciosDer);
    }

    // Formatea un monto monetario
    public static String formatearMonto(BigDecimal monto) {
        return String.format("$%,.2f", monto);
    }
}