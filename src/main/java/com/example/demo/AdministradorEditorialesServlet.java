package com.example.demo;

import dao.EditorialDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Editorial;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "editorialesAdmin", value = "/editorialesAdmin")
public class AdministradorEditorialesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Editorial> editoriales = EditorialDAO.listarEditorial();

        request.setAttribute("listaEditoriales", editoriales);

        RequestDispatcher rd = request.getRequestDispatcher("editorialesAdmin.jsp");
        rd.forward(request, response);


    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }


}
