package com.example.demo;

import dao.LectorDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Lector;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/editarPerfil")
public class EditarPerfilServlet extends HttpServlet {

    private LectorDAO lectorDAO = new LectorDAO();

    // Muestra los datos actuales del lector
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Obtener la cédula desde la URL
            String cedula = request.getParameter("cedula");

            if (cedula == null || cedula.isEmpty()) {
                response.getWriter().println("Cédula no especificada.");
                return;
            }

            // Buscar lector por cédula
           //Lector lector = lectorDAO.buscarPorCedula(Integer.parseInt(cedula));

            List<Lector> lectores = lectorDAO.existeLector(cedula);
            if (lectores.isEmpty()) {
                response.getWriter().println("No se encontró el lector con cédula: " + cedula);
                return;
            }

            Lector lectorExistente = lectores.get(0); // Tomamos el primer (y único) lector

            // Enviar el lector al JSP
            request.setAttribute("lector", lectorExistente);
            RequestDispatcher rd = request.getRequestDispatcher("editarPerfil.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al cargar el perfil", e);
        }
    }

    // Procesa la edición del perfil
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String cedula = request.getParameter("cedula");
            String nombre = request.getParameter("nombre");
            String telefono = request.getParameter("telefono");
            String direccion = request.getParameter("direccion");
            String correo = request.getParameter("correo");
            String contrasenia = request.getParameter("contrasenia");
            LocalDate fechaNac = LocalDate.parse(request.getParameter("fechaNac"));


            /*Lector lectorExistente = lectorDAO.buscarPorCedula(Integer.parseInt(cedula));*/

            List<Lector> lectores = lectorDAO.existeLector(cedula);
            if (lectores.isEmpty()) {
                response.getWriter().println("No se encontró el lector con cédula: " + cedula);
                return;
            }
            Lector lectorExistente = lectores.get(0); // Tomamos el primer (y único) lector

            // 🔒 Manejo correcto de contraseñas
            if (contrasenia == null || contrasenia.isEmpty()) {
                contrasenia = lectorExistente.getContrasenia(); // conserva la actual (hash)
            } else {
                contrasenia = org.mindrot.jbcrypt.BCrypt.hashpw(contrasenia, org.mindrot.jbcrypt.BCrypt.gensalt());
            }

            // Actualizar los datos
            lectorDAO.editarLector(
                    lectorExistente.getID(), // usamos el ID interno
                    nombre,
                    cedula,
                    telefono,
                    direccion,
                    true,
                    fechaNac,
                    true,
                    correo,
                    contrasenia
            );
            // Volvemos a obtener el lector actualizado
            Lector lectorActualizado = lectorDAO.buscarPorId(lectorExistente.getID());

            // 🔁 Actualizamos el atributo en sesión
            HttpSession sesion = request.getSession();
            sesion.setAttribute("authUser", lectorActualizado);

            // También lo mandamos a la vista por si querés usar request
            request.setAttribute("lector", lectorActualizado);
            // Redirigir nuevamente al perfil actualizado
            response.sendRedirect("perfil?cedula=" + cedula);

        } catch (Exception e) {
            throw new ServletException("Error al editar perfil", e);
        }
    }
}
