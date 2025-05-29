package rmi.client;

import java.net.InetAddress;
import java.rmi.RemoteException;
import rmi.pkginterface.IServer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

    private IServer server;
    private String name;
    private static Cliente instancia;
    private Map<String, ArrayList<String[]>> mensajes = new HashMap();

    public static synchronized Cliente getInstance() throws Exception {
        if (instancia == null) {
            instancia = new Cliente(); // Crea la instancia solo si no existe
        }
        return instancia;
    }

    private Cliente() {

    }

    public void latidoAlServidor() throws RemoteException {
        if (server != null && name != null) {
            server.latido(name);
        }
    }

    public void register() throws Exception {
        //192.168.254.91
        Registry reg = LocateRegistry.getRegistry("localhost", 3232);
        this.server = (IServer) reg.lookup("rmiserver");
        String localHost = InetAddress.getLocalHost().getHostAddress();
        System.out.println(server.registrarUsuario(name, localHost));

    }

    public List<String> getConnectedUsers() throws RemoteException {
        List<String> usuarios = server.getConnectedUsers();
        if (usuarios.remove("Chat Global")) {
            usuarios.add(0, "Chat Global");  // lo inserta en la primera posición
        }

        // Llama al método remoto del servidor para obtener la lista
        return usuarios;

    }

    public Map<String, ArrayList<String[]>> fetchMessages() {
        try {
            return server.fetchMessages(name);
        } catch (RemoteException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void desconectarUsuario() throws RemoteException {
        server.desconectarUsuario(name);
        System.exit(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void sendDirectMessage(String from, String to, String message) throws RemoteException {
        server.sendDirectMessage(from, to, message);
    }

    public void sendGlobalMessage(String from, String mensaje) throws RemoteException {

        server.sendGlobalMessage(from, mensaje);
    }

}
