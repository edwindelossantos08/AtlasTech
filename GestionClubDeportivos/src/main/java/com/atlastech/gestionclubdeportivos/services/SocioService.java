/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atlastech.gestionclubdeportivos.services;

import com.atlastech.gestionclubdeportivos.dao.SocioDAO;
import com.atlastech.gestionclubdeportivos.models.Socio;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Atlas_Tech
 */

public class SocioService {
    private SocioDAO socioDAO;
    
    public SocioService() {
        this.socioDAO = new SocioDAO();
    }
    
    // OPERACIONES CRUD CON VALIDACIONES
    
    /* Registra un nuevo socio con validaciones completas */
    public boolean registrarSocio(Socio socio) {
        // Validar datos obligatorios
        if (!validarDatosObligatorios(socio)) {
            return false;
        }
        
        // Validar formato de email
        if (!validarEmail(socio.getEmail())) {
            System.err.println("Formato de email inválido");
            return false;
        }
        
        // Verificar que el email no esté registrado
        if (socioDAO.existeEmail(socio.getEmail())) {
            System.err.println("El email ya está registrado");
            return false;
        }
        
        // Validar formato de teléfono
        if (!validarTelefono(socio.getTelefono())) {
            System.err.println("Formato de teléfono inválido");
            return false;
        }
        
        // Validar edad (debe ser mayor de 5 años y menor de 120)
        if (!validarEdad(socio.getFechaNacimiento())) {
            System.err.println("Edad inválida (debe tener entre 5 y 120 años)");
            return false;
        }
        
        // Establecer valores por defecto
        if (socio.getFechaRegistro() == null) {
            socio.setFechaRegistro(LocalDate.now());
        }
        
        // El estado por defecto es true (activo)
        socio.setEstado(true);
        
        // Intentar registrar
        return socioDAO.insertarSocio(socio);
    }
    
    /* Obtiene todos los socios */
    public List<Socio> listarTodosSocios() {
        return socioDAO.obtenerTodosSocios();
    }
    
    /* Busca un socio por ID */
    public Socio buscarSocioPorId(int id) {
        if (id <= 0) {
            System.err.println("ID inválido");
            return null;
        }
        
        Socio socio = socioDAO.obtenerSocioPorId(id);
        
        if (socio == null) {
            System.err.println("No se encontró socio con ID: " + id);
        }
        
        return socio;
    }
    
    /* Actualiza los datos de un socio */
    public boolean modificarSocio(Socio socio) {
        if (socio.getId() <= 0) {
            System.err.println("ID de socio inválido");
            return false;
        }
        
        // Verificar que el socio existe
        Socio socioExistente = socioDAO.obtenerSocioPorId(socio.getId());
        if (socioExistente == null) {
            System.err.println("El socio no existe");
            return false;
        }
        
        // Validar datos obligatorios
        if (!validarDatosObligatorios(socio)) {
            return false;
        }
        
        // Validar email (si cambió)
        if (!socio.getEmail().equals(socioExistente.getEmail())) {
            if (!validarEmail(socio.getEmail())) {
                System.err.println("Formato de email inválido");
                return false;
            }
            
            if (socioDAO.existeEmail(socio.getEmail())) {
                System.err.println("El nuevo email ya está registrado");
                return false;
            }
        }
        
        // Validar teléfono
        if (!validarTelefono(socio.getTelefono())) {
            System.err.println("Formato de teléfono inválido");
            return false;
        }
        
        return socioDAO.actualizarSocio(socio);
    }
    
    /* Da de baja a un socio (inactivo) */
    public boolean darDeBajaSocio(int id) {
        if (id <= 0) {
            System.err.println("ID inválido");
            return false;
        }
        
        // Verificar que el socio existe
        Socio socio = socioDAO.obtenerSocioPorId(id);
        if (socio == null) {
            System.err.println("El socio no existe");
            return false;
        }
        
        // Verificar si ya está inactivo
        if (!socio.isEstado()) {
            System.out.println("El socio ya está inactivo");
            return false;
        }
        
        return socioDAO.cambiarEstadoSocio(id, false);
    }
    
    /* Reactiva un socio */
    public boolean reactivarSocio(int id) {
        if (id <= 0) {
            System.err.println("ID inválido");
            return false;
        }
        
        Socio socio = socioDAO.obtenerSocioPorId(id);
        if (socio == null) {
            System.err.println("El socio no existe");
            return false;
        }
        
        if (socio.isEstado()) {
            System.out.println("El socio ya está activo");
            return false;
        }
        
        return socioDAO.cambiarEstadoSocio(id, true);
    }
    
    // BÚSQUEDAS Y CONSULTAS
    
    /* Busca socios por nombre */
    public List<Socio> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("Debe ingresar un nombre para buscar");
            return List.of();
        }
        
        return socioDAO.buscarSociosPorNombre(nombre.trim());
    }
    
    /* Obtiene solo socios activos */
    public List<Socio> listarSociosActivos() {
        return socioDAO.obtenerSociosActivos();
    }
    
    /* Obtiene socios con suscripción vigente */
    public List<Socio> listarSociosConSuscripcionVigente() {
        return socioDAO.obtenerSociosConSuscripcionVigente();
    }
    
    // ESTADÍSTICAS
    
    /* Obtiene el total de socios activos */
    public int obtenerTotalSociosActivos() {
        return socioDAO.contarSociosActivos();
    }
    
    /* Calcula la edad de un socio */
    public int calcularEdad(Socio socio) {
        if (socio == null || socio.getFechaNacimiento() == null) {
            return 0;
        }
        return Period.between(socio.getFechaNacimiento(), LocalDate.now()).getYears();
    }
    
    /* Obtiene estadísticas generales de socios */
    public String obtenerEstadisticas() {
        List<Socio> todos = socioDAO.obtenerTodosSocios();
        int activos = socioDAO.contarSociosActivos();
        int inactivos = todos.size() - activos;
        
        return String.format("""
                             ESTAD\u00cdSTICAS DE SOCIOS
                             ━━━━━━━━━━━━━━━━━━━━━━
                             Total: %d
                             Activos: %d
                             Inactivos: %d""",
            todos.size(), activos, inactivos
        );
    }
    
    // MÉTODOS DE VALIDACIÓN PRIVADOS
    
    /* Valida que los datos obligatorios estén completos */
    private boolean validarDatosObligatorios(Socio socio) {
        if (socio.getNombres() == null || socio.getNombres().trim().isEmpty()) {
            System.err.println("El nombre es obligatorio");
            return false;
        }
        
        if (socio.getApellidos() == null || socio.getApellidos().trim().isEmpty()) {
            System.err.println("El apellido es obligatorio");
            return false;
        }
        
        if (socio.getEmail() == null || socio.getEmail().trim().isEmpty()) {
            System.err.println("El email es obligatorio");
            return false;
        }
        
        if (socio.getTelefono() == null || socio.getTelefono().trim().isEmpty()) {
            System.err.println("El teléfono es obligatorio");
            return false;
        }
        
        if (socio.getFechaNacimiento() == null) {
            System.err.println("La fecha de nacimiento es obligatoria");
            return false;
        }
        
        return true;
    }
    
    /* Valida formato de email */
    private boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(regex, email);
    }
    
    /* Valida formato de teléfono (acepta varios formatos) */
    private boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return false;
        }
        
        // Acepta: 809-555-1234, (809) 555-1234, 8095551234, +1-809-555-1234
        String regex = "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$";
        return Pattern.matches(regex, telefono.replaceAll("\\s", ""));
    }
    
    /* Valida que la edad esté en un rango válido */
    private boolean validarEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return false;
        }
        
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        return edad >= 5 && edad <= 120;
    }
}
