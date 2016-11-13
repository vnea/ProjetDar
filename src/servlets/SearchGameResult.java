package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Player;
import models.PlayerDao;
import comparators.ComparatorIgnoreCase;
import dao.PlayerDaoImpl;
import utils.GiantBombUtils;
import utils.HTMLBuilder;
import utils.StringUtils;
import enums.PageTitle;
import enums.SessionData;

/**
 * Servlet implementation class SearchGameResults
 */
public class SearchGameResult extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String PARAM_GAME = "game";
	
	private PlayerDao playerDao;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchGameResult() {
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
            
            Player currentPlayer = playerDao.getPlayer(username);

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
                                    if (currentPlayer.getPlatforms().contains(platform)) {
                                        out.println("<img src=\"assets/images/star.png\">");
                                    }
                                }
                            out.println("</ul>");
                        out.println("</div>");
                    out.println("</div>");
                out.println("</section>");
                
                
                // Show games
                out.println("<section class=\"col-xs-8 well\">");
                    String gameName = StringUtils.getStr(request.getParameter(PARAM_GAME));
                    // Game not set
                    if (gameName.isEmpty()) {
                        out.println("<p class=\"errorMessage\">Le jeu n'a pas été spécifié.<p>");
                    }
                    else {
                        List<Map<String, String>> games = GiantBombUtils.getGames(gameName);
                        // No games found
                        if (games.isEmpty()) {
                            out.println("<p>Aucun résultat.<p>");
                        }
                        else {
                            for (int i = 0; i < games.size(); i++) {
                                out.println("</br><div class=\"row\">");
                                    out.println("<div class=\"col-xs-5 text-center\">");
                                        out.println("<img src=" + games.get(i).get("image") + " width=\"150px\" height=\"150px\" alt=\"?\"/>");
                                    out.println("</div>");
                                    out.println("<div class=\"col-xs-7\">");
                                        out.println("</br><div class=\"row\">");
                                            out.println("<B>Nom : </B>");
                                            if (games.get(i).get("name") != null && games.get(i).get("name") != "null") {
                                                out.println("<a href=\"Game?game=" + games.get(i).get("name") + "\">");
                                                    out.println(games.get(i).get("name").replace("\"", ""));
                                                out.println("</a>");
                                                if (currentPlayer.getGames().contains(games.get(i).get("name").replace("\"", ""))) {
                                                    out.println("<img src=\"assets/images/star.png\">");
                                                }
                                            }
                                        out.println("</div>");
                                        out.println("<div class=\"row\">");
                                            out.println("<B>Date de sortie : </B>");
                                            if (games.get(i).get("original_release_date") != null && games.get(i).get("original_release_date") != "null") {
                                                out.println(games.get(i).get("original_release_date").replace("\"", ""));
                                            }
                                        out.println("</div>");
                                    out.println("</div>");
                                out.println("</div>");
                            }
                        }
                    }
                out.println("</section>");
                
                // Show recent games
                out.println("<section class=\"col-xs-2 well\">");
                    out.println("<div id=\"wrapper\">");
                        out.println("<div id=\"sidebar-wrapper pull-right\">");
                            out.println("<ul class=\"sidebar-nav\">");
                                out.println("<h4 class=\"sidebar-brand\">");
                                    out.println("Derniers jeux ajoutés");
                                out.println("</h4>");
                                List<String> recentGames = GiantBombUtils.getMostRecentGames();
                                for (String recentGame : recentGames) {
                                    out.println("<li>");
                                        out.println("<a href=\"Game?game=" + recentGame + "\">");
                                            out.println(recentGame);
                                        out.println("</a>");
                                        if (currentPlayer.getGames().contains(recentGame)) {
                                            out.println("<img src=\"assets/images/star.png\">");
                                        }
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

}
