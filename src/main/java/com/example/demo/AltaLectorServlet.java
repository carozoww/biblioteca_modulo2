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
import java.util.List;
import java.util.UUID;

@WebServlet(name = "AltaLectorServlet", value = "/alta-lector-servlet")
public class AltaLectorServlet extends HttpServlet {
    private LectorDAO lectorDAO = new LectorDAO();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        try{


            List<Lector> lectores = lectorDAO.listarLectores();
            if(lectores.isEmpty()){
                System.out.println("No se obtienen lectores");
            }
            request.setAttribute("lectores", lectores);
            request.getRequestDispatcher("altaLector.jsp").forward(request, response);



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

            Boolean existe = false;
            if(!lectorDAO.existeLector(cedula).isEmpty()){
                existe = true;
                request.setAttribute("existeLector",existe);
                request.getRequestDispatcher("altaLector.jsp").forward(request, response);
            }else{
                response.sendRedirect(request.getContextPath() + "/dashboardAdmin");
                lectorDAO.crearLector(nombre,cedula,telefono,direccion,true,nuevaFecha ,false,correo,hashed);
                return;
            }
            request.setAttribute("existeLector",existe);
        }catch(Exception e){
            throw new ServletException(e);
        }



    }
}
