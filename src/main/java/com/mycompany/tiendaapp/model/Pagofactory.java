package com.mycompany.tiendaapp.model;

class PagoTarjeta extends Pago {
    private static final long serialVersionUID = 1L;
    private String ultimosCuatroDigitos;
    private String titular;

    public PagoTarjeta(String id, double monto, String fecha,
                       String ultimosCuatroDigitos, String titular) {
        super(id, monto, fecha);
        this.ultimosCuatroDigitos = ultimosCuatroDigitos;
        this.titular = titular;
    }

    @Override public String obtenerTipo()    { return "Tarjeta de Credito"; }

    @Override public String obtenerDetalle() {
        return "Tarjeta **** " + ultimosCuatroDigitos + " | Titular: " + titular;
    }

    // POLIMORFISMO: @Override que participa en el flujo de pago
    @Override
    public boolean procesar() throws Exception {
        if (ultimosCuatroDigitos == null || ultimosCuatroDigitos.length() != 4)
            throw new Exception("Numero de tarjeta invalido (se requieren 4 digitos).");
        if (titular == null || titular.trim().isEmpty())
            throw new Exception("El nombre del titular es obligatorio.");
        setProcesado(true);
        return true;
    }

    public String getUltimosCuatroDigitos() { return ultimosCuatroDigitos; }
    public String getTitular()              { return titular; }
}

// ── Pago con PayPal ─────────────────────────────────────────────────────────
class PagoPayPal extends Pago {
    private static final long serialVersionUID = 1L;
    private String emailPayPal;

    public PagoPayPal(String id, double monto, String fecha, String emailPayPal) {
        super(id, monto, fecha);
        this.emailPayPal = emailPayPal;
    }

    @Override public String obtenerTipo()    { return "PayPal"; }

    @Override public String obtenerDetalle() { return "PayPal: " + emailPayPal; }

    @Override
    public boolean procesar() throws Exception {
        if (emailPayPal == null || !emailPayPal.contains("@"))
            throw new Exception("Email de PayPal invalido.");
        setProcesado(true);
        return true;
    }

    public String getEmailPayPal() { return emailPayPal; }
}

// ── Pago en Efectivo ────────────────────────────────────────────────────────
class PagoEfectivo extends Pago {
    private static final long serialVersionUID = 1L;
    private String referencia;

    public PagoEfectivo(String id, double monto, String fecha, String referencia) {
        super(id, monto, fecha);
        this.referencia = referencia;
    }

    @Override public String obtenerTipo()    { return "Transferencia/Efectivo"; }

    @Override public String obtenerDetalle() {
        return "Transferencia bancaria. Ref: " + referencia;
    }

    @Override
    public boolean procesar() throws Exception {
        setProcesado(true);
        return true;
    }

    public String getReferencia() { return referencia; }
}

// ── Factory para crear instancias de Pago ───────────────────────────────────
/**
 * PagoFactory: crea el objeto Pago correcto segun el tipo elegido.
 * Patron de diseno que encapsula la creacion de objetos.
 */
public class Pagofactory {
    public static Pago crearPago(String tipo, String id, double monto,
                                 String fecha, String[] datos) throws Exception {
        switch (tipo) {
            case "TARJETA":
                // datos[0]=ultimos4digitos, datos[1]=titular
                return new PagoTarjeta(id, monto, fecha, datos[0], datos[1]);
            case "PAYPAL":
                // datos[0]=email
                return new PagoPayPal(id, monto, fecha, datos[0]);
            case "EFECTIVO":
                // datos[0]=referencia
                return new PagoEfectivo(id, monto, fecha, datos[0]);
            default:
                throw new Exception("Metodo de pago no reconocido: " + tipo);
        }
    }
}