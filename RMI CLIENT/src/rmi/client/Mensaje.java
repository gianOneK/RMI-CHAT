/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.client;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin
 */
public class Mensaje {
    private String remitente;
    private String texto;
    private String fecha;

    public Mensaje(String remitente, String texto, String fecha) {
        this.remitente = remitente;
        this.texto = texto;
        this.fecha = fecha;
    }

    public String getRemitente() { return remitente; }
    public String getTexto() { return texto; }
    public String getFecha() { return fecha; }

    public boolean esMio() {
        try {
            return Cliente.getInstance().getName().equals(remitente);
        } catch (Exception ex) {
            Logger.getLogger(Mensaje.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public String toString() {
        return texto; // Solo por compatibilidad, no se usará realmente
    }
}

