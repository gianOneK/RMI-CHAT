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
 * @author Kevin
 */
public class ThreadEnviarMensaje extends Thread{
    private ChatGUI vista;
    private Cliente fachada;
    private String destino;
    private String texto;

    public ThreadEnviarMensaje(ChatGUI vista, String destino, String texto) {
        try {
            this.vista = vista;
            this.fachada = Cliente.getInstance();
            this.destino = destino;
            this.texto = texto;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @Override
    public void run(){
        try {
            fachada.sendDirectMessage(fachada.getName(), destino, texto);
        } catch (RemoteException ex) {
            Logger.getLogger(ThreadEnviarMensaje.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
