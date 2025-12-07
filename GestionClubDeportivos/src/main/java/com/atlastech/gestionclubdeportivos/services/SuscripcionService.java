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
      private final SuscripcionDAO suscripcionDAO;

    public SuscripcionService() {
        this.suscripcionDAO = new SuscripcionDAO();
    }

    // =====================================================
    // REGISTRAR SUSCRIPCIÓN
    // =====================================================
    public boolean registrarSuscripcion(Suscripcion suscripcion) {

        if (suscripcion.getIdSocio() <= 0) {
            System.err.println("El ID del socio es inválido");
            return false;
        }

        if (suscripcion.getIdMembresia() <= 0) {
            System.err.println("El ID de la membresía es inválido");
            return false;
        }

        return suscripcionDAO.registrarSuscripcion(suscripcion);
    }

    // =====================================================
    // OBTENER TODAS LAS SUSCRIPCIONES
    // =====================================================
    public List<Suscripcion> obtenerTodas() {
        return suscripcionDAO.obtenerTodas();
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================
    public Suscripcion obtenerPorId(int id) {
        return suscripcionDAO.obtenerPorId(id);
    }

    // =====================================================
    // ELIMINAR
    // =====================================================
    public boolean eliminar(int id) {
        return suscripcionDAO.eliminar(id);
    }
}