package com.example.demo;

import dao.generoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "agregarGenero", value = "/agregarGenero")

public class AgregarGeneroServlet  extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.getRequestDispatcher("agregarGenero.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nombre = request.getParameter("nombre");

        try {
            if (nombre != null && !nombre.trim().isEmpty()){
                generoDAO.crearGenero(nombre);
                response.sendRedirect("generosAdmin?mensaje=Genero agregado correctamente");
            }else {
             response.sendRedirect("generosAdmin?mensaje=No se pudo agregar genero");
            }
        }catch (Exception e){
            throw new ServletException(e);
        }


    }

}
