package com.example.demo;

import dao.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Autor;
import models.Genero;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "EliminarLibroServlet", value = "/baja-libro-servlet")
public class EliminarLibroServlet extends HttpServlet {
    private String message;
    private LibroGeneroDAO librogenerodao = new LibroGeneroDAO();
    private LibroAutorDAO libroautordao = new LibroAutorDAO();
    private ReviewDAO reviewdao = new ReviewDAO();
    private PrestamoDAO prestamodao = new PrestamoDAO();
    private LibroDAO librodao = new LibroDAO();

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try{


            if(request.getParameter("id") == null){
                response.sendRedirect(request.getContextPath() + "/dashboardAdmin");
                return;
            }

            String id = request.getParameter("id");
            int id_libro = Integer.parseInt(id);

            if(!prestamodao.existenPrestamosDeLibro(id_libro).isEmpty()){
                System.out.println("El libro a eliminar ya esta reservado");
                response.sendRedirect(request.getContextPath() + "/dashboardAdmin");
                return;
            }

            if(reviewdao.existeReviewDeLibro(id_libro) != null){
                System.out.println("El libro a eliminar tiene resenias asociadas");
                response.sendRedirect(request.getContextPath() + "/dashboardAdmin");
                return;
            }

            List<Genero> generos = librogenerodao.listarGenerosDeLibro(id_libro);
            List<Autor> autores = libroautordao.listarAutoresDeUnLibro(id_libro);

            if(!generos.isEmpty()){
                librogenerodao.eliminarGenerosDeLibro(id_libro);
            }

            if(!autores.isEmpty()){
                libroautordao.eliminarAutoresDeLibro(id_libro);
            }

            LibroDAO librodao = new LibroDAO();
            librodao.eliminarLibro(id_libro);
            response.sendRedirect(request.getContextPath() + "/dashboardAdmin");
            return;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void destroy() {
    }
}