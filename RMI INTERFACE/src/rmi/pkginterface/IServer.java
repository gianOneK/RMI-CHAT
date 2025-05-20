package rmi.pkginterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IServer extends Remote {
    String darBienvenida(String n) throws RemoteException;
    int calcularMayor(int num1, int num2) throws RemoteException;
    String registrarUsuario(String name, String IP) throws RemoteException;
    void sendDirectMessage(String from, String to, String message) throws RemoteException;
    void sendGlobalMessage(String from, String message) throws RemoteException;
    List<String> fetchMessages(String name) throws RemoteException;
}  