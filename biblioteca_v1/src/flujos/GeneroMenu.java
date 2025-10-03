package flujos;

import dao.generoDAO;
import models.Genero;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class GeneroMenu {
    private final generoDAO generodao;
    private final leer instancia;

    public GeneroMenu(){
        this.generodao = new generoDAO();
        this.instancia = new leer();
    }

    public void iniciar(Scanner sc) throws SQLException {
        try{
            int op;
            do{
                System.out.println("\n=== Gestión de Autores ===");
                System.out.println("1. Crear genero");
                System.out.println("2. Listar generos");
                System.out.println("3. Editar genero");
                System.out.println("4. Eliminar genero");
                System.out.println("5. Volver");
                System.out.print("Opción: ");

                op = leerOpcion(sc);

                switch (op) {
                    case 1: crearGenero(sc);break;
                    case 2: mostrarGenero();break;
                    case 3: modificarGenero(sc);break;
                    case 4: eliminarGenero(sc);break;
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

    public void crearGenero(Scanner sc) throws SQLException {
        System.out.println("Ingrese el nombre del nuevo genero");
        String nombre = instancia.leerPalabra(sc);

        generoDAO.crearGenero(nombre);

    }

    public void mostrarGenero() throws SQLException{
        List<Genero> generos = generoDAO.mostrarGeneros();

        if(generos.isEmpty()){
            System.out.println("No hay generos registrados");
        }else{
            System.out.printf("%-5s %-20s%n", "ID", "Nombre");
            for(Genero genero:generos){
                genero.mostrarInfo();
            }
        }
    }

    public void modificarGenero(Scanner sc) throws SQLException {
        mostrarGenero();

        System.out.println("Ingrese el id del genero a modificar");
        int id = sc.nextInt();

        Genero gen = generodao.buscarGeneroPorId(id);

        if(gen == null){
            System.out.println("No existe un genero con ese id");
            return;
        }

        System.out.println("Ingrese el nuevo nombre del genero");
        String nombre = instancia.leerPalabra(sc);

        generoDAO.modificarGenero(id, nombre);

    }

    public void eliminarGenero(Scanner sc) throws SQLException {
        mostrarGenero();

        System.out.println("Ingrese el id del genero a eliminar");
        int id = sc.nextInt();

        Genero gen = generodao.buscarGeneroPorId(id);

        if(gen == null){
            System.out.println("No existe un genero con ese id");
            return;
        }

        generoDAO.eliminarGenero(id);
    }


}
