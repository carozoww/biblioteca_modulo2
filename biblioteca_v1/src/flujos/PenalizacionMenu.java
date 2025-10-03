package flujos;

import dao.AdministradorDAO;
import dao.PenalizacionDAO;
import dao.LectorDAO;
import models.Administrador;
import models.Lector;
import models.Penalizacion;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PenalizacionMenu {

    private final PenalizacionDAO penalizaciondao;
    private final LectorDAO lectordao;
    private final leer instancia;
    private final AdministradorDAO adminadao;

    public PenalizacionMenu() {
        this.penalizaciondao = new PenalizacionDAO();
        this.lectordao = new LectorDAO();
        this.instancia = new leer();
        this.adminadao = new AdministradorDAO();
    }

    public void mostarMenuPena(Scanner scanner) {
        try {
            int opcion = 0;
            do {
                System.out.println("\n === Gestion de Penalizaciones ===");
                System.out.println("1. Crear penalización");
                System.out.println("2. Eliminar penalización");
                System.out.println("3. Listar penalizaciones activas");
                System.out.println("4. Volver al menu principal");
                System.out.println("5. Salir");

                System.out.println("Ingrese la opcion: ");

                opcion = leerOpcion(scanner);

                switch (opcion) {
                    case 1:
                        aplicarPenalizacion(scanner);
                        break;
                    case 2:
                        quitarPenalizacion(scanner);
                        break;
                    case 3:
                        listarPenalizaciones();
                        break;
                    case 4:
                        System.out.println("Volviendo al menú principal");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            }while (opcion != 4);
        } catch (SQLException e) {
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


    public void aplicarPenalizacion(Scanner scanner) throws SQLException {
        List<Lector> lectores = lectordao.listarLectores();
        if (lectores.isEmpty()) {
            System.out.println("No hay lectores para mostrar");
            return;
        } else {
            System.out.println("LISTADO DE LECTORES");
            System.out.printf("%-5s %-20s %-15s %-15s %-20s %-12s %-12s %-8s %-25s%n",
                    "ID", "Nombre", "Cédula", "Teléfono", "Dirección",
                    "Autenticado", "FechaNac", "Membresía", "Correo");
            for (Lector lector : lectores) {
                lector.mostrarInformacion();
            }
        }
        System.out.println("Ingrese el id del lector a penalizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Lector existeL = lectordao.buscarPorId(id);
        if (existeL == null) {
            System.out.println("No existe un lector con ese id");
            return;
        }
        List<Penalizacion> penaActiva = penalizaciondao.buscarPenalizacionActivaPorUsuario(id);
        if (!penaActiva.isEmpty()) {
            System.out.println("Error: ya existe una penalización activa para ese usuario");
            return;
        }

        System.out.println("Ingrese la duración (en días) de la penalización: ");
        int duracion = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Ingrese el motivo de la penalizacion: ");
        String motivo = instancia.leerPalabra(scanner);

        List<Administrador> admins = adminadao.listarAdministradores();

        if(admins.isEmpty()) {
            System.out.println("No hay administradores para mostrar");
            return;
        } else {
            System.out.printf("%-5s %-20s%n", "ID", "Nombre");
            for (Administrador admin : admins) {
                System.out.printf("%-5d %-20s%n",
                        admin.getId(),
                        admin.getNombre());
            }
        }

        System.out.println("Ingrese el id del administrador: ");
        int idAd = scanner.nextInt();

        penalizaciondao.crearPenalizacion(duracion, motivo, id, idAd);
    }

    public void quitarPenalizacion(Scanner scanner) throws SQLException {


        List<Penalizacion> penas = penalizaciondao.listarPenalizaciones();

        if (penas.isEmpty()) {
            System.out.println("No hay penalizaciones para mostrar");
            return;
        } else {
            System.out.printf("%-5s %-10s %-30s %-10s %-20s %-10s %-20s%n",
                    "ID", "Duración", "Motivo", "ID Lector", "Lector", "ID Admin", "Administrador");
            for (Penalizacion pen : penas) {
                pen.mostrarInformacion();
            }
        }

        System.out.println("Ingrese el id del lector para quitarle la penalizacion: ");
        int id = scanner.nextInt();

        Lector lector = lectordao.buscarPorId(id);
        if (lector == null) {
            System.out.println("No hay lectores con ese id");
            return;
        }

        List<Penalizacion> penaActiva = penalizaciondao.buscarPenalizacionActivaPorUsuario(id);
        if(penaActiva.isEmpty()) {
            System.out.println("Error: no existe una penalizacion activa para este usuario");
            return;
        }

        penalizaciondao.eliminarPenalizacion(id);
    }

    public void listarPenalizaciones() {
        List<Penalizacion> pena = penalizaciondao.listarPenalizaciones();

        if (pena.isEmpty()) {
            System.out.println("No hay penalizaciones para mostrar");
        } else {
            System.out.printf("%-5s %-10s %-30s %-10s %-20s %-10s %-20s%n",
                    "ID", "Duración", "Motivo", "ID Lector", "Lector", "ID Admin", "Administrador");
            for (Penalizacion penalizacion : pena) {
                penalizacion.mostrarInformacion();
            }
        }
    }
}

