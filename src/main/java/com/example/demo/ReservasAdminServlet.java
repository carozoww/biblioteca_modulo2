package com.example.demo;

import dao.ReservaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Reserva;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "ReservaAdminServlet", value = "/reservasAdmin")
public class ReservasAdminServlet extends HttpServlet {
    private ReservaDAO reservaDAO = new ReservaDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ReservaDAO reservaDAO = new ReservaDAO();
        List<Reserva> reservas = reservaDAO.listarReservas();

        request.setAttribute("reservas", reservas);
        request.getRequestDispatcher("reservasAdmin.jsp").forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idReserva = Integer.parseInt(request.getParameter("idReserva"));
        String accion = request.getParameter("accion");

        try {
            switch (accion) {

                case "aceptar":
                    // reserva queda en estado "CONFIRMADO"
                    reservaDAO.confirmarReserva(idReserva);
                    break;

                case "rechazar":
                    // reserva se elimina de la bd
                    reservaDAO.eliminarReserva(idReserva);
                    break;

                case "levantada":
                    //El lector levanta la llave de la sala y reserva queda en estado "RESERVADA"

                    reservaDAO.marcarReservaComoReservada(idReserva);
                    break;

                case "finalizar":
                    LocalDateTime fechaFinalizadaReal = LocalDateTime.now();
                    // se actualiza al reserva con la fecha y hora actual (fecha de finalizacion real)
                    // y el estado "FINALIZADA"
                    reservaDAO.reservaFinalizada(idReserva, fechaFinalizadaReal);
                    break;
            }

            response.sendRedirect("reservasAdmin?msg=ok");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("reservasAdmin?msg=error");
        }

    }

}
