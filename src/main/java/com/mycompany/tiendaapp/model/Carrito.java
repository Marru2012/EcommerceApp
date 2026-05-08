package com.mycompany.tiendaapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Carrito implements Serializable {
        private static final long serialVersionUID = 1L;

    // ENCAPSULACION
    private String            clienteId;
    private ArrayList<itemCarrito> items; // COLECCIONES

    public Carrito(String clienteId) {
        this.clienteId = clienteId;
        this.items     = new ArrayList<>();
    }

    /** Agrega un producto. Si ya existe en el carrito, suma la cantidad. */
    public void agregarProducto(Producto producto, int cantidad) throws Exception {
        if (cantidad <= 0)
            throw new Exception("La cantidad debe ser mayor a 0.");
        if (cantidad > producto.getStock())
            throw new Exception("Stock insuficiente. Disponible: " + producto.getStock());

        for (itemCarrito item : items) {
            if (item.getProducto().getId().equals(producto.getId())) {
                int nueva = item.getCantidad() + cantidad;
                if (nueva > producto.getStock())
                    throw new Exception("No puede agregar mas. Stock disponible: " + producto.getStock());
                item.setCantidad(nueva);
                return;
            }
        }
        items.add(new itemCarrito(producto, cantidad));
    }

    /** Elimina un item por ID de producto. */
    public void eliminarProducto(String productoId) throws Exception {
        boolean ok = items.removeIf(i -> i.getProducto().getId().equals(productoId));
        if (!ok) throw new Exception("Producto no encontrado en el carrito.");
    }

    /** Modifica la cantidad de un item. Si es 0 o menos, lo elimina. */
    public void modificarCantidad(String productoId, int nuevaCantidad) throws Exception {
        if (nuevaCantidad <= 0) { eliminarProducto(productoId); return; }
        for (itemCarrito item : items) {
            if (item.getProducto().getId().equals(productoId)) {
                if (nuevaCantidad > item.getProducto().getStock())
                    throw new Exception("Stock insuficiente. Disponible: "
                            + item.getProducto().getStock());
                item.setCantidad(nuevaCantidad);
                return;
            }
        }
        throw new Exception("Producto no encontrado en el carrito.");
    }

    /** Calcula el total en tiempo real. */
    public double calcularTotal() {
        double total = 0;
        for (itemCarrito item : items) total += item.getSubtotal();
        return total;
    }

    public int getTotalItems() {
        int t = 0;
        for (itemCarrito item : items) t += item.getCantidad();
        return t;
    }

    public void   vaciar(){ 
        items.clear(); 
    }
    public boolean estaVacio(){ 
        return items.isEmpty(); 
    }

    public String getClienteId(){ 
        return clienteId; 
    }
    public void setClienteId(String id){ 
        this.clienteId = id; 
    }
    public ArrayList<itemCarrito> getItems(){ 
        return items; 
    }
    public void setItems(ArrayList<itemCarrito> i) { 
        this.items = i; 
    }
}

