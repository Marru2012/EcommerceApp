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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/** Servicio de flujo completo de compra. */
public class PedidoService {
    private DataStore store = DataStore.getInstance();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Pedido confirmarPedido(Carrito carrito, Usuario usuario, Pago pago) throws Exception {
        if (carrito.estaVacio()) throw new Exception("El carrito esta vacio.");
        for (itemCarrito item : carrito.getItems()) {
            Producto p = store.buscarProducto(item.getProducto().getId());
            if (p == null) throw new Exception("Producto no disponible: " + item.getProducto().getNombre());
            if (p.getStock() < item.getCantidad())
                throw new Exception("Stock insuficiente de '" + p.getNombre() + "'. Disponible: " + p.getStock());
        }
        if (!pago.procesar()) throw new Exception("El pago no pudo procesarse.");
        for (itemCarrito item : carrito.getItems()) {
            Producto p = store.buscarProducto(item.getProducto().getId());
            p.reducirStock(item.getCantidad());
            store.actualizarProducto(p);
        }
        String id = store.generarIdPedido();
        String fecha = LocalDateTime.now().format(FMT);
        Pedido pedido = new Pedido(id, usuario.getId(), usuario.getNombre(),
                carrito.getItems(), carrito.calcularTotal(), fecha);
        pedido.setPago(pago);
        pedido.setEstado(Pedido.Estado.PAGADO);
        store.agregarPedido(pedido);
        if (usuario instanceof Cliente) { ((Cliente) usuario).agregarPedido(pedido); store.guardar(); }
        carrito.vaciar();
        return pedido;
    }

    public ArrayList<Pedido> pedidosCliente(String clienteId) { return store.getPedidosCliente(clienteId); }
    public ArrayList<Pedido> todosPedidos()                   { return store.getPedidos(); }

    public void avanzarEstado(String pedidoId) throws Exception {
        for (Pedido p : store.getPedidos()) {
            if (p.getId().equals(pedidoId)) { p.avanzarEstado(); store.actualizarPedido(p); return; }
        }
        throw new Exception("Pedido no encontrado.");
    }
}


