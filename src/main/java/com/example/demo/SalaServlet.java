package com.example.demo;

import dao.SalaDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Sala;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SalaServlet", value = "/salas")
public class SalaServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try {
            // Obtener salas de la base de datos
            List<Sala> salas = new SalaDAO().listarSalas();
            // Agregar datos al request
            request.setAttribute("listaSalas", salas);

            // Solo UN forward
            request.getRequestDispatcher("salas.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String tipoDato = request.getParameter("tipoDato");
            String buscando = request.getParameter("buscando");
            List<Sala> salas;
            boolean valorValido = true;
            if (tipoDato != null && !tipoDato.equals("ubicacion") && buscando != null && !buscando.isEmpty()) {
                try{
                    Integer.parseInt(buscando);
                }catch(Exception e){
                    valorValido = false;
                }
            }
            if (valorValido) {
                if (tipoDato != null && !tipoDato.isEmpty() && buscando != null && !buscando.isEmpty()) {
                    // Obtener salas de la base de datos
                    System.out.println("mitad");
                    salas = new SalaDAO().listarSalas(tipoDato, buscando);
                }else{
                    // Obtener salas de la base de datos
                    System.out.println("completa");
                    salas = new SalaDAO().listarSalas();
                }
            }else{
                salas = new ArrayList<>();
            }


            // Agregar datos al request
            request.setAttribute("listaSalas", salas);

            // Solo UN forward
            request.getRequestDispatcher("salas.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
}