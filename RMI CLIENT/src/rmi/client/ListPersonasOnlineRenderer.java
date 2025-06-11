/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.client;

/**
 *
 * @author FABIAN FLOREZ
 */
import javax.swing.*;
import java.awt.*;
import java.util.Set;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class ListPersonasOnlineRenderer extends DefaultListCellRenderer {

    private final Set<String> usuariosConMensajes;

    public ListPersonasOnlineRenderer(Set<String> usuariosConMensajes) {
        this.usuariosConMensajes = usuariosConMensajes;
    }

    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        String nombreUsuario = value.toString();

        if (usuariosConMensajes.contains(nombreUsuario)) {
            label.setForeground(new Color(0, 255, 0)); 
            label.setFont(label.getFont().deriveFont(Font.BOLD)); 
        } else {
            label.setForeground(Color.BLACK);
            label.setFont(label.getFont().deriveFont(Font.PLAIN));
        }

        return label;
    }
}

