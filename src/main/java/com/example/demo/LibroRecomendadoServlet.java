package com.example.demo;

import com.google.gson.Gson;
import dao.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Autor;
import models.Genero;
import models.Libro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/librosRecomendados")
public class LibroRecomendadoServlet extends HttpServlet {
    private String message;
    private LibroGeneroDAO librogeneroDAO = new LibroGeneroDAO();
    private generoDAO generodao = new generoDAO();
    private autorDAO autordao = new autorDAO();
    private ReviewDAO reviewdao = new ReviewDAO();
    private LibroDAO librodao = new LibroDAO();
    private Gson gson = new Gson();


    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");



    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        try{
            int id_lector = Integer.parseInt(request.getParameter("id_lector"));
            List<Genero> generos = generodao.obtenerGenerosResenias(id_lector);
            List<Autor> autores = autordao.ObtenerAutoresResenias(id_lector);
            StringBuilder sb = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();

            System.out.println(id_lector);

            List<Integer> id_generos = new ArrayList<Integer>();
            generos.forEach(genero ->{
                id_generos.add(genero.getId_genero());
            });

            for (int i = 0; i < id_generos.size(); i++) {
                sb.append(id_generos.get(i));
                if (i < id_generos.size() - 1) {
                    sb.append(",");
                }
            }

            String generosIds = sb.toString();

            List<Integer> id_autores = new ArrayList<>();
            autores.forEach(autor ->{
                id_autores.add(autor.getId_autor());
            });

            for (int i = 0; i < id_autores.size(); i++) {
                sb1.append(id_autores.get(i));
                if (i < id_autores.size() - 1) {
                    sb1.append(",");
                }
            }

            String autoresIds = sb1.toString();

            System.out.println(generosIds);
            System.out.println(autoresIds);

            List<Libro> libros1 = librodao.obtenerLibrosRecomendados(autoresIds, generosIds);

            libros1.forEach(libro ->{
                System.out.println(libro.getTitulo());
            });

            String jsonLibros =  gson.toJson(libros1);
            response.getWriter().println(jsonLibros);

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public void destroy() {
    }
}