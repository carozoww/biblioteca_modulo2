package com.example.demo;

import dao.LectorDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Administrador;
import models.Lector;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "AutenticarServelet", value = "/autenticarLector")
public class AutenticarServlet extends HttpServlet {
    LectorDAO lectordao = new LectorDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Lector> lectores = lectordao.listarLectores();
        request.setAttribute("lectores", lectores);
        Administrador admin = (Administrador) request.getSession().getAttribute("authAdmin");
        System.out.println("Admin en sesión antes de ir a autenticarLector: " + admin);

        request.getRequestDispatcher("/autenticarLectores.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String accion = request.getParameter("accion");
            int idLector = Integer.parseInt(request.getParameter("idLector"));

            if ("autenticar".equals(accion)) {
                // autentica al lector, cambia autenticacion de false a true
                lectordao.autenticarLector(idLector);
            }
            response.sendRedirect("autenticarLector?msg=ok");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("autenticarLector?msg=error");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
}
