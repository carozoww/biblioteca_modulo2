package com.example.demo;

import dao.LectorDAO;
import dao.ReservaDAO;
import dao.SalaDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Lector;
import models.Reserva;
import models.Sala;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ReservaServlet", value = "/reservar")
public class ReservaServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try {
            String valorsalaSeleccionada = request.getParameter("salaSeleccionada");
            if (valorsalaSeleccionada == null) {
                response.sendRedirect(request.getContextPath() + "/salas");
                return;
            }

            int salaSeleccionada = Integer.parseInt(valorsalaSeleccionada);

            // Obtener sala
            Sala salas = new SalaDAO().buscarSalaPorNumeroSala(salaSeleccionada);

            // Agregar datos al request
            request.setAttribute("listaSalas", salas);

            // Solo UN forward
            request.getRequestDispatcher("reservarSala.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("authUser") == null) {
                response.sendRedirect(request.getContextPath() + "/login-lector");
                return;
            }

            String valorSalaSeleccionada = request.getParameter("salaSeleccionada");
            if (valorSalaSeleccionada == null) {
                response.sendRedirect(request.getContextPath() + "/salas");
                return;
            }
            List<Reserva> reservasMostrar = new ArrayList<>();
            int salaSeleccionada = Integer.parseInt(valorSalaSeleccionada);

            String idReserva = request.getParameter("id-reserva");
            String fecha = request.getParameter("fecha");
            String horaInicio = request.getParameter("hora-inicio");
            String horaFin = request.getParameter("hora-fin");

            if (idReserva != null && !idReserva.isEmpty()) {
                Lector lector = (Lector) session.getAttribute("authUser");
                Lector lectorCompleto = new LectorDAO().buscarPorCedula(Integer.parseInt(lector.getCedula()));
                //Eliminar reserva
                new ReservaDAO().eliminarReserva(Integer.parseInt(idReserva), lectorCompleto.getID());
            }

            Sala sala = new SalaDAO().buscarSalaPorNumeroSala(salaSeleccionada);
            if (fecha != null && !fecha.isEmpty()) {
                List<Reserva> reservas = new ReservaDAO().reservasNoTerminadasPorSalaYFecha(salaSeleccionada, fecha);

                Lector lector = (Lector) session.getAttribute("authUser");
                Lector lectorCompleto = new LectorDAO().buscarPorCedula(Integer.parseInt(lector.getCedula()));

                for (Reserva r : reservas) {
                    reservasMostrar.add(new Reserva(((lectorCompleto.getID() == r.getId_usuario()) ? r.getId_Reserva() : 0), //Mostrar id_reserva si le pertenece al lectior
                            r.getFecha_in(), r.getFecha_fin(), 0, 0, null, null, 0));
                }

                if (horaInicio != null && horaFin != null) {
                    boolean reservar = true;
                    LocalDate fechaSeleccionada = LocalDate.parse(fecha);
                    LocalTime horaInicioSeleccionada = LocalTime.parse(horaInicio);
                    LocalTime horaFinSeleccionada = LocalTime.parse(horaFin);

                    for (Reserva r : reservas) {
                        //Evitar reservas en mismo horario
                        if (!(!horaInicioSeleccionada.isBefore(r.getHora_fin()) || !horaFinSeleccionada.isAfter(r.getHora_in()))) {
                            reservar = false;
                            break;
                        }
                    }

                    if (horaInicioSeleccionada.isAfter(horaFinSeleccionada)) {
                        reservar = false;
                    }

                    if (reservar) {
                        LocalDateTime fechaInicio = LocalDateTime.of(fechaSeleccionada, horaInicioSeleccionada);
                        LocalDateTime fechaFin = LocalDateTime.of(fechaSeleccionada, horaFinSeleccionada);

                        //Completar reserva
                        new ReservaDAO().agregarReserva(sala.getIdSala(), lectorCompleto.getID(), fechaInicio, fechaFin);
                    }

                }
            }
            // Agregar datos al request
            if (fecha != null && !fecha.isEmpty()) request.setAttribute("fechaSeleccionada", fecha);
            request.setAttribute("salaSeleccionada", salaSeleccionada);
            request.setAttribute("sala", sala);
            request.setAttribute("listaReservas", reservasMostrar);

            // Solo UN forward
            request.getRequestDispatcher("reservarSala.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
}