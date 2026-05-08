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
import java.awt.*;

 //Dialogo modal para seleccionar metodo de pago y confirmar pedido.
public class PagoDialog extends JDialog {
    private AppContext ctx    = AppContext.get();
    private Carrito    carrito;
    private Cliente    cliente;

    private JComboBox<String> cbMetodo = new JComboBox<>(
            new String[]{"Tarjeta de Credito", "PayPal", "Efectivo/Transferencia"});
    private JPanel panelDatos = new JPanel(new CardLayout());

    // Campos tarjeta
    private JTextField  txtDigitos = new JTextField(4);
    private JTextField  txtTitular = new JTextField(20);

    // Campos PayPal
    private JTextField  txtPayPal  = new JTextField(20);

    // Campos efectivo
    private JTextField  txtRef     = new JTextField(20);

    public PagoDialog(JComponent parent, Carrito carrito, Cliente cliente) {
        super(SwingUtilities.getWindowAncestor(parent) instanceof Frame ? (Frame) SwingUtilities.getWindowAncestor(parent) : null, "Finalizar compra", true);
        this.carrito = carrito;
        this.cliente = cliente;

        setSize(420, 380);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Resumen
        JLabel resumen = new JLabel(
                "<html><b>Total a pagar: $"
                + String.format("%.2f", carrito.calcularTotal())
                + "</b><br>Items: " + carrito.getTotalItems() + "</html>");
        resumen.setFont(new Font("Arial", Font.PLAIN, 13));
        root.add(resumen, BorderLayout.NORTH);

        // Selector metodo
        JPanel metodo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        metodo.add(new JLabel("Metodo de pago:"));
        metodo.add(cbMetodo);
        cbMetodo.addActionListener(e -> cambiarPanel());

        // Paneles de datos por metodo
        panelDatos.add(panelTarjeta(), "Tarjeta de Credito");
        panelDatos.add(panelPayPal(),  "PayPal");
        panelDatos.add(panelEfectivo(),"Efectivo/Transferencia");

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(metodo,     BorderLayout.NORTH);
        centro.add(panelDatos, BorderLayout.CENTER);
        root.add(centro, BorderLayout.CENTER);

        // Botones
        JButton btnConfirmar = new JButton("✅ Confirmar pedido");
        JButton btnCancelar  = new JButton("Cancelar");
        btnConfirmar.setBackground(new Color(0, 153, 76));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 13));
        btnConfirmar.addActionListener(e -> confirmarPedido());
        btnCancelar.addActionListener(e  -> dispose());

        JPanel bots = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bots.add(btnCancelar);
        bots.add(btnConfirmar);
        root.add(bots, BorderLayout.SOUTH);

        add(root);
    }

    private void cambiarPanel() {
        CardLayout cl = (CardLayout) panelDatos.getLayout();
        cl.show(panelDatos, (String) cbMetodo.getSelectedItem());
    }

    private JPanel panelTarjeta() {
        JPanel p = new JPanel(new GridLayout(4, 2, 6, 6));
        p.setBorder(new EmptyBorder(10, 0, 0, 0));
        p.add(new JLabel("Ultimos 4 digitos:"));  p.add(txtDigitos);
        p.add(new JLabel("Nombre del titular:")); p.add(txtTitular);
        p.add(new JLabel(""));
        p.add(new JLabel("<html><font color=gray size=2>Solo simulacion, no ingrese datos reales.</font></html>"));
        return p;
    }

    private JPanel panelPayPal() {
        JPanel p = new JPanel(new GridLayout(2, 2, 6, 6));
        p.setBorder(new EmptyBorder(10, 0, 0, 0));
        p.add(new JLabel("Email de PayPal:")); p.add(txtPayPal);
        return p;
    }

    private JPanel panelEfectivo() {
        JPanel p = new JPanel(new GridLayout(2, 2, 6, 6));
        p.setBorder(new EmptyBorder(10, 0, 0, 0));
        p.add(new JLabel("Referencia/Banco:")); p.add(txtRef);
        p.add(new JLabel("<html><font color=gray size=2>Transferencia simulada.</font></html>"));
        return p;
    }

    // ── Confirmar pedido ──────────────────────────────────────────────────────
    private void confirmarPedido() {
        try {
            String metodo = (String) cbMetodo.getSelectedItem();
            String idPago = "PAY-" + System.currentTimeMillis()
                          + System.currentTimeMillis(); // ID unico simple
            String fecha  = java.time.LocalDateTime.now()
                          .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            double monto  = carrito.calcularTotal();

            Pago pago;
            if (metodo.contains("Tarjeta")) {
                String dig  = txtDigitos.getText().trim();
                String tit  = txtTitular.getText().trim();
                if (dig.isEmpty() || tit.isEmpty())
                    throw new Exception("Completa los datos de la tarjeta.");
                pago = PagoFactory.crearPago("TARJETA", idPago, monto, fecha,
                        new String[]{dig, tit});
            } else if (metodo.contains("PayPal")) {
                String email = txtPayPal.getText().trim();
                if (email.isEmpty()) throw new Exception("Ingresa tu email de PayPal.");
                pago = PagoFactory.crearPago("PAYPAL", idPago, monto, fecha,
                        new String[]{email});
            } else {
                String ref = txtRef.getText().trim();
                if (ref.isEmpty()) ref = "SIN_REFERENCIA";
                pago = PagoFactory.crearPago("EFECTIVO", idPago, monto, fecha,
                        new String[]{ref});
            }

            // POLIMORFISMO: confirmarPedido llama pago.procesar() segun subclase
            Pedido pedido = ctx.pedidos.confirmarPedido(carrito, cliente, pago);

            JOptionPane.showMessageDialog(this,
                    "✅ Pedido confirmado!\n"
                    + "Numero: " + pedido.getId() + "\n"
                    + "Total pagado: $" + String.format("%.2f", pedido.getTotal()) + "\n"
                    + "Metodo: " + pago.obtenerTipo(),
                    "Compra exitosa", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error al procesar pago", JOptionPane.ERROR_MESSAGE);
        }
    }
}


