package com.example.demo;

import com.google.gson.Gson;
import dao.LibroDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Libro;

import java.io.IOException;
import java.util.List;

@WebServlet("/libros")
public class LibroServlet extends HttpServlet {
    private String message;
    private LibroDAO libroDAO = new LibroDAO();
    private Gson gson = new Gson();

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try{
            List<Libro> libros = libroDAO.listarLibrosCompleto();
            String jsonLibros =  gson.toJson(libros);
            response.getWriter().println(jsonLibros);



        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void destroy() {
    }
}