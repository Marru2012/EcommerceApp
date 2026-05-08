package com.mycompany.tiendaapp.ui;

import com.mycompany.tiendaapp.service.AppContext;
import com.mycompany.tiendaapp.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private AppContext ctx = AppContext.get();

    // Componentes de login
    private JTextField txtEmail = new JTextField(20);
    private JPasswordField txtPass = new JPasswordField(20);
    private JButton btnLogin = new JButton("Iniciar sesion");
    private JButton btnIrRegistro = new JButton("Crear cuenta nueva");

    // Componentes de registro
    private JTextField txtRegNombre = new JTextField(20);
    private JTextField txtRegEmail = new JTextField(20);
    private JPasswordField txtRegPass = new JPasswordField(20);
    private JPasswordField txtRegPass2 = new JPasswordField(20);
    private JButton btnRegistrar = new JButton("Registrarme");
    private JButton btnIrLogin = new JButton("Ya tengo cuenta");

    private JPanel cardPanel;
    private CardLayout cards = new CardLayout();

    public LoginFrame() {
        setTitle("TechStore — Iniciar sesion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 380);
        setLocationRelativeTo(null);
        setResizable(false);

        cardPanel = new JPanel(cards);
        cardPanel.add(construirPanelLogin(), "LOGIN");
        cardPanel.add(construirPanelRegistro(), "REGISTRO");

        add(cardPanel);

        // Credenciales rapidas de prueba (quitar en produccion)
        txtEmail.setText("admin@tienda.com");
        txtPass.setText("admin123");
    }

    // ── Panel Login ──────────────────────────────────────────────────────────
    private JPanel construirPanelLogin() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(new EmptyBorder(30, 40, 30, 40));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("🛒  TechStore", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;
        p.add(titulo, g);

        JLabel sub = new JLabel("Inicia sesion para continuar", SwingConstants.CENTER);
        sub.setForeground(Color.GRAY);
        g.gridy = 1;
        p.add(sub, g);

        g.gridwidth = 1;
        g.gridx = 0;
        g.gridy = 2;
        p.add(new JLabel("Email:"), g);
        g.gridx = 1;
        p.add(txtEmail, g);

        g.gridx = 0;
        g.gridy = 3;
        p.add(new JLabel("Contrasena:"), g);
        g.gridx = 1;
        p.add(txtPass, g);

        g.gridx = 0;
        g.gridy = 4;
        g.gridwidth = 2;
        btnLogin.setBackground(new Color(0, 120, 215));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        p.add(btnLogin, g);

        g.gridy = 5;
        btnIrRegistro.setFont(new Font("Arial", Font.PLAIN, 11));
        p.add(btnIrRegistro, g);

        // Acciones
        btnLogin.addActionListener(e -> hacerLogin());
        txtPass.addActionListener(e -> hacerLogin());
        btnIrRegistro.addActionListener(e -> cards.show(cardPanel, "REGISTRO"));

        return p;
    }

    // ── Panel Registro ───────────────────────────────────────────────────────
    private JPanel construirPanelRegistro() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(new EmptyBorder(25, 40, 25, 40));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 4, 5, 4);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Crear cuenta nueva", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;
        p.add(titulo, g);

        g.gridwidth = 1;
        g.gridx = 0;
        g.gridy = 1;
        p.add(new JLabel("Nombre:"), g);
        g.gridx = 1;
        p.add(txtRegNombre, g);

        g.gridx = 0;
        g.gridy = 2;
        p.add(new JLabel("Email:"), g);
        g.gridx = 1;
        p.add(txtRegEmail, g);

        g.gridx = 0;
        g.gridy = 3;
        p.add(new JLabel("Contrasena:"), g);
        g.gridx = 1;
        p.add(txtRegPass, g);

        g.gridx = 0;
        g.gridy = 4;
        p.add(new JLabel("Confirmar:"), g);
        g.gridx = 1;
        p.add(txtRegPass2, g);

        g.gridx = 0;
        g.gridy = 5;
        g.gridwidth = 2;
        btnRegistrar.setBackground(new Color(0, 153, 76));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 13));
        btnRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        p.add(btnRegistrar, g);

        g.gridy = 6;
        p.add(btnIrLogin, g);

        btnRegistrar.addActionListener(e -> hacerRegistro());
        btnIrLogin.addActionListener(e -> cards.show(cardPanel, "LOGIN"));

        return p;
    }

    // ── Logica Login ─────────────────────────────────────────────────────────
    private void hacerLogin() {
        try {
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword());
            Usuario u = ctx.auth.login(email, pass);

            dispose();
            new MainFrame(u).setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de acceso", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Logica Registro ──────────────────────────────────────────────────────
    private void hacerRegistro() {
        try {
            String nombre = txtRegNombre.getText().trim();
            String email = txtRegEmail.getText().trim();
            String pass = new String(txtRegPass.getPassword());
            String confirma = new String(txtRegPass2.getPassword());

            ctx.auth.registrar(nombre, email, pass, confirma);
            JOptionPane.showMessageDialog(this,
                    "Cuenta creada correctamente. Inicia sesion.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            cards.show(cardPanel, "LOGIN");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de registro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
