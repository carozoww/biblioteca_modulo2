package com.example.demo;

import dao.LectorDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Lector;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "loginLectorServlet", value = "/login-lector")
public class LoginLectorServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try{

            request.getRequestDispatcher("login.jsp").forward(request, response);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        String ced = request.getParameter("cedula");
        String contrasenia = request.getParameter("passw");

        Boolean existeCedula = false;
        try{
            LectorDAO lectordao = new  LectorDAO();
            existeCedula = !(lectordao.existeLector(ced).isEmpty());
            List<Lector> lector = lectordao.existeLector(ced);
            if(existeCedula){
                String contradb;
                contradb = lector.get(0).getContrasenia();
                boolean ok = BCrypt.checkpw(contrasenia, contradb);
                if(ok){
                    HttpSession oldSession = request.getSession(false);
                    if (oldSession != null) {
                        oldSession.invalidate();
                    }
                    HttpSession newSession = request.getSession(true);
                    newSession.setAttribute("authUser", lector.get(0));
                    newSession.setMaxInactiveInterval(30 * 60);
                    String csrfToken = UUID.randomUUID().toString();
                    newSession.setAttribute("csrfToken", csrfToken);
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                    return;
                }else{


                    request.setAttribute("cedulaRegistrada",existeCedula);
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            }else{
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }


            request.setAttribute("cedulaRegistrada",existeCedula);

        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }


    public void destroy() {
    }
}