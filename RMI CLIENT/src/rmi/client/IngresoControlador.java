/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.client;

import java.rmi.RemoteException;
import javax.swing.JOptionPane;

/**
 *
 * @author juan llanos
 */
public class IngresoControlador {

    private IngresoGUI vista;

    public IngresoControlador(IngresoGUI vista) {
        this.vista = vista;

    }

    public void ingresarServer() throws RemoteException, Exception {

        String nombre = vista.getTxtNombre().getText();
        String ip = vista.getIpServidor();

        if (nombre == null || nombre.trim().isEmpty()) {

            System.out.println("No hay un nombre escrito");

        } else {
            Cliente.getInstance().setName(nombre);
            Cliente.getInstance().setIpServidor(ip);
            String estadoRegistro = Cliente.getInstance().register();
            generarGUI();
            JOptionPane msg = new JOptionPane(estadoRegistro);
            limpiar();
        }
    }

    public void limpiar() {
        vista.getTxtNombre().setText("");
    }

    public void generarGUI() throws RemoteException, Exception {
        ChatGUI chat = new ChatGUI();
        System.out.println("Nombre: " + Cliente.getInstance().getName());
        vista.dispose();
        chat.setVisible(true);

    }

}
