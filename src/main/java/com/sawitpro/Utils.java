package com.sawitpro;

public class Utils {
    public static boolean containsHanScript(String s) {
        return s.codePoints().anyMatch(
                codepoint ->
                        Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
    }

    public static boolean isCJK(String str){
        int length = str.length();
        for (int i = 0; i < length; i++){
            char ch = str.charAt(i);
            Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);
            if (Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS.equals(block)||
                    Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS.equals(block)||
                    Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A.equals(block)){
                return true;
            }
        }
        return false;
    }

    public static boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z -_]+$";
        return s.matches(pattern);
    }
}
