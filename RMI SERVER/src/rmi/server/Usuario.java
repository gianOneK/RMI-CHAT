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
    private final List<String> inbox = Collections.synchronizedList(new LinkedList<>());
    private final Map<String, ArrayList<Message>> mensajes = new HashMap<>();

    public Usuario(String name, String IP) {
        this.name = name;
        this.IP = IP;
    }

    public void saveMessage(String contacto, String remitente, String msg, LocalDateTime time) {
        Message mensaje = new Message(remitente, msg, time);
        mensajes.putIfAbsent(contacto, new ArrayList<>());
        mensajes.get(contacto).add(mensaje);
    }

    public String getName() {
        return name;
    }

    public String getIP() {
        return IP;
    }

    public void addMessage(String msg) {
        inbox.add(msg);
    }

    public List<String> fetchMessages() {
        List<String> msgs;
        synchronized (inbox) {
            msgs = new LinkedList<>(inbox);
            inbox.clear();
        }
        return msgs;
    }
}
