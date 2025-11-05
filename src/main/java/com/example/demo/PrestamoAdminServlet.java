package com.example.demo;

import dao.PrestamoDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Prestamo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "PrestamoAdminServlet", value = "/prestamosAdmin")
public class PrestamoAdminServlet extends HttpServlet {
    private PrestamoDAO prestamoDAO = new PrestamoDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrestamoDAO prestamoDAO = new PrestamoDAO();
        List<Prestamo> prestamos = prestamoDAO.listarPrestamos();

        request.setAttribute("prestamos", prestamos);
        request.getRequestDispatcher("prestamosAdmin.jsp").forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idPrestamo = Integer.parseInt(request.getParameter("idPrestamo"));
        String accion = request.getParameter("accion");

        try {
            switch (accion) {

                case "aceptar":
                    // prestamo queda en estado "CONFIRMADO"
                    prestamoDAO.confirmarPrestamo(idPrestamo);
                    break;

                case "rechazar":
                    // prestamo se elimina de la bd
                    prestamoDAO.eliminarPrestamo(idPrestamo);
                    break;

                case "levantado":
                    // Cuando el lector levanta el libro
                    LocalDateTime fechaPrestamo = LocalDateTime.now();
                    LocalDateTime fechaDevolucionEsperada = fechaPrestamo.plusDays(15);

                    // actualizar en BD con la fecha de devolucion esperada y el estado en "RESERVADO"
                    prestamoDAO.marcarPrestamoComoReservado(idPrestamo, fechaDevolucionEsperada);
                    break;

                case "finalizar":
                    LocalDateTime fechaDevolucionReal = LocalDateTime.now();
                    // se actualiza el prestamo con la fecha y hora actual (fecha de la devolucion real)
                    // y el estado "FINALIZADO"
                    prestamoDAO.prestamoFinalizado(idPrestamo, fechaDevolucionReal);
                    break;
            }

            response.sendRedirect("prestamosAdmin?msg=ok");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("prestamosAdmin?msg=error");
        }

    }
}
