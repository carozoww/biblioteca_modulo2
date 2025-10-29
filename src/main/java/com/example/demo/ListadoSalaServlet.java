package com.example.demo;

import dao.SalaDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Sala;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ListadoSalaServlet", value = "/listadoSalas")
public class ListadoSalaServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Sala> salas = new SalaDAO().listarSalas();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            for (Sala col : salas) {
                out.printf("<div class='sala-item' id='%s' onclick='actualizarValor(%s)'>%n", col.getIdSala(), col.getIdSala());
                out.printf("  <p>Sala %s</p>%n", col.getNumeroSala());
                if (col.getImagen() == null || col.getImagen().isEmpty()) {
                    out.println("  <img src='imgs/room.png' alt='Sala' width='150' height='150'>");
                } else {
                    out.printf("  <img src='%s' alt='Sala' width='150' height='150'>", col.getImagen());
                }
                out.println("<div class='info'>");
                out.printf("<p>Sala: %s</p><br>", col.getNumeroSala());
                out.printf("<p>Max Personas: %s</p><br>", col.getMaxPersonas());
                out.printf("<p>Ubicacion: %s</p>", col.getUbicacion());

                out.println("</div></div>");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}