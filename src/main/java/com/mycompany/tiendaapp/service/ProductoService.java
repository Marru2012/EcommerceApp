/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaapp.service;

/**
 *
 * @author Dell
 */
import com.mycompany.tiendaapp.dao.*;
import com.mycompany.tiendaapp.model.*;
import java.util.ArrayList;

/** Servicio CRUD de productos. */
public class ProductoService {
    private DataStore store = DataStore.getInstance();

    public ArrayList<Producto> listarProductos() { return store.getProductos(); }

    public ArrayList<Producto> buscarPorTexto(String txt) {
        ArrayList<Producto> res = new ArrayList<>();
        String low = txt.toLowerCase();
        for (Producto p : store.getProductos())
            if (p.getNombre().toLowerCase().contains(low)
             || p.getCategoria().toLowerCase().contains(low)
             || p.getDescripcion().toLowerCase().contains(low)) res.add(p);
        return res;
    }

    public Producto obtener(String id) throws Exception {
        Producto p = store.buscarProducto(id);
        if (p == null) throw new Exception("Producto no encontrado: " + id);
        return p;
    }

    public void crear(String nombre, String desc, double precio, int stock, String cat, String emoji) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) throw new Exception("El nombre es obligatorio.");
        if (precio < 0) throw new Exception("El precio no puede ser negativo.");
        if (stock  < 0) throw new Exception("El stock no puede ser negativo.");
        store.agregarProducto(new Producto(store.generarIdProducto(), nombre.trim(), desc.trim(), precio, stock, cat.trim(), emoji));
    }

    public void actualizar(String id, String nombre, String desc, double precio, int stock, String cat) throws Exception {
        Producto p = obtener(id);
        p.setNombre(nombre.trim()); p.setDescripcion(desc.trim());
        p.setPrecio(precio); p.setStock(stock); p.setCategoria(cat.trim());
        store.actualizarProducto(p);
    }

    public void eliminar(String id) throws Exception { store.eliminarProducto(id); }

    public ArrayList<String> getCategorias() {
        ArrayList<String> cats = new ArrayList<>();
        for (Producto p : store.getProductos())
            if (!cats.contains(p.getCategoria())) cats.add(p.getCategoria());
        return cats;
    }
}


