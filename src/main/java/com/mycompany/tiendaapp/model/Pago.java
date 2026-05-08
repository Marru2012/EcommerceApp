package com.mycompany.tiendaapp.model;

import java.io.Serializable;

public abstract class Pago implements Serializable {
    private static final long serialVersionUID = 1L;

    // ENCAPSULACION
    private String  id;
    private double  monto;
    private String  fecha;
    private boolean procesado;

    public Pago(String id, double monto, String fecha) {
        this.id        = id;
        this.monto     = monto;
        this.fecha     = fecha;
        this.procesado = false;
    }

    // Metodos abstractos — POLIMORFISMO: cada subclase los implementa con @Override
    public abstract String  obtenerTipo();
    public abstract String  obtenerDetalle();
    public abstract boolean procesar() throws Exception;

    // Getters y Setters
    public String getId(){ 
        return id; 
    }
    public void setId(String id){ 
        this.id = id; 
    }

    public double getMonto(){ 
        return monto; 
    }
    public void setMonto(double m){ 
        this.monto = m; 
    }

    public String getFecha(){ 
        return fecha; 
    }
    public void   setFecha(String f){ 
        this.fecha = f; 
    }

    public boolean isProcesado()           { return procesado; }
    public void    setProcesado(boolean p) { this.procesado = p; }

    @Override
    public String toString() {
        return obtenerTipo() + " – $" + String.format("%.2f", monto);
    }
}
