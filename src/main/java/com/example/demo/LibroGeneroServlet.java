package com.example.demo;

import com.google.gson.Gson;
import dao.LibroDAO;
import dao.LibroGeneroDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Libro;

import java.io.IOException;
import java.util.List;

@WebServlet("/librosGenero")
public class LibroGeneroServlet extends HttpServlet {
    private String message;
    private LibroGeneroDAO librogeneroDAO = new LibroGeneroDAO();
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
            String genero = request.getParameter("genero");
            System.out.println(genero);
            List<Libro> librosGenero = librogeneroDAO.obtenerLibrosPorGenero(genero);
            String jsonLibros =  gson.toJson(librosGenero);
            response.getWriter().println(jsonLibros);

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public void destroy() {
    }
}