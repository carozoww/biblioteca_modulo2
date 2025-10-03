package dao;

import basedatos.conexion;
import models.Penalizacion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PenalizacionDAO {
    public void crearPenalizacion(int duracion, String motivo, int id_lector, int id_admin) {
        String consulta = "INSERT INTO penalizacion (duracion, motivo, id_lector, id_admin) VALUES (?,?,?,?)";
        try{
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(consulta);
            ps.setInt(1, duracion);
            ps.setString(2, motivo);
            ps.setInt(3, id_lector);
            ps.setInt(4, id_admin);
            ps.executeUpdate();

            System.out.println("Penalizacion creada correctamente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void eliminarPenalizacion(int id) {
        String consulta = "DELETE FROM penalizacion WHERE id_lector = ?";
        try {
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(consulta);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Penalizacion eliminada correctamente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Penalizacion> listarPenalizaciones() {
        List<Penalizacion> lista = new ArrayList<>();
        String consulta = "SELECT p.id_pena, p.duracion, p.motivo, p.id_lector, p.id_admin, l.nombre AS lector, a.nombre AS admin " +
                "FROM penalizacion p " +
                "LEFT JOIN lector l ON p.id_lector = l.ID " +
                "LEFT JOIN administrador a ON p.id_admin = a.ID";
        try {
             Statement st = conexion.getInstancia().getConnection().createStatement();
             ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                lista.add(new Penalizacion(rs.getInt("id_pena"),
                rs.getInt("duracion"),
                rs.getString("motivo"),
                rs.getInt("id_lector"),
                rs.getInt("id_admin"),
                rs.getString("lector"),
                rs.getString("admin")));

            }
        } catch (SQLException e) {
            System.out.println("Error listando penalizaciones: " + e.getMessage());
        }
        return lista;
    }

    public List<Penalizacion> buscarPenalizacionActivaPorUsuario(int id) {
        List<Penalizacion> lista = new ArrayList<>();
        String consulta = "SELECT p.id_pena, p.duracion, p.motivo, p.id_lector, p.id_admin, " +
                "l.nombre AS lector, a.nombre AS admin " +
                "FROM penalizacion p " +
                "LEFT JOIN lector l ON p.id_lector = l.ID " +
                "LEFT JOIN administrador a ON p.id_admin = a.ID " +
                "WHERE p.id_lector = ?";

        try {
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(consulta);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Penalizacion(rs.getInt("id_pena"),
                        rs.getInt("duracion"),
                        rs.getString("motivo"),
                        rs.getInt("id_lector"),
                        rs.getInt("id_admin"),
                        rs.getString("lector"),
                        rs.getString("admin")));

            }
        } catch (SQLException e) {
            System.out.println("Error listando penalización: " + e.getMessage());
        }
        return lista;
    }


}
