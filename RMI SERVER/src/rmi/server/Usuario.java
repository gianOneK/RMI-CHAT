package rmi.server;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Usuario {

    private final String name;
    private final String IP;
    private boolean connected = true;
    private Map<String, ArrayList<String[]>> cola = new HashMap<>();
    private Map<String, ArrayList<String[]>> mensajes = new HashMap<>();

    public Usuario(String name, String IP) {
        this.name = name;
        this.IP = IP;
    }

    public void saveMessage(String contacto, String remitente, String msg, String time) {
        String[] mensaje = {remitente, msg, time};
        mensajes.putIfAbsent(contacto, new ArrayList<>());
        mensajes.get(contacto).add(mensaje);
    }

    public void sendMessage(String contacto, String remitente, String msg, String time) {
        String[] mensaje = {remitente, msg, time};
        cola.putIfAbsent(contacto, new ArrayList<>());
        cola.get(contacto).add(mensaje);
    }

    public Map<String, ArrayList<String[]>> fetchMessages() {
        Map<String, ArrayList<String[]>> msgs;
        synchronized (cola) {
            msgs = new HashMap<>(cola);
            loadMessages();
        }
        return msgs;
    }

    public void loadMessages() {
        for (String c : cola.keySet()) {
            for (String[] mensaje : cola.get(c)) {
                String remitente = mensaje[0];
                String msg = mensaje[1];
                String time = mensaje[2];
                saveMessage(c, remitente, msg, time);
            }
        }
        cola.clear();
    }

    public String getName() {
        return name;
    }

    public String getIP() {
        return IP;
    }

    public void cargarCopiaMensajes(Map<String, ArrayList<String[]>> m) {
        mensajes.clear();
        cola = new HashMap(m);
    }

    public boolean getConnected() {
        return this.connected;
    }

    public void setConnected(Boolean b) {
        this.connected = b;
    }

}
