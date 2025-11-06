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

@WebServlet(name = "ReservaServlet", value = "/reservas")
public class ReservaServlet extends HttpServlet {
    ReservaDAO reservaDAO = new ReservaDAO();
    SalaDAO salaDAO = new SalaDAO();
    LectorDAO lectorDAO = new LectorDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            LocalDate fechaSeleccionada = LocalDate.parse(request.getParameter("fecha"));

            List<Sala> salas = salaDAO.listarSalasHabilitadas();
            List<Reserva> reservasFiltradas = reservaDAO.listarReservasSinFinalizarPorFecha(fechaSeleccionada);

            List<Integer> horas = Arrays.asList(9,10,11,12,13,14,15,16,17);
            List<Map<String, Object>> listaSalas = new ArrayList<>();

            for (Sala col : salas) {
                Map<String, Object> salaData = new HashMap<>();
                salaData.put("idSala", col.getIdSala());
                salaData.put("numeroSala", col.getNumeroSala());

                List<Boolean> disponibilidad = new ArrayList<>();
                for (Integer hora : horas) {
                    LocalDateTime horaInicio = fechaSeleccionada.atTime(hora, 0);
                    LocalDateTime horaFin = horaInicio.plusHours(1);

                    boolean ocupado = reservasFiltradas.stream().anyMatch(o ->
                            o.getSala() == col.getNumeroSala() &&
                                    o.getFecha_in().isBefore(horaFin) &&
                                    o.getFecha_fin().isAfter(horaInicio)
                    );
                    disponibilidad.add(!ocupado);
                }

                salaData.put("disponibilidad", disponibilidad);
                listaSalas.add(salaData);
            }

            Map<String, Object> resultado = new HashMap<>();

            List<String> horasFormato = new ArrayList<>();
            for (Integer hora : horas) {
                horasFormato.add(String.format("%02d:00", hora));
            }

            resultado.put("horas", horasFormato);
            resultado.put("salas", listaSalas);

            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(new Gson().toJson(resultado));
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
}
