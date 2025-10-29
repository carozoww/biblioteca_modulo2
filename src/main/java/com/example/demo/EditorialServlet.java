package com.example.demo;

import com.google.gson.Gson;
import dao.EditorialDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Editorial;

import java.io.IOException;
import java.util.List;

@WebServlet("/editoriales")
public class EditorialServlet extends HttpServlet {
    private String message;
    private EditorialDAO edao = new EditorialDAO();
    private Gson gson = new Gson();

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try{
            List<Editorial> editoriales = edao.listarEditorial();
            String jsonEditoriales =  gson.toJson(editoriales);
            response.getWriter().println(jsonEditoriales);



        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void destroy() {
    }
}