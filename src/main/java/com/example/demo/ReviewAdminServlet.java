package com.example.demo;

import com.google.gson.Gson;
import dao.ReviewDAO;
import dao.ReviewComentarioDAO;
import models.Lector;
import models.Libro;
import models.Review;
import models.ReviewComentario;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/reviewAdmin")
public class ReviewAdminServlet extends HttpServlet {
    private ReviewDAO reviewDAO = new ReviewDAO();
    private ReviewComentarioDAO comentarioDAO = new ReviewComentarioDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");

        try {
            if ("listar".equals(accion)) {
                int pagina = 1;
                try { pagina = Integer.parseInt(request.getParameter("pagina")); } catch (Exception ignored) {}
                int offset = (pagina - 1) * 20;

                List<Review> reviews = reviewDAO.listarReviews(offset, 20);
                response.getWriter().println(gson.toJson(reviews));
                return;
            }

            if ("detalle".equals(accion)) {
                int idReview = Integer.parseInt(request.getParameter("id_review"));
                Review review = reviewDAO.obtenerReview(idReview);
                response.getWriter().println(gson.toJson(review));
                return;
            }

            if ("listarComentarios".equals(accion)) {
                int idReview = Integer.parseInt(request.getParameter("id_review"));
                List<ReviewComentario> comentarios = comentarioDAO.listarComentarios(idReview);
                response.getWriter().println(gson.toJson(comentarios));
                return;
            }

            response.getWriter().println("{\"error\":\"Acción no válida\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("{\"error\":\"Error en el servidor\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");

        try {
            if ("eliminar".equals(accion)) {
                System.out.println("Eliminar review id: " + request.getParameter("id_review"));
                int idReview = Integer.parseInt(request.getParameter("id_review"));
                boolean exito = reviewDAO.eliminarReview(idReview);
                response.getWriter().println(gson.toJson(Map.of("exito", exito)));
                return;

            }
            if ("eliminarComentario".equals(accion)) {
                int idComentario = Integer.parseInt(request.getParameter("id_comentario"));
                boolean exito = comentarioDAO.eliminarComentario(idComentario);
                response.getWriter().println(gson.toJson(Map.of("exito", exito)));
                return;
            }

            response.getWriter().println("{\"error\":\"Acción no válida\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(gson.toJson(Map.of("error", "Error en el servidor")));
        }
    }
}
