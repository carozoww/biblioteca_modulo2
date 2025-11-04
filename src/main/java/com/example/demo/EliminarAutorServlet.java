package com.example.demo;

import dao.autorDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;

@WebServlet(name = "eliminarAutor", value = "/eliminarAutor")

public class EliminarAutorServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mensaje;
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            autorDAO.eliminarAutor(id);
            mensaje = "Autor eliminado correctamente.";
        }catch (Exception e) {
            mensaje = e.getMessage() != null ? e.getMessage() : "Ocurrió un error al eliminar el autor.";

        }
        response.sendRedirect("autoresAdmin?mensaje=" + URLEncoder.encode(mensaje, "UTF-8"));

    }

}
