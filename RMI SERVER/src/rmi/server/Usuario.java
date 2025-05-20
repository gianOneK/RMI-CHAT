package rmi.server;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Usuario {
    private final String name;
    private final String IP;
    private final List<String> inbox = Collections.synchronizedList(new LinkedList<>());

    public Usuario(String name, String IP) {
        this.name = name;
        this.IP = IP;
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
