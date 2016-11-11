package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.GameSession;
import models.GameSessionDao;
import models.Player;
import models.PlayerDao;
import utils.HTMLBuilder;
import dao.GameSessionDaoImpl;
import dao.PlayerDaoImpl;
import enums.PageTitle;
import enums.SessionData;

/**
 * Servlet implementation class MyGameSessions
 */
public class MyGameSessions extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
    private GameSessionDao gameSessionDao;
    private PlayerDao playerDao;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyGameSessions() {
        super();
    }

    @Override
    public void init() throws ServletException {
        gameSessionDao = new GameSessionDaoImpl();
        playerDao = new PlayerDaoImpl();
        
        super.init();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    processeRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
	private void processeRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setCharacterEncoding("UTF-8");
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    
	    // DOCTYPE + html + head
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"fr\">");
        out.println(HTMLBuilder.createHeadTag(PageTitle.MY_GAME_SESSIONS));
        
        // Body
        out.println("<body>");
            // Top menus
            out.println(HTMLBuilder.createTopMenu());
            out.println(HTMLBuilder.createTabsMenu());
            
            // MAIN CONTENT
            // Created game sessions
            out.println("<div class=\"container well\">");
                out.println("<div class=\"row\">");
                    out.println("<h1>Parties hébergées</h1>");
                    out.println("<hr>");

                    String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
                    Player player = playerDao.getPlayer(username);
                    
                    List<GameSession> gameSessionsCreated = gameSessionDao.getGameSessionCreatedByRoot(player);
                    // No game sessions found
                    if (gameSessionsCreated == null || gameSessionsCreated.isEmpty()) {
                        out.println("<p>Vous n'avez pas créé de partie.</p>");
                    }
                    // Game sessions found
                    else {
                        for (GameSession gameSession : gameSessionsCreated) {
                            // Game sessions list
                            out.println("<div class=\"panel panel-default\">");
                                // Head
                                out.println("<div class=\"panel-heading\">");
                                    out.println(gameSession.getLabel());
                                out.print("</div>");
                                
                                // Content
                                out.println("<div class=\"panel-body\">");
                                    out.println("<span>Hébergé par: " + username + "</span><br>");
                                    out.println("<span>" + gameSession.getMeetingDate() + "</span><br><br>");
                                    out.println("<button type=\"button\" class=\"btn btn-primary btn-sm\" data-toggle=\"modal\" data-target=\"#myModal" + gameSession.getIdSession() + "\">Détails</button>");
                                out.print("</div>");
                            out.print("</div>");
                            
                            // Game sessions modals
                            out.println("<div class=\"modal fade\" id=\"myModal" + gameSession.getIdSession() + "\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">");
                                out.println("<div class=\"modal-dialog\" role=\"document\">");
                                    out.println("<div class=\"modal-content\">");
                                        // Header
                                        out.println("<div class=\"modal-header\">");
                                            out.println("<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>");
                                            out.println("<h4 class=\"modal-title display-4\" id=\"myModalLabel\">" + gameSession.getLabel() + "</h4>");
                                        out.println("</div>");
                                        
                                        // Body
                                        out.println("<div class=\"modal-body\">");
                                            // Author
                                            out.println("<span class=\"glyphicon glyphicon-user\" aria-hidden=\"true\">&nbsp;Hébergé par : " + username + "</span>");
                                            out.println("<hr>");
    
                                            // Desc
                                            out.println("<span class=\"glyphicon glyphicon-pencil\" aria-hidden=\"true\">&nbsp;Description : " + gameSession.getDescription() + "</span>");
                                            out.println("<hr>");
                                            
                                            // Platforms
                                            out.println("<span class=\"fa fa-television\" aria-hidden=\"true\">&nbsp;Plateformes :");
                                            List<String> platforms = gameSession.getPlatforms();
                                            for (String platform : platforms) {
                                                out.print(" " + platform);
                                            }
                                            out.println("</span>");
                                            out.println("<hr>");
                                            
                                            // Games
                                            out.println("<span class=\"fa fa-gamepad\" aria-hidden=\"true\">&nbsp;Jeux :");
                                            List<String> games = gameSession.getGames();
                                            for (String game : games) {
                                                out.print(" " + game);
                                            }
                                            out.println("</span>");
                                            out.println("<hr>");
                                            
                                            // Participants
                                            out.println("<span class=\"fa fa-users\" aria-hidden=\"true\">&nbsp;Participants :");
                                            List<Player> participants = gameSession.getPlayers();
                                            for (Player participant : participants) {
                                                out.print(" " + participant.getUsername());
                                            }
                                            out.println("</span>");
                                            out.println("<hr>");
                                            
                                            // Address
                                            out.println("<span class=\"fa fa-home\" aria-hidden=\"true\">&nbsp;Adresse : " +
                                                        gameSession.getAddress() + " " +
                                                        gameSession.getPostCode() +
                                                        "</span>");
                                            out.println("<hr>");
                                            
                                            // Date
                                            out.println("<span class=\"fa fa-clock-o\" aria-hidden=\"true\">&nbsp;Date : " + gameSession.getMeetingDate() + "</span>");
                                            out.println("<hr>");
                                            
                                        out.println("</div>");
                                        
                                        // Footer
                                        out.println("<div class=\"modal-footer\">");
                                            out.println("<form method=\"post\">");
                                            out.println("<input type=\"hidden\" name=\"input-gs-id\" value=\"" + gameSession.getIdSession() + "\">");
                                            out.println("<button type=\"submit\" name=\"btn-delete\" class=\"btn btn-primary\">Supprimer</button>");
                                            out.println("</form>");
                                        out.print("</div>");
                                    out.println("</div>");
                                out.println("</div>");
                            out.println("</div>");
                        }
                    }
                out.println("</div>");
            out.println("</div>");

            // Joined game sessions
            out.println("<div class=\"container well\">");
                out.println("<div class=\"row\">");
                    out.println("<h1>Parties auxquelles je participe</h1>");
                    out.println("<hr>");

                    List<GameSession> gameSessionsJoined = gameSessionDao.getGameSessionByRoot(player);
                    
                    // No game sessions found
                    if (gameSessionsJoined == null || gameSessionsJoined.isEmpty()) {
                        out.println("<p>Vous ne participez à aucune partie.</p>");
                    }
                    // Game sessions found
                    else {
                        for (GameSession gameSession : gameSessionsJoined) {
                            // Game sessions list
                            out.println("<div class=\"panel panel-default\">");
                                // Head
                                out.println("<div class=\"panel-heading\">");
                                    out.println(gameSession.getLabel());
                                out.print("</div>");
                                
                                // Content
                                out.println("<div class=\"panel-body\">");
                                    out.println("<span>Hébergé par: " + gameSession.getRoot().getUsername() + "</span><br>");
                                    out.println("<span>" + gameSession.getMeetingDate() + "</span><br><br>");
                                    out.println("<button type=\"button\" class=\"btn btn-primary btn-sm\" data-toggle=\"modal\" data-target=\"#myModal" + gameSession.getIdSession() + "\">Détails</button>");
                                out.print("</div>");
                            out.print("</div>");
                            
                            // Game sessions modals
                            out.println("<div class=\"modal fade\" id=\"myModal" + gameSession.getIdSession() + "\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">");
                                out.println("<div class=\"modal-dialog\" role=\"document\">");
                                    out.println("<div class=\"modal-content\">");
                                        // Header
                                        out.println("<div class=\"modal-header\">");
                                            out.println("<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>");
                                            out.println("<h4 class=\"modal-title display-4\" id=\"myModalLabel\">" + gameSession.getLabel() + "</h4>");
                                        out.println("</div>");
                                        
                                        // Body
                                        out.println("<div class=\"modal-body\">");
                                            // Author
                                            out.println("<span class=\"glyphicon glyphicon-user\" aria-hidden=\"true\">&nbsp;Hébergé par : " + username + "</span>");
                                            out.println("<hr>");
    
                                            // Desc
                                            out.println("<span class=\"glyphicon glyphicon-pencil\" aria-hidden=\"true\">&nbsp;Description : " + gameSession.getDescription() + "</span>");
                                            out.println("<hr>");
                                            
                                            // Platforms
                                            out.println("<span class=\"fa fa-television\" aria-hidden=\"true\">&nbsp;Plateformes :");
                                            List<String> platforms = gameSession.getPlatforms();
                                            for (String platform : platforms) {
                                                out.print(" " + platform);
                                            }
                                            out.println("</span>");
                                            out.println("<hr>");
                                            
                                            // Games
                                            out.println("<span class=\"fa fa-gamepad\" aria-hidden=\"true\">&nbsp;Jeux :");
                                            List<String> games = gameSession.getGames();
                                            for (String game : games) {
                                                out.print(" " + game);
                                            }
                                            out.println("</span>");
                                            out.println("<hr>");
                                            
                                            // Participants
                                            out.println("<span class=\"fa fa-users\" aria-hidden=\"true\">&nbsp;Participants :");
                                            List<Player> participants = gameSession.getPlayers();
                                            for (Player participant : participants) {
                                                out.print(" " + participant.getUsername());
                                            }
                                            out.println("</span>");
                                            out.println("<hr>");
                                            
                                            // Address
                                            out.println("<span class=\"fa fa-home\" aria-hidden=\"true\">&nbsp;Adresse : " +
                                                        gameSession.getAddress() + " " +
                                                        gameSession.getPostCode() +
                                                        "</span>");
                                            out.println("<hr>");
                                            
                                            // Date
                                            out.println("<span class=\"fa fa-clock-o\" aria-hidden=\"true\">&nbsp;Date : " + gameSession.getMeetingDate() + "</span>");
                                            out.println("<hr>");
                                            
                                        out.println("</div>");
                                        
                                        // Footer
                                        out.println("<div class=\"modal-footer\">");
                                            out.println("<form method=\"post\">");
                                                out.println("<input type=\"hidden\" name=\"input-gs-id\" value=\"" +  gameSession.getIdSession() + "\">");
                                                out.println("<button type=\"submit\" name=\"btn-leave\" class=\"btn btn-primary\">Quitter</button>");
                                            out.println("</form>");
                                        out.print("</div>");
                                    out.println("</div>");
                                out.println("</div>");
                            out.println("</div>");
                        }
                    }
                out.println("</div>");
            out.println("</div>");
            // END OF MAIN CONTENT
            
            // Scripts
            out.println(HTMLBuilder.createScriptsTags());
            out.println("<script src=\"assets/js/jquery.easing.1.3.js\"></script>\n");
            out.println("<script src=\"assets/js/jquery.waypoints.min.js\"></script>\n");
            out.println("<script src=\"assets/js/jquery.magnific-popup.min.js\"></script>\n");
            out.println("<script src=\"assets/js/salvattore.min.js\"></script>\n");
            out.println("<script src=\"assets/js/my-game-sessions.js\"></script>\n");
        out.println("</body>");
        out.print("</html>");  
	}

}
