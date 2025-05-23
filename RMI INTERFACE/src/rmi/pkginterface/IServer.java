package rmi.pkginterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IServer extends Remote{

    String registrarUsuario(String name, String IP) throws RemoteException;

    void sendDirectMessage(String from, String to, String message) throws RemoteException;

    void sendGlobalMessage(String from, String message) throws RemoteException;

    Map<String, ArrayList<String[]>> fetchMessages(String name) throws RemoteException;

    void desconectarUsuario(String name) throws RemoteException;
    
    List<String> getConnectedUsers() throws RemoteException ;
    
    void latido(String username) throws RemoteException;
    
}
