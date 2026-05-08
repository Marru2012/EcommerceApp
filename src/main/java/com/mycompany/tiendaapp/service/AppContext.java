/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaapp.service;

/**
 *
 * @author Dell
 */
public class AppContext {
    private static AppContext instancia;

    public final AuthService     auth      = new AuthService();
    public final ProductoService productos = new ProductoService();
    public final PedidoService   pedidos   = new PedidoService();

    private AppContext() {}

    public static AppContext get() {
        if (instancia == null) instancia = new AppContext();
        return instancia;
    }
}



