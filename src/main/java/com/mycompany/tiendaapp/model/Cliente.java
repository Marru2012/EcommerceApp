package com.mycompany.tiendaapp.model;

import java.util.ArrayList;

public class Cliente extends Usuario {
    private static final long serialVersionUID = 1L;

    // ENCAPSULACION
    private String direccion;
    private String telefono;
    private ArrayList<Pedido> historialPedidos; // COLECCIONES

    public Cliente(String id, String nombre, String email, String password) {
        super(id, nombre, email, password, "CLIENTE");
        this.historialPedidos = new ArrayList<>();
        this.direccion = "";
        this.telefono  = "";
    }

    // POLIMORFISMO: @Override del metodo abstracto de Usuario
    @Override
    public String obtenerDescripcionRol() {
        return "Cliente registrado con acceso a compras y seguimiento de pedidos.";
    }

    public void agregarPedido(Pedido pedido) {
        historialPedidos.add(pedido);
    }

    // Getters y Setters
    public String getDireccion()                              { return direccion; }
    public void   setDireccion(String d)                      { this.direccion = d; }

    public String getTelefono()                               { return telefono; }
    public void   setTelefono(String t)                       { this.telefono = t; }

    public ArrayList<Pedido> getHistorialPedidos()            { return historialPedidos; }
    public void setHistorialPedidos(ArrayList<Pedido> lista)  { this.historialPedidos = lista; }
}
