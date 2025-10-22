package com.example.demo;

import dao.SugerenciaDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "SugerenciaServlet", value = "/sugerencia")
public class SugerenciaServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try{


            HttpSession sesion = request.getSession();

            request.getRequestDispatcher("sugerencia.jsp").forward(request, response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response){

        try{
            System.out.println("Entramos en el POST");
            String contenido = request.getParameter("areatexto");
            HttpSession sesion = request.getSession();
            Integer idLector = (Integer) sesion.getAttribute("idLector");
            SugerenciaDAO sugerenciadao = new SugerenciaDAO();
            sugerenciadao.crearSugerencia(contenido,idLector);
            response.sendRedirect(request.getContextPath() + "/dashboard");



        }catch(Exception e){
            new RuntimeException(e);
        }
    }

    public void destroy() {
    }
}