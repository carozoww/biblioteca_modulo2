package com.example.demo;

import dao.autorDAO;
import dao.generoDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Autor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
@WebServlet(name = "autoresAdmin", value = "/autoresAdmin")

public class AdministradorAutoresServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            List<Autor> autores = autorDAO.mostrarAutores();
            request.setAttribute("listaAutores", autores);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        RequestDispatcher rd = request.getRequestDispatcher("autoresAdmin.jsp");
        rd.forward(request, response);



    }

}
