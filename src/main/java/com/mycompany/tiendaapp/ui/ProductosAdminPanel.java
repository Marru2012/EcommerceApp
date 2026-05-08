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

/**
 * Panel de gestion de productos para el Administrador.
 * Permite: listar, crear, editar y eliminar productos.
 *
 */
public class ProductosAdminPanel extends JPanel {
    private AppContext ctx = AppContext.get();

    private DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"", "ID", "Nombre", "Categoria", "Precio", "Stock"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private JTable tabla = new JTable(modelo);

    // Formulario
    private JTextField txtNombre = new JTextField(20);
    private JTextField txtDesc   = new JTextField(20);
    private JTextField txtPrecio = new JTextField(10);
    private JTextField txtStock  = new JTextField(6);
    private JTextField txtCat    = new JTextField(12);
    private JTextField txtEmoji  = new JTextField(4);

    private JButton btnCrear    = new JButton("➕ Crear");
    private JButton btnActualizar = new JButton("✏️ Actualizar");
    private JButton btnEliminar = new JButton("🗑 Eliminar");
    private JButton btnLimpiar  = new JButton("Limpiar");

    private String idSeleccionado = null;

    public ProductosAdminPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        add(construirTabla(),       BorderLayout.CENTER);
        add(construirFormulario(),  BorderLayout.EAST);

        cargarTabla();
    }

    // ── Tabla de productos ────────────────────────────────────────────────────
    private JPanel construirTabla() {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setBorder(new TitledBorder("Lista de productos"));

        tabla.setRowHeight(24);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(30);
        tabla.getColumnModel().getColumn(1).setMaxWidth(80);

        tabla.getSelectionModel().addListSelectionListener(e -> cargarEnFormulario());

        JButton btnRefrescar = new JButton("🔄 Refrescar");
        btnRefrescar.addActionListener(e -> { idSeleccionado = null; limpiarFormulario(); cargarTabla(); });

        p.add(new JScrollPane(tabla), BorderLayout.CENTER);
        p.add(btnRefrescar, BorderLayout.SOUTH);
        return p;
    }

    // ── Formulario lateral ────────────────────────────────────────────────────
    private JPanel construirFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(new TitledBorder("Datos del producto"));
        p.setPreferredSize(new Dimension(280, 0));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.fill   = GridBagConstraints.HORIZONTAL;

        String[][] campos = {
            {"Nombre:",      ""}, {"Descripcion:", ""},
            {"Precio ($):",  ""}, {"Stock:",       ""},
            {"Categoria:",   ""}, {"Emoji:",       ""}
        };
        JTextField[] campos_tf = {txtNombre, txtDesc, txtPrecio, txtStock, txtCat, txtEmoji};

        for (int i = 0; i < campos.length; i++) {
            g.gridx=0; g.gridy=i; g.gridwidth=1;
            p.add(new JLabel(campos[i][0]), g);
            g.gridx=1;
            p.add(campos_tf[i], g);
        }

        txtEmoji.setText("📦");

        // Botones
        btnCrear.setBackground(new Color(0, 120, 215));
        btnCrear.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(255, 140, 0));
        btnActualizar.setForeground(Color.WHITE);
        btnEliminar.setBackground(new Color(200, 30, 30));
        btnEliminar.setForeground(Color.WHITE);

        btnCrear.addActionListener(e     -> crearProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());
        btnEliminar.addActionListener(e   -> eliminarProducto());
        btnLimpiar.addActionListener(e    -> { idSeleccionado = null; limpiarFormulario(); });

        g.gridx=0; g.gridy=6; g.gridwidth=2; p.add(btnCrear,     g);
        g.gridy=7;                             p.add(btnActualizar, g);
        g.gridy=8;                             p.add(btnEliminar,   g);
        g.gridy=9;                             p.add(btnLimpiar,    g);

        JLabel nota = new JLabel("<html><font color=gray size=2>Selecciona una fila<br>para editar o eliminar.</font></html>");
        g.gridy=10; p.add(nota, g);

        return p;
    }

    // ── Cargar tabla ──────────────────────────────────────────────────────────
    private void cargarTabla() {
        modelo.setRowCount(0);
        for (Producto pr : ctx.productos.listarProductos()) {
            modelo.addRow(new Object[]{
                    pr.getEmoji(), pr.getId(), pr.getNombre(),
                    pr.getCategoria(),
                    "$" + String.format("%.2f", pr.getPrecio()),
                    pr.getStock()
            });
        }
    }

    // ── Cargar producto seleccionado en formulario ─────────────────────────────
    private void cargarEnFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        ArrayList<Producto> lista = ctx.productos.listarProductos();
        if (fila >= lista.size()) return;
        Producto pr = lista.get(fila);

        idSeleccionado = pr.getId();
        txtNombre.setText(pr.getNombre());
        txtDesc.setText(pr.getDescripcion());
        txtPrecio.setText(String.valueOf(pr.getPrecio()));
        txtStock.setText(String.valueOf(pr.getStock()));
        txtCat.setText(pr.getCategoria());
        txtEmoji.setText(pr.getEmoji());
    }

    private void limpiarFormulario() {
        txtNombre.setText(""); txtDesc.setText(""); txtPrecio.setText("");
        txtStock.setText("");  txtCat.setText(""); txtEmoji.setText("📦");
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────
    private void crearProducto() {
        try {
            ctx.productos.crear(
                    txtNombre.getText(), txtDesc.getText(),
                    Double.parseDouble(txtPrecio.getText().trim()),
                    Integer.parseInt(txtStock.getText().trim()),
                    txtCat.getText(), txtEmoji.getText().trim()
            );
            cargarTabla();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Producto creado correctamente.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y stock deben ser numeros validos.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarProducto() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para actualizar.");
            return;
        }
        try {
            ctx.productos.actualizar(
                    idSeleccionado,
                    txtNombre.getText(), txtDesc.getText(),
                    Double.parseDouble(txtPrecio.getText().trim()),
                    Integer.parseInt(txtStock.getText().trim()),
                    txtCat.getText()
            );
            cargarTabla();
            limpiarFormulario();
            idSeleccionado = null;
            JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y stock deben ser numeros validos.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar este producto?",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            ctx.productos.eliminar(idSeleccionado);
            cargarTabla();
            limpiarFormulario();
            idSeleccionado = null;
            JOptionPane.showMessageDialog(this, "Producto eliminado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


