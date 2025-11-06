package com.example.demo;

import dao.ReservaDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Lector;
import models.Reserva;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MisReservaServlet", value = "/mis-reservas")
public class MisReservasServlet extends HttpServlet {
    private ReservaDAO reservaDAO = new ReservaDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        try {
            HttpSession sesion = request.getSession();
            Lector lector = (Lector) sesion.getAttribute("authUser");

            List<Reserva> reservas = reservaDAO.listarReservasTerminadasDelLector(lector.getID());
            request.setAttribute("reservas", reservas);
            request.getRequestDispatcher("misReservas.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

}