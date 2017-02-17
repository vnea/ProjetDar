package utils;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.http.HttpSession;

public class TokenUtils {
    public static final String CSRF_TOKEN = "csrf-token";
    private static SecureRandom random = new SecureRandom();

    public static String generateUniqueToken() {
        return new BigInteger(130, random).toString(32);
    }
    
    public static boolean isTokenValid(HttpSession session, String clientToken) {
        final String serverToken = (String) session.getAttribute(CSRF_TOKEN);
        
        // Check if the token has been generated and if the sent token by the client is the same
        return serverToken != null && session.getAttribute(CSRF_TOKEN).equals(clientToken);
    }
}
