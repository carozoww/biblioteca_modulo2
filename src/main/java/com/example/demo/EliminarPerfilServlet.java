package com.example.demo;

import dao.LectorDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Lector;

import java.io.IOException;
import java.util.List;

@WebServlet("/eliminarLector")
public class EliminarPerfilServlet extends HttpServlet {

    private LectorDAO lectorDAO = new LectorDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Tomamos la cédula del formulario
            String cedula = request.getParameter("cedula");

            if (cedula == null || cedula.isEmpty()) {
                response.getWriter().println("Cédula no especificada.");
                return;
            }

            // Buscamos el lector para obtener su cedula
            List<Lector> lectores = lectorDAO.existeLector(cedula);
            if (lectores.isEmpty()) {
                response.getWriter().println("No se encontró el lector con cédula: " + cedula);
                return;
            }
            Lector lector = lectores.get(0); // Primer lector

            // Eliminamos el lector por ID
            lectorDAO.eliminarLector(lector.getID());

            // Cerramos sesión si está activa
            HttpSession sesion = request.getSession(false);
            if (sesion != null) {
                sesion.invalidate();
            }

            // Redirigir al login
            response.sendRedirect("login.jsp");

        } catch (Exception e) {
            throw new ServletException("Error al eliminar lector", e);
        }
    }
}
