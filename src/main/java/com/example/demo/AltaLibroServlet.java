package com.example.demo;

import basedatos.conexion;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.JsonObject;
import dao.LibroDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Map;
import java.util.Properties;

@WebServlet(name = "AltaLibroServlet", value = "/alta-libro-servlet")
@MultipartConfig(
        maxFileSize = 10 * 1024 * 1024,      // 10MB máximo
        maxRequestSize = 10 * 1024 * 1024,   // 10MB máximo
        fileSizeThreshold = 1024 * 1024      // 1MB en memoria
)
public class AltaLibroServlet extends HttpServlet {
    private String message;
    private static Properties reader = new Properties();
    private static String CLOUD_NAME = "";
    private static String API_KEY = "";
    private static String API_SECRET = "";

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try{

            request.getRequestDispatcher("altaLibro.jsp").forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();


        String titulo = request.getParameter("titulo");
        String isbn = request.getParameter("isbn");
        String fecha = request.getParameter("fecha");
        int editorial = Integer.parseInt(request.getParameter("editorialid"));
        String sinopsis = request.getParameter("sinopsis");
        int numpaginas = Integer.parseInt(request.getParameter("numpaginas"));
        Date fechaPublicacion = Date.valueOf(fecha);



        try{
            InputStream input = conexion.class.getResourceAsStream("/api.properties");
            reader.load(input);
            CLOUD_NAME = reader.getProperty("CLOUD_NAME");
            API_KEY = reader.getProperty("API_KEY");
            API_SECRET = reader.getProperty("API_SECRET");

            Part filePart = request.getPart("image");

            if (filePart == null || filePart.getSize() == 0) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "No se seleccionó ninguna imagen");
                out.print(jsonResponse.toString());
                return;
            }



            String imageUrl = uploadToCloudinary(filePart);

            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("message", "Libro e imagen guardados exitosamente");
            jsonResponse.addProperty("imageUrl", imageUrl);

            LibroDAO librodao = new LibroDAO();
            librodao.crearLibroConImagen(titulo,isbn,fechaPublicacion,editorial,sinopsis,numpaginas,imageUrl);
            out.print(jsonResponse.toString());
        }catch(Exception e){
            throw new RuntimeException(e);
        }


    }

    public void destroy() {
    }


    private String uploadToCloudinary(Part filePart) throws Exception {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET,
                "secure", true
        ));

        byte[] fileBytes = filePart.getInputStream().readAllBytes();

        Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap(
                "folder", "libros",
                "resource_type", "image",
                "use_filename", true,
                "unique_filename", true,
                "overwrite", false
        ));

        String secureUrl = (String) uploadResult.get("secure_url");
        System.out.println("URL de Cloudinary: " + secureUrl);

        return secureUrl;
    }
}