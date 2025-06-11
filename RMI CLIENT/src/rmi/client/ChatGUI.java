/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package rmi.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import rmi.client.Mensaje;

/**
 *
 * @author Usuario
 */
public class ChatGUI extends javax.swing.JFrame {

    private DefaultListModel<String> userModel = new DefaultListModel<>();
    private Map<String, DefaultListModel<Mensaje>> chats = new HashMap<>();
    private Set<String> usuariosConMensajes = new HashSet<>();
    private ChatControlador controlador;

   
    public ChatGUI() throws Exception {
        initComponents();
        controlador = new ChatControlador(this);

        // Configuro lstChat para que use mi renderer y modelo dinámico
        listPersonasOnline.setModel((userModel));
        listPersonasOnline.setCellRenderer(new ListPersonasOnlineRenderer(usuariosConMensajes));
        lstChat.setCellRenderer(new MensajeRenderer());
        lstChat.setFixedCellHeight(-1);  // permitir altura variable
        lstChat.setLayoutOrientation(JList.VERTICAL);
        ajustarScrollPaneChat();

        
        //LISTENERS Y ADAPTERS (Son como actualizar los objetos cuando se modifican)
        
        txtEnviarMensaje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnEnviarMensajeActionPerformed(e);
            }
        });

        layPaneChat.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> ajustarScrollPaneChat());
            }
        });

        listPersonasOnline.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String usuario = listPersonasOnline.getSelectedValue();
                    if (usuario != null) {
                        // Si no hay modelo para ese usuario, lo creo
                        chats.putIfAbsent(usuario, new DefaultListModel<>());
                        // Cambio el modelo de lstChat
                        lstChat.setModel(chats.get(usuario));
                        usuariosConMensajes.remove(usuario);
                        txtContacto.setText(usuario);
                    }
                }
            }
        });

    }
    // AJUSTA LAS DIMENSIONES DE LA SCROLLBAR DEL CHAT

    private void ajustarScrollPaneChat() {
        int alturaDisponible = layPaneChat.getHeight();
        int alturaPanelEnviar = panelEnviarMensaje.getHeight();

        // Altura final para el JScrollPane
        int nuevaAltura = alturaDisponible - alturaPanelEnviar;
        if (nuevaAltura < 0) {
            nuevaAltura = 0;
        }

        scrlPanelChat.setBounds(0, 0, layPaneChat.getWidth(), nuevaAltura);
        panelEnviarMensaje.setLocation(10, nuevaAltura); // Reubicar el panel al fondo
    }

    //ACTUALIZA TODAS LAS BANDEJAS DE MENSAJES (ThreadChatActualizar)
    public void refrescarMensajes(Map<String, ArrayList<String[]>> todosLosMensajes) {
        boolean needsUpdate = false;
        for (Map.Entry<String, ArrayList<String[]>> entry : todosLosMensajes.entrySet()) {
            String contacto = entry.getKey();
            chats.putIfAbsent(contacto, new DefaultListModel<>());
            DefaultListModel<Mensaje> modelo = chats.get(contacto); // Batch add - más eficiente 
            List<Mensaje> nuevos = new ArrayList<>();
            for (String[] msgArr : entry.getValue()) {
                nuevos.add(new Mensaje(msgArr[0], msgArr[1], msgArr[2]));
                if (contacto.equals(msgArr[0]) || contacto.equals("Chat Global")) {
                    marcarUsuarioConMensaje(contacto);
                }
            }
            // Agregar todos de una vez 
            for (Mensaje msg : nuevos) {
                modelo.addElement(msg);
            }
            if (!nuevos.isEmpty() && contacto.equals(listPersonasOnline.getSelectedValue())) {
                needsUpdate = true;
            }
        }
        // Una sola actualización al final 
        if (needsUpdate) {
            SwingUtilities.invokeLater(() -> {
                lstChat.revalidate();
                lstChat.ensureIndexIsVisible(lstChat.getModel().getSize() - 1);
            });
        }
    }

    //ACTUALIZA LOS USUARIOS ACTIVOS (ThreadChatListUsuarios)
    public void actulizarListado(List<String> usuarios) {
        // 1) Build a Set de los nuevos usuarios para búsquedas rápidas
        Set<String> nuevos = new HashSet<>(usuarios);
        // 2) Build a Set de los actuales en el modelo
        Set<String> actuales = new HashSet<>();
        for (int i = 0; i < userModel.getSize(); i++) {
            actuales.add(userModel.getElementAt(i));
        }

        // 3) Eliminar los que ya no están
        for (int i = userModel.getSize() - 1; i >= 0; i--) {
            String elemento = userModel.getElementAt(i);
            if (!nuevos.contains(elemento)) {
                userModel.remove(i);
            }
        }

        // 4) Añadir los que faltan
        for (String u : usuarios) {
            if (!actuales.contains(u)) {
                userModel.addElement(u);
            }
        }

        // 5) Asegurarte de que la selección sigue siendo válida
        String seleccionado = listPersonasOnline.getSelectedValue();
        if (seleccionado != null && nuevos.contains(seleccionado)) {
            listPersonasOnline.setSelectedValue(seleccionado, true);
        }
    }

    //CAMBIA EL COLOR DE MENSAJES SIN LEER (ListPersonasOnlineRenderer)
    public void marcarUsuarioConMensaje(String usuario) {
        usuariosConMensajes.add(usuario);
        listPersonasOnline.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelDerecha = new javax.swing.JPanel();
        panelSuperior = new javax.swing.JPanel();
        txtContacto = new javax.swing.JLabel();
        layPaneChat = new javax.swing.JLayeredPane();
        scrlPanelChat = new javax.swing.JScrollPane();
        lstChat = new javax.swing.JList<>();
        panelEnviarMensaje = new javax.swing.JPanel();
        txtEnviarMensaje = new javax.swing.JTextField();
        btnEnviarMensaje = new javax.swing.JButton();
        panelIzquierda = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listPersonasOnline = new javax.swing.JList<>();
        BtbSalir = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Whatsapp Web");
        setLocation(new java.awt.Point(0, 0));

        panelDerecha.setBackground(new java.awt.Color(51, 51, 255));
        panelDerecha.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        panelDerecha.setMinimumSize(new java.awt.Dimension(100, 100));
        panelDerecha.setOpaque(false);
        panelDerecha.setLayout(new java.awt.BorderLayout());

        panelSuperior.setBackground(new java.awt.Color(255, 255, 255));
        panelSuperior.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        txtContacto.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtContacto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtContacto.setText("Selecciona un chat !");

        javax.swing.GroupLayout panelSuperiorLayout = new javax.swing.GroupLayout(panelSuperior);
        panelSuperior.setLayout(panelSuperiorLayout);
        panelSuperiorLayout.setHorizontalGroup(
            panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSuperiorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtContacto, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        panelSuperiorLayout.setVerticalGroup(
            panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSuperiorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtContacto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelDerecha.add(panelSuperior, java.awt.BorderLayout.NORTH);

        layPaneChat.setPreferredSize(new java.awt.Dimension(598, 310));

        scrlPanelChat.setBackground(new java.awt.Color(51, 255, 255));
        scrlPanelChat.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrlPanelChat.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrlPanelChat.setAlignmentY(Component.TOP_ALIGNMENT);
        scrlPanelChat.setMaximumSize(new java.awt.Dimension(592, 310));
        scrlPanelChat.setPreferredSize(new java.awt.Dimension(598, 310));

        lstChat.setModel(new DefaultListModel<>());
        lstChat.setMaximumSize(new java.awt.Dimension(598, 310));
        lstChat.setMinimumSize(new java.awt.Dimension(598, 310));
        scrlPanelChat.setViewportView(lstChat);

        layPaneChat.setLayer(scrlPanelChat, javax.swing.JLayeredPane.MODAL_LAYER);
        layPaneChat.add(scrlPanelChat);
        scrlPanelChat.setBounds(0, 0, 580, 340);

        panelEnviarMensaje.setBackground(new java.awt.Color(255, 255, 153));
        panelEnviarMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEnviarMensaje.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        panelEnviarMensaje.setMaximumSize(new java.awt.Dimension(300, 50));
        panelEnviarMensaje.setMinimumSize(new java.awt.Dimension(100, 20));
        panelEnviarMensaje.setOpaque(false);
        panelEnviarMensaje.setPreferredSize(new java.awt.Dimension(200, 50));
        panelEnviarMensaje.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 10));

        txtEnviarMensaje.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtEnviarMensaje.setToolTipText("Escribe...");
        txtEnviarMensaje.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtEnviarMensaje.setKeymap(null);
        txtEnviarMensaje.setMargin(new java.awt.Insets(2, 4, 2, 6));
        txtEnviarMensaje.setMaximumSize(new java.awt.Dimension(500, 500));
        txtEnviarMensaje.setMinimumSize(new java.awt.Dimension(100, 80));
        txtEnviarMensaje.setOpaque(true);
        txtEnviarMensaje.setPreferredSize(new java.awt.Dimension(450, 50));
        panelEnviarMensaje.add(txtEnviarMensaje);

        btnEnviarMensaje.setBackground(new java.awt.Color(51, 255, 51));
        btnEnviarMensaje.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnEnviarMensaje.setText(">");
        btnEnviarMensaje.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnEnviarMensaje.setPreferredSize(new java.awt.Dimension(50, 30));
        btnEnviarMensaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarMensajeActionPerformed(evt);
            }
        });
        panelEnviarMensaje.add(btnEnviarMensaje);

        layPaneChat.setLayer(panelEnviarMensaje, javax.swing.JLayeredPane.POPUP_LAYER);
        layPaneChat.add(panelEnviarMensaje);
        panelEnviarMensaje.setBounds(10, 260, 586, 73);

        panelDerecha.add(layPaneChat, java.awt.BorderLayout.CENTER);

        panelIzquierda.setBorder(panelDerecha.getBorder());
        panelIzquierda.setPreferredSize(new java.awt.Dimension(235, 294));
        panelIzquierda.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(258, 180));

        listPersonasOnline.setModel(new DefaultListModel<>());
        jScrollPane1.setViewportView(listPersonasOnline);

        panelIzquierda.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        BtbSalir.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        BtbSalir.setText("Salir");
        BtbSalir.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        BtbSalir.setPreferredSize(new java.awt.Dimension(72, 46));
        BtbSalir.setRequestFocusEnabled(false);
        panelIzquierda.add(BtbSalir, java.awt.BorderLayout.SOUTH);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(panelSuperior.getBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(223, 46));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Whatsapp");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel1)
                .addContainerGap(81, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(37, 37, 37))
        );

        panelIzquierda.add(jPanel1, java.awt.BorderLayout.NORTH);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelIzquierda, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panelDerecha, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panelIzquierda, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelDerecha, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // ENVIA MENSAJES (Controlador -> ThreadEnviarMensaje)
    private void btnEnviarMensajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarMensajeActionPerformed
        try {
            // VERIFICACION DEL MENSAJE
            String texto = txtEnviarMensaje.getText().trim();
            String destino = listPersonasOnline.getSelectedValue();
            if (!texto.isEmpty() && destino != null) {

                //MENSAJE GLOBAL
                if ("Chat Global".equals(destino)) {
                    controlador.enviarMensajeGlobal(texto);
                    txtEnviarMensaje.setText("");
                } //MENSAJE DIRECTO
                else {
                    // 1)Enviar por RMI
                    controlador.enviarMensajeDirecto(destino, texto);
                    // 2) Añadir al propio modelo (para verlo instantáneo)
                    DefaultListModel<Mensaje> modelo = chats.get(destino);

                    //VACIAR BANDEJA DE MENSAJERIA
                    txtEnviarMensaje.setText("");
                    //AJUSTAR LA BARRA DE SCROLL AL FINAL
                    lstChat.ensureIndexIsVisible(modelo.getSize() - 1);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("error btnMensajes");
        }
    }//GEN-LAST:event_btnEnviarMensajeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new ChatGUI().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtbSalir;
    private javax.swing.JButton btnEnviarMensaje;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLayeredPane layPaneChat;
    private javax.swing.JList<String> listPersonasOnline;
    private javax.swing.JList<Mensaje> lstChat;
    private javax.swing.JPanel panelDerecha;
    private javax.swing.JPanel panelEnviarMensaje;
    private javax.swing.JPanel panelIzquierda;
    private javax.swing.JPanel panelSuperior;
    private javax.swing.JScrollPane scrlPanelChat;
    private javax.swing.JLabel txtContacto;
    private javax.swing.JTextField txtEnviarMensaje;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the BtbSalir
     */
    public javax.swing.JButton getBtbSalir() {
        return BtbSalir;
    }

}
