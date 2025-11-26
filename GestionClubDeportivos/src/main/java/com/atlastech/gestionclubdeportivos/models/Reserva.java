
package com.atlastech.gestionclubdeportivos.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Alexander
 */
public class Reserva {
    private int id;
    private int idSocio;
    private int idInstalacion;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
    private String tipoReserva;
    private BigDecimal costo;
    private String notas;
    
    //Constructor
    public Reserva() {
        this.estado = "confirmada";
    }
    
    //Constructor con parametros principales
    public Reserva(int idSocio, int idInstalacion, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, BigDecimal costo) {
        this();
        this.idSocio = idSocio;
        this.idInstalacion = idInstalacion;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.costo = costo;
    }
    
    //Constructor completo
    public Reserva(int id, int idSocio, int idInstalacion, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String estado, String tipoReserva, BigDecimal costo, String notas) {
        this.id = id;
        this.idSocio = idSocio;
        this.idInstalacion = idInstalacion;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
        this.tipoReserva = tipoReserva;
        this.costo = costo;
        this.notas = notas;
    }
    
    //Metodo de negocio
    
    public int getDuracionHoras() {
        if (horaInicio == null || horaFin == null) return 0;
        return horaFin.getHour() - horaInicio.getHour();
    }
    
    public boolean isActiva() {
        return "confirmada".equalsIgnoreCase(estado);
    }
    
    //Getters y Setters
    public int getId() {return id; }
    public void setId(int id) {this.id = id; }
    
    public int getIdSocio() {return idSocio; }
    public void setIdSocio(int idSocio) {this.idSocio = idSocio; }
    
    public int getIdInstalacion() {return idInstalacion; }
    public void setIdInstalacion(int idInstalacion) {this.idInstalacion = idInstalacion; }
    
    public LocalDate getFecha() {return fecha; }
    public void setFecha(LocalDate fecha) {this.fecha = fecha; }
    
    public LocalTime getHoraInicio() {return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) {this.horaInicio = horaInicio; }
    
    public LocalTime getHoraFin() {return horaFin; }
    public void setHoraFin(LocalTime horaFin) {this.horaFin = horaFin; }
    
    public String getEstado() {return estado; }
    public void setEstado(String estado) {this.estado = estado; }
    
    public String getTipoReserva() {return tipoReserva; }
    public void setTipoReserva(String tipoReserva) {this.tipoReserva = tipoReserva; }
    
    public BigDecimal getCosto() {return costo; }
    public void setCosto(BigDecimal costo) {this.costo = costo; }
    
    public String getNotas() {return notas; }
    public void setNotas(String notas) {this.notas = notas; }
    
    @Override
    public String toString() {
        return String.format("Reserva{id=%d, fecha=%s, horario=%s-%s}",
                id, fecha, horaInicio, horaFin);
    }
    
}
