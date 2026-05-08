package com.mycompany.tiendaapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Estado {
        PENDIENTE, PAGADO, EN_PROCESO, ENVIADO, ENTREGADO, CANCELADO
    }

    // ENCAPSULACION
    private String id;
    private String clienteId;
    private String clienteNombre;
    private ArrayList<ItemCarrito> items; // COLECCIONES
    private double total;
    private Estado estado;
    private String fecha;
    private Pago   pago;
    private String direccionEntrega;

    public Pedido(String id, String clienteId, String clienteNombre,
                  ArrayList<ItemCarrito> items, double total, String fecha) {
        this.id             = id;
        this.clienteId      = clienteId;
        this.clienteNombre  = clienteNombre;
        this.items          = new ArrayList<>(items);
        this.total          = total;
        this.estado         = Estado.PENDIENTE;
        this.fecha          = fecha;
    }

    public void avanzarEstado() {
        switch (estado) {
            case PENDIENTE:  estado = Estado.PAGADO;     break;
            case PAGADO:     estado = Estado.EN_PROCESO; break;
            case EN_PROCESO: estado = Estado.ENVIADO;    break;
            case ENVIADO:    estado = Estado.ENTREGADO;  break;
            default: break;
        }
    }

    public String getEstadoTexto() {
        switch (estado) {
            case PENDIENTE:  return "Pendiente";
            case PAGADO:     return "Pagado";
            case EN_PROCESO: return "En proceso";
            case ENVIADO:    return "Enviado";
            case ENTREGADO:  return "Entregado";
            case CANCELADO:  return "Cancelado";
            default:         return "Desconocido";
        }
    }

    // Getters y Setters
    public String getId()                              { return id; }
    public void   setId(String id)                     { this.id = id; }
    public String getClienteId()                       { return clienteId; }
    public void   setClienteId(String c)               { this.clienteId = c; }
    public String getClienteNombre()                   { return clienteNombre; }
    public void   setClienteNombre(String n)           { this.clienteNombre = n; }
    public ArrayList<ItemCarrito> getItems()            { return items; }
    public void   setItems(ArrayList<ItemCarrito> i)    { this.items = i; }
    public double getTotal()                           { return total; }
    public void   setTotal(double t)                   { this.total = t; }
    public Estado getEstado()                          { return estado; }
    public void   setEstado(Estado e)                  { this.estado = e; }
    public String getFecha()                           { return fecha; }
    public void   setFecha(String f)                   { this.fecha = f; }
    public Pago   getPago()                            { return pago; }
    public void   setPago(Pago p)                      { this.pago = p; }
    public String getDireccionEntrega()                { return direccionEntrega; }
    public void   setDireccionEntrega(String d)        { this.direccionEntrega = d; }

    @Override
    public String toString() {
        return "Pedido #" + id + " | " + fecha + " | $"
                + String.format("%.2f", total) + " | " + getEstadoTexto();
    }
}