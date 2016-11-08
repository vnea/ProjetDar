package utils;

public class StringUtils {

    public static String getStr(String str) {
        return str == null ? "" : str;
    }
    
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();   
    }

}
