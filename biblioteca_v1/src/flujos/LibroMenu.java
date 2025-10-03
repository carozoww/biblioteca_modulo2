package flujos;

import dao.EditorialDAO;
import dao.LibroDAO;
import models.Editorial;
import models.Libro;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class LibroMenu {

    private final LibroDAO librodao;
    private final EditorialDAO editorialdao;
    private final leer instancia;

    public LibroMenu() {
        this.librodao = new LibroDAO();
        this.editorialdao = new EditorialDAO();
        this.instancia = new leer();
    }

    public void mostrarMenuLibro(Scanner scanner) {
        try {
            int opcion = 0;
            do {
                System.out.println("\n === Gestion de Libros ===");
                System.out.println("1. Crear libro");
                System.out.println("2. Modificar libro");
                System.out.println("3. Eliminar libro");
                System.out.println("4. Listar libros");
                System.out.println("5. Volver al menu principal");

                System.out.println("Ingrese la opcion: ");

                opcion = leerOpcion(scanner);

                switch (opcion) {
                    case 1:
                        crearLibro(scanner);
                        break;
                    case 2:
                        editarLibro(scanner);
                        break;
                    case 3:
                        eliminarLibro(scanner);
                        break;
                    case 4:
                        listarLibros();
                        break;
                    case 5:
                        System.out.println("Volviendo al menú principal");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            }while (opcion != 5);
        }catch (SQLException e){
            System.out.println("Error en la base de datos" + e.getMessage());
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

    public void crearLibro(Scanner scanner) throws SQLException {
        System.out.println("Ingrese el titulo del libro: ");
        String titulo = instancia.leerPalabra(scanner);

        System.out.println("Ingrese el isbn del libro: ");
        String isbn = scanner.next();

        List<Libro> libro = librodao.existeISBN(isbn);
        if(!libro.isEmpty()){
            System.out.println("Ya existe un libro con ese isbn");
            return;
        }

        System.out.println("Ingrese la fecha de publicacion del libro(formato: YYYY-MM-DD): ");
        Date fechaPublicacion = Date.valueOf(scanner.next());

        List<Editorial> editoriales = editorialdao.listarEditorial();
        if(editoriales.isEmpty()){
            System.out.println("No hay editoriales para mostrar");
            return;
        } else {
            System.out.printf("%-5s %-20s%n", "ID", "Nombre");
            for(Editorial editorial : editoriales){
                editorial.mostrarInformacion();
            }
        }

        System.out.println("Ingrese el id de la editorial del libro: ");
        int idEditorial = scanner.nextInt();

        Editorial editorial = editorialdao.buscarEditorialPorId(idEditorial);

        if(editorial == null){
            System.out.println("La editorial no existe");
            return;
        }

        librodao.crearLibro(titulo, isbn, fechaPublicacion, idEditorial);
    }

    public void eliminarLibro(Scanner scanner) {
        listarLibros();
        System.out.println("\n Ingrese el id del libro a eliminar: ");
        int idLibro = scanner.nextInt();

        Libro libro = librodao.buscarPorId(idLibro);

        if(libro == null){
            System.out.println("Error: no existe un libro con ese id");
            return;
        }

        librodao.eliminarLibro(idLibro);
    }

    public void editarLibro(Scanner scanner) {
        listarLibros();
        System.out.println("\n Ingrese el id del libro a modificar: ");
        int idLibro = scanner.nextInt();
        scanner.nextLine();

        Libro libro = librodao.buscarPorId(idLibro);

        if(libro == null){
            System.out.println("Error: no existe un libro con ese id");
            return;
        }

        System.out.println("Ingrese el nuevo nombre del libro: ");
        String nombre = instancia.leerPalabra(scanner);

        System.out.println("Ingrese el nuevo isbn del libro: ");
        String isbn = scanner.next();

        List<Libro> libro1 = librodao.existeISBNporId(idLibro,isbn);
        if(!libro1.isEmpty()){
            System.out.println("Ya existe un libro con ese isbn");
            return;
        }


        System.out.println("Ingrese la fecha de publicacion del libro(formato: YYYY-MM-DD): ");
        Date fechaPublicacion = Date.valueOf(scanner.next());

        List<Editorial> editoriales = editorialdao.listarEditorial();
        if(editoriales.isEmpty()){
            System.out.println("No hay editoriales para mostrar");
            return;
        } else {
            System.out.printf("%-5s %-20s%n", "ID", "Nombre");
            for(Editorial editorial : editoriales){
                editorial.mostrarInformacion();
            }
        }

        System.out.println("Ingrese el id de la editorial del libro: ");
        int idEditorial = scanner.nextInt();

        Editorial editorial = editorialdao.buscarEditorialPorId(idEditorial);

        if(editorial == null){
            System.out.println("La editorial no existe");
            return;
        }

        librodao.editarLibro(idLibro, nombre, isbn, fechaPublicacion, idEditorial);
    }

    public void listarLibros() {
        List<Libro> libros = librodao.listarLibros();

        if (libros.isEmpty()) {
            System.out.println("No hay libros para mostrar");
        } else {
            System.out.printf("%-5s %-50s %-20s %-30s %-30s %-15s %n", "ID", "Titulo", "ISBN", "Fecha de Publicacion", "Editorial", "Estado-editorial");
            for (Libro libro : libros) {
                libro.mostrarInformacion();
            }
        }
    }

}


