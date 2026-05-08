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

/** Servicio de autenticacion: registro e inicio de sesion. */
public class AuthService {
    private DataStore store = DataStore.getInstance();
    private Usuario usuarioActual;

    public Usuario login(String email, String password) throws Exception {
        try {
            if (email == null || email.trim().isEmpty())
                throw new Exception("El email no puede estar vacio.");
            if (password == null || password.trim().isEmpty())
                throw new Exception("La contrasena no puede estar vacia.");
            Usuario u = store.buscarPorEmail(email.trim());
            if (u == null) throw new Exception("No existe usuario con ese email.");
            if (!u.getPassword().equals(password)) throw new Exception("Contrasena incorrecta.");
            usuarioActual = u;
            return u;
        } catch (Exception e) { throw new Exception(e.getMessage()); }
    }

    public Cliente registrar(String nombre, String email, String password, String confirmar) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) throw new Exception("El nombre es obligatorio.");
        if (email == null || !email.contains("@")) throw new Exception("Email invalido.");
        if (password == null || password.length() < 6) throw new Exception("La contrasena debe tener al menos 6 caracteres.");
        if (!password.equals(confirmar)) throw new Exception("Las contrasenas no coinciden.");
        String id = store.generarIdUsuario();
        Cliente c = new Cliente(id, nombre.trim(), email.trim().toLowerCase(), password);
        store.registrarUsuario(c);
        return c;
    }

    public void    logout(){ 
        usuarioActual = null; 
    }
    public Usuario getUsuarioActual(){ 
        return usuarioActual; 
    }
    public boolean estaLogueado(){ 
        return usuarioActual != null; 
    }
    public boolean esAdmin(){ 
        return usuarioActual instanceof Administrador; 
    }
    public boolean esCliente(){ 
        return usuarioActual instanceof Cliente; 
    }
}


