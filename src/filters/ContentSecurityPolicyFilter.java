package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class ContentSecurityPolicyFilter
 */
public class ContentSecurityPolicyFilter implements Filter {

    /**
     * Default constructor. 
     */
    public ContentSecurityPolicyFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    // Only allow iframes from the same domain
	    final String BLOCK_EXTERNAL_SUSPICIOUS_IFRAMES = "child-src 'self'";
	    
	    // Only allow AJAX call to our domain and to the API we use the get data for video games
	    final String BLOCK_EXTERNAL_SUSPICIOUS_IFRAMES_AJAX_CALLS = "connect-src 'self' www.giantbomb.com";
	    
	    // Add the Content-Security-Policy to the header
	    final HttpServletResponse hsr = (HttpServletResponse) response;
	    hsr.addHeader("Content-Security-Policy", BLOCK_EXTERNAL_SUSPICIOUS_IFRAMES + "; " + BLOCK_EXTERNAL_SUSPICIOUS_IFRAMES_AJAX_CALLS);
	    
	    // Send the request
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
