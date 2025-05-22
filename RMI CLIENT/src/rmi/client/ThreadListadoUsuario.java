/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.client;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author estudiante
 */
public class ThreadListadoUsuario extends Thread {

    private guiChat vista;
    private Invocador fachada;

    private List<String> usuarios;

    public ThreadListadoUsuario(guiChat vista, String usuario) {

        try {
            this.vista = vista;
            fachada = Invocador.getInstance(usuario);
        } catch (Exception ex) {
            Logger.getLogger(ThreadListadoUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void actualizarListadoChat() {
        while (true) {
            try {
                this.usuarios = fachada.getConnectedUsers();
                vista.actulizarListado(usuarios);
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadListadoUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(ThreadListadoUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void run() {
        actualizarListadoChat();
    }

}
