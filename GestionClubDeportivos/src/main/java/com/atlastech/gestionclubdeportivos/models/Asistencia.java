package com.atlastech.gestionclubdeportivos.models;

import java.time.LocalDateTime;
import java.time.Duration;
/**
 *
 * @author Alexander
 */
public class Asistencia {
    private int id;
    private int idSocio;
    private Integer idInstalacion;
    private LocalDateTime fechaHoraEntrada;
    private LocalDateTime fechaHoraSalida;
    private String tipoAcceso;

    //Constructor
    public Asistencia() {
        this.fechaHoraEntrada = LocalDateTime.now();
    }

    //Constructor con parametros principales
    public Asistencia(int idSocio, String tipoAcceso) {
        this();
        this.idSocio = idSocio;
        this.tipoAcceso = tipoAcceso;
    }
 
    //Constructor completo
    public Asistencia(int id, int idSocio, Integer idInstalacion, LocalDateTime fechaHoraEntrada,
                     LocalDateTime fechaHoraSalida, String tipoAcceso) {
        this.id = id;
        this.idSocio = idSocio;
        this.idInstalacion = idInstalacion;
        this.fechaHoraEntrada = fechaHoraEntrada;
        this.fechaHoraSalida = fechaHoraSalida;
        this.tipoAcceso = tipoAcceso;
    }

    public boolean isEnCurso() {
        return fechaHoraSalida == null;
    }

    public long getTiempoPermanenciaMinutos() {
        if (fechaHoraSalida == null) {
            return Duration.between(fechaHoraEntrada, LocalDateTime.now()).toMinutes();
        }
        return Duration.between(fechaHoraEntrada, fechaHoraSalida).toMinutes();
    }

    // Getters y Setters
    public int getId() {return id; }
    public void setId(int id) {this.id = id; }
    
    public int getIdSocio() {return idSocio; }
    public void setIdSocio(int idSocio) {this.idSocio = idSocio; }
    
    public Integer getIdInstalacion() {return idInstalacion; }
    public void setIdInstalacion(Integer idInstalacion) {this.idInstalacion = idInstalacion; }
    
    public LocalDateTime getFechaHoraEntrada() {return fechaHoraEntrada; }
    public void setFechaHoraEntrada(LocalDateTime fechaHoraEntrada) {this.fechaHoraEntrada = fechaHoraEntrada; }
    
    public LocalDateTime getFechaHoraSalida() {return fechaHoraSalida; }
    public void setFechaHoraSalida(LocalDateTime fechaHoraSalida) {this.fechaHoraSalida = fechaHoraSalida; }
    
    public String getTipoAcceso() {return tipoAcceso; }
    public void setTipoAcceso(String tipoAcceso) {this.tipoAcceso = tipoAcceso; }

    @Override
    public String toString() {
        return String.format("Asistencia{id=%d, socio=%d, entrada=%s, enCurso=%s}",
                id, idSocio, fechaHoraEntrada, isEnCurso() ? "SÍ" : "NO");
    }
    
}
