
package com.atlastech.gestionclubdeportivos.models;
import java.math.BigDecimal;

/**
 *
 * @author Alexander
 */
public class Pago {
    private int id;
    private Integer idSuscripcion;
    private Integer idReserva;
    private String concepto;
    private BigDecimal monto;
    private String metodoPago;
    private String estado;
    private String comprobanteUrl;
    
    //Constructor
    public Pago() {
        this.estado = "pendiente";
    }
    
    //Constructor con parametros principales
    public Pago(String concepto, BigDecimal monto, String metodoPago) {
        this();
        this.concepto = concepto;
        this.monto = monto;
        this.metodoPago = metodoPago;
    }
    
    //Constructor completo
    public Pago(int id, Integer idSuscripcion, Integer idReserva, Integer idTorneo, String concepto, BigDecimal monto, String metodoPago, String estado, String comprobanteUrl) {
        this.id = id;
        this.idSuscripcion = idSuscripcion;
        this.idReserva = idReserva;
        this.concepto = concepto;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.estado = estado;
        this.comprobanteUrl = comprobanteUrl;
    }
    
    public boolean isPagado() {
        return "pagado".equalsIgnoreCase(estado);
    }
    
    //Getters y Setters
    public int getId() {return id;}
    public void setId (int id) {this.id = id; }
    
    public Integer getIdSuscripcion() { return idSuscripcion; }
    public void setIdSuscripcion(Integer idSuscripcion) { this.idSuscripcion = idSuscripcion; }
    
    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }
    
    
    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }
    
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getComprobanteUrl() { return comprobanteUrl; }
    public void setComprobanteUrl(String comprobanteUrl) { this.comprobanteUrl = comprobanteUrl; }
    
     @Override
    public String toString() {
        return String.format("Pago{id=%d, concepto='%s', monto=$%.2f, estado=%s}",
                id, concepto, monto, estado);
    }

    public Object getDescripcion() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object getFechaPago() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    
}
