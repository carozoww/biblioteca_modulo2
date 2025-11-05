package com.example.demo;

import basedatos.conexion;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.ReservaDAO;
import dao.SalaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import models.Sala;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@WebServlet(name = "AdministradorSalaServlet", value = "/administradorSalas")
@MultipartConfig(
        maxFileSize = 10 * 1024 * 1024,      // 10MB máximo
        maxRequestSize = 10 * 1024 * 1024,   // 10MB máximo
        fileSizeThreshold = 1024 * 1024      // 1MB en memoria
)
public class AdministradorSalaServlet extends HttpServlet {
    SalaDAO salaDAO = new SalaDAO();
    ReservaDAO reservaDAO = new ReservaDAO();
    private static Properties reader = new Properties();
    private static String CLOUD_NAME = "";
    private static String API_KEY = "";
    private static String API_SECRET = "";

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
                JsonObject jsonResponse = new JsonObject();
                PrintWriter out = response.getWriter();

                int numero = Integer.parseInt(request.getParameter("numero-sala"));
                String ubicacion = request.getParameter("ubicacion");
                boolean habilitada = Boolean.parseBoolean(request.getParameter("habilitada"));

                int personas = Integer.parseInt(request.getParameter("max-personas"));
                List<Sala> salas = salaDAO.listarSalas();

                for (Sala s : salas) {
                    if (s.getNumeroSala() == numero) {
                        request.getSession().setAttribute("mensaje", "Error: El numero de sala ya existe.");
                        response.sendRedirect("administradorSalas");
                        return;
                    }
                }

                InputStream input = conexion.class.getResourceAsStream("/api.properties");
                reader.load(input);
                CLOUD_NAME = reader.getProperty("CLOUD_NAME");
                API_KEY = reader.getProperty("API_KEY");
                API_SECRET = reader.getProperty("API_SECRET");

                Part filePart = request.getPart("image");

                if (filePart == null || filePart.getSize() == 0) {
                    request.getSession().setAttribute("mensaje", "Error: No se seleccionó ninguna imagen");
                    return;
                }

                String imageUrl = uploadToCloudinary(filePart);
                salaDAO.crearSala(numero, ubicacion, personas, imageUrl, habilitada);

                request.getSession().setAttribute("mensaje", "sala agregada");
            } else if ("editar".equals(accion)) {

                int id = Integer.parseInt(request.getParameter("id-sala"));
                String imagen = request.getParameter("imagen-anterior");
                int numero = Integer.parseInt(request.getParameter("numero-sala"));
                String ubicacion = request.getParameter("ubicacion");
                int personas = Integer.parseInt(request.getParameter("max-personas"));
                boolean habilitada = Boolean.parseBoolean(request.getParameter("habilitada"));

                List<Sala> salas = salaDAO.listarSalas();
                for (Sala s : salas) {
                    if (s.getNumeroSala() == numero && s.getIdSala() != id) {
                        request.getSession().setAttribute("mensaje", "Error: El numero de sala ya existe.");
                        response.sendRedirect("administradorSalas");
                        return;
                    }
                }
                InputStream input = conexion.class.getResourceAsStream("/api.properties");
                reader.load(input);
                CLOUD_NAME = reader.getProperty("CLOUD_NAME");
                API_KEY = reader.getProperty("API_KEY");
                API_SECRET = reader.getProperty("API_SECRET");

                Part filePart = request.getPart("image");
                String imageUrl = "";
                if (filePart == null || filePart.getSize() == 0) {
                    imageUrl = imagen;
                } else {
                    imageUrl = uploadToCloudinary(filePart);
                }

                salaDAO.editarSala(id, numero, ubicacion, personas, imageUrl, habilitada);
                request.getSession().setAttribute("mensaje", "Sala editada");
            } else if ("eliminar".equals(accion)) {

                int id = Integer.parseInt(request.getParameter("eliminar-sala"));
                if (reservaDAO.existeReservaParaSala(id)) {
                    request.getSession().setAttribute("mensaje", "Existen reservas realizadas para la sala. No se puede elimiar.");
                }else{
                    salaDAO.borrarSala(id);
                    request.getSession().setAttribute("mensaje", "Sala borrada");
                }

            }
            response.sendRedirect("administradorSalas");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
                "folder", "salas",
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