/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaapp.dao;
import com.mycompany.tiendaapp.model.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Dell
 */
public class DataStore {
    private static final String DIR            = "data/";
    private static final String PRODUCTOS_FILE = DIR + "productos.dat";
    private static final String USUARIOS_FILE  = DIR + "usuarios.dat";
    private static final String PEDIDOS_FILE   = DIR + "pedidos.dat";

    // COLECCIONES en memoria
    private ArrayList<Producto> productos;
    private ArrayList<Usuario>  usuarios;
    private ArrayList<Pedido>   pedidos;

    private static DataStore instancia; // Singleton

    private DataStore() {
        new File(DIR).mkdirs();
        productos = cargarObjeto(PRODUCTOS_FILE, new ArrayList<>());
        usuarios  = cargarObjeto(USUARIOS_FILE,  new ArrayList<>());
        pedidos   = cargarObjeto(PEDIDOS_FILE,   new ArrayList<>());
        if (usuarios.isEmpty()) inicializarDatos();
    }

    public static DataStore getInstance() {
        if (instancia == null) instancia = new DataStore();
        return instancia;
    }

    // ── Datos iniciales de demo ──────────────────────────────────────────────
    private void inicializarDatos() {
        usuarios.add(new Administrador("admin1", "Administrador", "admin@tienda.com", "admin123"));

        Cliente demo = new Cliente("cli1", "Maria Garcia", "maria@demo.com", "demo123");
        demo.setDireccion("Calle Principal 123");
        demo.setTelefono("555-1234");
        usuarios.add(demo);

        productos.add(new Producto("p1",  "Laptop Pro 15",       "Intel Core i7, 16GB RAM, 512GB SSD",      1299.99, 15, "Tecnologia",   "💻"));
        productos.add(new Producto("p2",  "Smartphone X12",      "6.5\", 128GB, camara 50MP",                699.99, 30, "Tecnologia",   "📱"));
        productos.add(new Producto("p3",  "Auriculares BT",      "Cancelacion de ruido, 30h bateria",        249.99, 50, "Audio",        "🎧"));
        productos.add(new Producto("p4",  "Teclado Mecanico RGB","Cherry MX, retroiluminacion RGB",          129.99, 40, "Perifericos",  "⌨️"));
        productos.add(new Producto("p5",  "Mouse Gaming Pro",    "6400 DPI, 7 botones programables",          59.99, 60, "Perifericos",  "🖱️"));
        productos.add(new Producto("p6",  "Monitor 4K 27\"",     "IPS, 144Hz, HDR10",                        549.99, 20, "Monitores",    "🖥️"));
        productos.add(new Producto("p7",  "Mochila Laptop 15\"", "Resistente al agua, puerto USB",            79.99, 25, "Accesorios",   "🎒"));
        productos.add(new Producto("p8",  "Webcam HD 1080p",     "1080p 60fps, microfono integrado",          89.99, 35, "Accesorios",   "📷"));
        productos.add(new Producto("p9",  "SSD Externo 1TB",     "USB 3.2, 1050MB/s",                        109.99, 45, "Almacenamiento","💾"));
        productos.add(new Producto("p10", "Hub USB-C 7en1",      "HDMI 4K, 3xUSB-A, SD, PD 100W",            49.99, 55, "Accesorios",   "🔌"));
        guardar();
    }

    // ── Serializacion ────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private <T> T cargarObjeto(String ruta, T defaultVal) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            return (T) ois.readObject();
        } catch (Exception e) {
            return defaultVal;
        }
    }

    private void guardarObjeto(String ruta, Object obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.err.println("Error al guardar " + ruta + ": " + e.getMessage());
        }
    }

    public void guardar() {
        guardarObjeto(PRODUCTOS_FILE, productos);
        guardarObjeto(USUARIOS_FILE,  usuarios);
        guardarObjeto(PEDIDOS_FILE,   pedidos);
    }

    // ── Productos ────────────────────────────────────────────────────────────
    public ArrayList<Producto> getProductos()  { return productos; }

    public Producto buscarProducto(String id) {
        for (Producto p : productos) if (p.getId().equals(id)) return p;
        return null;
    }

    public void agregarProducto(Producto p) throws Exception {
        if (buscarProducto(p.getId()) != null)
            throw new Exception("Ya existe un producto con ese ID.");
        productos.add(p);
        guardar();
    }

    public void actualizarProducto(Producto p) throws Exception {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId().equals(p.getId())) {
                productos.set(i, p); guardar(); return;
            }
        }
        throw new Exception("Producto no encontrado.");
    }

    public void eliminarProducto(String id) throws Exception {
        if (!productos.removeIf(p -> p.getId().equals(id)))
            throw new Exception("Producto no encontrado.");
        guardar();
    }

    // ── Usuarios ─────────────────────────────────────────────────────────────
    public ArrayList<Usuario> getUsuarios()     { return usuarios; }

    public Usuario buscarPorEmail(String email) {
        for (Usuario u : usuarios)
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        return null;
    }

    public Usuario buscarUsuario(String id) {
        for (Usuario u : usuarios) if (u.getId().equals(id)) return u;
        return null;
    }

    public void registrarUsuario(Usuario u) throws Exception {
        if (buscarPorEmail(u.getEmail()) != null)
            throw new Exception("El email ya esta registrado.");
        usuarios.add(u);
        guardar();
    }

    // ── Pedidos ──────────────────────────────────────────────────────────────
    public ArrayList<Pedido> getPedidos()       { return pedidos; }

    public void agregarPedido(Pedido p)         { pedidos.add(p); guardar(); }

    public ArrayList<Pedido> getPedidosCliente(String clienteId) {
        ArrayList<Pedido> res = new ArrayList<>();
        for (Pedido p : pedidos) if (p.getClienteId().equals(clienteId)) res.add(p);
        return res;
    }

    public void actualizarPedido(Pedido p) {
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getId().equals(p.getId())) {
                pedidos.set(i, p); guardar(); return;
            }
        }
    }

    // ── Generadores de ID ────────────────────────────────────────────────────
    public String generarIdProducto() { return "p"   + System.currentTimeMillis(); }
    public String generarIdUsuario()  { return "u"   + System.currentTimeMillis(); }
    public String generarIdPedido()   { return "ORD-" + System.currentTimeMillis(); }
    public String generarIdPago()     { return "PAY-" + System.currentTimeMillis(); }
}


