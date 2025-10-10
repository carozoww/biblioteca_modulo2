package com.example.demo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import dao.LectorDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Lector;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try{

            ServletContext contexto = getServletContext();

            HttpSession sesion = request.getSession();


            if(sesion.getAttribute("logueado") != null){
                request.getRequestDispatcher("usuarios.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy() {
    }
}