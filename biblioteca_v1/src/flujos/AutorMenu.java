package flujos;

import dao.autorDAO;
import models.Autor;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AutorMenu {

    private final autorDAO autordao;

    public AutorMenu(){ this.autordao = new autorDAO(); }

    public void iniciar(Scanner sc) throws SQLException {
        try{
            int op;
            do{
                System.out.println("\n=== Gestión de Autores ===");
                System.out.println("1. Crear autor");
                System.out.println("2. Listar autores");
                System.out.println("3. Editar autor");
                System.out.println("4. Eliminar autor");
                System.out.println("5. Volver");
                System.out.print("Opción: ");

                op = leerOpcion(sc);

                switch (op) {
                    case 1: crearAutor(sc);break;
                    case 2: mostrarAutor();break;
                    case 3: modificarAutor(sc);break;
                    case 4: eliminarAutor(sc);break;
                    case 5: {return;}
                    default:System.out.printf("Opción no válida");
                }
            }while (op != 6);
        }catch (SQLException ex){
            System.out.println("Error en la base de datos" + ex.getMessage());
        }
    }

    private int leerOpcion(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Ingrese un número válido: ");
            sc.next();
        }
        int opcion = sc.nextInt();
        sc.nextLine();
        return opcion;
    }

    public void crearAutor(Scanner sc) throws SQLException {
        System.out.println("Ingrese el nombre del autor");
        String nombre = sc.next();

        System.out.println("Ingrese el apellido del autor");
        String ape = sc.next();

        autorDAO.crearAutor(nombre, ape);
    }

    public void mostrarAutor() throws SQLException {
        List<Autor> autores = autorDAO.mostrarAutores();
        if(autores.isEmpty()){
            System.out.println("No existen autores registrados en el sistema");
        }else{
            System.out.printf("%-5s %-20s %-20s%n", "ID", "Nombre", "Apellido");
            for(Autor autor: autores){
                autor.mostrarInfo();
            }
        }

    }

    public void modificarAutor(Scanner sc) throws SQLException {
        mostrarAutor();
        System.out.println("Ingrese el id del autor a modificar");
        int id = sc.nextInt();

        Autor autor = autordao.buscarAutorPorId(id);
        if(autor == null) {
            System.out.println("El autor con ese id no existe");
            return;
        }

        System.out.println("Ingrese el nuevo nombre del autor");
        String nombre = sc.next();

        System.out.println("Ingrese el nuevo apellido del autor");
        String ape = sc.next();

        autorDAO.modificarAutor(id, nombre, ape);


    }

    public void eliminarAutor(Scanner sc) throws SQLException {
        mostrarAutor();

        System.out.println("Ingrese el id del autor a eliminar");
        int id = sc.nextInt();

        Autor autor = autordao.buscarAutorPorId(id);
        if(autor == null) {
            System.out.println("El autor con ese id no existe");
            return;
        }

        autorDAO.eliminarAutor(id);

    }



}
