/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.client;

import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Kevin
 */
public class MensajeRenderer extends JPanel implements ListCellRenderer<Mensaje> {
    private JLabel lblTexto = new JLabel();
    private JLabel lblFecha = new JLabel();

    public MensajeRenderer() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(true);
        lblTexto.setOpaque(true);
        lblFecha.setFont(lblFecha.getFont().deriveFont(10f));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Mensaje> list, Mensaje value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        removeAll();

        lblTexto.setText("<html><body style='width: 200px;'>" + value.getTexto() + "</body></html>");
        lblFecha.setText(value.getFecha());

        if (value.esMio()) {
            setAlignmentX(Component.RIGHT_ALIGNMENT);
            lblTexto.setHorizontalAlignment(JLabel.RIGHT);
        } else {
            setAlignmentX(Component.LEFT_ALIGNMENT);
            lblTexto.setHorizontalAlignment(JLabel.LEFT);
        }

        add(lblTexto);
        add(lblFecha);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            lblTexto.setBackground(list.getSelectionBackground());
        } else {
            setBackground(list.getBackground());
            lblTexto.setBackground(list.getBackground());
        }

        return this;
    }
}

