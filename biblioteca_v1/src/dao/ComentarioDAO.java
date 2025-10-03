package dao;

import basedatos.conexion;
import models.Comentario;
import models.Libro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ComentarioDAO {

    public void eliminarComentario(int id_comentario){

        String consulta = "DELETE FROM comentario WHERE id_comentario = ?";
        try {
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(consulta);

            ps.setInt(1, id_comentario);
            ps.executeUpdate();

            System.out.println("Comentario eliminado exitosamente");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public List<Comentario> listarComentario() {
        String consulta = "SELECT * FROM comentario";
        List<Comentario> comentarios = new ArrayList<>();

        try {
            Statement st = conexion.getInstancia().getConnection().createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                comentarios.add(new Comentario(rs.getInt("id_comentario"),
                        rs.getString("contenido")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comentarios;
    }

    public Comentario buscarComentarioPorId(int ID) {
        String consulta = "SELECT * FROM comentario WHERE id_comentario = ?";
        try (PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Comentario(
                        rs.getInt("ID"),
                        rs.getString("contenido")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar comentario: " + e.getMessage(), e);
        }
        return null;
    }

}
