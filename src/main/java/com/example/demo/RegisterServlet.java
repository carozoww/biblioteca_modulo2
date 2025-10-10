package com.example.demo;

import dao.LectorDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Lector;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        try{
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }catch(Exception e){
            throw new ServletException(e);
        }



    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String cedula = request.getParameter("ced");
        String correo = request.getParameter("correo");
        String  password = request.getParameter("pass");
        String telefono = request.getParameter("tele");
        String direccion = request.getParameter("dir");
        String fecha = request.getParameter("fecha");
        LocalDate nuevaFecha = LocalDate.parse(fecha);

        LectorDAO lectorDAO = new LectorDAO();
        Boolean existe = false;
        if(!lectorDAO.existeLector(cedula).isEmpty()){
            existe = true;
            request.setAttribute("existeLector",existe);
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }else{
            existe = false;
            request.getRequestDispatcher("bienvenida.jsp").forward(request, response);
            lectorDAO.crearLector(nombre,cedula,telefono,direccion,false,nuevaFecha ,false,correo,password);
        }
        request.setAttribute("existeLector",existe);


    }
}
