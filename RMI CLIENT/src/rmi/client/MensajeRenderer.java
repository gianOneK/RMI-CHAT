package rmi.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.*;
import java.awt.Dimension;

public class MensajeRenderer extends JPanel implements ListCellRenderer<Mensaje> {

    public MensajeRenderer() {
        setOpaque(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // más control, sin márgenes automáticos
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Mensaje> list, Mensaje value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        removeAll();
        setBackground(list.getBackground());

        // Panel contenedor de todo
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setOpaque(true);
        panelContenido.setBackground(list.getBackground());

        // Remitente
        JLabel lblRemitente = new JLabel(value.getRemitente());
        lblRemitente.setFont(lblRemitente.getFont().deriveFont(Font.BOLD));
        lblRemitente.setForeground(Color.DARK_GRAY);

        // Texto
        JTextArea txtMensaje = new JTextArea(value.getTexto());
        txtMensaje.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        txtMensaje.setEditable(false);
        txtMensaje.setOpaque(true);
        txtMensaje.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        txtMensaje.setBackground(value.esMio() ? new Color(220, 248, 198) : new Color(240, 240, 240));
        txtMensaje.setForeground(Color.BLACK);

        // Establecer el ancho y dejar que JTextArea calcule la altura
        int maxAncho = 200;
        txtMensaje.setSize(new Dimension(maxAncho, Short.MAX_VALUE));
        txtMensaje.setMaximumSize(new Dimension(maxAncho, txtMensaje.getPreferredSize().height));

        // Fecha
        JLabel lblFecha = new JLabel(value.getFecha());
        lblFecha.setFont(lblFecha.getFont().deriveFont(10f));
        lblFecha.setForeground(Color.GRAY);

        // Alineaciones (remitente, mensaje, fecha)
        float align = value.esMio() ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT;
        lblRemitente.setAlignmentX(align);
        txtMensaje.setAlignmentX(align);
        lblFecha.setAlignmentX(align);
        panelContenido.setAlignmentX(align);

        // Agregar todo
        panelContenido.add(lblRemitente);
        panelContenido.add(Box.createVerticalStrut(2));
        panelContenido.add(txtMensaje);
        panelContenido.add(Box.createVerticalStrut(2));
        panelContenido.add(lblFecha);

        // Agregar padding lateral
        int paddingLateral = 10;
        panelContenido.setBorder(BorderFactory.createEmptyBorder(5, paddingLateral, 5, paddingLateral));

        // Envolver en panel para espacio horizontal
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.setOpaque(false);

        if (value.esMio()) {
            wrapper.add(Box.createHorizontalGlue());
            wrapper.add(panelContenido);
        } else {
            wrapper.add(panelContenido);
            wrapper.add(Box.createHorizontalGlue());
        }

        add(wrapper);
        return this;
    }
}
