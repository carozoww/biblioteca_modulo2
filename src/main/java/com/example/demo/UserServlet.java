package com.example.demo;

import dao.AdministradorDAO;
import dao.LectorDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Administrador;
import models.Lector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UserServlet", value = "/users")
public class UserServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try {
            String accion = request.getParameter("accion");

            if(accion != null && accion.equals("bienvenida")){
                request.getRequestDispatcher("bienvenida.jsp").forward(request, response);
                return; // ← IMPORTANTE: terminar la ejecución aquí
            } else {
                // Obtener lectores de la base de datos
                List<Lector> lectores = new LectorDAO().listarLectores();
                List<String> nombres = new ArrayList<>();

                lectores.forEach(lector -> {
                    nombres.add(lector.getNombre());
                });

                // Agregar datos al request
                request.setAttribute("listaLectores", nombres); // ← CORREGIDO: "listaLectores"
                request.setAttribute("mensajeBienvenida", "Lista de Lectores:");

                // Solo UN forward
                request.getRequestDispatcher("usuarios.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace(); // ← Mejor que RuntimeException
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String password = request.getParameter("pass");

            // Aquí iría la lógica para guardar en BD

            String correo = request.getParameter("correo");
            String contrasenia = request.getParameter("pass");

            List<Lector> lector = new LectorDAO().inicioSesion(correo,contrasenia);
            if(lector.isEmpty()){
                System.out.println("Administradores no encontrado");
            }else{
                System.out.println("Existe administrador");
            }

            List<Lector> lectores2 = new LectorDAO().listarLectores();
            List<String> nombres2 = new ArrayList<>();

            lectores2.forEach( lector1 -> {
                nombres2.add(lector1.getNombre());
            });

            request.setAttribute("listaLectores", nombres2);


            request.getRequestDispatcher("login.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
}