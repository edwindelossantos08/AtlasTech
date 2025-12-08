/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.atlastech.gestionclubdeportivos;

import com.atlastech.gestionclubdeportivos.dao.UsuarioDAO;
import com.atlastech.gestionclubdeportivos.models.Usuario;
import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author bryanpeguerocamilo
 */

public class GestionClubDeportivos {
    
    
    public static void main(String[] args) {

        UsuarioDAO dao = new UsuarioDAO();

        // ==========================================
        // 1. PROBAR INSERTAR
        // ==========================================
        Usuario u = new Usuario();
        u.setNombreUsuario("prueba1");
        u.setEmail("prueba1@example.com");   // Necesario por tu tabla
        u.setContraseña("12345");
        u.setTipoUsuario("administrador");  
        u.setIdSocio(1);     // NULL permitido según tu diseño
        u.setEstado(true);

        boolean ok = dao.insertar(u);

        if (ok) {
            System.out.println("✔ Usuario insertado correctamente. ID: " + u.getId());
        } else {
            System.out.println("❌ Error al insertar usuario.");
            return;
        }

        // ==========================================
        // 2. PROBAR AUTENTICAR
        // ==========================================
        Usuario login = dao.autenticar("prueba1", "12345");

        if (login != null) {
            System.out.println("\n✔ AUTENTICADO:");
            System.out.println("ID: " + login.getId());
            System.out.println("Usuario: " + login.getNombreUsuario());
            System.out.println("Tipo: " + login.getTipoUsuario());
        } else {
            System.out.println("\n❌ Error al autenticar.");
        }

        // ==========================================
        // 3. PROBAR OBTENER POR ID
        // ==========================================
        Usuario buscado = dao.obtenerPorId(u.getId());

        if (buscado != null) {
            System.out.println("\n✔ ENCONTRADO POR ID:");
            System.out.println("Usuario: " + buscado.getNombreUsuario());
            System.out.println("Estado: " + buscado.isEstado());
        } else {
            System.out.println("\n❌ No se encontró el usuario por ID.");
        }

        // ==========================================
        // 4. PROBAR LISTAR TODOS
        // ==========================================
        System.out.println("\n🔎 LISTA DE USUARIOS:");
        for (Usuario user : dao.obtenerTodos()) {
            System.out.println("- " + user.getId() + " | " + user.getNombreUsuario());
        }

        // ==========================================
        // 5. PROBAR ACTUALIZAR
        // ==========================================
        buscado.setNombreUsuario("prueba1_editado");
        buscado.setEstado(false);

        if (dao.actualizar(buscado)) {
            System.out.println("\n✔ Usuario actualizado correctamente.");
        } else {
            System.out.println("\n❌ Error al actualizar usuario.");
        }


    }
}
