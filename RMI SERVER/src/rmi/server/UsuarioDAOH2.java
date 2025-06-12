/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi.server;

/**
 *
 * @author Kevin
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioDAOH2 {

    // Crea la tabla si no existe. Cada tabla representa el historial de un usuario.
    
    public void vaciarTodasLasTablasMensajes() {
    String sqlTablas = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME LIKE 'MSG_%'";
        System.out.println("VACIANDO TABLAS");

    try (Connection conn = H2Utils.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sqlTablas)) {

        while (rs.next()) {
            String nombreTabla = rs.getString("TABLE_NAME");
            String deleteSql = "DELETE FROM " + nombreTabla;

            try (Statement deleteStmt = conn.createStatement()) {
                deleteStmt.executeUpdate(deleteSql);
                System.out.println("Vaciada tabla: " + nombreTabla);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    
    public void crearTablaSiNoExiste(String nombreUsuario) {
        String tableName = obtenerNombreTabla(nombreUsuario);
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                + "id IDENTITY PRIMARY KEY, "
                + "contacto VARCHAR(255), "
                + "remitente VARCHAR(255), "
                + "mensaje VARCHAR(2000), "
                + "fechaHora VARCHAR(50))";
        try (Connection conn = H2Utils.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Guarda un nuevo mensaje
    public void insertarMensaje(String nombreUsuario, String contacto, String remitente, String mensaje, String fechaHora) {
        String sql = "INSERT INTO " + obtenerNombreTabla(nombreUsuario)
                + " (contacto, remitente, mensaje, fechaHora) VALUES (?, ?, ?, ?)";
        try (Connection conn = H2Utils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, contacto);
            ps.setString(2, remitente);
            ps.setString(3, mensaje);
            ps.setString(4, fechaHora);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lee todos los mensajes organizados por contacto
    public Map<String, ArrayList<String[]>> obtenerMensajes(String nombreUsuario) {
        HashMap<String, ArrayList<String[]>> mensajesPorContacto = new HashMap<>();
        String sql = "SELECT contacto, remitente, mensaje, fechaHora FROM " + obtenerNombreTabla(nombreUsuario);

        try (Connection conn = H2Utils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String contacto = rs.getString("contacto");
                String remitente = rs.getString("remitente");
                String mensaje = rs.getString("mensaje");
                String fechaHora = rs.getString("fechaHora");

                String[] mensajeInfo = {remitente, mensaje, fechaHora};

                mensajesPorContacto
                        .computeIfAbsent(contacto, k -> new ArrayList<>())
                        .add(mensajeInfo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mensajesPorContacto;
    }

    // Formatea el nombre de la tabla
    private String obtenerNombreTabla(String nombreUsuario) {
        return "msg_" + nombreUsuario.replaceAll("\\W", "_").toLowerCase();
    }
}
