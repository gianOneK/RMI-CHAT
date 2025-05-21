/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.client;

import java.rmi.RemoteException;

/**
 *
 * @author Usuario
 */
public class ChatControlador {

    private guiChat vistaChat;
    private Invocador fachada;

    public ChatControlador(guiChat vistaChat, String nombreUsuario) throws Exception {
        this.vistaChat = vistaChat;
        this.fachada = new Invocador(nombreUsuario);
        
        
        
        actualizarListadoUsuarios();
    }

    public void actualizarListadoUsuarios() throws RemoteException {
        vistaChat.actulizarListado(fachada.getConnectedUsers());
    }

}
