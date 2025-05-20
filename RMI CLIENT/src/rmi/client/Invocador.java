package rmi.client;

import rmi.pkginterface.IServer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class Invocador {
    private final IServer server;
    private final String name;

    public Invocador(String name) throws Exception {
        this.name = name;
        Registry reg = LocateRegistry.getRegistry("localhost", 3232);
        this.server = (IServer) reg.lookup("rmiserver");
        System.out.println(server.registrarUsuario(name, "127.0.0.1"));

        // Inicia hilo de consulta de mensajes
        new Thread(this::pollMessages).start();

        // Bucle de entrada de usuario
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Elige opción: 1) Directo 2) Global");
            String option = sc.nextLine();
            if ("1".equals(option)) {
                System.out.print("Para: ");
                String to = sc.nextLine();
                System.out.print("Mensaje: ");
                String msg = sc.nextLine();
                server.sendDirectMessage(name, to, msg);
            } else {
                System.out.print("Mensaje global: ");
                String msg = sc.nextLine();
                server.sendGlobalMessage(name, msg);
            }
        }
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
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            System.out.print("Tu nombre: ");
            String name = new Scanner(System.in).nextLine();
            new Invocador(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}