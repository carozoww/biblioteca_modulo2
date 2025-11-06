package com.example.demo;

import dao.LectorDAO;
import dao.PenalizacionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Administrador;
import models.Lector;
import models.Penalizacion;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "PenalizacionServlet", value = "/penalizaciones")
public class PenalizacionServlet extends HttpServlet {

    private final PenalizacionDAO penadao = new PenalizacionDAO();
    private final LectorDAO lectordao = new LectorDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Penalizacion> penalizaciones = penadao.listarPenalizaciones();
        List<Lector> lectores = lectordao.listarLectores();
        request.setAttribute("lectores", lectores);
        request.setAttribute("penalizaciones", penalizaciones);
        request.getRequestDispatcher("/gestionarPenalizaciones.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String accion = request.getParameter("accion");
            Administrador admin = (Administrador) request.getSession().getAttribute("authAdmin");

            if (accion == null || admin == null) {
                return;
            }

            switch (accion) {
                case "aplicar": {
                    int idLector = Integer.parseInt(request.getParameter("idLector"));
                    int idAdmin = admin.getId(); // tomamos el admin logueado
                    int duracion = Integer.parseInt(request.getParameter("duracion"));
                    String motivo = request.getParameter("motivo");

                    if (!penadao.tienePenalizacionActiva(idLector)) {
                        LocalDateTime fechaInicio = LocalDateTime.now();
                        LocalDateTime fechaFin = fechaInicio.plusDays(duracion);
                        penadao.crearPenalizacion(duracion, motivo, fechaInicio, fechaFin, idLector, idAdmin);

                    } else {
                        System.out.println("El lector ya tiene una penalización activa.");
                    }
                }
                break;
                case "quitar": {
                    int idPena = Integer.parseInt(request.getParameter("idPena"));
                    penadao.desactivarPenalizacion(idPena);
                }
                break;
                default: System.out.println("Acción no reconocida: " + accion);
            }

            response.sendRedirect("penalizaciones?msg=ok");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("penalizaciones?msg=error");
        }
    }
}
