package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Player;
import models.PlayerDao;
import utils.HTMLBuilder;
import utils.StringUtils;
import dao.PlayerDaoImpl;
import enums.PageTitle;
import enums.SessionData;

/**
 * Servlet implementation class SearchUserResult
 */
public class SearchUserResult extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String PARAM_USER = "user";
	
	private PlayerDao playerDao;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchUserResult() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
        playerDao = new PlayerDaoImpl();

        super.init();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false) == null) {
            response.sendRedirect(".");
            return;
        }
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // DOCTYPE + html + head
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"fr\">");
        out.println(HTMLBuilder.createHeadTag(PageTitle.HOME));

        // Body
        out.println("<body>");
            // Menu connection
            String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
            out.println(HTMLBuilder.createTopMenu());
            out.println(HTMLBuilder.createTabsMenu(username));
            
            
            out.println("<div class=\"container well\">");
                String paramUser = StringUtils.getStr(request.getParameter(PARAM_USER));

                // No user specified
                if (paramUser.isEmpty()) {
                    out.println("<p class=\"errorMessage\">Le nom de l'utilisateur n'a pas été spécifié.<p>");
                }
                else {
                    List<Player> users = playerDao.getPlayerStartWith(paramUser, username);
                    // No users found
                    if (users == null) {
                        out.println("<p>Aucun résultat.<p>");
                    }
                    else {
                        out.println("<h2>" + users.size() + " utilisateur(s) trouvé(s).</h2>");

                        // Print users
                        for (Player user : users) {
                            out.println("<a href=\"Profile?user=" + user.getUsername() + "\">" + user.getUsername() + "<a><br>\n");
                        }
                    } 
                }
            
            out.println("</div>");
            // Scripts
            out.println(HTMLBuilder.createScriptsTags());
        out.println("</body>");
        out.print("</html>");
	}

}
