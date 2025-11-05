package com.example.demo;

import dao.EditorialDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Editorial;

import java.io.IOException;

@WebServlet(name = "editarEditorial", value = "/editarEditorial")
public class EditarEditorialServlet extends HttpServlet {
    EditorialDAO editorialDAO = new EditorialDAO();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
         Editorial editorial = new EditorialDAO().buscarEditorialPorId(id);

         request.setAttribute("editorial", editorial);
         request.getRequestDispatcher("editarEditorial.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");

        try {
            if (nombre != null && !nombre.trim().isEmpty()) {

                editorialDAO.editarEditorial(id, nombre.trim());

                response.sendRedirect("editorialesAdmin?mensaje=Editorial modificada correctamente");

            } else {
                response.sendRedirect("editorialesAdmin?mensaje=Error: nombre vacío");
            }
        }catch (Exception e){
            throw new ServletException(e);
        }
    }
}
