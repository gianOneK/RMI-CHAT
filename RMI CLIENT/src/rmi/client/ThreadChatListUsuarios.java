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
public class ThreadChatListUsuarios extends Thread {

    private ChatGUI vista;
    private Cliente fachada;
    private String nombre;

    private List<String> usuarios;

    public ThreadChatListUsuarios(ChatGUI vista) {

        try {
            this.vista = vista;
            this.fachada = Cliente.getInstance();
            this.nombre = fachada.getName();
        } catch (Exception ex) {
            Logger.getLogger(ThreadChatListUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void actualizarListadoChat() {
        while (true) {
            try {
                this.usuarios = fachada.getConnectedUsers();
                usuarios.remove(nombre);
                vista.actulizarListado(usuarios);
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadChatListUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(ThreadChatListUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void run() {
        actualizarListadoChat();
    }

}
