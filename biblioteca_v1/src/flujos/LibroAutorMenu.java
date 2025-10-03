package flujos;

import dao.LibroAutorDAO;
import dao.LibroDAO;
import dao.autorDAO;
import models.Autor;
import models.Libro;
import models.LibroAutor;

import java.sql.SQLException;
import java.util.*;

public class LibroAutorMenu {
    private final LibroAutorDAO libroautorDAO;
    private final AutorMenu autormenu;
    private final LibroMenu libromenu;
    private final autorDAO autordao;
    private final LibroDAO librodao;

    public LibroAutorMenu() {
        this.libroautorDAO = new LibroAutorDAO();
        this.autormenu = new AutorMenu();
        this.libromenu = new LibroMenu();
        this.autordao = new autorDAO();
        this.librodao = new LibroDAO();

    }

    public void iniciar(Scanner sc) throws SQLException {
        try{
            int op;
            do{
                System.out.println("\n=== Gestión de autores de libros ===");
                System.out.println("1. Asignar autor a libro");
                System.out.println("2. Listar autores asignados a libro");
                System.out.println("3. Editar autor asignado a libro");
                System.out.println("4. Eliminar autor asignado a libro");
                System.out.println("5. Volver");
                System.out.print("Opción: ");

                op = leerOpcion(sc);

                switch (op) {
                    case 1: asignarAutorALibro(sc);break;
                    case 2: mostrarAutoresDeLibros();break;
                    case 3: modificarAutorDeLibro(sc);break;
                    case 4: eliminarAutorDeLibro(sc);break;
                    case 5: {return;}
                    default:System.out.printf("Opción no válida");
                }
            }while (op != 5);
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

    public void asignarAutorALibro(Scanner sc) throws SQLException {
        System.out.println("Ingrese el id del libro al cual desea asignar un nuevo autor: ");
        libromenu.listarLibros();
        int id_libro = sc.nextInt();

        Libro libro = librodao.buscarPorId(id_libro);

        if(libro == null){
            System.out.println("Error: no existe un libro con ese id");
            return;
        }

        System.out.println("Ingrese un autor: ");
        autormenu.mostrarAutor();
        int id_autor = sc.nextInt();

        Autor autor = autordao.buscarAutorPorId(id_autor);
        if(autor == null) {
            System.out.println("El autor con ese id no existe");
            return;
        }

        libroautorDAO.asignarAutorALibro(id_autor,id_libro);

    }

    public void mostrarAutoresDeLibros(){
        List<LibroAutor> autores = libroautorDAO.listarAutoresDeLibros();
        Map<Integer,String> mapa = new HashMap<>();
        if(autores.isEmpty()){
            System.out.println("No hay autores asignados a libros");
        }else{
            for(LibroAutor libroautor : autores){
                mapa.put(libroautor.getLibro().getIdLibro(),libroautor.getLibro().getTitulo());
            }
            for(Map.Entry<Integer, String> entry : mapa.entrySet()){
                System.out.println("\nLibro: "+entry.getValue());
                System.out.printf("   %-5s %-25s %-25s%n", "ID", "Autor", "Apellido");
                for(LibroAutor libroautor : autores){
                    if(entry.getKey() == libroautor.getLibro().getIdLibro()){
                        System.out.printf("   %-5d %-25s %-25s%n",
                                libroautor.getAutor().getId_autor(),
                                libroautor.getAutor().getNombre(),
                                libroautor.getAutor().getApellido());
                    }
                }
            }
        }


    }


    public void mostrarAutoresDeUnLibro(int id_libro){
        List<Autor> autores = libroautorDAO.listarAutoresDeUnLibro(id_libro);

        if(autores.isEmpty()){
            System.out.println("No hay autores asignados a ese libro");
        }else{
            for(Autor autor: autores){
                autor.mostrarInfo();
            }
        }

    }

    public void modificarAutorDeLibro(Scanner sc) throws SQLException {
        System.out.println("Ingrese el id de un libro que desee modificar su autor: ");
        libromenu.listarLibros();
        int id_libro = sc.nextInt();

        Libro libro = librodao.buscarPorId(id_libro);

        if(libro == null){
            System.out.println("Error: no existe un libro con ese id");
            return;
        }

        mostrarAutoresDeUnLibro(id_libro);
        System.out.println("Ingrese un id de un autor a modificar: ");
        int id_autor_old = sc.nextInt();

        Autor autor = autordao.buscarAutorPorId(id_autor_old);
        if(autor == null) {
            System.out.println("El autor con ese id no existe");
            return;
        }

        autormenu.mostrarAutor();
        System.out.println("Ingrese un id del nuevo autor del libro: ");
        int id_autor_new = sc.nextInt();

        Autor autor2 = autordao.buscarAutorPorId(id_autor_new);
        if(autor2 == null) {
            System.out.println("El autor con ese id no existe");
            return;
        }

        libroautorDAO.modificarAutorDeLibro(id_libro,id_autor_old,id_autor_new);
    }

    public void eliminarAutorDeLibro(Scanner sc){
        libromenu.listarLibros();
        System.out.println("Ingrese el id de un libro que desee eliminar un autor: ");
        int id_libro = sc.nextInt();
        mostrarAutoresDeUnLibro(id_libro);
        System.out.println("Ingrese un id de un autor a eliminar: ");
        int id_autor = sc.nextInt();

        libroautorDAO.eliminarAutorDeLibro(id_libro,id_autor);
    }




}
