package com.example.demo;

import dao.AdministradorDAO;
import dao.LectorDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Administrador;
import models.Lector;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.sql.Date;
import java.util.UUID;

@WebServlet(name = "RegisterAdminServlet", value = "/registerAdmin")
public class RegisterAdminServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        try{
            request.getRequestDispatcher("registroAdmin.jsp").forward(request, response);
        }catch(Exception e){
            throw new ServletException(e);
        }



    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("passwordConfirm");
        String fecha = request.getParameter("fecha");
        LocalDate nuevaFecha = LocalDate.parse(fecha);
        Boolean contraNoCoincide = false;

        try{
            if(!password.equals(passwordConfirm)){
                contraNoCoincide = true;
                request.setAttribute("contraNoCoincide",contraNoCoincide);
            }

            String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12)); // 12 = factor de costo
            System.out.println("Hash generado: " + hashed);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaNacimiento = Date.valueOf(fecha);
            AdministradorDAO admindao = new AdministradorDAO();
            Administrador administrador = null;
            int ultimaId = admindao.obtenerUltimaID() + 1 ;
            administrador = admindao.existeAdmin(correo);
            Boolean existe = false;
            if(administrador != null){
                existe = true;
                request.setAttribute("existeAdmin",existe);
                request.getRequestDispatcher("registroAdmin.jsp").forward(request, response);
            }else{
                administrador = new Administrador(ultimaId,nombre,correo,fechaNacimiento,hashed);
                HttpSession newSession = request.getSession(true);
                newSession.setAttribute("authAdmin", administrador);
                newSession.setMaxInactiveInterval(30 * 60);
                String csrfToken = UUID.randomUUID().toString();
                newSession.setAttribute("csrfToken", csrfToken);
                response.sendRedirect(request.getContextPath() + "/dashboardAdmin");
                admindao.crearAdministradorDAO(nombre,fechaNacimiento,correo,hashed);
                return;
            }
            request.setAttribute("existeAdmin",existe);
        }catch(Exception e){
            throw new ServletException(e);
        }



    }
}
