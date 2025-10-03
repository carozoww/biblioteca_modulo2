package flujos;

import dao.LectorDAO;
import dao.PrestamoDAO;
import models.Lector;
import models.Prestamo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class PrestamoMenu {

    private final Scanner scanner;
    private final PrestamoDAO prestamoDAO;
    private final LectorDAO lectorDAO;
    private final LibroMenu libromenu;

    public PrestamoMenu(Scanner scanner, PrestamoDAO prestamoDAO, LectorDAO lectorDAO) {
        this.scanner = scanner;
        this.prestamoDAO = prestamoDAO;
        this.lectorDAO = lectorDAO;
        this.libromenu = new LibroMenu();
    }

    public void mostrarMenuPrestamo() {
        int opcion;
        do {
            System.out.println("\n=== Gestión de Préstamos ===");
            System.out.println("1. Crear préstamo");
            System.out.println("2. Finalizar préstamo");
            System.out.println("3. Listar préstamos");
            System.out.println("4. Menú principal");
            System.out.print("Opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> crearPrestamo();
                case 2 -> finalizarPrestamo();
                case 3 -> listarPrestamos();
                case 4 -> System.out.println("Volviendo al menú principal");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 4);
    }

    private void crearPrestamo() {
        try {
            List<Lector> lectores = lectorDAO.listarLectores();
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

            System.out.print("Ingrese ID del lector: ");
            int idLector = scanner.nextInt();
            scanner.nextLine();

            Lector lector = lectorDAO.buscarPorId(idLector);
            if (lector == null) {
                System.out.println("No se encontró lector con ID " + idLector);
                return;
            }

            libromenu.listarLibros();

            System.out.print("Ingrese ID del libro: ");
            int idLibro = scanner.nextInt();
            scanner.nextLine();

            if (!prestamoDAO.isLibroDisponible(idLibro)) {
                System.out.println("El libro no está disponible para préstamo.");
                return;
            }

            LocalDateTime fechaPrestamo = LocalDateTime.now();
            String estado = "RESERVADO";

            prestamoDAO.crearPrestamo(idLector, idLibro, fechaPrestamo, estado);


        } catch (Exception e) {
            System.out.println("Error al crear préstamo: " + e.getMessage());
        }
    }

    private void finalizarPrestamo() {
        try {
            listarPrestamos();
            System.out.print("Ingrese ID del préstamo a finalizar: ");
            int idPrestamo = scanner.nextInt();
            scanner.nextLine();

            Prestamo prestamo = prestamoDAO.buscarPorId(idPrestamo);
            if (prestamo == null) {
                System.out.println("No se encontró préstamo con ID " + idPrestamo);
                return;
            }

            if ("DISPONIBLE".equals(prestamo.getEstado())) {
                System.out.println("El préstamo ya fue finalizado.");
                return;
            }

            LocalDateTime fechaDevolucion = LocalDateTime.now();
            prestamoDAO.finalizarPrestamo(idPrestamo, fechaDevolucion);

            Duration duracion = Duration.between(prestamo.getFechaPrestamo(), fechaDevolucion);
            long dias = duracion.toDays();
            long horas = duracion.toHours() % 24;
            long minutos = duracion.toMinutes() % 60;

            System.out.println("Préstamo finalizado.");
            System.out.println("Duración: " + dias + " días, " + horas + " horas, " + minutos + " minutos.");

        } catch (Exception e) {
            System.out.println("Error al finalizar préstamo: " + e.getMessage());
        }
    }


    private void listarPrestamos() {
        try {
            List<Prestamo> prestamos = prestamoDAO.listarPrestamos();
            if (prestamos.isEmpty()) {
                System.out.println("No hay préstamos registrados.");
                return;
            }

            System.out.printf("%-5s %-10s %-10s %-20s %-20s %-12s%n",
                    "ID", "ID Lector", "ID Libro", "Fecha Préstamo", "Fecha Devolución", "Estado");

            for (Prestamo p : prestamos) {
                System.out.printf("%-5d %-10d %-10d %-20s %-20s %-12s%n",
                        p.getIdPrestamo(),
                        p.getIdLector(),
                        p.getIdLibro(),
                        p.getFechaPrestamo(),
                        p.getFechaDevolucion() != null ? p.getFechaDevolucion() : "-",
                        p.getEstado());
            }

        } catch (Exception e) {
            System.out.println("Error al listar préstamos: " + e.getMessage());
        }
    }
}
