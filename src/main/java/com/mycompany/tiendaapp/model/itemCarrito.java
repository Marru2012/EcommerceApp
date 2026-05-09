package com.mycompany.tiendaapp.model;

import java.io.Serializable;
import java.util.ArrayList;


public class itemCarrito implements Serializable {
        private static final long serialVersionUID = 1L;

    private Producto producto;
    private int      cantidad;

    public itemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    public Producto getProducto(){ 
        return producto; 
    }
    public void setProducto(Producto p){ 
        this.producto = p; 
    }

    public int  getCantidad(){ 
        return cantidad; 
    }
    public void setCantidad(int c){ 
        this.cantidad = c; 
    }

    @Override
    public String toString() {
        return producto.getNombre() + " x" + cantidad
                + " = $" + String.format("%.2f", getSubtotal());
    }
}

