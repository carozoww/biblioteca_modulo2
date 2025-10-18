package com.example.demo;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Lector;

import java.io.IOException;

@WebServlet(name = "dashboardServlet", value = "/dashboard")
public class DashboardServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login-lector");
            return;
        }

        Lector usuario = (Lector) session.getAttribute("authUser");
        request.setAttribute("usuario", usuario);
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    public void destroy() {
    }
}