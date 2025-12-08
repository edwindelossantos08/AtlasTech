/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.atlastech.gestionclubdeportivos;

import com.atlastech.gestionclubdeportivos.dao.DeporteDAO;
import com.atlastech.gestionclubdeportivos.models.Deporte;
import java.util.List;

/**
 *
 * @author bryanpeguerocamilo
 */

public class GestionClubDeportivos {
    
    public static void main(String[] args) {

        DeporteDAO dao = new DeporteDAO();

        System.out.println("=== INICIO PRUEBA DeporteDAO ===");

        // =============================
        // 1. Crear deporte de prueba
        // =============================
        Deporte d = new Deporte();
        d.setNombre("Baloncesto");
        d.setDescripcion("Deporte de prueba automatizada");

        // =============================
        // 2. Insertar
        // =============================
        System.out.println("\nInsertando deporte...");
        boolean insertado = dao.insertarDeporte(d);

        if (!insertado) {
            System.err.println("No se pudo insertar el deporte");
            return;
        }

        System.out.println("insertado correctamente con ID: " + d.getId());

        // =============================
        // 3. Buscar por ID
        // =============================
        System.out.println("\nBuscando por ID...");
        Deporte buscado = dao.obtenerDeportePorId(d.getId());

        if (buscado != null) {
            System.out.println("Encontrado: " + buscado.getNombre());
        } else {
            System.err.println("No se encontró el deporte por ID");
        }

        // =============================
        // 4. Buscar por nombre
        // =============================
        System.out.println("\nBuscando por nombre 'Balon'...");
        Deporte buscadoNombre = dao.buscarDeportePorNombre("Balon");

        if (buscadoNombre != null) {
            System.out.println("Búsqueda OK: " + buscadoNombre.getNombre());
        } else {
            System.err.println("No se encontró el deporte por nombre");
        }

        // =============================
        // 5. Actualizar
        // =============================
        System.out.println("\nActualizando deporte...");
        d.setId(3);
        d.setNombre("Baloncesto Modificado");
        d.setDescripcion("Descripción actualizada");

        boolean actualizado = dao.actualizarDeporte(d);
        System.out.println(actualizado ? "Actualizado" : "Error al actualizar");

        // =============================
        // 6. Listar todos
        // =============================
        System.out.println("\nListando deportes...");
        List<Deporte> lista = dao.obtenerTodosDeportes();
        lista.forEach(dep -> System.out.println(dep.getId() + " - " + dep.getNombre()));


    }
}
