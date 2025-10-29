package com.example.demo;

import com.google.gson.Gson;
import dao.SalaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Sala;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdministradorSalaServlet", value = "/administradorSalas")
public class AdministradorSalaServlet extends HttpServlet {
    SalaDAO salaDAO = new SalaDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cargarSalas = request.getParameter("cargarSalas");

            if ("true".equalsIgnoreCase(cargarSalas)) {
                List<Sala> salas = salaDAO.listarSalas();
                Gson gson = new Gson();
                String json = gson.toJson(salas);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);

                return;
            }
            request.getRequestDispatcher("administradorSalas.jsp").forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String accion = request.getParameter("accion");
            if ("agregar".equals(accion)) {
                String imagen = request.getParameter("imagen-url");
                int numero = Integer.parseInt(request.getParameter("numero-sala"));
                String ubicacion = request.getParameter("ubicacion");
                int personas = Integer.parseInt(request.getParameter("max-personas"));

                salaDAO.crearSala(numero, ubicacion, personas, imagen);
                request.getSession().setAttribute("mensaje", "sala agregada");
            } else if ("editar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id-sala"));
                String imagen = request.getParameter("imagen-url");
                int numero = Integer.parseInt(request.getParameter("numero-sala"));
                String ubicacion = request.getParameter("ubicacion");
                int personas = Integer.parseInt(request.getParameter("max-personas"));

                salaDAO.editarSala(id, numero, ubicacion, personas, imagen);
                request.getSession().setAttribute("mensaje", "sala editada");
            } else if ("eliminar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("eliminar-sala"));

                salaDAO.borrarSala(id);
                request.getSession().setAttribute("mensaje", "sala borrada");
            }
            response.sendRedirect("administradorSalas.jsp");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}