package com.example.demo;

import com.google.gson.Gson;
import dao.generoDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Genero;

import java.io.IOException;
import java.util.List;

@WebServlet("/generos")
public class GeneroServlet extends HttpServlet {
    private String message;
    private generoDAO generodao = new generoDAO();
    private Gson gson = new Gson();

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try{
            List<Genero> generos = generodao.mostrarGeneros();
            String jsonGeneros =  gson.toJson(generos);
            response.getWriter().println(jsonGeneros);



        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void destroy() {
    }
}