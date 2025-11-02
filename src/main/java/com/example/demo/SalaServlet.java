package com.example.demo;

import com.google.gson.Gson;
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
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@WebServlet(name = "SalaServlet", value = "/salas")
public class SalaServlet extends HttpServlet {
    ReservaDAO reservaDAO = new ReservaDAO();
    LectorDAO lectorDAO = new LectorDAO();
    SalaDAO salaDAO = new SalaDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            HttpSession sesion = request.getSession();
            Lector lector = (Lector) sesion.getAttribute("authUser");
            if (lector == null) {
                response.sendRedirect(request.getContextPath() + "/login-lector");
                return;
            }
            Lector lectorCompleto = lectorDAO.buscarPorCedula(Integer.parseInt(lector.getCedula()));
            Reserva reservaActiva = reservaDAO.listarReservaActivaDelLector(lectorCompleto.getID());

            if (reservaActiva != null) {
                request.setAttribute("reservaActiva", reservaActiva);
            } else {
                String action = request.getParameter("action");
                if (action != null && action.equals("listar")) {
                    List<Sala> salas = salaDAO.listarSalas();
                    response.setContentType("application/json;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.print(new Gson().toJson(salas));
                    out.flush();
                    return;
                }
            }
            request.getRequestDispatcher("salas.jsp").forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String horaInicio = request.getParameter("hora-inicio");
            String horaFin = request.getParameter("hora-fin");
            String fecha = request.getParameter("fecha-enviar");
            String sala = request.getParameter("sala-enviar");
            String accion = request.getParameter("accion");

            HttpSession sesion = request.getSession();
            Lector lector = (Lector) sesion.getAttribute("authUser");
            Lector lectorCompleto = lectorDAO.buscarPorCedula(Integer.parseInt(lector.getCedula()));
            boolean reservaPresente = reservaDAO.reservaActivaPorLector(lectorCompleto.getID());

            if (accion.equals("reservar") && horaFin != null && !horaFin.isEmpty() && horaInicio != null && !horaInicio.isEmpty() && fecha != null && !fecha.isEmpty() && sala != null) {
                int salaId = Integer.parseInt(sala);
                if (reservaPresente) {
                    request.getRequestDispatcher("salas.jsp").forward(request, response);
                    return;
                }

                LocalDate fechaActual = LocalDate.now();
                LocalTime horaActual = LocalTime.now();

                LocalDate fechaDate = LocalDate.parse(fecha);
                LocalTime horaInicial = LocalTime.parse(horaInicio);
                LocalTime horaFinal = LocalTime.parse(horaFin);

                if (horaInicial.isAfter(horaFinal) || fechaDate.equals(fechaActual) && horaActual.isAfter(horaInicial)) {
                    request.setAttribute("mensaje", "Horas seleccionadas no validas");
                    request.getRequestDispatcher("salas.jsp").forward(request, response);
                    return;
                }

                LocalDateTime fechaHoraInicio = fechaDate.atTime(horaInicial);
                LocalDateTime fechaHoraFin = fechaDate.atTime(horaFinal);

                reservaDAO.agregarReserva(salaId, lectorCompleto.getID(), fechaHoraInicio, fechaHoraFin);
            } else if (accion.equals("cancelar")) {
                reservaDAO.cancelarReservaPorLector(lectorCompleto.getID());
            } else if (accion.equals("terminar")) {
                reservaDAO.finalizarReservaPorLector(lectorCompleto.getID());
            }

            Reserva reservaActiva = reservaDAO.listarReservaActivaDelLector(lectorCompleto.getID());
            request.setAttribute("reservaActiva", null);
            if (reservaActiva != null) {
                request.setAttribute("reservaActiva", reservaActiva);
            }

            response.sendRedirect("salas");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}