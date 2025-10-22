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

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Verificar si hay usuario logueado
        if (session == null || session.getAttribute("authUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login-lector");
            return;
        }

        // Obtener usuario de la sesión
        Lector usuario = (Lector) session.getAttribute("authUser");

        // Pasar usuario al JSP
        request.setAttribute("usuario", usuario);

        // Redirigir a perfil.jsp
        request.getRequestDispatcher("/perfil.jsp").forward(request, response);
    }
}
