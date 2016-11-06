package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.PlayerDaoImpl;
import enums.PageTitle;
import enums.SessionData;
import model.Player;
import model.PlayerDao;
import utils.GiantBombUtils;
import utils.HTMLBuilder;

/**
 * Servlet implementation class SearchGameResults
 */
public class SearchGameResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private PlayerDao playerDao;
	private Player player;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchGameResult() {
        super();
        // TODO Auto-generated constructor stub
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
		// If session doesn't exists redirect to SigninSignup page
        HttpSession session = request.getSession(false);
        String playerUsername;
        String gameName = null;
        
        if (session == null) {
            response.sendRedirect(".");
        }
        else {
        	playerUsername = (String) session.getAttribute(SessionData.PLAYER_USERNAME.toString());
        	player = this.playerDao.getPlayer(playerUsername);
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
            out.println(HTMLBuilder.createTopMenu());
            out.println(HTMLBuilder.createTabsMenu());
            
            if (request.getParameter("game") == null) {
		        out.println("error");
		    } else {
		        gameName = request.getParameter("game");
		    }
            
            out.println("<div class=\"row\">");
	            out.println("<section class=\"col-xs-2 well\">");
		            out.println("<div id=\"wrapper\">");
						out.println("<div id=\"sidebar-wrapper\">");
							out.println("<ul class=\"sidebar-nav\">");
								out.println("<h4 class=\"sidebar-brand\">");
									out.println("Plateformes");
		                        out.println("</h4>");
		                        List<String> platforms = GiantBombUtils.getPlatformsList();
								for(int i=0; i<platforms.size(); i++){
									out.println("<li>");
										out.println("<a href=\"Platform?platform="+platforms.get(i)+"\">");
											out.println(platforms.get(i));
										out.println("</a>");
									out.println("</li>");
								}
							out.println("</ul>");
						out.println("</div>");
	    			out.println("</div>");
				out.println("</section>");
				
				out.println("<section class=\"col-xs-8 well\">");
					List<Map<String, String>> games = GiantBombUtils.getGames(gameName); 
					for(int i = 0; i < games.size(); i++){
						out.println("</br><div class=\"row\">");
				            out.println("<div class=\"col-xs-5 text-center\">");
				            	out.println("<img src="+games.get(i).get("image")+" width=\"150px\" height=\"150px\" alt=\"?\"/>");
				            out.println("</div>");
				            out.println("<div class=\"col-xs-7\">");
					            out.println("</br><div class=\"row\">");
					            	out.println("<B>Nom : </B>");
					            	if(games.get(i).get("name") != null  &&  games.get(i).get("name") != "null")
						            	out.println("<a href=\"Game?game="+games.get(i).get("name")+"\">");
											out.println(games.get(i).get("name"));
										out.println("</a>");
			            		out.println("</div>");
			            		out.println("<div class=\"row\">");
					            	out.println("<B>Date de sortie : </B>");
					            	if(games.get(i).get("original_release_date") != null  &&  games.get(i).get("original_release_date") != "null")
					            		out.println(games.get(i).get("original_release_date"));
			            		out.println("</div>");
			        		out.println("</div>");
		        		out.println("</div>");
					}
				out.println("</section>");
				
				out.println("<section class=\"col-xs-2 well\">");
		            out.println("<div id=\"wrapper\">");
						out.println("<div id=\"sidebar-wrapper pull-right\">");
							out.println("<ul class=\"sidebar-nav\">");
								out.println("<h4 class=\"sidebar-brand\">");
									out.println("Latest games added");
		                        out.println("</h4>");
		                        List<String> recentGames = GiantBombUtils.getMostRecentGames();
		                        for(int i=0; i<recentGames.size(); i++){
									out.println("<li>");
										out.println("<a href=\"Game?game="+recentGames.get(i)+"\">");
											out.println(recentGames.get(i));
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
        out.println("</body>");
        out.print("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
