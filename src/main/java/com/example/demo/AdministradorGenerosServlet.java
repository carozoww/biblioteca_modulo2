package com.example.demo;

import dao.generoDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Genero;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "generosAdmin", value = "/generosAdmin")
public class AdministradorGenerosServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Genero> generos = generoDAO.mostrarGeneros();

        request.setAttribute("listaGeneros", generos);

        RequestDispatcher rd = request.getRequestDispatcher("generosAdmin.jsp");
        rd.forward(request, response);



    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { }


}
