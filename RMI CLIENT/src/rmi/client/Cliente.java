package rmi.client;

import java.net.InetAddress;
import java.rmi.RemoteException;
import rmi.pkginterface.IServer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

    private IServer server;
    private String name;
    private static Cliente instancia;

    public static synchronized Cliente getInstance() throws Exception {
        if (instancia == null) {
            instancia = new Cliente(); // Crea la instancia solo si no existe
        }
        return instancia;
    }
    
    
    private Cliente(){
        
    }
    
    public void register()  throws Exception {
        Registry reg = LocateRegistry.getRegistry("LocalHost", 3232);
        this.server = (IServer) reg.lookup("rmiserver");
        String localHost = InetAddress.getLocalHost().getHostAddress();
        System.out.println(server.registrarUsuario(name, localHost));

        // Inicia hilo de consulta de mensajes
        new Thread(this::pollMessages).start();

//        // Bucle de entrada de usuario
//        Scanner sc = new Scanner(System.in);
//        while (true) {
//            System.out.println("Elige opción: 1) Directo 2) Global 3) Salir");
//            String option = sc.nextLine();
//            if ("1".equals(option)) {
//                System.out.print("Para: ");
//                String to = sc.nextLine();
//                System.out.print("Mensaje: ");
//                String msg = sc.nextLine();
//                server.sendDirectMessage(name, to, msg);
//            } else if ("2".equals(option)) {
//                System.out.print("Mensaje global: ");
//                String msg = sc.nextLine();
//                server.sendGlobalMessage(name, msg);
//            } else if ("3".equals(option)) {
//                server.desconectarUsuario(name);
//                System.out.println("Desconectado.");
//                System.exit(0);
//            }
//        }
    }


    public List<String> getConnectedUsers() throws RemoteException {

        // Llama al método remoto del servidor para obtener la lista
        return server.getConnectedUsers();

    }

    private void pollMessages() {
        try {
            while (true) {
                List<String> msgs = server.fetchMessages(name);
                for (String m : msgs) {
                    System.out.println(m);
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Se perdió conexión con el servidor. Cerrando cliente...");
            try {
                server.desconectarUsuario(name); // intento de limpiar si es posible
            } catch (Exception ignored) {
            }
            System.exit(1);
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
