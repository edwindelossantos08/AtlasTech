/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atlastech.gestionclubdeportivos.services;

import com.atlastech.gestionclubdeportivos.dao.SuscripcionDAO;
import com.atlastech.gestionclubdeportivos.dao.SocioDAO;
import com.atlastech.gestionclubdeportivos.dao.MembresiaDAO;
import com.atlastech.gestionclubdeportivos.models.Suscripcion;
import com.atlastech.gestionclubdeportivos.models.Socio;
import com.atlastech.gestionclubdeportivos.models.Membresia;

import java.util.List;
/**
 *
 * @author Atlas_Tech
 */
public class SuscripcionService {
    private SuscripcionDAO suscripcionDAO;
    private SocioDAO socioDAO;
    private MembresiaDAO membresiaDAO;
    
    public SuscripcionService() {
        this.suscripcionDAO = new SuscripcionDAO();
        this.socioDAO = new SocioDAO();
        this.membresiaDAO = new MembresiaDAO();
    }}
    
    // OPERACIONES CRUD CON VALIDACIONES
    
    /* Crea una nueva suscripción con validaciones completas */
    /*public boolean crearSuscripcion(Suscripcion suscripcion) {
        // Validar IDs
        if (!validarIds(suscripcion)) {
            return false;
        }
        
        // Verificar que el socio existe y está activo
        Socio socio = socioDAO.obtenerSocioPorId(suscripcion.getIdSocio());
        if (socio == null) {
            System.err.println("El socio no existe");
            return false;
        }
        
        if (!socio.isEstado()) {
            System.err.println("El socio está inactivo. Debe reactivarlo primero");
            return false;
        }
        
        // Verificar que la membresía existe
        Membresia membresia = membresiaDAO.obtenerMembresiaPorId(suscripcion.getIdMembresia());
        if (membresia == null) {
            System.err.println("La membresía no existe");
            return false;
        }
        
        /* Verificar que el socio no tenga una suscripción activa
        if (suscripcionDAO.verificarSuscripcionVigente(suscripcion.getIdSocio())) {
            System.err.println("El socio ya tiene una suscripción activa");
            System.out.println("Debe cancelar la suscripción actual antes de crear una nueva");
            return false;
        }
        
        // Registrar la suscripción
        boolean resultado = suscripcionDAO.insertarSuscripcion(suscripcion);
        
        if (resultado) {
            System.out.println("Suscripción creada exitosamente");
            System.out.println("Detalles:");
            System.out.println("   - Socio: " + socio.getNombres() + " " + socio.getApellidos());
            System.out.println("   - Membresía: " + membresia.getTipo());
            System.out.println("   - Precio: $" + membresia.getPrecio());
            System.out.println("   - Duración: " + membresia.getDuracionDias() + " días");
        }
        
        return resultado;
    }
    
    /* Obtiene todas las suscripciones */
    /*public List<Suscripcion> listarTodasSuscripciones() {
        return suscripcionDAO.obtenerTodasSuscripciones();
    }
    
    /* Busca una suscripción por ID */
    /*public Suscripcion buscarSuscripcionPorId(int id) {
        if (id <= 0) {
            System.err.println("ID inválido");
            return null;
        }
        
        Suscripcion suscripcion = suscripcionDAO.obtenerSuscripcionPorId(id);
        
        if (suscripcion == null) {
            System.err.println("No se encontró suscripción con ID: " + id);
        }
        
        return suscripcion;
    }
    
    /* Actualiza una suscripción */
    /*public boolean modificarSuscripcion(Suscripcion suscripcion) {
        if (suscripcion.getId() <= 0) {
            System.err.println("ID de suscripción inválido");
            return false;
        }
        
        // Verificar que la suscripción existe
        /*Suscripcion suscripcionExistente = suscripcionDAO.obtenerSuscripcionPorId(suscripcion.getId());
        if (suscripcionExistente == null) {
            System.err.println("La suscripción no existe");
            return false;
        }
        
        // Validar IDs
        if (!validarIds(suscripcion)) {
            return false;
        }
        
        // Verificar que el socio y la membresía existen
        if (socioDAO.obtenerSocioPorId(suscripcion.getIdSocio()) == null) {
            System.err.println("El socio no existe");
            return false;
        }
        
        if (membresiaDAO.obtenerMembresiaPorId(suscripcion.getIdMembresia()) == null) {
            System.err.println("La membresía no existe");
            return false;
        }
        
        return suscripcionDAO.actualizarSuscripcion(suscripcion);
    }
    
    /* Cancela una suscripción (desactiva renovación automática) */
    /*public boolean cancelarSuscripcion(int id) {
        if (id <= 0) {
            System.err.println("ID inválido");
            return false;
        }
        
        Suscripcion suscripcion = suscripcionDAO.obtenerSuscripcionPorId(id);
        if (suscripcion == null) {
            System.err.println("La suscripción no existe");
            return false;
        }
        
        if (!suscripcion.isRenovacionAutomatica()) {
            System.out.println("La suscripción ya está cancelada");
            return false;
        }
        
        return suscripcionDAO.cancelarRenovacion(id);
    }
    
    /* Elimina permanentemente una suscripción */
   /* public boolean eliminarSuscripcion(int id) {
        if (id <= 0) {
            System.err.println("ID inválido");
            return false;
        }
        
        Suscripcion suscripcion = suscripcionDAO.obtenerSuscripcionPorId(id);
        if (suscripcion == null) {
            System.err.println("La suscripción no existe");
            return false;
        }
        
        System.out.println("ADVERTENCIA: Esto eliminará permanentemente la suscripción");
        
        return suscripcionDAO.eliminarSuscripcion(id);
    }
    
    // OPERACIONES ESPECÍFICAS
    
    /* Renueva una suscripción */
    /*public boolean renovarSuscripcion(int idSuscripcion) {
        if (idSuscripcion <= 0) {
            System.err.println("ID inválido");
            return false;
        }
        
        Suscripcion suscripcion = suscripcionDAO.obtenerSuscripcionPorId(idSuscripcion);
        if (suscripcion == null) {
            System.err.println("La suscripción no existe");
            return false;
        }
        
        // Verificar que el socio siga activo
        Socio socio = socioDAO.obtenerSocioPorId(suscripcion.getIdSocio());
        if (socio == null || !socio.isEstado()) {
            System.err.println("El socio está inactivo. No se puede renovar la suscripción");
            return false;
        }
        
        boolean resultado = suscripcionDAO.renovarSuscripcion(idSuscripcion);
        
        if (resultado) {
            Membresia membresia = membresiaDAO.obtenerMembresiaPorId(suscripcion.getIdMembresia());
            System.out.println("Suscripción renovada exitosamente");
            System.out.println("Monto a cobrar: $" + membresia.getPrecio());
        }
        
        return resultado;
    }
    
    /* Verifica si un socio tiene suscripción vigente */
    /*public boolean verificarSuscripcionVigente(int idSocio) {
        if (idSocio <= 0) {
            System.err.println("ID de socio inválido");
            return false;
        }
        
        return suscripcionDAO.verificarSuscripcionVigente(idSocio);
    }
    
    // BÚSQUEDAS Y CONSULTAS
    
    /* Obtiene suscripciones activas */
    /*public List<Suscripcion> listarSuscripcionesActivas() {
        return suscripcionDAO.obtenerSuscripcionesActivas();
    }
    
    /* Obtiene suscripciones de un socio */
    /*public List<Suscripcion> obtenerSuscripcionesPorSocio(int idSocio) {
        if (idSocio <= 0) {
            System.err.println("ID de socio inválido");
            return List.of();
        }
        
        return suscripcionDAO.obtenerSuscripcionesPorSocio(idSocio);
    }
    
    /* Obtiene la suscripción activa de un socio */
    /*public Suscripcion obtenerSuscripcionActivaDeSocio(int idSocio) {
        if (idSocio <= 0) {
            System.err.println("ID de socio inválido");
            return null;
        }
        
        return suscripcionDAO.obtenerSuscripcionActivaDeSocio(idSocio);
    }
    
    /* Obtiene suscripciones por tipo de membresía */
    /*public List<Suscripcion> obtenerSuscripcionesPorMembresia(int idMembresia) {
        if (idMembresia <= 0) {
            System.err.println("ID de membresía inválido");
            return List.of();
        }
        
        return suscripcionDAO.obtenerSuscripcionesPorMembresia(idMembresia);
    }
    
    // ESTADÍSTICAS
    
    /* Obtiene el total de suscripciones activas */
    /*public int obtenerTotalSuscripcionesActivas() {
        return suscripcionDAO.contarSuscripcionesActivas();
    }
    
    /* Obtiene información detallada de una suscripción */
   /* public String obtenerDetallesSuscripcion(int idSuscripcion) {
        Suscripcion suscripcion = suscripcionDAO.obtenerSuscripcionPorId(idSuscripcion);
        
        if (suscripcion == null) {
            return "Suscripción no encontrada";
        }
        
        Socio socio = socioDAO.obtenerSocioPorId(suscripcion.getIdSocio());
        Membresia membresia = membresiaDAO.obtenerMembresiaPorId(suscripcion.getIdMembresia());
        
        return String.format(
            "DETALLES DE SUSCRIPCIÓN #%d\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "Socio: %s %s\n" +
            "Membresía: %s\n" +
            "Precio: $%.2f\n" +
            "Duración: %d días\n" +
            "Renovación Automática: %s\n" +
            "Estado: %s",
            suscripcion.getId(),
            socio.getNombres(),
            socio.getApellidos(),
            membresia.getTipo(),
            membresia.getPrecio(),
            membresia.getDuracionDias(),
            suscripcion.isRenovacionAutomatica() ? "SÍ" : "NO",
            suscripcion.isRenovacionAutomatica() ? "ACTIVA" : "CANCELADA"
        );
    }
    
    /* Obtiene estadísticas generales */
    /*public String obtenerEstadisticas() {
        List<Suscripcion> todas = suscripcionDAO.obtenerTodasSuscripciones();
        int activas = suscripcionDAO.contarSuscripcionesActivas();
        int canceladas = todas.size() - activas;
        
        return String.format(
            "ESTADÍSTICAS DE SUSCRIPCIONES\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "Total: %d\n" +
            "Activas: %d\n" +
            "Canceladas: %d",
            todas.size(),
            activas,
            canceladas
        );
    }
    
    // MÉTODOS DE VALIDACIÓN PRIVADOS
    
    /* Valida que los IDs sean válidos */
   /* private boolean validarIds(Suscripcion suscripcion) {
        if (suscripcion.getIdSocio() <= 0) {
            System.err.println("ID de socio inválido");
            return false;
        }
        
        if (suscripcion.getIdMembresia() <= 0) {
            System.err.println("ID de membresía inválido");
            return false;
        }
        
        return true;
    }

}*/