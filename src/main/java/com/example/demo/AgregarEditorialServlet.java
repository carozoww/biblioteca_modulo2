package com.example.demo;

import dao.EditorialDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "agregarEditorial", value = "/agregarEditorial")
public class AgregarEditorialServlet extends HttpServlet {
    EditorialDAO editorialDAO = new EditorialDAO();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.getRequestDispatcher("agregarEditorial.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");

        try {
            if (nombre != null && !nombre.trim().isEmpty()){
                editorialDAO.crearEditorial(nombre);
                response.sendRedirect("editorialesAdmin?mensaje=Editorial agregada correctamente");
            }else {
             response.sendRedirect("editorialesAdmin?mensaje=No se puede agregar editorial");
            }
        }catch (Exception e){
            throw new ServletException(e);
        }


    }

}
