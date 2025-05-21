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

    public IngresoControlador(GUIIngreso vista) {
        this.vista = vista;

    }

    public void ingresarServer() throws RemoteException {

        String nombre = vista.getTxtNombre().getText();

        if (nombre == null || nombre.trim().isEmpty()) {

            System.out.println("No hay un nombre escrito");

        } else {

            generarGUI(nombre);
            limpiar();
        }
    }

    public void limpiar() {
        vista.getTxtNombre().setText("");
    }

    public void generarGUI(String nombre) throws RemoteException {
        guiChat chat = new guiChat(nombre);
        System.out.println("Nombre: " + nombre);
        chat.setNombreUsuario(nombre);
        vista.setVisible(false);
        chat.setVisible(true);

    }

}
