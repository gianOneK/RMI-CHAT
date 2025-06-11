package rmi.server;

import rmi.pkginterface.IServer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Server extends UnicastRemoteObject implements IServer {

    private static final int PUERTO = 3232;
    private static final long TIEMPO_INACTIVIDAD = 10000;
    private static final long INTERVALO_VERIFICACION = 5_000; // 5 segundos
    private static final String USUARIO_GLOBAL = "Chat Global";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();
    private final Map<String, Long> ultimoLatido = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    protected Server() throws RemoteException {
        super();
        inicializarUsuarioGlobal();
        iniciarVerificadorInactividad();
    }

    public static void main(String[] args) throws Exception {
        Server srv = new Server();
        Registry reg = LocateRegistry.createRegistry(PUERTO);
        reg.bind("rmiserver", srv);
        System.out.println("Servidor RMI listo en puerto " + PUERTO);
    }

    private void iniciarVerificadorInactividad() {
        Runnable verificador = new Runnable() {
            public void run() {
                long ahora = System.currentTimeMillis();
                List<String> usuariosInactivos = new ArrayList<String>();

                // Identificar usuarios inactivos
                for (Map.Entry<String, Long> entry : ultimoLatido.entrySet()) {
                    String usuario = entry.getKey();
                    Long ultimoTiempo = entry.getValue();

                    if (!USUARIO_GLOBAL.equals(usuario) && (ahora - ultimoTiempo) > TIEMPO_INACTIVIDAD) {
                        usuariosInactivos.add(usuario);
                    }
                }

                // Desconectar usuarios inactivos
                for (String usuario : usuariosInactivos) {
                    logger.info("Desconectando por inactividad a: " + usuario);
                    try {
                        desconectarUsuario(usuario);
                    } catch (RemoteException e) {
                        logger.log(Level.WARNING, "Error al desconectar usuario inactivo: " + usuario, e);
                    }
                }
            }
        };

        scheduler.scheduleAtFixedRate(verificador, INTERVALO_VERIFICACION, INTERVALO_VERIFICACION, TimeUnit.MILLISECONDS);
    }

    /* Este metodo crea el ChatGlobal y se ejecuta en el constructor, toma el ip 
    del propio equipo y agrega a chatGlobal como usuario en el listado Usuarios,
    el logger solo funciona como un manejo de excepciones o depuracion, en consola*/
    private void inicializarUsuarioGlobal() {
        try {
            String ipServidor = InetAddress.getLocalHost().getHostAddress();
            Usuario global = new Usuario(USUARIO_GLOBAL, ipServidor);
            usuarios.put(USUARIO_GLOBAL, global);
            logger.info("Usuario global inicializado con IP: " + ipServidor);
        } catch (UnknownHostException e) {
            logger.log(Level.WARNING, "No se pudo obtener la IP del servidor", e);

        }
    }

    @Override
    public synchronized String registrarUsuario(String name, String IP) throws RemoteException {
        // Validaciones de entrada
        if (name == null || name.trim().isEmpty()) {
            return "Error: El nombre de usuario no puede estar vacío.";
        }

        name = name.trim();

        if (USUARIO_GLOBAL.equalsIgnoreCase(name)) {
            return "Error: El nombre '" + USUARIO_GLOBAL + "' está reservado para el sistema.";
        }

        if (usuarios.containsKey(name)) {
            return "Error: El usuario '" + name + "' ya está conectado.";
        }

        if (IP == null || IP.trim().isEmpty()) {
            return "Error: La IP no puede estar vacía.";
        }

        // Logica para crear el usuario y agregarlo al listado de usuarios
        Usuario nuevoUsuario = new Usuario(name, IP.trim());
        usuarios.put(name, nuevoUsuario);
        ultimoLatido.put(name, System.currentTimeMillis());
        
       // Mensaje en consola para depurar
        logger.info("Usuario registrado: " + name + " desde IP: " + IP);

        return imprimirUsuarios();
    }

    @Override
    public List<String> getConnectedUsers() throws RemoteException {
        // Devuelve una copia inmutable de las claves (nombres de usuario)
        return new ArrayList<>(usuarios.keySet());
    }

    @Override
    public void sendDirectMessage(String from, String to, String message) throws RemoteException {
        Usuario destinatario = usuarios.get(to);
        Usuario remitente = usuarios.get(from);
        if (destinatario != null) {
            remitente.sendMessage(to, from, message, LocalDateTime.now().toString());
            destinatario.sendMessage(from, from, message, LocalDateTime.now().toString());
        }
    }

    @Override
    public void sendGlobalMessage(String from, String message) throws RemoteException {
        String timestamp = LocalDateTime.now().toString();
        Usuario chatGlobal = usuarios.get("Chat Global");

        if (chatGlobal != null) {
            chatGlobal.sendMessage("Chat Global", from, message, timestamp);
        }

        for (Usuario u : usuarios.values()) {
            if (!u.getName().equals("Chat Global")) {
                u.sendMessage("Chat Global", from, message, timestamp);
            }
        }
    }

    @Override
    public Map<String, ArrayList<String[]>> fetchMessages(String name) throws RemoteException {
        Usuario u = usuarios.get(name);
        return u != null ? u.fetchMessages() : Map.of();
    }

    private String imprimirUsuarios() {
        StringBuilder sb = new StringBuilder("Usuarios:\n-------------------\n");
        usuarios.keySet().forEach(k -> sb.append(k).append("\n"));
        sb.append("-------------------");
        return sb.toString();
    }

    @Override
    public synchronized void desconectarUsuario(String name) throws RemoteException {
        Usuario removed = null;

        if (!name.equals("Chat Global")) {
            removed = usuarios.remove(name);
        }
        if (removed != null) {
            String notif = "Sistema: " + name + " se ha desconectado.";
            //usuarios.values().forEach(user -> user.addMessage(notif));
        }
    }

    @Override
    public void latido(String username) throws RemoteException {
        if (usuarios.containsKey(username)) {
            ultimoLatido.put(username, System.currentTimeMillis());
            System.out.println("Ping recibido de " + username);
        }
    }

}
