package com.example.demo;

import dao.generoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Genero;

import java.io.IOException;

@WebServlet(name = "editarGenero", value = "/editarGenero")
public class EditarGeneroServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
         Genero genero = new generoDAO().buscarGeneroPorId(id);

         request.setAttribute("genero", genero);
         request.getRequestDispatcher("editarGenero.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");

        try {
            if (nombre != null && !nombre.trim().isEmpty()) {
                generoDAO.modificarGenero(id, nombre.trim());
                response.sendRedirect("generosAdmin?mensaje=Género modificado correctamente");
            } else {
                response.sendRedirect("generosAdmin?mensaje=Error: nombre vacío");
            }
        }catch (Exception e){
            throw new ServletException(e);
        }
    }
}
