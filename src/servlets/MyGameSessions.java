package servlets;

import java.io.IOException;
import java.io.PrintWriter;
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
import utils.HTMLBuilder;
import utils.TokenUtils;
import dao.GameSessionDaoImpl;
import dao.PlayerDaoImpl;
import enums.PageTitle;
import enums.SessionData;

/**
 * Servlet implementation class MyGameSessions
 */
public class MyGameSessions extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String INPUT_NAME_VALUE = "input-gs-id";
    private static final String BTN_LEAVE= "btn-leave";
    private static final String BTN_DELETE = "btn-delete";

	private static final Map<String, String> BUTTONS = new HashMap<>(3);
	
    private GameSessionDao gameSessionDao;
    private PlayerDao playerDao;
    
    private String message;
    private Boolean success;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyGameSessions() {
        super();
    }

    @Override
    public void init() throws ServletException {
        resetStatus();
        
        gameSessionDao = new GameSessionDaoImpl();
        playerDao = new PlayerDaoImpl();
        
        BUTTONS.put(BTN_LEAVE, "Quitter");
        BUTTONS.put(BTN_DELETE, "Supprimer");
        
        super.init();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getSession(false) == null) {
            response.sendRedirect(".");
            return;
        }
	    
	    resetStatus();
	    processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getSession(false) == null) {
            response.sendRedirect(".");
            return;
        }
	    
        resetStatus();

        // Delete button pressed
	    if (request.getParameter(BTN_DELETE) != null) {
	       performDeleteGameSession(request);
	    }
        // Leave button pressed
        else if (request.getParameter(BTN_LEAVE) != null) {
            performLeaveGameSession(request);
        }
    	    
	    processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Generate a unique token and store it session
        String uniqueToken = TokenUtils.generateUniqueToken();
        request.getSession().setAttribute(TokenUtils.CSRF_TOKEN, uniqueToken);

        request.setCharacterEncoding("UTF-8");
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
            String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
            out.println(HTMLBuilder.createTopMenu());
            out.println(HTMLBuilder.createTabsMenu(username));
            
            // MAIN CONTENT
            if (success != null) {
                out.println("<div class=\"container well\">");
                    out.println("<a href=\"MyGameSessions\">&#8592; Retour</a>");
                    String msgClass = success ? "successMessage" : "errorMessage";
                    out.println("<p class=\"" + msgClass + "\">" + message + "</p>");
                out.println("</div>");

                resetStatus();
            }
            else {
                // Created game sessions
                out.println("<div class=\"container well\">");
                    out.println("<div class=\"row\">");                
                        out.println("<h1>Parties hébergées</h1>");
                        out.println("<hr>");
                        
                        Player player = playerDao.getPlayer(username);
                        
                        List<GameSession> gameSessionsCreated = gameSessionDao.getGameSessionsCreatedByRoot(player);
                        // No game sessions found
                        if (gameSessionsCreated == null || gameSessionsCreated.isEmpty()) {
                            out.println("<p>Vous n'avez pas créé de partie.</p>");
                        }
                        // Game sessions found
                        else {
                            for (GameSession gameSession : gameSessionsCreated) {
                                // Game sessions panels
                                out.println(HTMLBuilder.createPanelGameSession(gameSession));
                                
                                
                                // Game sessions modals
                                out.println(HTMLBuilder.createModalGameSession(uniqueToken, gameSession, INPUT_NAME_VALUE, BTN_DELETE, BUTTONS.get(BTN_DELETE)));
                            }
                        }
                    out.println("</div>");
                out.println("</div>");
    
                // Joined game sessions
                out.println("<div class=\"container well\">");
                    out.println("<div class=\"row\">");
                        out.println("<h1>Parties auxquelles je participe</h1>");
                        out.println("<hr>");
    
                        List<GameSession> gameSessionsJoined = gameSessionDao.getGameSessionsByRoot(player);
                        
                        // No game sessions found
                        if (gameSessionsJoined == null || gameSessionsJoined.isEmpty()) {
                            out.println("<p>Vous ne participez à aucune partie.</p>");
                        }
                        // Game sessions found
                        else {
                            for (GameSession gameSession : gameSessionsJoined) {
                                // Game sessions panels
                                out.println(HTMLBuilder.createPanelGameSession(gameSession));
                                
                                // Game sessions modals
                                out.println(HTMLBuilder.createModalGameSession(uniqueToken, gameSession, INPUT_NAME_VALUE, BTN_LEAVE, BUTTONS.get(BTN_LEAVE)));
                            }
                        }
                    out.println("</div>");
                out.println("</div>");
            }
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

	private void performDeleteGameSession(HttpServletRequest request) throws ServletException, IOException {
	    if (!TokenUtils.isTokenValid(request.getSession(), request.getParameter(TokenUtils.CSRF_TOKEN))) {
            success = false;
            message = "Undefined or invalid token, the meeting can't be deleted.";
	        return;
	    }
	    
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
                else {
                    success = false;
                    message = "Impossible de supprimer la partie.";
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
	
	 private void performLeaveGameSession(HttpServletRequest request) throws ServletException, IOException {
        try {
            // Get game session that we want to delete
            Integer gameSessionId = Integer.parseInt(request.getParameter(INPUT_NAME_VALUE));
            GameSession gs = gameSessionDao.getGameSession(gameSessionId);
            
            if (gs != null) {
                final String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
                
                // Authenticated user can't leave its own game session
                if (username.equals(gs.getRoot().getUsername())) {
                    success = false;
                    message = "Vous ne pouvez pas quittez la partie " + gs.getLabel() + " comme vous êtes l'hébergeur de cette partie.";
                }
                else {
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
