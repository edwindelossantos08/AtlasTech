package com.atlastech.gestionclubdeportivos.models;

import java.time.LocalDateTime;

/**
 *
 * @author Alexander
 */
public class Partido {
    private int id;
    private int idTorneo;
    private int idInstalacion;
    private String equipoLocal;
    private String equipoVisitante;
    private String resultadoLocal;
    private String resultadoVisitante;
    private LocalDateTime fechaHora;
    private boolean estado;
    
    //Constructor
    public Partido() {
        this.estado = true;
    }
    
    //Constructor con parametros principales
    public Partido(int idTorneo, int idInstalacion, String equipoLocal, String equipoVisitante, LocalDateTime fechaHora) {
        this();
        this.idTorneo = idTorneo;
        this.idInstalacion = idInstalacion;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.fechaHora = fechaHora;
    }
    
    //Constructor completo
     public Partido(int id, int idTorneo, int idInstalacion, String equipoLocal, String equipoVisitante,
                  String resultadoLocal, String resultadoVisitante, LocalDateTime fechaHora, boolean estado) {
        this.id = id;
        this.idTorneo = idTorneo;
        this.idInstalacion = idInstalacion;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.resultadoLocal = resultadoLocal;
        this.resultadoVisitante = resultadoVisitante;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }
    
     public boolean tieneResultado() {
        return resultadoLocal != null && resultadoVisitante != null;
    }

    // Getters y Setters
    public int getId() {return id; }
    public void setId(int id) {this.id = id; }
    
    public int getIdTorneo() {return idTorneo; }
    public void setIdTorneo(int idTorneo) {this.idTorneo = idTorneo; }
    
    public int getIdInstalacion() {return idInstalacion; }
    public void setIdInstalacion(int idInstalacion) {this.idInstalacion = idInstalacion; }
    
    public String getEquipoLocal() {return equipoLocal; }
    public void setEquipoLocal(String equipoLocal) {this.equipoLocal = equipoLocal; }
    
    public String getEquipoVisitante() {return equipoVisitante; }
    public void setEquipoVisitante(String equipoVisitante) {this.equipoVisitante = equipoVisitante; }
    
    public String getResultadoLocal() {return resultadoLocal; }
    public void setResultadoLocal(String resultadoLocal) {this.resultadoLocal = resultadoLocal; }
    
    public String getResultadoVisitante() {return resultadoVisitante; }
    public void setResultadoVisitante(String resultadoVisitante) {this.resultadoVisitante = resultadoVisitante; }
    
    public LocalDateTime getFechaHora() {return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) {this.fechaHora = fechaHora; }
    
    public boolean isEstado() {return estado; }
    public void setEstado(boolean estado) {this.estado = estado; }

    @Override
    public String toString() {
        return String.format("Partido{id=%d, %s vs %s, fecha=%s}",
                id, equipoLocal, equipoVisitante, fechaHora);
    }
}
