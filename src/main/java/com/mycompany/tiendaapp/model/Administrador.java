/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaapp.model;

/**
 *
 * @author Dell
 */
public class Administrador extends Usuario {
  private static final long serialVersionUID = 1L;

    // ENCAPSULACION
    private String nivelAcceso;
    private boolean puedeEliminarUsuarios;

    public Administrador(String id, String nombre, String email, String password) {
        super(id, nombre, email, password, "ADMIN");
        this.nivelAcceso = "TOTAL";
        this.puedeEliminarUsuarios = true;
    }

    // 
    @Override
    public String obtenerDescripcionRol() {
        return "Administrador con acceso total: gestion de productos, usuarios y pedidos.";
    }

    // Getters y Setters
    public String getNivelAcceso(){ 
        return nivelAcceso; 
    }
    public void setNivelAcceso(String n){ 
        this.nivelAcceso = n; 
    }

    public boolean isPuedeEliminarUsuarios(){ 
        return puedeEliminarUsuarios; 
    }
    public void setPuedeEliminarUsuarios(boolean b){ 
        this.puedeEliminarUsuarios = b; 
    }
}
