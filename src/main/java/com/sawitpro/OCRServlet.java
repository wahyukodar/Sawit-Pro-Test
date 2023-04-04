package com.sawitpro;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class OCRServlet extends HttpServlet {
    private static final String RULE_LETTER = "o";

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
            String[] textLines = rawText.split(System.lineSeparator());
            for (String textLine : textLines) {
                if (!Utils.isCJK(textLine) && Utils.isAlphaNumeric(textLine)) {
                    String[] words = textLine.split("\\s+");
                    int c = 0;
                    for (String word : words) {
                        if (word.contains(RULE_LETTER)) {
                            System.out.print("\033[34m");
                            System.out.print(word);
                            System.out.print("\033[0m");
                        } else {
                            System.out.print(word);
                        }
                        if (c < words.length) {
                            System.out.print(" ");
                        }
                        c++;
                    }
                    System.out.println();
                } else {
                    System.out.println(textLine);
                }
            }
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
