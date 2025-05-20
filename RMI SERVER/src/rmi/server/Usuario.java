/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.server;

/**
 *
 * @author Kevin
 */
public class Usuario {
    private String name;
    private String IP;

    public Usuario(String name, String IP) {
        this.name = name;
        this.IP = IP;
    }

    public Usuario() {
        
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
    
}
