package utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ResultRequester {
    
    public static String resultRequest(String requestURL, String userAgent, String accept) {
        String responseBody = null; 
        
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(requestURL);
            httpget.setHeader(HttpHeaders.USER_AGENT, userAgent);
            httpget.setHeader(HttpHeaders.ACCEPT, accept);
    
            // Custom Response Handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    // Success
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    }
                    // Fail
                    else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            
            responseBody = httpclient.execute(httpget, responseHandler); 
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        
        return responseBody;
    }
    
}
