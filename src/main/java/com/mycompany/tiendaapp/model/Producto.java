package com.mycompany.tiendaapp.model;

import java.io.Serializable;

public class Producto {
    private static final long serialVersionUID = 1L;

    // ENCAPSULACION
    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int    stock;
    private String categoria;
    private String emoji;

    public Producto(String id, String nombre, String descripcion,
                    double precio, int stock, String categoria, String emoji) {
        this.id          = id;
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.precio      = precio;
        this.stock       = stock;
        this.categoria   = categoria;
        this.emoji       = emoji;
    }

    /**
     * Reduce el stock tras una venta.
     * MANEJO DE EXCEPCIONES: lanza excepcion si el stock es insuficiente.
     */
    public void reducirStock(int cantidad) throws Exception {
        if (cantidad <= 0)
            throw new Exception("La cantidad debe ser mayor a 0.");
        if (cantidad > this.stock)
            throw new Exception("Stock insuficiente. Disponible: " + this.stock
                    + ", solicitado: " + cantidad);
        this.stock -= cantidad;
    }

    public void aumentarStock(int cantidad) throws Exception {
        if (cantidad <= 0) throw new Exception("La cantidad debe ser mayor a 0.");
        this.stock += cantidad;
    }

    public boolean estaDisponible() { return stock > 0; }

    // Getters y Setters
    public String getId(){ 
        return id; 
    }
    public void   setId(String id){ 
        this.id = id; 
    }

    public String getNombre() { 
        return nombre; 
    }
    public void   setNombre(String n){ 
        this.nombre = n; 
    }

    public String getDescripcion(){ 
        return descripcion; 
    }
    public void   setDescripcion(String d){ 
        this.descripcion = d; 
    }

    public double getPrecio(){ 
        return precio; 
    }
    public void   setPrecio(double p){ 
        this.precio = p; 
    }

    public int  getStock(){ 
        return stock; 
    }
    public void setStock(int s){ 
        this.stock = s; 
    }

    public String getCategoria(){ 
        return categoria; 
    }
    public void   setCategoria(String c){ 
        this.categoria = c; 
    }

    public String getEmoji(){ 
        return emoji; 
    }
    public void   setEmoji(String e){ 
        this.emoji = e; 
    }

    @Override
    public String toString() {
        return emoji + " " + nombre + "  –  $" + String.format("%.2f", precio)
                + "  (Stock: " + stock + ")";
    }
}


