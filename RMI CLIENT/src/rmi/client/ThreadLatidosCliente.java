/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.client;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class ThreadLatidosCliente extends Thread {

    private  Cliente fachada;
    private volatile boolean activo = true;
    private String nombreUsuario;

    public ThreadLatidosCliente(String usuario) {
        try {
            this.nombreUsuario = usuario;
            this.fachada = Cliente.getInstance();
        } catch (Exception ex) {
            Logger.getLogger(ThreadLatidosCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void detener() {
        activo = false;
    }

   @Override
    public void run() {
        while (activo) {
            try {
                fachada.latidoAlServidor();
                Thread.sleep(1); // cada 5 segundos
            } catch (RemoteException e) {
                Logger.getLogger(ThreadLatidosCliente.class.getName()).log(Level.SEVERE, "Ping fallido", e);
                break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}