package com.example.demo;

import dao.LectorDAO;
import models.Lector;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/test-lectores")
public class TestLectorServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h1>🔧 TEST LectorDAO</h1>");

        try {
            LectorDAO lectorDAO = new LectorDAO();
            List<Lector> lectores = lectorDAO.listarLectores();

            out.println("<h3>Número de lectores: " + lectores.size() + "</h3>");

            if(lectores.isEmpty()) {
                out.println("<p style='color: red;'>⚠️ La lista está VACÍA</p>");
            } else {
                out.println("<ul>");
                for(Lector lector : lectores) {
                    out.println("<li>📖 " + lector.getNombre() + " " + lector.getCedula() + "</li>");
                }
                out.println("</ul>");
            }

        } catch (Exception e) {
            out.println("<h2 style='color: red;'>❌ ERROR:</h2>");
            out.println("<pre>" + e.getMessage() + "</pre>");
            e.printStackTrace(out);
        }

        out.println("</body></html>");
    }
}
