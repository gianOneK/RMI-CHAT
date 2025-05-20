/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.server;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmi.pkginterface.IServer;

/**
 *
 * @author estudiante
 */
public class Server extends UnicastRemoteObject implements IServer{
    
    private final int PUERTO = 3232;
    private ArrayList<Usuario> usuarios = new ArrayList<>();
 
    public Server () throws RemoteException{
        
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("Iniciando...");
        (new Server()).iniciar();
    }
    
    private void iniciar() {
        try {
            String dirIP = (InetAddress.getLocalHost()).toString();
            System.out.println(dirIP+" : "+PUERTO);
            System.out.println("ola");
            Registry registry = LocateRegistry.createRegistry(PUERTO);
            registry.bind("rmiserver", this);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public String darBienvenida(String string) throws RemoteException {
        System.out.println("Ejecutando darBienvenida()...");
        return "Hola "+string;
    }

    @Override
    public int calcularMayor(int i, int i1) throws RemoteException {
        System.out.println("Ejecutando calcularMayor()...");
        return Math.max(i, i1);
    }
    
    public void violentarAGianKarlo(int numVeces){
        System.out.println("Gian ha sido violentado "+numVeces+" veces");
    }
    
    // REGISTRAR USUARIO
    
    @Override
    public void registrarUsuario(String name, String IP) throws RemoteException{
        usuarios.add(new Usuario(name, IP));
        System.out.println("ola");
        imprimirUsuarios();
    }
    
    public void imprimirUsuarios(){
        String lista = "";
        for(Usuario u:usuarios){
            lista+=u.getName()+"\n";
        }
    }
    
    
    
}
