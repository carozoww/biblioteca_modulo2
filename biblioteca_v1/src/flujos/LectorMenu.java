package flujos;

import dao.LectorDAO;
import models.Lector;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class LectorMenu {
    private final LectorDAO lectorDAO;
    private final leer instancia;

    public LectorMenu() {
        this.instancia = new leer();
        this.lectorDAO = new LectorDAO();
    }

    public void iniciar(Scanner scanner) {
        int opcion = 0;

        do {
            System.out.println("\n=== Gestión de Lectores ===");
            System.out.println("1. Crear lector");
            System.out.println("2. Editar lector");
            System.out.println("3. Eliminar lector");
            System.out.println("4. Listar lectores");
            System.out.println("5. Menú principal");

            System.out.print("Opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    crearLector(scanner);
                    break;
                case 2:
                    editarLector(scanner);
                    break;
                case 3:
                    eliminarLector(scanner);
                    break;
                case 4:
                    listarLectores();
                    break;
                case 5:
                    System.out.println("Volviendo al menú principal");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 5);
    }

    private void crearLector(Scanner scanner) {
        try {
            System.out.println("Ingrese nombre del lector: ");
            String nombre = instancia.leerPalabra(scanner);

            System.out.println("Ingrese cédula: ");
            String cedula = scanner.next();

            System.out.println("Ingrese teléfono: ");
            String telefono = instancia.leerPalabra(scanner);

            System.out.println("Ingrese dirección: ");
            String direccion = instancia.leerPalabra(scanner);

            System.out.println("Desea autentificar al lector? (si/no): ");
            String inputAut = scanner.next();
            boolean autenticacion = inputAut.equalsIgnoreCase("si");

            System.out.println("Ingrese fecha de nacimiento (YYYY-MM-DD): ");
            String fechaNacStr = scanner.next();
            LocalDate fechaNac;
            if (fechaNacStr.isEmpty()) {
                fechaNac = LocalDate.now();
            } else {
                fechaNac = LocalDate.parse(fechaNacStr);
            }

            System.out.println("Desea activar la membresía? (si/no): ");
            String inputMemb = scanner.next();
            boolean membresia = inputMemb.equalsIgnoreCase("si");

            System.out.println("Ingrese el correo: ");
            String correo = scanner.next();

            System.out.println("Ingrese la contraseña: ");
            String contrasenia = scanner.next();

            lectorDAO.crearLector(nombre, cedula, telefono, direccion, autenticacion,
                    fechaNac, membresia, correo, contrasenia);

        } catch (Exception e) {
            System.out.println("Error al crear lector: " + e.getMessage());
        }
    }

    private void editarLector(Scanner scanner) {
        try {
            listarLectores();

            System.out.println("Ingrese el id del lector a modificar: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Lector lectorActual = lectorDAO.buscarPorId(id);
            if (lectorActual == null) {
                System.out.println("No se encontró el lector con ID " + id);
                return;
            }

            boolean autenticacion = lectorActual.isAutenticacion();
            boolean membresiaActual = lectorActual.isMembresia();


            System.out.println("Ingrese el nuevo correo: ");
            String correo = scanner.next();

            System.out.println("Ingrese la nueva cedula: ");
            String cedula = scanner.next();

            System.out.println("Ingrese el nuevo telefono: ");
            String telefono = instancia.leerPalabra(scanner);

            System.out.println("Ingrese la nueva dirección: ");
            String direccion = instancia.leerPalabra(scanner);

            if (!autenticacion) {
                System.out.println("Autenticación actualmente inactiva. ¿Desea activarla? (si/no): ");
                String inputAuth = scanner.nextLine().trim().toLowerCase();
                autenticacion = inputAuth.equals("si");
            } else {
                System.out.println("El lector ya está autenticado.");
            }

            System.out.println("Ingrese fecha de nacimiento (YYYY-MM-DD): ");
            LocalDate fechaNac = LocalDate.parse(scanner.next());

            System.out.println("Membresía actual: " + membresiaActual);
            System.out.println("¿Desea cambiar la membresía? (si/no): ");
            String inputMembresia = scanner.next().trim().toLowerCase();
            boolean membresia = inputMembresia.isEmpty() ? membresiaActual : inputMembresia.equals("si");

            System.out.println("Ingrese el nuevo nombre: ");
            String nombre = instancia.leerPalabra(scanner);

            System.out.println("Ingrese la nueva contraseña: ");
            String contrasenia = scanner.next();

            lectorDAO.editarLector(
                    id, nombre, cedula, telefono, direccion,
                    autenticacion, fechaNac, membresia, correo, contrasenia
            );
        } catch (Exception e) {
            System.out.println("Error al editar lector: " + e.getMessage());
        }
    }

    private void listarLectores() {
        try {
            List<Lector> lectores = lectorDAO.listarLectores();

            if (lectores.isEmpty()) {
                System.out.println("No hay lectores registrados.");
            } else {
                System.out.printf("%-5s %-20s %-15s %-15s %-20s %-12s %-12s %-8s %-25s%n",
                        "ID", "Nombre", "Cédula", "Teléfono", "Dirección",
                        "Autenticado", "FechaNac", "Membresía", "Correo");

                for (Lector lector : lectores) {
                    System.out.printf("%-5d %-20s %-15s %-15s %-20s %-12s %-12s %-8s %-25s%n",
                            lector.getID(),
                            lector.getNombre(),
                            lector.getCedula(),
                            lector.getTelefono(),
                            lector.getDireccion(),
                            lector.isAutenticacion(),
                            lector.getFechaNac(),
                            lector.isMembresia(),
                            lector.getCorreo());
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar lectores: " + e.getMessage());
        }
    }

    private void eliminarLector(Scanner scanner) {
        try {
            listarLectores();
            System.out.println("Ingrese el id del lector a eliminar: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Lector lectorActual = lectorDAO.buscarPorId(id);
            if (lectorActual == null) {
                System.out.println("No se encontró el lector con ID " + id);
                return;
            }

            lectorDAO.eliminarLector(id);
        } catch (Exception e) {
            System.out.println("Error al eliminar lector: " + e.getMessage());
        }
    }
}
