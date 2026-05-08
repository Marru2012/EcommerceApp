/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaapp.ui;

/**
 *
 * @author Dell
 */
import com.mycompany.tiendaapp.model.*;
import com.mycompany.tiendaapp.service.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel principal del cliente: catalogo de productos + carrito de compras.
 * Cubre los requisitos: carrito, flujo de pedido, historial.
 */
public class TiendaClientePanel extends JPanel {
    private AppContext ctx     = AppContext.get();
    private Cliente    cliente;
    private Carrito    carrito;

    // Tabla de catalogo
    private DefaultTableModel modeloCatalogo = new DefaultTableModel(
            new Object[]{"", "Producto", "Categoria", "Precio", "Stock"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private JTable tablaCatalogo = new JTable(modeloCatalogo);

    // Tabla de carrito
    private DefaultTableModel modeloCarrito = new DefaultTableModel(
            new Object[]{"Producto", "Precio unit.", "Cantidad", "Subtotal"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private JTable tablaCarrito = new JTable(modeloCarrito);

    private JLabel lblTotal     = new JLabel("Total: $0.00");
    private JTextField txtBuscar = new JTextField(15);

    public TiendaClientePanel(Cliente cliente) {
        this.cliente = cliente;
        this.carrito = new Carrito(cliente.getId());
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        add(construirBuscador(),  BorderLayout.NORTH);
        add(construirCatalogo(),  BorderLayout.CENTER);
        add(construirCarrito(),   BorderLayout.EAST);

        cargarCatalogo(ctx.productos.listarProductos());
    }

    // ── Buscador ─────────────────────────────────────────────────────────────
    private JPanel construirBuscador() {
        JPanel p  = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton b = new JButton("Buscar");
        b.addActionListener(e -> {
            String txt = txtBuscar.getText().trim();
            if (txt.isEmpty()) cargarCatalogo(ctx.productos.listarProductos());
            else               cargarCatalogo(ctx.productos.buscarPorTexto(txt));
        });
        txtBuscar.addActionListener(e -> b.doClick());
        JButton todos = new JButton("Ver todos");
        todos.addActionListener(e -> {
            txtBuscar.setText("");
            cargarCatalogo(ctx.productos.listarProductos());
        });
        p.add(new JLabel("🔍 Buscar:"));
        p.add(txtBuscar);
        p.add(b);
        p.add(todos);
        return p;
    }

    // ── Catalogo ─────────────────────────────────────────────────────────────
    private JPanel construirCatalogo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new TitledBorder("Catalogo de productos"));

        tablaCatalogo.setRowHeight(24);
        tablaCatalogo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCatalogo.getColumnModel().getColumn(0).setMaxWidth(30);

        JButton btnAgregar = new JButton("➕ Agregar al carrito");
        btnAgregar.setBackground(new Color(0, 120, 215));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.addActionListener(e -> agregarAlCarrito());

        p.add(new JScrollPane(tablaCatalogo), BorderLayout.CENTER);
        p.add(btnAgregar, BorderLayout.SOUTH);
        return p;
    }

    // ── Carrito lateral ───────────────────────────────────────────────────────
    private JPanel construirCarrito() {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setBorder(new TitledBorder("Mi carrito"));
        p.setPreferredSize(new Dimension(340, 0));

        tablaCarrito.setRowHeight(22);
        tablaCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotal.setForeground(new Color(0, 120, 0));

        JButton btnEliminar = new JButton("🗑 Eliminar item");
        JButton btnPagar    = new JButton("💳 Proceder al pago");
        JButton btnVaciar   = new JButton("Vaciar carrito");

        btnEliminar.addActionListener(e -> eliminarDelCarrito());
        btnPagar.addActionListener(e    -> abrirDialogoPago());
        btnVaciar.addActionListener(e   -> vaciarCarrito());

        btnPagar.setBackground(new Color(0, 153, 76));
        btnPagar.setForeground(Color.WHITE);
        btnPagar.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel botones = new JPanel(new GridLayout(3, 1, 4, 4));
        botones.add(btnEliminar);
        botones.add(btnVaciar);
        botones.add(btnPagar);

        JPanel sur = new JPanel(new BorderLayout());
        sur.add(lblTotal, BorderLayout.NORTH);
        sur.add(botones,  BorderLayout.CENTER);

        p.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);
        p.add(sur, BorderLayout.SOUTH);
        return p;
    }

    // ── Cargar catalogo en tabla ──────────────────────────────────────────────
    private void cargarCatalogo(ArrayList<Producto> lista) {
        modeloCatalogo.setRowCount(0);
        for (Producto pr : lista) {
            modeloCatalogo.addRow(new Object[]{
                    pr.getEmoji(), pr.getNombre(), pr.getCategoria(),
                    "$" + String.format("%.2f", pr.getPrecio()), pr.getStock()
            });
        }
    }

    private ArrayList<Producto> getProductosActuales() {
        String txt = txtBuscar.getText().trim();
        return txt.isEmpty()
                ? ctx.productos.listarProductos()
                : ctx.productos.buscarPorTexto(txt);
    }

    // ── Agregar producto al carrito ───────────────────────────────────────────
    private void agregarAlCarrito() {
        int fila = tablaCatalogo.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto primero.");
            return;
        }
        ArrayList<Producto> lista = getProductosActuales();
        if (fila >= lista.size()) return;
        Producto pr = lista.get(fila);

        if (!pr.estaDisponible()) {
            JOptionPane.showMessageDialog(this, "Producto sin stock.");
            return;
        }

        String entrada = JOptionPane.showInputDialog(this,
                "Cantidad a agregar (stock: " + pr.getStock() + "):", "1");
        if (entrada == null) return;

        try {
            int cant = Integer.parseInt(entrada.trim());
            carrito.agregarProducto(pr, cant);
            actualizarTablaCarrito();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un numero valido.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Eliminar del carrito ──────────────────────────────────────────────────
    private void eliminarDelCarrito() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un item."); return; }
        itemCarrito item = carrito.getItems().get(fila);
        try {
            carrito.eliminarProducto(item.getProducto().getId());
            actualizarTablaCarrito();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void vaciarCarrito() {
        carrito.vaciar();
        actualizarTablaCarrito();
    }

    // ── Actualizar vista del carrito ──────────────────────────────────────────
    private void actualizarTablaCarrito() {
        modeloCarrito.setRowCount(0);
        for (itemCarrito item : carrito.getItems()) {
            modeloCarrito.addRow(new Object[]{
                    item.getProducto().getNombre(),
                    "$" + String.format("%.2f", item.getProducto().getPrecio()),
                    item.getCantidad(),
                    "$" + String.format("%.2f", item.getSubtotal())
            });
        }
        lblTotal.setText("Total: $" + String.format("%.2f", carrito.calcularTotal()));
    }

    // ── Dialogo de pago ───────────────────────────────────────────────────────
    private void abrirDialogoPago() {
        if (carrito.estaVacio()) {
            JOptionPane.showMessageDialog(this, "El carrito esta vacio.");
            return;
        }
        new PagoDialog(this, carrito, cliente).setVisible(true);
        // Tras el pago, refrescar catalogo (stock actualizado)
        cargarCatalogo(ctx.productos.listarProductos());
        actualizarTablaCarrito();
    }
}


