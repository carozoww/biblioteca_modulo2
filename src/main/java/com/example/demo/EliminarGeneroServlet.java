package com.example.demo;

import dao.generoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
@WebServlet(name = "eliminarGenero", value = "/eliminarGenero")

public class EliminarGeneroServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mensaje;
       try {
           int id = Integer.parseInt(request.getParameter("id"));
           generoDAO.eliminarGenero(id);
           mensaje = "Género eliminado correctamente.";

       }catch (RuntimeException e) {
           mensaje = e.getMessage() != null ? e.getMessage() : "Ocurrió un error al eliminar el género.";

       }
        response.sendRedirect("generosAdmin?mensaje=" + URLEncoder.encode(mensaje, "UTF-8"));
    }


}
