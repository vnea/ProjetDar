package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.GameSession;
import models.GameSessionDao;
import models.Player;
import models.PlayerDao;
import utils.GiantBombUtils;
import utils.HTMLBuilder;
import dao.GameSessionDaoImpl;
import dao.PlayerDaoImpl;
import enums.PageTitle;
import enums.SessionData;


/**
 * Servlet implementation class Game
 */
public class Game extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private static final String INPUT_NAME_VALUE = "input-gs-id";
    
    private static final String BTN_JOIN = "btn-join";
    private static final String BTN_LEAVE= "btn-leave";
    private static final String BTN_DELETE = "btn-delete";
    
    private static final Map<String, String> BUTTONS = new HashMap<>(3);
    
    private String message;
    private Boolean success;
    private String lastGameName;
	
	private PlayerDao playerDao;
    private GameSessionDao gameSessionDao;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Game() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
        playerDao = new PlayerDaoImpl();
        gameSessionDao = new GameSessionDaoImpl();
        
        resetStatus();
        
        BUTTONS.put(BTN_JOIN, "Rejoindre");
        BUTTONS.put(BTN_LEAVE, "Quitter");
        BUTTONS.put(BTN_DELETE, "Supprimer");

        super.init();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false) == null) {
            response.sendRedirect(".");
            return;
        }

        processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");

        // Delete button pressed
        if (request.getParameter(BTN_DELETE) != null) {
           performDeleteGameSession(request, response);
        }
        // Leave button pressed
        else if (request.getParameter(BTN_LEAVE) != null) {
            performLeaveGameSession(request, response);
        }
        // Join button pressed
        else if (request.getParameter(BTN_JOIN) != null) {
            performJoinGameSession(request, response);
        }
        
        processRequest(request, response);
    }

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"fr\">");
        out.println(HTMLBuilder.createHeadTag(PageTitle.GAME));
        
            
        // Body
        out.println("<body>");
            // Menu connection
            String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
            out.println(HTMLBuilder.createTopMenu());
            out.println(HTMLBuilder.createTabsMenu(username));
            String gameName = request.getParameter("game");

            if (success != null) {
                out.println("<section class=\"col-xs-2 well\">");
                    out.println("<div id=\"wrapper\">");
                        out.println("<div id=\"sidebar-wrapper\">");
                        out.println("</div>");
                    out.println("</div>");
                out.println("</section>");
            
                out.println("<div class=\"col-xs-8 well\">");
                    
                    out.println("<a href=\"Game?game=" + lastGameName + "\">&#8592; Retour</a>");
                    String msgClass = success ? "successMessage" : "errorMessage";
                    out.println("<p class=\"" + msgClass + "\">" + message + "</p>");
                out.println("</div>");

                resetStatus();
            }
            else {
                out.println("<div class=\"row\">");
                    // game parameter not set
                    if (gameName == null) {
                        out.println("<section class=\"col-xs-2 well\">");
                            out.println("<div id=\"wrapper\">");
                                out.println("<div id=\"sidebar-wrapper\">");
                                out.println("</div>");
                            out.println("</div>");
                        out.println("</section>");
                        
                        out.println("<div class=\"col-xs-8 well\">");
                            out.println("<p class=\"errorMessage\">Le jeu n'a pas été spécifiée.</p>");
                        out.println("</div>");
                    }
                    else {
                        Map<String, String> gameInfos = GiantBombUtils.getGameInfos(gameName);
                        // Invalid game
                        if (gameInfos == null) {
                            out.println("<section class=\"col-xs-2 well\">");
                                out.println("<div id=\"wrapper\">");
                                    out.println("<div id=\"sidebar-wrapper\">");
                                    out.println("</div>");
                                out.println("</div>");
                            out.println("</section>");
                        
                            out.println("<div class=\"col-xs-8 well\">");
                                out.println("<p class=\"errorMessage\">La jeu n'est pas répertorié.</p>");
                            out.println("</div>");
                        }
                        else {
                            lastGameName = gameName;
                            
                         // Show related game sessions
                            out.println("<section class=\"col-xs-2 well\">");
                                out.println("<div id=\"wrapper\">");
                                    out.println("<div id=\"sidebar-wrapper\">");
                                        out.println("<h4 class=\"sidebar-brand\">");
                                            out.println("Parties pour ce jeu");
                                        out.println("</h4>");
                                        List<GameSession> gameSessions = gameSessionDao.getGameSessionsByGame(gameName);
                                        
                                        for (GameSession gameSession : gameSessions) {
                                            // Default case : show join btn
                                            String btnName = BTN_JOIN;
                                            String btnValue = BUTTONS.get(BTN_JOIN);
                                            
                                            // Show delete btn
                                            if (gameSession.getRoot().getUsername().equals(username)) {
                                                btnName = BTN_DELETE;
                                                btnValue = BUTTONS.get(BTN_DELETE);
                                            }
                                            else {                        
                                                List<Player> players = gameSession.getPlayers();
                                                for (Player player : players) {
                                                    // Show leave btn
                                                    if (player.getUsername().equals(username)) {
                                                        btnName = BTN_LEAVE;
                                                        btnValue = BUTTONS.get(BTN_LEAVE);
                                                        break;
                                                    }
                                                }
                                            }
                                            out.println(HTMLBuilder.createPanelGameSession(gameSession));
                                            out.println(HTMLBuilder.createModalGameSession(gameSession, INPUT_NAME_VALUE, btnName, btnValue));
                                        }
                                    out.println("</div>");
                                out.println("</div>");
                            out.println("</section>");
                            
                            // Show info
                            out.println("<section class=\"col-xs-8 well\">");
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
                                
                                // Add game
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
                        }
                    }
                }
            
                // Show last games
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
        out.println("</body>");
        out.print("</html>");   
	}

    private void performDeleteGameSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get game session that we want to delete
            Integer gameSessionId = Integer.parseInt(request.getParameter(INPUT_NAME_VALUE));
            GameSession gs = gameSessionDao.getGameSession(gameSessionId);
            
            if (gs != null) {
                String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
                // Only allow delete the current user is the root
                if (gs.getRoot().getUsername().equals(username)) {
                    String lastDeletedGameSession = gs.getLabel();
                    gameSessionDao.delete(gs);
                    success = true;
                    message = "La partie " + lastDeletedGameSession + " a bien été supprimée";
                }
            }
            else {
                success = false;
                message = "La partie n'existe pas.";
            }

        }
        catch (NumberFormatException e) {
            success = false;
            message = "L'identifiant de la partie est invalide.";
        }
    }
    
    private void performLeaveGameSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get game session that we want to delete
            Integer gameSessionId = Integer.parseInt(request.getParameter(INPUT_NAME_VALUE));
            GameSession gs = gameSessionDao.getGameSession(gameSessionId);
            
            if (gs != null) {
                final String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
               
                boolean playerJoinedGs = false;
                List<Player> players = gs.getPlayers();
                for (Player player : players) {
                    if (player.getUsername().equals(username)) {
                        playerJoinedGs = true;
                        
                        // Need to change this ugly code...
                        Predicate<Player> pred = new Predicate<Player>() {
                            @Override
                            public boolean test(Player arg0) {
                                return username.equals(arg0.getUsername());
                            }
                        };
                        players.removeIf(pred);
                        // End of ugly code
                        
                        // This line is maybe useless, need to check more info about Hibernate
                        gs.setPlayers(players);

                        gameSessionDao.update(gs);
                        
                        success = true;
                        message = "Vous avez bien quitté la partie " + gs.getLabel() + ".";
                        break;
                    }
                }
                
                if (!playerJoinedGs) {
                    success = false;
                    message = "Vous ne particpez à la partie " + gs.getLabel() + " donc vous ne pouvez pas la quitter.";
                }
            }
            else {
                success = false;
                message = "La partie n'existe pas.";
            }
        }
        catch (NumberFormatException e) {
            success = false;
            message = "L'identifiant de la partie est invalide.";
        }
    }
    
    private void performJoinGameSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get game session that we want to delete
            Integer gameSessionId = Integer.parseInt(request.getParameter(INPUT_NAME_VALUE));
            GameSession gs = gameSessionDao.getGameSession(gameSessionId);
            
            if (gs != null) {
                // The game session is held after today
                if (new Date().before(gs.getMeetingDate())) {
                    String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
                    boolean playerAlreadyJoinedGs = false;
                    List<Player> players = gs.getPlayers();
                    for (Player player : players) {
                        if (player.getUsername().equals(username)) {
                            playerAlreadyJoinedGs = true;
                            
                            success = false;
                            message = "Vous participez déjà à la partie " + gs.getLabel() + ".";
                            break;
                        }
                    }
                    
                    if (!playerAlreadyJoinedGs) {
                        players.add(playerDao.getPlayer(username));
                        // This line is maybe useless, need to check more info about Hibernate
                        gs.setPlayers(players);
    
                        gameSessionDao.update(gs);
                        
                        success = true;
                        message = "Vous participation à la partie " + gs.getLabel() + " a bien été confirmée.";
                    }
                }
                // Can't join cause of invalid date
                else {
                    success = false;
                    message = "Vous ne pouvez pas participer à la partie " + gs.getLabel() + " car la date est déjà passée.";
                }
            }
            else {
                success = false;
                message = "La partie n'existe pas.";
            }
        }
        catch (NumberFormatException e) {
            success = false;
            message = "L'identifiant de la partie est invalide.";
        }
    }
    
    private void resetStatus() {
        message = null;
        success = null;
    }

}
