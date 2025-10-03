package dao;

import basedatos.conexion;
import models.Genero;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class generoDAO {

    public static void crearGenero(String nombre){
        String queryAlta = "INSERT INTO genero(nombre) VALUES(?)";
        try{
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(queryAlta);
            ps.setString(1,nombre);
            ps.executeUpdate();

            System.out.println("Genero Creado");

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static List<Genero> mostrarGeneros(){
        List<Genero> generos = new ArrayList<>();
        String query = "SELECT * FROM genero";
        try{
            Statement st = conexion.getInstancia().getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                generos.add(new Genero(rs.getInt("id_genero"),rs.getString("nombre")));
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return generos;
    }

    public static void modificarGenero(int id_genero, String nombre_genero){
        String queryEd = "UPDATE genero SET nombre = ? WHERE id_genero = ?";
        try{
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(queryEd);
            ps.setString(1,nombre_genero);
            ps.setInt(2,id_genero);
            ps.executeUpdate();

            System.out.println("Genero modificado correctamente");

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static void eliminarGenero(int id_genero){
        String queryDel = "DELETE FROM genero WHERE id_genero = ?";

        try{
            PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(queryDel);
            ps.setInt(1,id_genero);
            ps.executeUpdate();

            System.out.println("Genero eliminado correctamente");

        }catch(SQLException e ){
            throw new RuntimeException(e);
        }
    }

    public Genero buscarGeneroPorId(int ID) {
        String consulta = "SELECT * FROM genero WHERE id_genero = ?";
        try (PreparedStatement ps = conexion.getInstancia().getConnection().prepareStatement(consulta)) {
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Genero(
                        rs.getInt("id_genero"),
                        rs.getString("nombre")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar genero: " + e.getMessage(), e);
        }
        return null;
    }
}
