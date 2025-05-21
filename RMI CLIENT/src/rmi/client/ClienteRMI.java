/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.client;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class ClienteRMI {
    private final String name;
    private final String IP;
    private final List<String> inbox = Collections.synchronizedList(new LinkedList<>());

    public ClienteRMI(String name, String IP) {
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
