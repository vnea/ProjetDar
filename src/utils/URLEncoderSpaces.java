package utils;


public class URLEncoderSpaces {
    
    public static String encode(String str) {        
        return str.replace(" ", "%20");
    }

}
