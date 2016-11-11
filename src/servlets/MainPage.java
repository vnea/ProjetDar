package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utils.GiantBombUtils;
import utils.GoogleMapsUtils;
import utils.HTMLBuilder;
import comparators.ComparatorIgnoreCase;
import enums.PageTitle;

/**
 * Servlet implementation class MainPage
 */
public class MainPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//private PlayerDao playerDao;
	//private Player player;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainPage() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
        //playerDao = new PlayerDaoImpl();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// If session doesn't exists redirect to SigninSignup page
        HttpSession session = request.getSession(false);
        //String playerUsername;
        
        if (session == null) {
            response.sendRedirect(".");
        }
        else {
            request.setCharacterEncoding("UTF-8");

        	//playerUsername = (String) session.getAttribute(SessionData.PLAYER_USERNAME.toString());
        	//player = this.playerDao.getPlayer(playerUsername);
        }
        
		response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // DOCTYPE + html + head
        out.println("<!DOCTYPE html class=\"max-height\">");
        out.println("<html lang=\"fr\">");
        out.println(HTMLBuilder.createHeadTag(PageTitle.HOME));

        // Body
        out.println("<body class=\"max-height\">");
            // Top menus
            out.println(HTMLBuilder.createTopMenu());
            out.println(HTMLBuilder.createTabsMenu());
            
            out.println("<div class=\"row\">");
            
                // Show platforms
	            out.println("<section class=\"col-xs-2 well\">");
		            out.println("<div id=\"wrapper\">");
						out.println("<div id=\"sidebar-wrapper\">");
							out.println("<ul class=\"sidebar-nav\">");
								out.println("<h4 class=\"sidebar-brand\">");
									out.println("Plateformes");
		                        out.println("</h4>");
		                        List<String> platforms = GiantBombUtils.getPlatformsList();
		                        platforms.sort(new ComparatorIgnoreCase());
								for (String platform : platforms) {
									out.println("<li>");
										out.println("<a href=\"Platform?platform=" + platform + "\">");
											out.println(platform);
										out.println("</a>");
									out.println("</li>");
								}
							out.println("</ul>");
						out.println("</div>");
	    			out.println("</div>");
				out.println("</section>");
				
				out.println("<div class=\"col-xs-8 well\">");
                    out.print("<div id=\"gmaps\" style=\"height:100%; width: 100%; position: relative;\">");
                    out.println("</div>");
				out.println("</div>");

				GoogleMapsUtils.geocode("21 rue de draveil juvisy 91260");

				// Show recent games
				out.println("<section class=\"col-xs-2 well\">");
		            out.println("<div id=\"wrapper\">");
						out.println("<div id=\"sidebar-wrapper pull-right\">");
							out.println("<ul class=\"sidebar-nav\">");
								out.println("<h4 class=\"sidebar-brand\">");
									out.println("Latest games added");
		                        out.println("</h4>");
		                        List<String> recentGames = GiantBombUtils.getMostRecentGames();
		                        for (String recentGame : recentGames) {
									out.println("<li>");
										out.println("<a href=\"Game?game=" + recentGame + "\">");
											out.println(recentGame);
										out.println("</a>");
									out.println("</li>");
								}
							out.println("</ul>");
		    			out.println("</div>");
					out.println("</div>");
				out.println("</section>");
									
			out.println("</div>");
			// Scripts
            out.println(HTMLBuilder.createScriptsTags());
            
            // Google maps
            out.println("<script src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyC5U4UKF009_yNlDjGO9X3JEoE9QsADw9g\"></script>\n");
            out.println("<script src=\"assets/js/main-page.js\"></script>\n");
        out.println("</body>");
        out.print("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
