package com.example.demo;

import dao.autorDAO;
import dao.generoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "agregarAutor", value = "/agregarAutor")

public class AgregarAutorServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.getRequestDispatcher("agregarAutor.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");

        try {
            if (nombre != null && !nombre.trim().isEmpty() && apellido != null && !apellido.trim().isEmpty()) {
                autorDAO.crearAutor(nombre, apellido);
                response.sendRedirect("autoresAdmin?mensaje=Genero agregado correctamente");
            }else {
                response.sendRedirect("autoresAdmin?mensaje=No se pudo agregar genero");
            }
        }catch (Exception e){
            throw new ServletException(e);
        }


    }

}
