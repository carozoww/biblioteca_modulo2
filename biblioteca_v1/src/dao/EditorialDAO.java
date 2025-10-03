package dao;

import basedatos.conexion;
import models.Editorial;
import models.Libro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EditorialDAO {
    public void crearEditorial(String nombre) {
        String consulta = "INSERT INTO editorial (nombre) VALUES (?)";

        try {
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(consulta);
            ps.setString(1, nombre);
            ps.executeUpdate();
            System.out.println("Editorial creada correctamente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editarEditorial(int idEditorial, String nombre) {
        String consulta = "UPDATE editorial SET  nombre = ? WHERE id_editorial = ?";

        try {
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(consulta);
            ps.setString(1, nombre);
            ps.setInt(2, idEditorial);
            ps.executeUpdate();


            System.out.println("Editorial modificada correctamente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void eliminarEditorial(int idEditorial) {
        String consultaUpd = "UPDATE libro SET id_editorial = null WHERE id_editorial = ?";
        String consulta = "DELETE FROM editorial WHERE id_editorial = ?";
        try {
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(consultaUpd);
            ps.setInt(1, idEditorial);
            ps.executeUpdate();

            PreparedStatement ps1 = conexion.getInstancia().getConnection().prepareStatement(consulta);
            ps1.setInt(1, idEditorial);
            ps1.executeUpdate();

            System.out.println("Editorial eliminada correctamente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Editorial> listarEditorial() {
        List<Editorial> editoriales = new ArrayList<>();
        String consulta = "SELECT  * FROM editorial";

        try{
            Statement st = conexion.getInstancia().getConnection().createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while(rs.next()){
                editoriales.add(new Editorial(rs.getInt("id_editorial"),
                        rs.getString("nombre")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return editoriales;
    }
    public Editorial buscarEditorialPorId(int idEditorial) {
        String consulta = "SELECT * FROM editorial WHERE id_editorial = ?";
        try {
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(consulta);
            ps.setInt(1, idEditorial);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Editorial(rs.getInt("id_editorial"),
                        rs.getString("nombre"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar editorial: " + e.getMessage(), e);
        }
        return null;
    }


    public void desasignarEditorial(int id_editorial){
        String query =  "UPDATE libro SET ed_asignada = FALSE WHERE id_editorial = ?";
        try{
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(query);
            ps.setInt(1, id_editorial);
            ps.executeUpdate();
            System.out.println("Todos los libros asignados a la editorial con la id "+id_editorial);
            System.out.println("han sido desasignados correctamente");
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

}
