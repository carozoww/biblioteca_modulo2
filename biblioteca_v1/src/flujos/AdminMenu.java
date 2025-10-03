package flujos;

import dao.AdministradorDAO;
import models.Administrador;

import java.security.spec.RSAOtherPrimeInfo;
import java.sql.*;

import static java.util.Date.*;
import java.util.List;
import java.util.Scanner;


public class AdminMenu {

    private final AdministradorDAO admindao;
    private final leer instancia;

    public AdminMenu() {

        this.admindao = new AdministradorDAO();
        this.instancia = new leer();
    }

    public void iniciar(Scanner sc) throws SQLException {
        try {
            int op;
            do {
                System.out.println("\n=== Gestión de Administradores ===");
                System.out.println("1. Crear Administrador");
                System.out.println("2. Listar Administrador");
                System.out.println("3. Editar Administrador");
                System.out.println("4. Borrar Administrador");
                //   System.out.println("5. Iniciar Sesion");
                System.out.println("6. Volver");
                System.out.print("Opción: ");

        op = leerOpcion(sc);

                switch (op) {
                    case 1:
                        crearAdministrador(sc);
                        break;
                    case 2:
                        listarAdministradores();
                        break;
                    case 3:
                        editarAdministrador(sc);
                        break;
                    case 4:
                        eliminarAdministrador(sc);
                        break;
                    //    case 5: iniciarSesion(sc);break;
                    case 6: {
                        return;
                    }
                    default:
                        System.out.printf("Opcion no valido");
                }
            } while (op != 6);
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

    public void crearAdministrador(Scanner sc) throws SQLException {

        System.out.println("Ingrese nombre del  administrador: ");
        String nombre = instancia.leerPalabra(sc);

        System.out.println("Ingrese fecha Nacimiento del administrador(formato: YYYY-MM-DD): ");
        Date fechaNacimiento = Date.valueOf(sc.next());

        sc.nextLine();

        System.out.println("Ingrese correo del administrador(sin espacios): ");
        String correo = sc.next();

        System.out.println("Ingrese  contrasenia del administrador(sin espacios): ");
        String contra = sc.next();

        admindao.crearAdministradorDAO(nombre, fechaNacimiento, correo, contra);
    }


    public void eliminarAdministrador(Scanner sc) throws SQLException {
        listarAdministradores();
        System.out.println("Ingrese el id del administrador: ");
        int id = sc.nextInt();
        sc.nextLine();

        Administrador admin = admindao.buscarAdminPorId(id);
        if(admin == null) {
            System.out.println("El administrador no existe");
            return;
        }

        admindao.eliminarAdministrador(id);
    }

    public void editarAdministrador(Scanner sc) throws SQLException {

        listarAdministradores();
        System.out.println("Ingrese el id del administrador a modificar: ");
        int id = sc.nextInt();
        sc.nextLine();

        Administrador admin = admindao.buscarAdminPorId(id);
        if(admin == null) {
            System.out.println("El administrador no existe");
            return;
        }

        System.out.println("Ingrese nuevo nombre del administrador: ");
        String nombre = sc.next();
        System.out.println("Ingrese nuevo fecha nacimiento del administrador(formato: yyyy-mm-dd): ");
        Date fechaNacimiento = Date.valueOf(sc.next());
        sc.nextLine();
        System.out.println("Ingrese nuevo correo del administrador: ");
        String correo = sc.next();
        System.out.println("Ingrese nueva contrasenia del administrador: ");
        String contra = sc.next();

        admindao.editarAdministrador(id, nombre, fechaNacimiento, correo, contra);
    }

    public void listarAdministradores() throws SQLException {
        List<Administrador> administrador = admindao.listarAdministradores();
        if (administrador.isEmpty()) {
            System.out.println("No hay administradores para mostrar");
        } else {
            System.out.printf("%-5s %-10s %-30s %-30s %-20s%n",
                    "ID","Nombre","Fecha Nacimiento","Correo","Contraseña");
            for (Administrador administrador1 : administrador) {
                administrador1.mostrarInfo();
            }
        }
    }
}
