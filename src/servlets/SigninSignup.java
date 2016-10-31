package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import enums.PageTitle;
import utils.HTMLBuilder;

/**
 * Servlet implementation class Home
 */
public class SigninSignup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SigninSignup() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    
	    // DOCTYPE + html + head
	    out.println("<!DOCTYPE html>");
	    out.println("<html lang=\"fr\">");
        out.println(HTMLBuilder.createHeadTag(PageTitle.HOME));

        // Body
	    out.println("<body>");
	        // Menu connexion
            out.println(HTMLBuilder.createTopMenuConnexion());

            /**
             * BEGIN OF MAIN CONTENT
             */
            
            
            
            /**
             * END OF MAIN CONTENT
             */
            
            // Scripts
	        out.println(HTMLBuilder.createScriptsTags());
	    out.println("</body>");
	    out.print("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
