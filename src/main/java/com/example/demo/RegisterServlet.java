package com.example.demo;

import dao.LectorDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Lector;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        try{
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12)); // 12 = factor de costo
            System.out.println("Hash generado: " + hashed);
            LectorDAO lectorDAO = new LectorDAO();
            Boolean existe = false;
            if(!lectorDAO.existeLector(cedula).isEmpty()){
                existe = true;
                request.setAttribute("existeLector",existe);
                request.getRequestDispatcher("registro.jsp").forward(request, response);
            }else{
                Lector lector = new Lector(nombre,cedula,telefono,direccion,false,nuevaFecha ,false,correo,hashed);
                HttpSession newSession = request.getSession(true);
                newSession.setAttribute("authUser", lector);
                newSession.setMaxInactiveInterval(30 * 60);
                String csrfToken = UUID.randomUUID().toString();
                newSession.setAttribute("csrfToken", csrfToken);
                response.sendRedirect(request.getContextPath() + "/dashboard");
                lectorDAO.crearLector(nombre,cedula,telefono,direccion,false,nuevaFecha ,false,correo,hashed);
                return;
            }
            request.setAttribute("existeLector",existe);
        }catch(Exception e){
            throw new ServletException(e);
        }



    }
}
