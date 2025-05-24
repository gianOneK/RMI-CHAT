package rmi.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class MensajeRenderer extends JPanel implements ListCellRenderer<Mensaje> {

    public MensajeRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Mensaje> list, Mensaje value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        removeAll();

        // Panel de burbuja de mensaje
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new javax.swing.BoxLayout(panelContenido, javax.swing.BoxLayout.Y_AXIS));
        panelContenido.setOpaque(true);

        // Etiqueta de remitente en negrilla
        JLabel lblRemitente = new JLabel(value.getRemitente());
        lblRemitente.setFont(lblRemitente.getFont().deriveFont(Font.BOLD));

        // Etiqueta de texto del mensaje
        JLabel lblTexto = new JLabel("<html><body style='width: 200px;'>" + value.getTexto() + "</body></html>");
        lblTexto.setFont(lblTexto.getFont().deriveFont(Font.PLAIN));
        lblTexto.setOpaque(true);
        lblTexto.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Etiqueta de la fecha
        JLabel lblFecha = new JLabel(value.getFecha());
        lblFecha.setFont(lblFecha.getFont().deriveFont(10f));

        // Colores diferentes si es propio o ajeno
        if (value.esMio()) {
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            lblTexto.setBackground(new Color(220, 248, 198)); // verde claro
        } else {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            lblTexto.setBackground(new Color(240, 240, 240)); // gris claro
        }

        // Agregamos etiquetas al panel de contenido
        panelContenido.add(lblRemitente);
        panelContenido.add(lblTexto);
        panelContenido.add(lblFecha);

        // Colores de selección
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            panelContenido.setBackground(list.getSelectionBackground());
        } else {
            setBackground(list.getBackground());
            panelContenido.setBackground(list.getBackground());
        }

        add(panelContenido);
        return this;
    }
}
