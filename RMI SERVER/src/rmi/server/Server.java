
package rmi.server;

import rmi.pkginterface.IServer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

public class Server extends UnicastRemoteObject implements IServer {

    private static final int PUERTO = 3232;
    private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();

    protected Server() throws RemoteException {
        super();
    }

    public static void main(String[] args) throws Exception {
        Server srv = new Server();
        Registry reg = LocateRegistry.createRegistry(PUERTO);
        reg.bind("rmiserver", srv);
        System.out.println("Servidor RMI listo en puerto " + PUERTO);
    }

    @Override
    public synchronized String registrarUsuario(String name, String IP) throws RemoteException {
        Usuario u = new Usuario(name, IP);
        usuarios.put(name, u);
        String notif = "Sistema: " + name + " se ha unido.";
        usuarios.values().forEach(user -> user.addMessage(notif));
        return imprimirUsuarios();
    }
    
    @Override
    public List<String> getConnectedUsers() throws RemoteException {
        // Devuelve una copia inmutable de las claves (nombres de usuario)
        return new ArrayList<>(usuarios.keySet());
    }

    @Override
    public void sendDirectMessage(String from, String to, String message) throws RemoteException {
        Usuario dest = usuarios.get(to);
        if (dest != null) {
            dest.addMessage(from + " -> Tú: " + message);
        }
    }

    @Override
    public void sendGlobalMessage(String from, String message) throws RemoteException {
        String msg = from + " (global): " + message;
        usuarios.values().forEach(user -> user.addMessage(msg));
    }

    @Override
    public List<String> fetchMessages(String name) throws RemoteException {
        Usuario u = usuarios.get(name);
        return u != null ? u.fetchMessages() : List.of();
    }

    private String imprimirUsuarios() {
        StringBuilder sb = new StringBuilder("Usuarios:\n-------------------\n");
        usuarios.keySet().forEach(k -> sb.append(k).append("\n"));
        sb.append("-------------------");
        return sb.toString();
    }

    @Override
    public synchronized void desconectarUsuario(String name) throws RemoteException {
        Usuario removed = usuarios.remove(name);
        if (removed != null) {
            String notif = "Sistema: " + name + " se ha desconectado.";
            usuarios.values().forEach(user -> user.addMessage(notif));
        }
    }

    
    

    

}
