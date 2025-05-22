/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin
 */
public class ThreadChatActualizar extends Thread {
    private ChatGUI vista;
    private Cliente fachada;


    public ThreadChatActualizar(ChatGUI vista) {
        try {
            this.vista = vista;
            this.fachada = Cliente.getInstance();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @Override
    public void run(){
         while (true) {
            try {
                Map<String, ArrayList<String[]>> mensajes = Cliente.getInstance().fetchMessages();
                
                
                Thread.sleep(1000);
            } catch (InterruptedException | RemoteException ex) {
                Logger.getLogger(ThreadChatListUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                 Logger.getLogger(ThreadChatActualizar.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
    }
    
}
