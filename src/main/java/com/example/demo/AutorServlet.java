package com.example.demo;

import com.google.gson.Gson;
import dao.EditorialDAO;
import dao.autorDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Autor;
import models.Editorial;

import java.io.IOException;
import java.util.List;

@WebServlet("/autores")
public class AutorServlet extends HttpServlet {
    private String message;
    private autorDAO autordao = new autorDAO();
    private Gson gson = new Gson();

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try{
            List<Autor> autores = autordao.mostrarAutores();
            String jsonAutores =  gson.toJson(autores);
            response.getWriter().println(jsonAutores);



        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void destroy() {
    }
}