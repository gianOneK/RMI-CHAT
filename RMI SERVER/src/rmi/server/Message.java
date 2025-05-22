/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.server;

import java.time.LocalDateTime;

/**
 *
 * @author Kevin
 */
public class Message {
    private String remitente;
    private String msg;
    private LocalDateTime time;

    public Message(String remitente, String msg, LocalDateTime time) {
        this.remitente = remitente;
        this.msg = msg;
        this.time = time;
    }

    public Message() {
    }
    
    
    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    
    
}
