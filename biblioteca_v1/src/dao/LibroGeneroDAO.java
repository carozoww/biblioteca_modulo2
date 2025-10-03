package dao;

import basedatos.conexion;
import models.Genero;
import models.Libro;
import models.LibroGenero;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LibroGeneroDAO {

    public static void asignarGeneroLibro(int id_libro, int id_genero){
        String query = "INSERT INTO libro_genero(id_libro,id_genero) VALUES (?,?)";

        try{
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(query);
            ps.setInt(1, id_libro);
            ps.setInt(2, id_genero);
            ps.executeUpdate();

            System.out.println("Genero asignado correctamente a libro ");

        }catch(SQLException e){
            new RuntimeException(e);
        }
    }

    public List<LibroGenero> listarGenerosDeLibros(){
        List<LibroGenero> lista = new ArrayList<>();
        String query = "Select l.id_libro,titulo,fecha_publicacion,isbn,id_editorial, g.id_genero,nombre from libro l JOIN libro_genero lg\n" +
                "on l.id_libro = lg.id_libro JOIN genero g ON lg.id_genero = g.id_genero;";
        try{
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                lista.add(new LibroGenero(
                        new Libro(
                                rs.getInt("id_libro"),
                                rs.getString("titulo"),
                                rs.getString("isbn"),
                                rs.getDate("fecha_publicacion"),
                                rs.getInt("id_editorial"),
                                rs.getString("nombre")
                        ),
                        new Genero(
                                rs.getInt("id_genero"),
                                rs.getString("nombre")
                        ))
                );
            }


        }catch(SQLException e){
            new RuntimeException(e);
        }
        return lista;
    }

    public List<Genero> listarGenerosDeLibro(int id_libro){
        List<Genero> lista = new ArrayList<>();
        String query = "SELECT g.id_genero, g.nombre from genero g JOIN libro_genero lg ON g.id_genero = lg.id_genero\n" +
                "WHERE lg.id_libro = ?";
        try{
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(query);
            ps.setInt(1, id_libro);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                lista.add(new Genero(rs.getInt("id_genero"), rs.getString("nombre")));
            }

        }catch(SQLException e){
            new RuntimeException(e);
        }
        return lista;
    }



    public void modificarLibroGenero(int id_libro, int id_genero_old,int id_genero_new){
        String querymod = "UPDATE libro_genero SET id_genero = ? WHERE id_libro = ? and id_genero = ?";
        try{
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(querymod);
            ps.setInt(1, id_genero_new);
            ps.setInt(2, id_libro);
            ps.setInt(3, id_genero_old);

            ps.executeUpdate();

            System.out.println("Genero de libro modificado correctamente!!!! ");


        }catch(SQLException e){
            new RuntimeException(e);
        }
    }

    public void eliminarGeneroDeLibro(int id_libro, int id_genero){
        String queryDel = "DELETE FROM libro_genero WHERE id_libro = ? AND id_genero = ?";
        try{
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(queryDel);
            ps.setInt(1,id_libro);
            ps.setInt(2,id_genero);
            ps.executeUpdate();

            System.out.println("Genero de libro eliminado correctamente!!!! ");

        }catch(SQLException e){
            new RuntimeException(e);
        }
    }

}
