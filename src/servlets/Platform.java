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

import comparators.ComparatorIgnoreCase;

import dao.GameSessionDaoImpl;
import dao.PlayerDaoImpl;
import enums.PageTitle;
import enums.SessionData;


//////////////////////////// Page pour afficher les genres, jeux, caractéristiques d'une console //////////////////

/**
 * Servlet implementation class Platform
 */
public class Platform extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private static final String INPUT_NAME_VALUE = "input-gs-id";
	
    private static final String BTN_JOIN = "btn-join";
    private static final String BTN_LEAVE= "btn-leave";
    private static final String BTN_DELETE = "btn-delete";
    private static final String BTN_ADD_PLATFORM = "btn-add-platform";
    private static final String BTN_DELETE_PLATFORM = "btn-delete-platform";

    private static final Map<String, String> BUTTONS = new HashMap<>(3);
    
    private String message;
    private Boolean success;
    private String lastPlatformName;
    
    private PlayerDao playerDao;
    private GameSessionDao gameSessionDao;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Platform() {
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
           performDeleteGameSession(request);
        }
        // Leave button pressed
        else if (request.getParameter(BTN_LEAVE) != null) {
            performLeaveGameSession(request);
        }
        // Join button pressed
        else if (request.getParameter(BTN_JOIN) != null) {
            performJoinGameSession(request);
        }
        // Add platform pressed
        else if (request.getParameter(BTN_ADD_PLATFORM) != null) {
            performAddPlatform(request);
        }
        // Delete platform pressed
        else if (request.getParameter(BTN_DELETE_PLATFORM) != null) {
            performDeletePlatform(request);
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
        out.println(HTMLBuilder.createHeadTag(PageTitle.PLATFORM));
        
        // Body
        out.println("<body>");
            // Menu connection
            String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
            out.println(HTMLBuilder.createTopMenu());
            out.println(HTMLBuilder.createTabsMenu(username));
            String platformName = request.getParameter("platform");
            
            if (success != null) {
                out.println("<section class=\"col-xs-2 well\">");
                    out.println("<div id=\"wrapper\">");
                        out.println("<div id=\"sidebar-wrapper\">");
                        out.println("</div>");
                    out.println("</div>");
                out.println("</section>");
            
                out.println("<div class=\"col-xs-8 well\">");
                    
                    out.println("<a href=\"Platform?platform=" + lastPlatformName + "\">&#8592; Retour</a>");
                    String msgClass = success ? "successMessage" : "errorMessage";
                    out.println("<p class=\"" + msgClass + "\">" + message + "</p>");
                out.println("</div>");

                resetStatus();
            }
            else {
                out.println("<div class=\"row\">");
                    // platform parameter not set
                    if (platformName == null) {
                        out.println("<section class=\"col-xs-2 well\">");
                            out.println("<div id=\"wrapper\">");
                                out.println("<div id=\"sidebar-wrapper\">");
                                out.println("</div>");
                            out.println("</div>");
                        out.println("</section>");
                        
                        out.println("<div class=\"col-xs-8 well\">");
                            out.println("<p class=\"errorMessage\">La plateforme n'a pas été spécifiée.</p>");
                        out.println("</div>");
                    }
                    else {
                        Map<String, String> platformInfos = GiantBombUtils.getPlatformInfos(platformName);
                        // Invalid platform
                        if (platformInfos == null) {
                            out.println("<section class=\"col-xs-2 well\">");
                                out.println("<div id=\"wrapper\">");
                                    out.println("<div id=\"sidebar-wrapper\">");
                                    out.println("</div>");
                                out.println("</div>");
                            out.println("</section>");
                        
                            out.println("<div class=\"col-xs-8 well\">");
                                out.println("<p class=\"errorMessage\">La plateforme n'est pas répertoriée.</p>");
                            out.println("</div>");
                        }
                        // Everything is ok
                        else {
                            lastPlatformName = platformName;
                            
                            // Show related game sessions
                            out.println("<section class=\"col-xs-2 well\">");
                                out.println("<div id=\"wrapper\">");
                                    out.println("<div id=\"sidebar-wrapper\">");
                                        out.println("<h4 class=\"sidebar-brand\">");
                                            out.println("Parties pour cette platforme");
                                        out.println("</h4>");
                                        List<GameSession> gameSessions = gameSessionDao.getGameSessionsByPlatform(platformName);
                                        
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
                            
                            // Show info about platform
                            out.println("<section class=\"col-xs-8 well\">");
                                out.println("<div class=\"row\">");
                                    out.println("<div class=\"col-xs-5 text-center\">");
                                        out.println("<img src="+platformInfos.get("image")+" width=\"250px\" height=\"200px\" alt=\"?\"/>");
                                    out.println("</div>");
                                    out.println("<div class=\"col-xs-7\">");
                                    out.println("<div class=\"row\">");
                                        out.println("<B>Nom : </B>");
                                            if (platformInfos.get("name") != null && platformInfos.get("name") != "null") {
                                                out.println(platformInfos.get("name"));
                                            }
                                        out.println("</div>");
                                        out.println("<div class=\"row\">");
                                            out.println("<B>Compagnie : </B>");
                                            if (platformInfos.get("company") != null && platformInfos.get("company") != "null") {
                                                out.println(platformInfos.get("company"));
                                            }
                                        out.println("</div>");
                                        out.println("<div class=\"row\">");
                                            out.println("<B>Description : </B>");
                                            if (platformInfos.get("deck") != null && platformInfos.get("deck") != "null") {
                                                out.println(platformInfos.get("deck"));
                                            }
                                        out.println("</div>");
                                        out.println("<div class=\"row\">");
                                            out.println("<B>Original release date : </B>");
                                            if (platformInfos.get("release_date") != null && platformInfos.get("release_date") != "null") {
                                                out.println(platformInfos.get("release_date"));
                                            }
                                        out.println("</div>");
                                    out.println("</div>");  
                                out.println("</div>");
                                
                                // Add/delete form
                                out.println("</br><div class=\"row\">");
                                    Player player = playerDao.getPlayer(username);
                                    out.println("<form method=\"post\" class=\"col-xs-12 text-center\">");
                                        out.println(player.getPlatforms().contains(platformName)
                                                    // Show delete platform
                                                    ? "<input type=\"submit\" class=\"btn btn-primary\" value=\"Supprimer de mes plateformes\" name=\"" + BTN_DELETE_PLATFORM + "\">"
                                                    // Show add platform
                                                    : "<input type=\"submit\" class=\"btn btn-primary\" value=\"Ajouter à mes plateformes\" name=\"" + BTN_ADD_PLATFORM + "\">");
                                    out.println("</form>");
                                out.println("</div></br>");
                            
                                out.println("<div class=\"row\">");
                                    out.println("<div class=\"col-xs-12\">");
                                        out.println("<B>Description : </B>");
                                        if (platformInfos.get("description") != null && platformInfos.get("description") != "null") {
                                            out.println(platformInfos.get("description"));
                                        }
                                    out.println("</div>");
                                out.println("</div>");
                                
                            out.println("</section>");
                        }
                    }
                }
            
                // Show others platforms
                out.println("<div class=\"row\">");
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
                                    
            out.println("</div>");
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
    
    private void performLeaveGameSession(HttpServletRequest request) throws ServletException, IOException {
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
    
    private void performJoinGameSession(HttpServletRequest request) throws ServletException, IOException {
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
    
    private void performAddPlatform(HttpServletRequest request) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
        Player player = playerDao.getPlayer(username);
        
        // Can't add platform as it already has this platform
        if (player.getPlatforms().contains(lastPlatformName)) {
            success = false;
            message = "Vous avez déjà ajouté cette plateforme à votre liste de plateformes.";
        }
        else {
            player.getPlatforms().add(lastPlatformName);
            playerDao.update(player);
            
            success = true;
            message = "La plateforme a bien été ajoutée à votre liste de plateformes.";
        }
    }
    
    private void performDeletePlatform(HttpServletRequest request) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
        Player player = playerDao.getPlayer(username);
        
        // Can't add platform as it already has this platform
        if (player.getPlatforms().contains(lastPlatformName)) {
            player.getPlatforms().remove(lastPlatformName);
            playerDao.update(player);
            
            success = true;
            message = "La plateforme a bien été supprimée de votre liste de plateformes.";

        }
        else {
            success = false;
            message = "La platforme ne fait pas partie de votre liste de plateformes.";
        }
    }
    
    private void resetStatus() {
        message = null;
        success = null;
    }

}
