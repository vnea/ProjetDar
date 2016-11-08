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

import models.Player;
import models.PlayerDao;
import utils.GiantBombUtils;
import utils.HTMLBuilder;
import dao.PlayerDaoImpl;
import enums.PageTitle;
import enums.SessionData;


/**
 * Servlet implementation class Game
 */
public class Game extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private PlayerDao playerDao;
	private Player player;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Game() {
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
									out.println("Parties pour ce jeu");
		                        out.println("</h4>");
		                        // A completer
							out.println("</ul>");
						out.println("</div>");
	    			out.println("</div>");
				out.println("</section>");
				
				out.println("<section class=\"col-xs-8 well\">");
					Map<String, String> gameInfos = GiantBombUtils.getGameInfos(gameName);
					out.println("<div class=\"row\">");
			            out.println("<div class=\"col-xs-5 text-center\">");
			            	out.println("<img src="+gameInfos.get("image")+" width=\"250px\" height=\"200px\" alt=\"?\"/>");
			            out.println("</div>");
			            out.println("<div class=\"col-xs-7\">");
				            out.println("<div class=\"row\">");
				            	out.println("<B>Nom : </B>");
				            	if(gameInfos.get("name") != null  &&  gameInfos.get("name") != "null")
				            		out.println(gameInfos.get("name"));
		            		out.println("</div>");
				            out.println("<div class=\"row\">");
				            	out.println("<B>Note : </B>");
				            	if(gameInfos.get("original_game_rating") != null  &&  gameInfos.get("original_game_rating") != "null")
				            		out.println(gameInfos.get("original_game_rating"));
		            		out.println("</div>");
			            	out.println("<div class=\"row\">");
			            		out.println("<B>Description : </B>");
			            		if(gameInfos.get("deck") != null  &&  gameInfos.get("deck") != "null")
			            			out.println(gameInfos.get("deck"));
		            		out.println("</div>");
		            		out.println("<div class=\"row\">");
			            		out.println("<B>Date de sortie : </B>");
			            		if(gameInfos.get("original_release_date") != null  &&  gameInfos.get("original_release_date") != "null")
			            			out.println(gameInfos.get("original_release_date"));
			            	out.println("</div>");
			            	out.println("<div class=\"row\">");
			            		out.println("<B>Genres : </B>");
			            		if(gameInfos.get("genre") != null  &&  gameInfos.get("genres") != "null")
			            			out.println(gameInfos.get("genres"));
			            	out.println("</div>");
			            	out.println("<div class=\"row\">");
			            		out.println("<B>Platformes : </B>");
			            		if(gameInfos.get("platforms") != null  &&  gameInfos.get("platforms") != "null")
			            			out.println(gameInfos.get("platforms"));
			            	out.println("</div>");
				            	out.println("<div class=\"row\">");
			            		out.println("<B>Développeurs : </B>");
			            		if(gameInfos.get("developers") != null  &&  gameInfos.get("developers") != "null")
			            			out.println(gameInfos.get("developers"));
			            	out.println("</div>");
			            	out.println("<div class=\"row\">");
			            		out.println("<B>Editeurs : </B>");
			            		if(gameInfos.get("publishers") != null  &&  gameInfos.get("publishers") != "null")
			            			out.println(gameInfos.get("publishers"));
			            	out.println("</div>");
			            	out.println("<div class=\"row\">");
			            		out.println("<B>Jeux similaires : </B>");
			            		if(gameInfos.get("similar_games") != null  &&  gameInfos.get("similar_games") != "null")
			            			out.println(gameInfos.get("similar_games"));
			            	out.println("</div>");
			            out.println("</div>");  
					out.println("</div>");
					
					out.println("</br><div class=\"row\">");
		            	out.println("<div class=\"col-xs-12 text-center\">");
	            			out.println("<button type=\"button\" class=\"btn btn-primary\">Ajouter à mes jeux</button>");
		            	out.println("</div>");
	            	out.println("</div></br>");
	            	
	            	out.println("<div class=\"row\">");
		            	out.println("<div class=\"col-xs-12\">");
		            		out.println("<B>Description : </B>");
		            		if(gameInfos.get("description") != null  &&  gameInfos.get("description") != "null")
		            			out.println(gameInfos.get("description"));
		            	out.println("</div>");
	            	out.println("</div>");
	            	
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
