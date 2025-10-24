package com.example.demo;

import dao.LibroAutorDAO;
import dao.LibroDAO;
import dao.PrestamoDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Autor;
import models.Lector;
import models.Libro;
import models.Prestamo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PrestamoServlet", value = "/prestamos")
public class PrestamoServlet extends HttpServlet {

    private PrestamoDAO prestamoDAO = new PrestamoDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        try {
            String accion = request.getParameter("accion");
            HttpSession sesion = request.getSession();
            Integer idLector = (Integer) sesion.getAttribute("idLector");

            if (idLector == null) {
                Lector lector = (Lector) sesion.getAttribute("authUser");
                if (lector != null) {
                    idLector = lector.getID();
                    sesion.setAttribute("idLector", idLector);
                }
            }

            switch (accion) {
                case "listar": // mostrar los prestamos del lector
                    List<Prestamo> prestamos = prestamoDAO.listarPrestamosPorLector(idLector);
                    request.setAttribute("prestamos", prestamos);
                    request.getRequestDispatcher("misPrestamos.jsp").forward(request, response);
                    break;

                case "catalogo": // mostrar todos los libros con disponibilidad
                    LibroDAO libroDAO = new LibroDAO();
                    List<models.Libro> libros = libroDAO.listarLibros(); // todos los libros
                    List<String> estados = new ArrayList<>();

                    //  ver si el lector ya tiene un prestamo activo
                    boolean tienePrestamo = prestamoDAO.prestamoActivoPorLector(idLector);

                    // ver la disponibilidad de cada libro
                    for (Libro libro : libros) {
                        boolean activo = prestamoDAO.prestamoActivoPorLibro(libro.getIdLibro());
                        if (activo) {
                            estados.add("NO DISPONIBLE");
                        } else {
                            estados.add("DISPONIBLE");
                        }
                    }

                    //  Pasamos la info al JSP
                    request.setAttribute("libros", libros);
                    request.setAttribute("estados", estados);
                    request.setAttribute("tienePrestamo", tienePrestamo);
                    request.getRequestDispatcher("libros.jsp").forward(request, response);
                    break;
                case "detalle": // mostrar detalle de un libro
                    int idLibro = Integer.parseInt(request.getParameter("id"));
                    Libro libro = new LibroDAO().buscarPorId(idLibro);
                    boolean tienePrestamos = prestamoDAO.prestamoActivoPorLector(idLector);
                    boolean disponible = !prestamoDAO.prestamoActivoPorLector(idLector);

                    LibroAutorDAO libroAutorDAO = new LibroAutorDAO();
                    List<Autor> autores = libroAutorDAO.listarAutoresDeUnLibro(idLibro);

                    request.setAttribute("libro", libro);
                    request.setAttribute("estado", disponible ? "DISPONIBLE" : "NO DISPONIBLE");
                    request.setAttribute("tienePrestamo", tienePrestamos);
                    request.setAttribute("autores", autores);
                    request.getRequestDispatcher("detalleLibro.jsp").forward(request, response);
                    break;
                default:
                    response.sendRedirect("libros.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String accion = request.getParameter("accion");
            HttpSession sesion = request.getSession();
            Integer idLector = (Integer) sesion.getAttribute("idLector");

            boolean exito = false;

            if ("reservar".equals(accion)) {
                int idLibro = Integer.parseInt(request.getParameter("idLibro"));

                // 🔍 verificar si el lector ya tiene un prestamo activo
                boolean tienePrestamoActivo = prestamoDAO.prestamoActivoPorLector(idLector);

                if (tienePrestamoActivo) {
                    // ya tiene uno, no permitir nuevo prestamo
                    response.sendRedirect("prestamos?accion=listar&msg=ya_tiene_prestamo");
                    return;
                }

                // crear prestamo en estado PENDIENTE
                prestamoDAO.crearPrestamo(idLector, idLibro, LocalDateTime.now(), "PENDIENTE");
                exito = true;

            } else if ("cancelar".equals(accion)) {
                int idPrestamo = Integer.parseInt(request.getParameter("idPrestamo"));
                // finalizar prestamo (cambia fecha de devolucion y estado en la tabla prestamo)
                prestamoDAO.finalizarPrestamo(idPrestamo, LocalDateTime.now());
                exito = true;
            }

            if (exito) {
                response.sendRedirect("prestamos?accion=listar&msg=ok");
            } else {
                response.sendRedirect("prestamos?accion=listar&msg=error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

}