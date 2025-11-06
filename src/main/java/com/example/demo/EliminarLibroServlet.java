package com.example.demo;

import basedatos.conexion;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dao.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Autor;
import models.Genero;
import models.Libro;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@WebServlet(name = "EliminarLibroServlet", value = "/baja-libro-servlet")
public class EliminarLibroServlet extends HttpServlet {
    private String message;
    private Cloudinary cloudinary;
    private LibroGeneroDAO librogenerodao = new LibroGeneroDAO();
    private LibroAutorDAO libroautordao = new LibroAutorDAO();
    private ReviewDAO reviewdao = new ReviewDAO();
    private PrestamoDAO prestamodao = new PrestamoDAO();
    private LibroDAO librodao = new LibroDAO();
    private static Properties reader = new Properties();
    private static String CLOUD_NAME = "";
    private static String API_KEY = "";
    private static String API_SECRET = "";


    public void init() {
        cargarConfiguracionCloudinary();
    }


    private void cargarConfiguracionCloudinary() {
        try {
            InputStream input = conexion.class.getResourceAsStream("/api.properties");
            Properties reader = new Properties();
            reader.load(input);

            String CLOUD_NAME = reader.getProperty("CLOUD_NAME");
            String API_KEY = reader.getProperty("API_KEY");
            String API_SECRET = reader.getProperty("API_SECRET");


            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", CLOUD_NAME,
                    "api_key", API_KEY,
                    "api_secret", API_SECRET
            ));


        } catch (Exception e) {
            System.err.println("Error al cargar configuración de Cloudinary: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        HttpSession session = request.getSession();
        try{


            if(request.getParameter("id") == null){
                session.setAttribute("error", "No existe el libro con esa id");
                response.sendRedirect(request.getContextPath() + "/dashboardAdmin");
                return;
            }

            String id = request.getParameter("id");
            int id_libro = Integer.parseInt(id);

            if(!prestamodao.existenPrestamosDeLibro(id_libro).isEmpty()){
                session.setAttribute("error", "El libro a eliminar ya esta reservado");
                response.sendRedirect(request.getContextPath() + "/dashboardAdmin");
                return;
            }

            if(reviewdao.existeReviewDeLibro(id_libro) != null){
                session.setAttribute("error", "El libro a eliminar tiene resenias asociadas");
                response.sendRedirect(request.getContextPath() + "/dashboardAdmin");
                return;
            }

            InputStream input = conexion.class.getResourceAsStream("/api.properties");
            reader.load(input);
            CLOUD_NAME = reader.getProperty("CLOUD_NAME");
            API_KEY = reader.getProperty("API_KEY");
            API_SECRET = reader.getProperty("API_SECRET");

            List<Genero> generos = librogenerodao.listarGenerosDeLibro(id_libro);
            List<Autor> autores = libroautordao.listarAutoresDeUnLibro(id_libro);

            if(!generos.isEmpty()){
                librogenerodao.eliminarGenerosDeLibro(id_libro);
            }

            if(!autores.isEmpty()){
                libroautordao.eliminarAutoresDeLibro(id_libro);
            }



            Libro libro = librodao.buscarLibroPorID(id_libro);
            String imagenUrl = libro.getImagen_url();
            if (imagenUrl != null && !imagenUrl.isEmpty()) {
                boolean imagenEliminada = eliminarImagenCloudinary(imagenUrl);
                if (!imagenEliminada) {
                    System.out.println("Advertencia: No se pudo eliminar la imagen de Cloudinary");
                    // No detenemos el proceso aunque falle la eliminación de la imagen
                }
            }

            librodao.eliminarLibro(id_libro);
            response.sendRedirect(request.getContextPath() + "/dashboardAdmin");
            return;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void destroy() {
    }

    private boolean eliminarImagenCloudinary(String imageUrl) {
        try {
            String publicId = obtenerPublicId(imageUrl);
            if (publicId == null) {
                System.out.println("No se pudo extraer el public_id de la URL: " + imageUrl);
                return false;
            }

            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            String resultStatus = (String) result.get("result");
            boolean eliminado = "ok".equals(resultStatus);

            if (eliminado) {
                System.out.println("Imagen eliminada correctamente de Cloudinary");
            } else {
                System.out.println("No se pudo eliminar la imagen de Cloudinary. Result: " + resultStatus);
            }

            return eliminado;

        } catch (Exception e) {
            System.err.println("Error al eliminar imagen de Cloudinary: " + e.getMessage());
            return false;
        }
    }


    private String obtenerPublicId(String imageUrl) {
        try {
            if (imageUrl == null || imageUrl.isEmpty()) {
                return null;
            }

            String[] partes = imageUrl.split("/upload/");
            if (partes.length < 2) {
                return null;
            }

            String pathConVersion = partes[1];

            String[] pathPartes = pathConVersion.split("/");
            StringBuilder publicId = new StringBuilder();

            for (String parte : pathPartes) {
                if (!parte.startsWith("v") || !parte.matches("^v\\d+$")) {
                    if (publicId.length() > 0) {
                        publicId.append("/");
                    }
                    publicId.append(parte);
                }
            }

            String publicIdCompleto = publicId.toString();
            int ultimoPunto = publicIdCompleto.lastIndexOf(".");
            if (ultimoPunto > 0) {
                publicIdCompleto = publicIdCompleto.substring(0, ultimoPunto);
            }

            return publicIdCompleto;

        } catch (Exception e) {
            System.err.println("Error al extraer public_id de URL: " + e.getMessage());
            return null;
        }
    }


}