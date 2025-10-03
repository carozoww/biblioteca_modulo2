package dao;

import basedatos.conexion;
import models.Lector;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LectorDAO {

    public void crearLector(String nombre, String cedula, String telefono, String direccion,
                            boolean autenticacion, LocalDate fechaNac, boolean membresia,
                            String correo, String contrasenia) {

        String consulta = "INSERT INTO lector (nombre, cedula, telefono, direccion, autenticacion, fechaNac, membresia, correo, contrasenia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, nombre);
            ps.setString(2, cedula);
            ps.setString(3, telefono);
            ps.setString(4, direccion);
            ps.setBoolean(5, autenticacion);

            if (fechaNac != null) ps.setDate(6, Date.valueOf(fechaNac));
            else ps.setNull(6, Types.DATE);

            ps.setBoolean(7, membresia);      // faltaba
            ps.setString(8, correo);           // faltaba
            ps.setString(9, contrasenia);      // faltaba

            ps.executeUpdate();
            System.out.println("Lector creado correctamente");

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear lector: " + e.getMessage(), e);
        }
    }


    public Lector buscarPorId(int ID) {
        String consulta = "SELECT * FROM lector WHERE ID = ?";
        try (PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Lector(
                        rs.getInt("ID"),
                        rs.getString("nombre"),
                        rs.getString("cedula"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("autenticacion"),
                        rs.getString("fechaNac"),
                        rs.getString("membresia"),
                        rs.getString("correo")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar lector: " + e.getMessage(), e);
        }
        return null;
    }



    public void editarLector(int ID, String nombre, String cedula, String telefono, String direccion, boolean autenticacion, LocalDate fechaNac, boolean membresia, String correo, String contrasenia) {
        String consulta = "UPDATE lector SET nombre = ?, cedula = ?, telefono = ?, direccion = ?, autenticacion = ?, fechaNac = ?, membresia = ?, correo = ?, contrasenia = ? WHERE ID = ?";
        try (PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setString(1, nombre);
            ps.setString(2, cedula);
            ps.setString(3, telefono);
            ps.setString(4, direccion);
            ps.setBoolean(5, autenticacion);
            if (fechaNac != null) ps.setDate(6, Date.valueOf(fechaNac));
            else ps.setNull(6, Types.DATE);
            ps.setBoolean(7, membresia);
            ps.setString(8, correo);
            ps.setString(9, contrasenia);
            ps.setInt(10, ID);
            ps.executeUpdate();
            System.out.println("Lector modificado correctamente");
        } catch (SQLException e) {
            throw new RuntimeException("Error al editar lector: " + e.getMessage(), e);
        }
    }

    public void eliminarLector(int ID) {
        String consulta = "DELETE FROM lector WHERE ID = ?";
        try (PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, ID);
            ps.executeUpdate();
            System.out.println("Lector eliminado correctamente");
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar lector: " + e.getMessage(), e);
        }
    }

    public List<Lector> listarLectores() {
        List<Lector> lectores = new ArrayList<>();
        String consulta = "SELECT * FROM lector";
        try (Statement st = conexion.getInstancia().getConnection().createStatement();
             ResultSet rs = st.executeQuery(consulta)) {
            while (rs.next()) {
                lectores.add(new Lector(
                        rs.getInt("ID"),
                        rs.getString("nombre"),
                        rs.getString("cedula"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getBoolean("autenticacion"),
                        rs.getDate("fechaNac")!= null ? rs.getDate("fechaNac").toLocalDate() : null,
                        rs.getBoolean("membresia"),
                        rs.getString("correo"),
                        rs.getString("contrasenia")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar lectores: " + e.getMessage(), e);
        }
        return lectores;
    }
}

