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
    private ThreadLatidosCliente hiloPing;

    public ChatControlador(ChatGUI vistaChat) throws Exception {
        this.vistaChat = vistaChat;

        actualizarChats();
        actualizarListadoUsuarios();

        // Inicia el hilo de ping para mantener al usuario "vivo"
        hiloPing = new ThreadLatidosCliente(fachada.getName());
        hiloPing.start();

        vistaChat.getBtbSalir().addActionListener(e -> {
            try {
                desconectarUsuario();
            } catch (Exception ex) {
                Logger.getLogger(ChatControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void actualizarChats() throws RemoteException {
        ThreadChatActualizar actualizar = new ThreadChatActualizar(vistaChat);
        actualizar.start();
    }

    private void actualizarListadoUsuarios() throws RemoteException {
        ThreadChatListUsuarios actualizar = new ThreadChatListUsuarios(vistaChat);
        actualizar.start();
    }

    private void enviarMensajeThread(String destino, String texto) throws RemoteException {
        ThreadEnviarMensaje enviar = new ThreadEnviarMensaje(vistaChat, destino, texto);
        enviar.start();
    }

    public void desconectarUsuario() throws RemoteException {
        fachada.desconectarUsuario();
        vistaChat.setVisible(false);
    }

    void enviarMensajeDirecto(String destino, String texto) throws RemoteException {
        enviarMensajeThread(destino, texto);
    }

    public void enviarMensajeGlobal(String texto) throws RemoteException {
        ThreadEnviarMensajeGlobal envio = new ThreadEnviarMensajeGlobal(fachada.getName(), texto);
        envio.start();
    }

}
