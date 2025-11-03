package com.example.demo;

import dao.autorDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Autor;


import java.io.IOException;

@WebServlet(name = "editarAutor", value = "/editarAutor")

public class EditarAutorServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Autor autor = new autorDAO().buscarAutorPorId(id);

        request.setAttribute("autor", autor);
        request.getRequestDispatcher("editarAutor.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");

        try {
            if (nombre != null && !nombre.trim().isEmpty()) {
                autorDAO.modificarAutor(id, nombre, apellido);
                response.sendRedirect("autoresAdmin?mensaje=Autor modificado correctamente");
            } else {
                response.sendRedirect("autoresAdmin?mensaje=Error: nombre vacío");
            }
        }catch (Exception e){
            throw new ServletException(e);
        }
    }
}
