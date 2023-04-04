package com.sawitpro;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Utils {
    public static boolean containsHanScript(String s) {
        return s.codePoints().anyMatch(
                codepoint ->
                        Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
    }

    public static boolean isCJK(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char ch = str.charAt(i);
            Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);
            if (Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS.equals(block) ||
                    Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS.equals(block) ||
                    Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A.equals(block)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAlphaNumeric(String s) {
        String pattern = "^[a-zA-Z -_]+$";
        return s.matches(pattern);
    }

    public static LanguageModel printTerminalAndGetHTMLText(String rawText) {
        LanguageModel result = new LanguageModel();
        StringBuilder htmlTextChinese = new StringBuilder();
        StringBuilder htmlTextEnglish = new StringBuilder();
        String[] textLines = rawText.split(System.lineSeparator());
        for (String textLine : textLines) {
            if (!Utils.isAlphaNumeric(textLine) && Utils.containsHanScript(textLine)) {
                System.out.println(textLine);
                htmlTextChinese.append("<p>").append(textLine).append("</p>");
            } else {
                String[] words = textLine.split("\\s+");
                int c = 0;
                for (String word : words) {
                    if (c == 0) {
                        htmlTextEnglish.append("<p>");
                    }

                    if (word.contains("o")) {
                        System.out.print("\033[34m");
                        System.out.print(word);
                        System.out.print("\033[0m");
                        htmlTextEnglish.append("<font color=\"blue\">").append(word).append("</font>");
                    } else {
                        System.out.print(word);
                        htmlTextEnglish.append(word);
                    }
                    if (c < words.length) {
                        System.out.print(" ");
                        htmlTextEnglish.append(" ");
                    }

                    if (c == words.length) {
                        htmlTextEnglish.append("</p>");
                    }
                    c++;
                }
                System.out.println();
            }
        }
        result.setChinese(htmlTextChinese.toString());
        result.setEnglish(htmlTextEnglish.toString());
        return result;
    }

    public static void writeHTMLFile(String htmlText, String fileName) {
        FileWriter fWriter;
        BufferedWriter writer;
        try {
            fWriter = new FileWriter(fileName);
            writer = new BufferedWriter(fWriter);
            writer.write(htmlText);
            writer.newLine();
            writer.close();
        } catch (Exception e) {
            System.err.println("Failed write file!");
        }
    }
}
