package com.example.demo;

import com.google.gson.Gson;
import dao.ReviewDAO;
import dao.ReviewComentarioDAO;
import models.Lector;
import models.Review;
import models.ReviewComentario;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/reviews")
public class ReviewServlet extends HttpServlet {
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

            if ("porLibroYLector".equals(accion)) {
                int idLibro = Integer.parseInt(request.getParameter("id_libro"));
                int idLector = Integer.parseInt(request.getParameter("id_lector"));
                Review review = reviewDAO.obtenerReviewPorLibroYLector(idLector, idLibro);
                response.getWriter().println(review != null ? gson.toJson(review) : "null");
                return;
            }

            if ("listarComentarios".equals(accion)) {
                int idReview = Integer.parseInt(request.getParameter("id_review"));
                List<ReviewComentario> comentarios = comentarioDAO.listarComentarios(idReview);
                response.getWriter().println(gson.toJson(comentarios));
                return;
            }

            if ("contarComentarios".equals(accion)) {
                int idReview = Integer.parseInt(request.getParameter("id_review"));
                int total = comentarioDAO.contarComentarios(idReview);
                response.getWriter().println(gson.toJson(Map.of("total", total)));
                return;
            }

            if ("filtrarMisReviews".equals(accion)) {
                Lector usuario = (Lector) request.getSession().getAttribute("authUser");

                if (usuario == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().println(gson.toJson(List.of()));
                    return;
                }

                int idLector = usuario.getID();
                List<Review> misReviews = reviewDAO.listarReviewsPorLector(idLector);

                response.getWriter().println(gson.toJson(misReviews));
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
            // Declarar usuario
            Lector usuario = (Lector) request.getSession().getAttribute("authUser");
            if (usuario == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println(gson.toJson(Map.of("error", "Usuario no logueado")));
                return;
            }
            int idL = usuario.getID();

            if ("like".equals(accion)) {
                int idReview = Integer.parseInt(request.getParameter("id_review"));
                int nuevoConteo = reviewDAO.darLike(idReview, idL);
                response.getWriter().println(gson.toJson(Map.of("exito", true, "nuevoConteo", nuevoConteo)));
                return;
            }

            if ("crearComentario".equals(accion) || "agregarComentario".equals(accion)) {
                int idReview = Integer.parseInt(request.getParameter("id_review"));
                String contenido = request.getParameter("contenido");
                boolean exito = comentarioDAO.crearComentario(idReview, idL, contenido);
                response.getWriter().println(gson.toJson(Map.of("exito", exito)));
                return;
            }

            if ("eliminar".equals(accion)) {
                int idReview = Integer.parseInt(request.getParameter("id_review"));
                Review review = reviewDAO.obtenerReview(idReview);

                if (review == null || review.getIdLector() != idL) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().println(gson.toJson(Map.of("error", "No puedes eliminar esta review")));
                    return;
                }

                boolean exito = reviewDAO.eliminarReview(idReview);
                response.getWriter().println(gson.toJson(Map.of("exito", exito)));
                return;
            }

            // Crear o actualizar review
            String idReviewStr = request.getParameter("id_review");
            String idLibroStr = request.getParameter("id_libro");
            String valoracionStr = request.getParameter("valoracion");
            String resenia = request.getParameter("resenia");

            boolean exito = false;

            if (idReviewStr != null && !idReviewStr.isEmpty()) {
                int idR = Integer.parseInt(idReviewStr);
                int valoracion = Integer.parseInt(valoracionStr);
                exito = reviewDAO.actualizarReview(idR, valoracion, resenia);
            } else {
                int idLib = Integer.parseInt(idLibroStr);
                int valoracion = Integer.parseInt(valoracionStr);
                exito = reviewDAO.crearReview(idL, idLib, valoracion, resenia);
            }

            if (exito) {
                response.getWriter().println(gson.toJson(Map.of("mensaje", "Review guardada correctamente")));
            } else {
                response.getWriter().println(gson.toJson(Map.of("error", "No se pudo guardar la review")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println(gson.toJson(Map.of("error", "Datos incorrectos")));
        }
    }

}
