/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaapp.ui;

import com.mycompany.tiendaapp.model.*;
import com.mycompany.tiendaapp.service.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
/**
 *
 * @author Dell
 */
public class MainFrame extends JFrame {
    
    private AppContext ctx = AppContext.get();
    private Usuario usuario;
    private JTabbedPane tabs = new JTabbedPane();

    public MainFrame(Usuario usuario) {
        this.usuario = usuario;
        setTitle("TechStore — " + usuario.getNombre() + " [" + usuario.getRol() + "]");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(920, 650);
        setLocationRelativeTo(null);

        // Barra superior
        add(construirHeader(), BorderLayout.NORTH);

        // Pestanas segun rol
        if (usuario instanceof Administrador) {
            tabs.addTab("📦 Productos",    new ProductosAdminPanel());
            tabs.addTab("🛒 Pedidos",      new PedidosAdminPanel());
            tabs.addTab("👤 Mi perfil",    new PerfilPanel(usuario));
        } else {
            tabs.addTab("🏪 Tienda",       new TiendaClientePanel((Cliente) usuario));
            tabs.addTab("🛍️ Mis pedidos", new MisPedidosPanel(usuario));
            tabs.addTab("👤 Mi perfil",    new PerfilPanel(usuario));
        }

        tabs.setFont(new Font("Arial", Font.PLAIN, 13));
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel construirHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(new Color(0, 90, 170));
        h.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel titulo = new JLabel("🛒 TechStore");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);

        JLabel info = new JLabel("Bienvenido, " + usuario.getNombre()
                + "  |  " + usuario.obtenerDescripcionRol()); // POLIMORFISMO visible
        info.setFont(new Font("Arial", Font.PLAIN, 11));
        info.setForeground(new Color(200, 220, 255));

        JButton btnSalir = new JButton("Cerrar sesion");
        btnSalir.setBackground(new Color(200, 50, 50));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> {
            ctx.auth.logout();
            dispose();
            new LoginFrame().setVisible(true);
        });

        JPanel izq = new JPanel(new GridLayout(2, 1));
        izq.setOpaque(false);
        izq.add(titulo);
        izq.add(info);

        h.add(izq,      BorderLayout.WEST);
        h.add(btnSalir, BorderLayout.EAST);
        return h;
    }
}


