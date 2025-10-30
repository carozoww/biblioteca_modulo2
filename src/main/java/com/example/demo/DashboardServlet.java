package com.example.demo;

import dao.generoDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Genero;
import models.Lector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "dashboardServlet", value = "/dashboard")
public class DashboardServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login-lector");
            return;
        }

        List<Genero> generos = generoDAO.mostrarGeneros();
        List<String> generos2 = new ArrayList<>();

        generos.forEach( genero ->{
            generos2.add(genero.getNombre());
        });

        request.setAttribute("listaGeneros", generos2);
        Lector usuario = (Lector) session.getAttribute("authUser");
        request.setAttribute("usuario", usuario);
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    public void destroy() {
    }
}