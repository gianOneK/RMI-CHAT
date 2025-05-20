/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.client;


import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import rmi.pkginterface.IServer;
/**
 *
 * @author estudiante
 */
public class Invocador {
    
    public static void main(String[] args) {
        try{
            Registry reg = LocateRegistry.getRegistry("localhost", 3232);
            IServer objRemoto = (IServer) reg.lookup("rmiserver");
            System.out.println("ola2");
            
            // REGISTRAR USUARIO
            Scanner sc = new Scanner(System.in);
            String name = sc.next();
            String IP = sc.next();
            
            objRemoto.registrarUsuario(name, IP);
                    
            String saludo = objRemoto.darBienvenida("Juan Karlo");
            int num = objRemoto.calcularMayor(2, 10);
            
            
            System.out.println(saludo);
            System.out.println("Número = "+ num);
            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
