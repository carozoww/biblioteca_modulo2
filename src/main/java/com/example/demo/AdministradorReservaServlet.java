package com.example.demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import dao.LectorDAO;
import dao.ReservaDAO;
import dao.SalaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Lector;
import models.Reserva;
import models.Sala;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdministradorReservaServlet", value = "/administradorReservas")
public class AdministradorReservaServlet extends HttpServlet {
    ReservaDAO reservaDAO = new ReservaDAO();
    SalaDAO salaDAO = new SalaDAO();
    LectorDAO lectorDAO = new LectorDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cargarReservas = request.getParameter("cargarReservas");
            if ("true".equalsIgnoreCase(cargarReservas)) {
                List<Reserva> reservas = reservaDAO.listarReservas();
                List<Sala> salas = salaDAO.listarSalas();
                List<Lector> lectores = lectorDAO.listarLectores();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class,
                                (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                                        new JsonPrimitive(src.toString()))
                        .registerTypeAdapter(LocalDateTime.class,
                                (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                                        new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                        .create();

                Map<String, Object> data = new HashMap<>();
                data.put("reservas", reservas);
                data.put("salas", salas);
                data.put("lectores", lectores);

                String json = gson.toJson(data);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);

                return;
            }
            request.getRequestDispatcher("administradorReservas.jsp").forward(request, response);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String accion = request.getParameter("accion");
            if ("agregar".equals(accion)) {
                int salaId = Integer.parseInt(request.getParameter("id-sala"));
                int userId = Integer.parseInt(request.getParameter("id-lector"));
                LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
                LocalTime horaInicio = LocalTime.parse(request.getParameter("hora-inicio"));
                LocalTime horaFin = LocalTime.parse(request.getParameter("hora-fin"));
                LocalDateTime fechaInicio = LocalDateTime.of(fecha, horaInicio);
                LocalDateTime fechaFin = LocalDateTime.of(fecha, horaFin);

                List<Reserva> reservaDelDia = reservaDAO.listarReservasSinFinalizarPorFecha(fecha);
                boolean permitida = true;
                for (Reserva r : reservaDelDia) {
                    LocalTime inicio = r.getHora_in();
                    LocalTime fin = r.getHora_fin();

                    if (horaInicio.isBefore(fin) && horaFin.isAfter(inicio))
                        permitida = false;
                }
                if (permitida) {
                    reservaDAO.agregarReserva(salaId, userId, fechaInicio, fechaFin);
                    request.getSession().setAttribute("mensaje", "reserva agregada");
                } else {
                    request.getSession().setAttribute("mensaje", "horario de la reserva no permitido");
                }

            } else if ("editar".equals(accion)) {
                int reservaId = Integer.parseInt(request.getParameter("id-reserva"));
                int salaId = Integer.parseInt(request.getParameter("id-sala"));
                int userId = Integer.parseInt(request.getParameter("id-lector"));
                String estado = request.getParameter("estado");
                LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
                LocalTime horaInicio = LocalTime.parse(request.getParameter("hora-inicio"));
                LocalTime horaFin = LocalTime.parse(request.getParameter("hora-fin"));

                LocalDateTime fechaInicio = LocalDateTime.of(fecha, horaInicio);
                LocalDateTime fechaFin = LocalDateTime.of(fecha, horaFin);

                List<Reserva> reservaDelDia = reservaDAO.listarReservasSinFinalizarPorFecha(fecha);
                boolean permitida = true;
                for (Reserva r : reservaDelDia) {
                    LocalTime inicio = r.getHora_in();
                    LocalTime fin = r.getHora_fin();

                    if (horaInicio.isBefore(fin) && horaFin.isAfter(inicio)) {
                        if (r.getId_Reserva() != reservaId)
                            permitida = false;
                    }
                }
                if (permitida) {
                    reservaDAO.editarReserva(reservaId, salaId, userId, fechaInicio, fechaFin, estado);
                    request.getSession().setAttribute("mensaje", "reserva editada");
                } else {
                    request.getSession().setAttribute("mensaje", "horario de la reserva no permitido");
                }
            }
            response.sendRedirect("administradorReservas.jsp");
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}