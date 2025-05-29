package rmi.client;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadEnviarMensajeGlobal extends Thread {

    private String from;
    private String texto;
    private Cliente fachada;

    public ThreadEnviarMensajeGlobal(String from, String texto) {
        try {
            this.from = from;
            this.texto = texto;
            this.fachada = Cliente.getInstance();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            fachada.sendGlobalMessage(from, texto);
        } catch (RemoteException ex) {
            Logger.getLogger(ThreadEnviarMensajeGlobal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
