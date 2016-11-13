package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Player;
import models.PlayerDao;
import utils.HTMLBuilder;
import dao.PlayerDaoImpl;
import enums.PageTitle;
import enums.SessionData;

/**
 * Servlet implementation class MyGames
 */
public class MyGames extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private PlayerDao playerDao;
	private Player player;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyGames() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        playerDao = new PlayerDaoImpl();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        out.println(HTMLBuilder.createHeadTag(PageTitle.MY_GAMES));

        // Body
        out.println("<body>");
            // Menu connection
            String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
            player = this.playerDao.getPlayer(username);
            out.println(HTMLBuilder.createTopMenu());
            out.println(HTMLBuilder.createTabsMenu(username));
            
            out.println("<div class=\"container-fluid\">");
    			out.println("<form class=\"form\" method=\"post\">");
	    			out.println("<div class=\"row\">");
	    			
		                List<String> games = player.getGames();
	                
		                int j = games.size() % 4;
		                int k = 0;
		                out.println("<div class=\"col-xs-2 col-xs-offset-1\">");
		                for (int i = 0; i < games.size(); i++) {
		                	if (i!= 0 && (i % (games.size() / 4) == k)) {
		                		if (j > 0) {
		                			j--;
		                			k++;
			                		i++;
			                		out.println(checkbox(games.get(i)));
		                		}
								out.println("</div>");
								out.println("<div class=\"col-xs-2 col-xs-offset-1\">");
		                	}
		                	out.println(checkbox(games.get(i)));
		                }
		                out.println("</div>");	
					
					out.println("</div>");
					out.println("<div class=\"row\">");
    					out.println("<div class=\"col-xs-1 col-xs-offset-6\">");
        					out.println("<input type=\"submit\" name=\"submit\" value=\"Valider\">");
    					out.println("</div>");	
        			out.println("</div>");
                out.println("</form>");
            out.println("</div>");
        
            // Scripts
            out.println(HTMLBuilder.createScriptsTags());
        out.println("</body>");
        out.print("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// If session doesn't exists redirect to SigninSignup page
        HttpSession session = request.getSession(false);        
        if (session == null) {
            response.sendRedirect(".");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String playerUsername = (String) session.getAttribute(SessionData.PLAYER_USERNAME.toString());
    	player = this.playerDao.getPlayer(playerUsername);
    	
    	if (player != null) {
    		String[] games = request.getParameterValues("game");
    		player.setGames(games == null ? new ArrayList<String>() : Arrays.asList(games));
    		this.playerDao.update(player);
    		response.sendRedirect("MyGames");
    	}
	}
	
	/**
	 * @param buttonName
	 * @return
	 */
	private String checkbox(String buttonName) {
		String
		
		res = "<label class=\"checkbox white\"><input class=\"inputGender\" type=\"checkbox\" checked=\"checked\"" +
				"name=\"game\"" + " value=\"" + buttonName + "\">\n";
			res += "<a href=\"Game?game=" + buttonName + "\">\n";
				res += buttonName;
			res += "</a>\n";
		res += "</label>";
			     
        return res;
	}

}
