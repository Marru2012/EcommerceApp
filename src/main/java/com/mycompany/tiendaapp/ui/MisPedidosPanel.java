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
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

// ═══════════════════════════════════════════════════════════════════════════
// MisPedidosPanel – historial de pedidos del cliente
// ═══════════════════════════════════════════════════════════════════════════
/**
 * Panel donde el cliente consulta sus pedidos anteriores con estado y detalle.
 */
public class MisPedidosPanel extends JPanel{
    
    private AppContext ctx = AppContext.get();
    private Usuario    usuario;

    private DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"# Pedido", "Fecha", "Total", "Estado", "Metodo pago"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private JTable tabla = new JTable(modelo);
    private JTextArea areaDetalle = new JTextArea(6, 30);

    public MisPedidosPanel(Usuario usuario) {
        this.usuario = usuario;
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnRefrescar = new JButton("🔄 Actualizar");
        btnRefrescar.addActionListener(e -> cargarPedidos());

        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norte.add(new JLabel("Historial de mis pedidos"));
        norte.add(btnRefrescar);
        add(norte, BorderLayout.NORTH);

        tabla.setRowHeight(22);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> mostrarDetalle());

        areaDetalle.setEditable(false);
        areaDetalle.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaDetalle.setBorder(new TitledBorder("Detalle del pedido"));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tabla),
                new JScrollPane(areaDetalle));
        split.setDividerLocation(300);
        add(split, BorderLayout.CENTER);

        cargarPedidos();
    }

    private void cargarPedidos() {
        modelo.setRowCount(0);
        ArrayList<Pedido> pedidos = ctx.pedidos.pedidosCliente(usuario.getId());
        for (Pedido p : pedidos) {
            String metodo = p.getPago() != null ? p.getPago().obtenerTipo() : "—";
            modelo.addRow(new Object[]{
                    p.getId(), p.getFecha(),
                    "$" + String.format("%.2f", p.getTotal()),
                    p.getEstadoTexto(), metodo
            });
        }
        if (pedidos.isEmpty()) {
            areaDetalle.setText("No tienes pedidos aun. Ve a la tienda y realiza tu primera compra!");
        }
    }

    private void mostrarDetalle() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        ArrayList<Pedido> pedidos = ctx.pedidos.pedidosCliente(usuario.getId());
        if (fila >= pedidos.size()) return;
        Pedido p = pedidos.get(fila);

        StringBuilder sb = new StringBuilder();
        sb.append("Pedido: ").append(p.getId()).append("\n");
        sb.append("Fecha:  ").append(p.getFecha()).append("\n");
        sb.append("Estado: ").append(p.getEstadoTexto()).append("\n");
        sb.append("Pago:   ").append(p.getPago() != null ? p.getPago().obtenerDetalle() : "—").append("\n");
        sb.append("\nProductos:\n");
        for (itemCarrito item : p.getItems()) {
            sb.append("  - ").append(item.toString()).append("\n");
        }
        sb.append("\nTOTAL: $").append(String.format("%.2f", p.getTotal()));
        areaDetalle.setText(sb.toString());
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// PedidosAdminPanel – administrador ve y gestiona todos los pedidos
// ═══════════════════════════════════════════════════════════════════════════
class PedidosAdminPanel extends JPanel {

    private AppContext ctx = AppContext.get();

    private DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"# Pedido", "Cliente", "Fecha", "Total", "Estado"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private JTable tabla = new JTable(modelo);

    public PedidosAdminPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnRefrescar = new JButton("🔄 Actualizar");
        JButton btnAvanzar   = new JButton("▶ Avanzar estado");

        btnRefrescar.addActionListener(e -> cargarPedidos());
        btnAvanzar.addActionListener(e   -> avanzarEstado());

        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norte.add(new JLabel("Todos los pedidos del sistema:"));
        norte.add(btnRefrescar);
        norte.add(btnAvanzar);
        add(norte, BorderLayout.NORTH);

        tabla.setRowHeight(22);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarPedidos();
    }

    private void cargarPedidos() {
        modelo.setRowCount(0);
        for (Pedido p : ctx.pedidos.todosPedidos()) {
            modelo.addRow(new Object[]{
                    p.getId(), p.getClienteNombre(), p.getFecha(),
                    "$" + String.format("%.2f", p.getTotal()), p.getEstadoTexto()
            });
        }
    }

    private void avanzarEstado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un pedido."); return; }
        ArrayList<Pedido> lista = ctx.pedidos.todosPedidos();
        if (fila >= lista.size()) return;
        String id = lista.get(fila).getId();
        try {
            ctx.pedidos.avanzarEstado(id);
            cargarPedidos();
            JOptionPane.showMessageDialog(this, "Estado actualizado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// PerfilPanel – muestra informacion del usuario actual
// ═══════════════════════════════════════════════════════════════════════════
class PerfilPanel extends JPanel {

    public PerfilPanel(Usuario usuario) {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(30, 50, 30, 50));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill   = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("👤 Mi perfil", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        g.gridx=0; g.gridy=0; g.gridwidth=2; add(titulo, g);

        // POLIMORFISMO visible: obtenerDescripcionRol() llama subclase correcta
        JLabel rol = new JLabel(usuario.obtenerDescripcionRol(), SwingConstants.CENTER);
        rol.setForeground(new Color(0, 100, 180));
        g.gridy=1; add(rol, g);

        g.gridwidth=1;
        addFila("Nombre:",  usuario.getNombre(),  g, 2);
        addFila("Email:",   usuario.getEmail(),   g, 3);
        addFila("Rol:",     usuario.getRol(),     g, 4);
        addFila("ID:",      usuario.getId(),      g, 5);

        if (usuario instanceof Cliente) {
            Cliente c = (Cliente) usuario;
            addFila("Direccion:", c.getDireccion().isEmpty() ? "(no registrada)" : c.getDireccion(), g, 6);
            addFila("Telefono:",  c.getTelefono().isEmpty()  ? "(no registrado)"  : c.getTelefono(),  g, 7);
        }
        if (usuario instanceof Administrador) {
            Administrador a = (Administrador) usuario;
            addFila("Nivel de acceso:", a.getNivelAcceso(), g, 6);
        }
    }

    private void addFila(String etiqueta, String valor, GridBagConstraints g, int fila) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        JLabel val = new JLabel(valor);
        val.setFont(new Font("Arial", Font.PLAIN, 13));
        g.gridx=0; g.gridy=fila; add(lbl, g);
        g.gridx=1;               add(val, g);
    }
}


