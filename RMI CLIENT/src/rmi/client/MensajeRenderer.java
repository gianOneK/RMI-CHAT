package rmi.client;

import java.awt.*;
import javax.swing.*;

public class MensajeRenderer extends JPanel implements ListCellRenderer<Mensaje> {

    private static final int MAX_WIDTH = 200;

    public MensajeRenderer() {
        setOpaque(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Mensaje> list, Mensaje value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        removeAll();
        setBackground(list.getBackground());

        // Panel contenido con BoxLayout vertical
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setOpaque(true);
        panelContenido.setBackground(list.getBackground());

        // Remitente
        JLabel lblRemitente = new JLabel(value.getRemitente());
        lblRemitente.setFont(lblRemitente.getFont().deriveFont(Font.BOLD));
        lblRemitente.setForeground(Color.DARK_GRAY);

        // Texto: usar JTextPane para mejor manejo de texto multilínea
        JTextPane txtMensaje = new JTextPane();
        txtMensaje.setContentType("text/plain");
        txtMensaje.setText(value.getTexto());
        txtMensaje.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMensaje.setEditable(false);
        txtMensaje.setOpaque(true);
        txtMensaje.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        txtMensaje.setBackground(value.esMio() ? new Color(220, 248, 198) : new Color(240, 240, 240));
        txtMensaje.setForeground(Color.BLACK);

        // Limitar ancho máximo y permitir altura variable
        txtMensaje.setSize(new Dimension(MAX_WIDTH, Short.MAX_VALUE));
        Dimension preferred = txtMensaje.getPreferredSize();
        txtMensaje.setPreferredSize(new Dimension(MAX_WIDTH, preferred.height));
        txtMensaje.setMaximumSize(new Dimension(MAX_WIDTH, preferred.height));

        // Fecha
        JLabel lblFecha = new JLabel(value.getFecha());
        lblFecha.setFont(lblFecha.getFont().deriveFont(10f));
        lblFecha.setForeground(Color.GRAY);

        // Alineación basada en si es propio o ajeno
        float align = value.esMio() ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT;
        lblRemitente.setAlignmentX(align);
        txtMensaje.setAlignmentX(align);
        lblFecha.setAlignmentX(align);
        panelContenido.setAlignmentX(align);

        panelContenido.add(lblRemitente);
        panelContenido.add(Box.createVerticalStrut(2));
        panelContenido.add(txtMensaje);
        panelContenido.add(Box.createVerticalStrut(2));
        panelContenido.add(lblFecha);

        panelContenido.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Wrapper para alinear a izquierda o derecha
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

        // Manejar selección
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            panelContenido.setBackground(list.getSelectionBackground());
            txtMensaje.setBackground(list.getSelectionBackground());
        }

        return this;
    }
}
