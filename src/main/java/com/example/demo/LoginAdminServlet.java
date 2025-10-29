package com.example.demo;

import dao.AdministradorDAO;
import dao.LectorDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Administrador;
import models.Lector;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "loginAdminServlet", value = "/login-admin")
public class LoginAdminServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try{

            request.getRequestDispatcher("loginAdmin.jsp").forward(request, response);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Boolean existeCorreo = false;
        try{
            String correo = request.getParameter("correo");
            String contrasenia = request.getParameter("password");


            AdministradorDAO admindao = new AdministradorDAO();
            Administrador admin = admindao.existeAdmin(correo);
            if(admin != null){
                existeCorreo = true;
                String contra2 = admin.getContrasenia();
                boolean ok = BCrypt.checkpw(contrasenia, contra2);
                if(ok){
                    HttpSession newSession = request.getSession(true);
                    newSession.setAttribute("authAdmin", admin);
                    newSession.setMaxInactiveInterval(30 * 60);
                    String csrfToken = UUID.randomUUID().toString();
                    newSession.setAttribute("csrfToken", csrfToken);
                    response.sendRedirect(request.getContextPath() + "/dashboardAdmin");
                    return;
                }else{
                    request.setAttribute("existeCorreo", existeCorreo);
                    request.getRequestDispatcher("loginAdmin.jsp").forward(request, response);
                }
            }
            request.setAttribute("existeCorreo", existeCorreo);
            request.getRequestDispatcher("loginAdmin.jsp").forward(request, response);

        }catch(Exception e){
            throw new RuntimeException(e);
        }


    }


    public void destroy() {
    }
}