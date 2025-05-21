/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.client;

import java.rmi.RemoteException;

/**
 *
 * @author juan llanos
 */
public class IngresoControlador {
    
    private GUIIngreso vista;
   
    public IngresoControlador(GUIIngreso vista){
    this.vista=vista;
    
    }
    
  
    
    
    
    public void ingresarServer() throws RemoteException{
        
       if(vista.getTxtNombre()==null){
           limpiar();
           System.out.println("No hay un nombre escrito");
           return ;
       }
      String nombre=vista.getTxtNombre().getText();
      
       generarGUI(nombre);
        limpiar();
    }
    
    
    public void limpiar(){
       vista.getTxtNombre().setText("");
    }
    
    
    public void generarGUI(String nombre) throws RemoteException{
        //GUICliente g=new GUICliente();
        //GUIClienteControlador c=new GUIClienteControlador(g,nombre);
        //g.setControlador(c);
        //g.setVisible(true);
        vista.setVisible(false);
    }
    
}
