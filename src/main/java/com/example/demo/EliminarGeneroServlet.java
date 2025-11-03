package com.example.demo;

import dao.generoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
@WebServlet(name = "eliminarGenero", value = "/eliminarGenero")

public class EliminarGeneroServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
       try {


           generoDAO.eliminarGenero(id);
           response.sendRedirect("generosAdmin?mensaje=Género eliminado correctamente");
       }catch (Exception e) {
           throw  new ServletException(e);

       }

       }


}
