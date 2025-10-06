package com.example.demo;

import dao.LectorDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Lector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UserServlet", value = "/users")
public class UserServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try {
            String accion = request.getParameter("accion");

            if(accion != null && accion.equals("bienvenida")){
                request.getRequestDispatcher("bienvenida.jsp").forward(request, response);
                return; // ← IMPORTANTE: terminar la ejecución aquí
            } else {
                // Obtener lectores de la base de datos
                List<Lector> lectores = new LectorDAO().listarLectores();
                List<String> nombres = new ArrayList<>();

                lectores.forEach(lector -> {
                    nombres.add(lector.getNombre());
                });

                // Agregar datos al request
                request.setAttribute("listaLectores", nombres); // ← CORREGIDO: "listaLectores"
                request.setAttribute("mensajeBienvenida", "Lista de Lectores:");

                // Solo UN forward
                request.getRequestDispatcher("usuarios.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace(); // ← Mejor que RuntimeException
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String password = request.getParameter("pass");

            // Aquí iría la lógica para guardar en BD

            request.setAttribute("exito", true);
            request.setAttribute("nombreUsuario", nombre.toUpperCase());
            request.getSession().setAttribute("logueado", nombre);

            request.getRequestDispatcher("registro.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
}