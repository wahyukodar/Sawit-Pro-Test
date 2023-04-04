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
        GoogleService googleService = new GoogleService();
        response.setContentType("text/plain");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            googleService.getCredentials();
            String fileName = request.getParameter("filename");
            String fileId = googleService.uploadImageAsDocument(fileName);
            String rawText = googleService.downloadExtractedText(fileId);
            googleService.deleteFile(fileId);
            rawText = rawText.replace("________________\n\n", "");
            LanguageModel htmlText = Utils.printTerminalAndGetHTMLText(rawText);
            Utils.writeHTMLFile(htmlText.getChinese(), fileName.concat("-chinese.html"));
            Utils.writeHTMLFile(htmlText.getEnglish(), fileName.concat("-english.html"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(500);
            response.getWriter().write(e.getMessage());
            return;
        }
        response.setStatus(200);
        response.getWriter().write("success");
    }
}
