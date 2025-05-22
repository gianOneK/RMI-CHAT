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

    private guiChat vistaChat;
    private Invocador fachada;
    private String nombrecito;

    public ChatControlador(guiChat vistaChat, String nombreUsuario) throws Exception {
        this.vistaChat = vistaChat;
        this.fachada = Invocador.getInstance(nombreUsuario);
        this.nombrecito = nombreUsuario;
        actualizarListadoUsuarios();

    }

    private void actualizarListadoUsuarios() throws RemoteException {

        ThreadListadoUsuario actualizar = new ThreadListadoUsuario(vistaChat, nombrecito);
        actualizar.start();

    }

}
