package flujos;

import dao.LectorDAO;
import dao.SalaDAO;
import dao.ReservaDAO;
import models.Lector;
import models.Sala;
import models.Reserva;

import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class SalaMenu {
    private final SalaDAO dao;
    private final ReservaDAO reservaDAO;
    private final leer instancia;
    private final SalaDAO saladao;
    private final LectorDAO lectorDAO;

    public SalaMenu() {
        this.dao = new SalaDAO();
        this.reservaDAO = new ReservaDAO();
        this.saladao = new SalaDAO();
        this.lectorDAO = new LectorDAO();
        this.instancia = new leer();
    }

    public void iniciar(Scanner sc) throws SQLException{
        while (true) {
            System.out.println("\n=== Gestión de Salas ===");
            System.out.println("1. Registrar Sala");
            System.out.println("2. Listar salas");
            System.out.println("3. Editar sala");
            System.out.println("4. Borrar sala");
            System.out.println("5. Gestionar reservas");
            System.out.println("6. Volver");
            System.out.print("Opción: ");

            int op = leerEntero(sc);
            switch (op) {
                case 1 -> crearSala(sc);
                case 2 -> listarSalas();
                case 3 -> editarSala(sc);
                case 4 -> borrarSala(sc);
                case 5 -> gestionarReservas(sc);
                case 6 -> { return; }
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private void crearSala(Scanner sc) throws SQLException{

        System.out.println("Número de sala: ");
        int numeroSala = leerEntero(sc);

        System.out.println("Ubicacion: ");
        String ubicacion = instancia.leerPalabra(sc);

        System.out.println("Máximo de Personas: ");
        int maxPersonas = leerEntero(sc);

        dao.crearSala(numeroSala, ubicacion, maxPersonas);

    }

    private void listarSalas() throws SQLException {
        List<Sala> salas = dao.listarSalas();

        if (salas.isEmpty()){
            System.out.println("No hay salas registradas.");
        }else {
            System.out.printf("%-5s %-20s %-20s %-20s%n",
                    "ID", "Numero de sala", "Ubicacion", "Max. Personas");
            for (Sala sala: salas) {
                sala.mostrarInformacion();
            }
        }
    }

    private void editarSala(Scanner sc) throws SQLException{
        List<Sala> salas = dao.listarSalas();
        if (salas.isEmpty()){
            System.out.println("No hay salas registradas.");
            return;
        }
        listarSalas();

        System.out.println("ID a editar: ");
        int idEditar = leerEntero(sc);

        Sala sala = saladao.buscarSalaPorId(idEditar);

        if(sala == null){
            System.out.println("No existe sala con ese id");
            return;
        }

        System.out.println("Nuevo numero de sala: ");
        int numeroSala = leerEntero(sc);

        System.out.println("Nueva ubicacion: ");
        String nuevaUbicacion = instancia.leerPalabra(sc);

        System.out.println("Nuevo Max Personas: ");
        int nuevaMaxPersonas = leerEntero(sc);

        dao.editarSala(idEditar, numeroSala, nuevaUbicacion, nuevaMaxPersonas);
    }

    private void borrarSala(Scanner sc) throws SQLException{
        listarSalas();
        System.out.println("ID a borrar: ");
        int idBorrar = leerEntero(sc);

        Sala sala = saladao.buscarSalaPorId(idBorrar);

        if(sala == null){
            System.out.println("No existe sala con ese id");
            return;
        }

        dao.borrarSala(idBorrar);
    }

    private int leerEntero(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.println("Ingrese un número válido: ");
            sc.next();
        }
        int n = sc.nextInt();
        sc.nextLine();
        return n;
    }

    private void gestionarReservas(Scanner sc) throws SQLException {
        try {
            while (true) {
                System.out.println("\n=== Reservas por Sala ===");
                System.out.println("1. Agregar reserva a sala");
                System.out.println("2. Finalizar reserva de sala");
                System.out.println("3. Listar reservas");
                System.out.println("4. Volver");
                System.out.print("Opción: ");

                int op = leerEntero(sc);
                switch (op) {
                    case 1 -> agregarReservaASala(sc);
                    case 2 -> finalizarReserva(sc);
                    case 3 -> listarReservas(sc);
                    case 4 -> { return; }
                    default -> System.out.println("Opción inválida.");
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void finalizarReserva(Scanner sc) {
        try {
            List<Reserva> reservas = reservaDAO.listarReservasSinFinalizar();
            if (reservas.isEmpty()){
                System.out.println("No hay reservas registradas.");
                return;
            }
            listarReservasSinFinalizar(sc);
            System.out.print("Ingrese ID de la reserva a finalizar: ");
            int idReserva = leerEntero(sc);

            reservaDAO.finalizarReserva(idReserva);
        } catch (Exception e) {
            System.out.println("Error al finalizar préstamo: " + e.getMessage());
        }
    }

    private void listarReservasSinFinalizar(Scanner sc){
        try {
            List<Reserva> reservas = reservaDAO.listarReservasSinFinalizar();
            if (reservas.isEmpty()){
                System.out.println("No hay reservas en curso.");
                return;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            System.out.printf("%-12s %-20s %-20s %-10s %-10s %-10s%n",
                    "ID Reserva", "Fecha Entrada", "Fecha Fin", "ID Sala", "ID Usuario", "Estado");

            for (Reserva r : reservas) {
                System.out.printf("%-12d %-20s %-20s %-10d %-10d %-10s%n",
                        r.getId_Reserva(),
                        r.getFecha_in().format(formatter),
                        r.getFecha_fin()!= null ? r.getFecha_fin().format(formatter) : "-",
                        r.getId_sala(),
                        r.getId_usuario(),
                        r.getEstado()
                );
            }

        } catch (Exception e){
            System.out.println("Error al listar reservas: " + e.getMessage());
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


    private void listarReservas(Scanner sc){
        try {
            List<Reserva> reservas = reservaDAO.listarReservas();
            if (reservas.isEmpty()){
                System.out.println("No hay reservas registradas.");
                return;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            System.out.printf("%-12s %-20s %-20s %-10s %-10s %-10s%n",
                    "ID Reserva", "Fecha Entrada", "Fecha Fin", "ID Sala", "ID Usuario", "Estado");

            for (Reserva r : reservas) {
                System.out.printf("%-12d %-20s %-20s %-10d %-10d %-10s%n",
                        r.getId_Reserva(),
                        r.getFecha_in().format(formatter),
                        r.getFecha_fin()!= null ? r.getFecha_fin().format(formatter) : "-",
                        r.getId_sala(),
                        r.getId_usuario(),
                        r.getEstado()
                );
            }

        } catch (Exception e){
            System.out.println("Error al listar reservas: " + e.getMessage());
        }
    }

    private void agregarReservaASala(Scanner sc){
        try {
            List<Sala> salas = dao.listarSalas();
            if (salas.isEmpty()){
                System.out.println("No hay salas registradas.");
                return;
            }
            listarSalas();
            System.out.println("Sala ID: ");
            int salaId = leerEntero(sc);

            List<Lector> lectores = lectorDAO.listarLectores();
            if (lectores.isEmpty()){
                System.out.println("No hay lectores registrados.");
                return;
            }
            listarLectores();

            System.out.println("Usuario ID: ");
            int userId = leerEntero(sc);

            reservaDAO.agregarReserva(salaId, userId);
        } catch (Exception e){
            System.out.println("Error al agregar la reserva: " + e.getMessage());
        }
    }
    private void eliminarReservaDeSala(Scanner sc) throws SQLException {
        System.out.print("Reserva ID: ");
        int reservaId = leerEntero(sc);

        Reserva reserva = reservaDAO.buscarReservaPorId(reservaId);
        if (reserva == null) {
            System.out.println("Reserva no encontrada");
            return;
        }

        reservaDAO.eliminarReserva(reservaId);
    }

    private void listarReservasDeSala(Scanner sc) throws SQLException {
        System.out.print("Sala ID: ");
        int salaId = leerEntero(sc);

        List<Reserva> reservas = reservaDAO.listarReservasDeSala(salaId);
        if (reservas.isEmpty()) {
            System.out.println("La sala no tiene reservas cargadas.");
        } else {
            reservas.forEach(Reserva::mostrarInformacion);
        }
    }
}