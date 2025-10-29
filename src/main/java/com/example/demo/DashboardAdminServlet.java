package com.example.demo;

import dao.generoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Administrador;
import models.Genero;
import models.Lector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "dashboardAdminServlet", value = "/dashboardAdmin")
public class DashboardAdminServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authAdmin") == null) {
            response.sendRedirect(request.getContextPath() + "/login-admin");
            return;
        }

        List<Genero> generos = generoDAO.mostrarGeneros();
        List<String> generos2 = new ArrayList<>();

        generos.forEach( genero ->{
            generos2.add(genero.getNombre());
        });

        request.setAttribute("listaGeneros", generos2);
        Administrador admin = (Administrador) session.getAttribute("authUser");
        request.setAttribute("admin", admin);
        request.getRequestDispatcher("/dashboardAdmin.jsp").forward(request, response);
    }

    public void destroy() {
    }
}