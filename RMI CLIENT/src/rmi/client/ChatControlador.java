/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.client;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
    private ExecutorService executorService = Executors.newCachedThreadPool();
    
    public ChatControlador(ChatGUI vistaChat) throws Exception {
        this.vistaChat = vistaChat;
        
        actualizarChats();
        actualizarListadoUsuarios();
        iniciarHiloPing();
        Runtime.getRuntime().addShutdownHook(new Thread(this::cerrarRecursosPing));
        
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
    
    public void iniciarHiloPing() {
        hiloPing = new ThreadLatidosCliente(fachada.getName());
        executorService.submit(hiloPing);
        
    }

    // Este metodo es para gestionar el hilo de latidos, será llamado por <iniciarHiloPing()>
    public void cerrarRecursosPing() {
        if (hiloPing != null) {
            hiloPing.detener();
            /* Método para parar el hilo de forma segura, 
                                   es llamado desde la clase ThreadLatidosCliente*/
        }
        
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        }
    }
    
}
