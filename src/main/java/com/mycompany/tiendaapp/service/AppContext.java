package com.mycompany.tiendaapp.service;
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



