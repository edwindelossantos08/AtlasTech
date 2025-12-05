/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atlastech.gestionclubdeportivos.services;
import com.atlastech.gestionclubdeportivos.dao.MembresiaDAO;
import com.atlastech.gestionclubdeportivos.models.Membresia;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Atlas_Tech
 */

public class MembresiaService {
    private final MembresiaDAO membresiaDAO;
    
    public MembresiaService() {
        this.membresiaDAO = new MembresiaDAO();
    }
    
    // OPERACIONES CRUD CON VALIDACIONES
    
    /* Registra una nueva membresía con validaciones */
    public boolean registrarMembresia(Membresia membresia) {
        // Validar datos obligatorios
        if (!validarDatosObligatorios(membresia)) {
            return false;
        }
        
        // Validar que el precio sea positivo
        if (!validarPrecio(membresia.getPrecio())) {
            System.err.println("El precio debe ser mayor a 0");
            return false;
        }
        
        // Validar duración mínima (según tu BD: mínimo 30 días)
        if (!validarDuracion(membresia.getDuracionDias())) {
            System.err.println("La duración mínima debe ser de 30 días");
            return false;
        }
        
        // Verificar que no exista una membresía con el mismo tipo
        if (membresiaDAO.existeTipoMembresia(membresia.getTipo())) {
            System.err.println("Ya existe una membresía con ese tipo");
            return false;
        }
        
        return membresiaDAO.insertarMembresia(membresia);
    }
    
    /*  Obtiene todas las membresías */
    public List<Membresia> listarTodasMembresias() {
        return membresiaDAO.obtenerTodasMembresias();
    }
    
    /* Busca una membresía por ID */
    public Membresia buscarMembresiaPorId(int id) {
        if (id <= 0) {
            System.err.println("ID inválido");
            return null;
        }
        
        Membresia membresia = membresiaDAO.obtenerMembresiaPorId(id);
        
        if (membresia == null) {
            System.err.println("No se encontró membresía con ID: " + id);
        }
        
        return membresia;
    }
    
    /* Actualiza una membresía */
    public boolean modificarMembresia(Membresia membresia) {
        if (membresia.getId() <= 0) {
            System.err.println("ID de membresía inválido");
            return false;
        }
        
        // Verificar que la membresía existe
        Membresia membresiaExistente = membresiaDAO.obtenerMembresiaPorId(membresia.getId());
        if (membresiaExistente == null) {
            System.err.println("La membresía no existe");
            return false;
        }
        
        // Validar datos
        if (!validarDatosObligatorios(membresia)) {
            return false;
        }
        
        if (!validarPrecio(membresia.getPrecio())) {
            System.err.println("El precio debe ser mayor a 0");
            return false;
        }
        
        if (!validarDuracion(membresia.getDuracionDias())) {
            System.err.println("La duración mínima debe ser de 30 días");
            return false;
        }
        
        // Si cambió el tipo, verificar que no exista
        if (!membresia.getTipo().equals(membresiaExistente.getTipo())) {
            if (membresiaDAO.existeTipoMembresia(membresia.getTipo())) {
                System.err.println("Ya existe una membresía con ese tipo");
                return false;
            }
        }
        
        return membresiaDAO.actualizarMembresia(membresia);
    }
    
    /* Elimina una membresía */
    public boolean eliminarMembresia(int id) {
        if (id <= 0) {
            System.err.println("ID inválido");
            return false;
        }
        
        Membresia membresia = membresiaDAO.obtenerMembresiaPorId(id);
        if (membresia == null) {
            System.err.println("La membresía no existe");
            return false;
        }
        
        // ADVERTENCIA: Verificar si hay suscripciones activas antes de eliminar
        System.out.println("ADVERTENCIA: Asegúrese de que no hay suscripciones activas con esta membresía");
        
        return membresiaDAO.eliminarMembresia(id);
    }
    
    // BÚSQUEDAS Y CONSULTAS
    
    /* Obtiene membresías disponibles */
    public List<Membresia> listarMembresiasDisponibles() {
        return membresiaDAO.obtenerMembresiasDisponibles();
    }
    
    /* Busca membresía por tipo */
    public Membresia buscarMembresiaPorTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            System.err.println("Debe especificar un tipo de membresía");
            return null;
        }
        
        return membresiaDAO.obtenerMembresiaPorTipo(tipo.trim());
    }
    
    /* Obtiene membresías ordenadas por precio */
    public List<Membresia> listarMembresiasOrdenadas() {
        return membresiaDAO.obtenerMembresiasOrdenadasPorPrecio();
    }
    
    /* Busca membresías por duración */
    public List<Membresia> buscarMembresiaPorDuracion(int duracionDias) {
        if (duracionDias < 30) {
            System.err.println("❌ La duración mínima es de 30 días");
            return List.of();
        }
        
        return membresiaDAO.obtenerMembresiasPorDuracion(duracionDias);
    }
    
    // CÁLCULOS Y ESTADÍSTICAS
    
    /* Calcula el precio diario de una membresía */
    public BigDecimal calcularPrecioDiario(Membresia membresia) {
        if (membresia == null || membresia.getDuracionDias() <= 0) {
            return BigDecimal.ZERO;
        }
        
        return membresia.getPrecio().divide(
            new BigDecimal(membresia.getDuracionDias()), 
            2, 
            BigDecimal.ROUND_HALF_UP
        );
    }
    
    /* Calcula descuento por cantidad de meses */
    public BigDecimal calcularPrecioConDescuento(Membresia membresia, int porcentajeDescuento) {
        if (membresia == null || porcentajeDescuento < 0 || porcentajeDescuento > 100) {
            return membresia != null ? membresia.getPrecio() : BigDecimal.ZERO;
        }
        
        BigDecimal descuento = membresia.getPrecio()
            .multiply(new BigDecimal(porcentajeDescuento))
            .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        
        return membresia.getPrecio().subtract(descuento);
    }
    
    /* Compara dos membresías y retorna la más económica por día */
    public Membresia compararEconomia(Membresia m1, Membresia m2) {
        if (m1 == null) return m2;
        if (m2 == null) return m1;
        
        BigDecimal precioDiarioM1 = calcularPrecioDiario(m1);
        BigDecimal precioDiarioM2 = calcularPrecioDiario(m2);
        
        return precioDiarioM1.compareTo(precioDiarioM2) <= 0 ? m1 : m2;
    }
    
    /* Obtiene estadísticas de membresías */
    public String obtenerEstadisticas() {
        List<Membresia> todas = membresiaDAO.obtenerTodasMembresias();
        
        if (todas.isEmpty()) {
            return "No hay membresías registradas";
        }
        
        BigDecimal precioMin = todas.get(0).getPrecio();
        BigDecimal precioMax = todas.get(0).getPrecio();
        BigDecimal sumaPrecios = BigDecimal.ZERO;
        
        for (Membresia m : todas) {
            if (m.getPrecio().compareTo(precioMin) < 0) {
                precioMin = m.getPrecio();
            }
            if (m.getPrecio().compareTo(precioMax) > 0) {
                precioMax = m.getPrecio();
            }
            sumaPrecios = sumaPrecios.add(m.getPrecio());
        }
        
        BigDecimal precioPromedio = sumaPrecios.divide(
            new BigDecimal(todas.size()), 
            2, 
            BigDecimal.ROUND_HALF_UP
        );
        
        return String.format("""
                             ESTAD\u00cdSTICAS DE MEMBRES/n
                             ━━━━━━━━━━━━━━━━━━━━━━━━━━━
                             Total de membres\u00edas: %d
                             Precio m\u00ednimo: $%.2f
                             Precio m\u00e1ximo: $%.2f
                             Precio promedio: $%.2f""",
            todas.size(),
            precioMin,
            precioMax,
            precioPromedio
        );
    }
    
    // MÉTODOS DE VALIDACIÓN PRIVADOS
    
    /* Valida datos obligatorios */
    private boolean validarDatosObligatorios(Membresia membresia) {
        if (membresia.getTipo() == null || membresia.getTipo().trim().isEmpty()) {
            System.err.println("El tipo de membresía es obligatorio");
            return false;
        }
        
        if (membresia.getPrecio() == null) {
            System.err.println("El precio es obligatorio");
            return false;
        }
        
        if (membresia.getDuracionDias() <= 0) {
            System.err.println("La duración es obligatoria");
            return false;
        }
        
        return true;
    }
    
    /* Valida que el precio sea positivo */
    private boolean validarPrecio(BigDecimal precio) {
        return precio != null && precio.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /* Valida que la duración sea mínimo 30 días */
    private boolean validarDuracion(int duracionDias) {
        return duracionDias >= 30;
    }
}
