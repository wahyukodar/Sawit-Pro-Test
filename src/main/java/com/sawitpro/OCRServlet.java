package com.sawitpro;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class OCRServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write("Only the post method is available");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String text = "success";
        GoogleService googleService = new GoogleService();
        response.setContentType("text/plain");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            googleService.getCredentials();
            googleService.uploadImageAsDocument(request.getParameter("filename"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(500);
            response.getWriter().write(e.getMessage());
            return;
        }
        response.setStatus(200);
        response.getWriter().write(text);
    }
}
