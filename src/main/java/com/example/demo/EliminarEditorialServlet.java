package com.example.demo;

import dao.EditorialDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "eliminarEditorial", value = "/eliminarEditorial")

public class EliminarEditorialServlet extends HttpServlet {
    EditorialDAO editorialDAO = new EditorialDAO();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
       try {

           editorialDAO.eliminarEditorial(id);
           response.sendRedirect("editorialesAdmin?mensaje=Editorial eliminada correctamente");
       }catch (Exception e) {
           throw  new ServletException(e);

       }

       }


}
