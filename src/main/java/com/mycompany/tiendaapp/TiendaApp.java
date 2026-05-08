package com.mycompany.tiendaapp;


import com.mycompany.tiendaapp.ui.*;
import javax.swing.*;

public class TiendaApp {
    
        public static void main(String[] args) {
        // Usar el Look and Feel nativo del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, usa el look and feel por defecto de Java
            System.err.println("No se pudo cargar el L&F del sistema: " + e.getMessage());
        }

        // Lanzar la UI en el Event Dispatch Thread (buena practica en Swing)
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}

    

