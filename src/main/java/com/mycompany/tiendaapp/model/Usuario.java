package com.mycompany.tiendaapp.model;

import java.io.Serializable;

    public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    // ENCAPSULACION: atributos privados
    private String id;
    private String nombre;
    private String email;
    private String password;
    private String rol;

    public Usuario(String id, String nombre, String email, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    // POLIMORFISMO: metodo abstracto que cada subclase implementara con @Override
    public abstract String obtenerDescripcionRol();

    // Getters y Setters - ENCAPSULACION
    public String getId(){ 
        return id; 
    }
    public void   setId(String id){ 
        this.id = id; 
    }

    public String getNombre(){ 
        return nombre; 
    }
    public void   setNombre(String n){ 
        this.nombre = n; 
    }

    public String getEmail(){ 
        return email; 
    }
    public void   setEmail(String e){ 
        this.email = e; 
    }

    public String getPassword(){ 
        return password; 
    }
    public void   setPassword(String p){ 
        this.password = p; 
    }

    public String getRol(){ 
        return rol; 
    }
    public void   setRol(String r){ 
        this.rol = r; 
    }

    @Override
    public String toString() {
        return nombre + " (" + email + ") - " + rol;
    }
}

