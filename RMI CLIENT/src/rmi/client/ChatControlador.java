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
public class ChatControlador {

    private ChatGUI vistaChat;
    private Cliente fachada = Cliente.getInstance();

    public ChatControlador(ChatGUI vistaChat) throws Exception {
        this.vistaChat = vistaChat;
        actualizarListadoUsuarios();

    }

    private void actualizarChats() throws RemoteException {
        ThreadChatActualizar actualizar = new ThreadChatActualizar(vistaChat);
        actualizar.start();
    }

    private void actualizarListadoUsuarios() throws RemoteException {
        ThreadChatListUsuarios actualizar = new ThreadChatListUsuarios(vistaChat);
        actualizar.start();
    }

}
