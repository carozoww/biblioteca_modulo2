package com.example.demo;

import com.google.gson.Gson;
import dao.LibroAutorDAO;
import dao.LibroGeneroDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Libro;

import java.io.IOException;
import java.util.List;

@WebServlet("/librosAutor")
public class LibroAutorServlet extends HttpServlet {
    private String message;
    private LibroAutorDAO libroautorDAO = new LibroAutorDAO();
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
            int autor = Integer.parseInt(request.getParameter("autor"));
            System.out.println(autor);
            List<Libro> librosAutor = libroautorDAO.listarLibroPorAutor(autor);
            String jsonLibros =  gson.toJson(librosAutor);
            response.getWriter().println(jsonLibros);

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public void destroy() {
    }
}