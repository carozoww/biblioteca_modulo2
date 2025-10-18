package com.example.demo;

import com.google.gson.Gson;
import dao.EditorialDAO;
import dao.LibroAutorDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Libro;

import java.io.IOException;
import java.util.List;

@WebServlet("/librosEditorial")
public class LibroEditorialServlet extends HttpServlet {
    private String message;
    private EditorialDAO editorialDAO = new EditorialDAO();
    private Gson gson = new Gson();

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        try{
            int ed = Integer.parseInt(request.getParameter("editorial"));
            System.out.println(ed);
            List<Libro> librosEd = editorialDAO.listarLibroEditorial(ed);
            String jsonLibros =  gson.toJson(librosEd);
            response.getWriter().println(jsonLibros);

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public void destroy() {
    }
}