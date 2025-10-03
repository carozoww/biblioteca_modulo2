package flujos;

import dao.LibroDAO;
import dao.LibroGeneroDAO;
import dao.generoDAO;
import models.Genero;
import models.Libro;
import models.LibroGenero;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LibroGeneroMenu {
    private final LibroGeneroDAO librogeneroDAO;
    private final GeneroMenu generomenu;
    private final LibroDAO librodao;
    private final generoDAO generodao;

    public LibroGeneroMenu() {
        this.librogeneroDAO = new LibroGeneroDAO();
        this.generomenu = new GeneroMenu();
        this.librodao = new LibroDAO();
        this.generodao = new generoDAO();
    }

    public void iniciar(Scanner sc) throws SQLException {
        try {
            int op;
            do {
                System.out.println("\n=== Gestión de generos de libros ===");
                System.out.println("1. Asignar genero a libro");
                System.out.println("2. Listar generos asignados a libros");
                System.out.println("3. Editar genero asignado a libro");
                System.out.println("4. Eliminar genero asignado a libro");
                System.out.println("5. Volver");
                System.out.print("Opción: ");

                op = leerOpcion(sc);

                switch (op) {
                    case 1:
                        asignarGeneroLibro(sc);
                        break;
                    case 2:
                        mostrarGenerosLibro();
                        break;
                    case 3:
                        modificarGeneroLibro(sc);
                        break;
                    case 4:
                        eliminarGeneroDeLibro(sc);
                        break;
                    case 5: {
                        return;
                    }
                    default:
                        System.out.printf("Opción no válida");
                }
            } while (op != 5);
        } catch (SQLException ex) {
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

    public void asignarGeneroLibro(Scanner sc) throws SQLException {
        System.out.println("Elija un libro al que asignarle un genero: ");
        listarLibros();

        int id_libro = sc.nextInt();

        Libro libro = librodao.buscarPorId(id_libro);

        if(libro == null){
            System.out.println("Error: no existe un libro con ese id");
            return;
        }

        System.out.println("Elija un genero: ");
        generomenu.mostrarGenero();
        int id_genero = sc.nextInt();

        Genero gen = generodao.buscarGeneroPorId(id_genero);

        if(gen == null){
            System.out.println("No existe un genero con ese id");
            return;
        }

        librogeneroDAO.asignarGeneroLibro(id_libro, id_genero);

    }

    private void mostrarGenerosLibro(){
        List<LibroGenero> librosGeneros = librogeneroDAO.listarGenerosDeLibros();
        Map<Integer,String> mapa = new HashMap<>();
        for(LibroGenero libroGenero : librosGeneros){
            mapa.put(libroGenero.getLibro().getIdLibro(),libroGenero.getLibro().getTitulo());

        }

        for(Map.Entry<Integer, String> entry : mapa.entrySet()){
            System.out.println("Libro: "+entry.getValue());
            for(LibroGenero librogenero : librosGeneros){
                if(entry.getKey() == librogenero.getLibro().getIdLibro()){
                    librogenero.getGenero().mostrarInfo();
                }
            }
        }

    }

    private void mostrarGenerosDeUnLibro(int id_libro){
        List<Genero> generos = librogeneroDAO.listarGenerosDeLibro(id_libro);

        if(generos.isEmpty()){
            System.out.println("El libro no tiene generos asignados");
        }else{
            System.out.printf("%-5s %-20s%n", "ID", "Nombre");
            for(Genero genero:generos){
                genero.mostrarInfo();
        }
    }
    }

    public void modificarGeneroLibro(Scanner sc) throws SQLException {
        System.out.println("Ingrese el id del libro a modificar: ");
        listarLibros();
        int id_libro = sc.nextInt();

        Libro libro = librodao.buscarPorId(id_libro);

        if(libro == null){
            System.out.println("Error: no existe un libro con ese id");
            return;
        }
        System.out.println("Ingrese el genero a modificar de ese libro: ");
        mostrarGenerosDeUnLibro(id_libro);
        int id_genero_old = sc.nextInt();
        generomenu.mostrarGenero();

        Genero gen = generodao.buscarGeneroPorId(id_genero_old);

        if(gen == null){
            System.out.println("No existe un genero con ese id");
            return;
        }

        System.out.println("Ingrese el genero a modificar de ese libro: ");
        int id_genero_new = sc.nextInt();

        librogeneroDAO.modificarLibroGenero(id_libro, id_genero_old,id_genero_new);

    }

    public void eliminarGeneroDeLibro(Scanner sc){
        System.out.println("Ingrese el id del libro de cual desea eliminar un genero: ");
        listarLibros();
        int id_libro = sc.nextInt();

        Libro libro = librodao.buscarPorId(id_libro);

        if(libro == null){
            System.out.println("Error: no existe un libro con ese id");
            return;
        }
        System.out.println("Ingrese el genero a eliminar de ese libro: ");
        mostrarGenerosDeUnLibro(id_libro);
        int id_genero = sc.nextInt();

        Genero gen = generodao.buscarGeneroPorId(id_genero);

        if(gen == null){
            System.out.println("No existe un genero con ese id");
            return;
        }

        librogeneroDAO.eliminarGeneroDeLibro(id_libro, id_genero);
    }

    public void listarLibros() {
        List<Libro> libros = librodao.listarLibros();

        if (libros.isEmpty()) {
            System.out.println("No hay libros para mostrar");
        } else {
            System.out.printf("%-5s %-50s %-20s %-30s %-30s%n", "ID", "Titulo", "ISBN", "Fecha de Publicacion", "Editorial");
            for (Libro libro : libros) {
                libro.mostrarInformacion();
            }
        }
    }
}
