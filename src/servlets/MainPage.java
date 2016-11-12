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
import models.Location;
import models.Player;
import models.PlayerDao;
import utils.GiantBombUtils;
import utils.GoogleMapsUtils;
import utils.HTMLBuilder;
import comparators.ComparatorIgnoreCase;
import dao.GameSessionDaoImpl;
import dao.PlayerDaoImpl;
import enums.PageTitle;
import enums.SessionData;

/**
 * Servlet implementation class MainPage
 */
public class MainPage extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	// Default location is paris
	private static final Location DEFAULT_LOCATION = new Location(48.866667, 2.333333);
    private static final int DEFAULT_ZOOM = 8;
	private static final String GMAPS = "map";
	
	private static final String BASE_IMG_MARKERS = "assets/images/markers/";
	private static final String IMG_MARKER_CREATED_GS = BASE_IMG_MARKERS + "created-gs.png";
    private static final String IMG_MARKER_JOINED_GS = BASE_IMG_MARKERS + "joined-gs.png";
    private static final String IMG_MARKER_OTHERS_GS = BASE_IMG_MARKERS + "others-gs.png";


    private static final String INPUT_NAME_VALUE = "input-gs-id";
    private static final String BTN_JOIN = "btn-join";
    private static final String BTN_LEAVE= "btn-leave";
    private static final String BTN_DELETE = "btn-delete";
    
    private static final Map<String, String> BUTTONS = new HashMap<>(3);
    
	private PlayerDao playerDao;
	private GameSessionDao gameSessionDao;
    
    private String message;
    private Boolean success;
    
    private String username;
    private List<GameSession> gameSessions;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainPage() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
        gameSessionDao = new GameSessionDaoImpl();
        playerDao = new PlayerDaoImpl();

        BUTTONS.put(BTN_JOIN, "Rejoindre");
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
                
                if (success != null) {
                    out.println("<div class=\"col-xs-8 well\">");
                        out.println("<a href=\"MainPage\">&#8592; Retour</a>");
                        String msgClass = success ? "successMessage" : "errorMessage";
                        out.println("<p class=\"" + msgClass + "\">" + message + "</p>");
                    out.println("</div>");
    
                    success = null;
                }
                else {
                    // Google maps
                    out.println("<div class=\"col-xs-8 well\">");
                        out.println("<div id=\"gmaps\" style=\"height:100%; width: 100%; position: relative;\">");
                            out.println("Chargement de la carte.");
                        out.println("</div>");
                    out.println("</div>");

                    username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
                    gameSessions = gameSessionDao.getGameSessions();
                    // Create modals
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
                        out.println(HTMLBuilder.createModalGameSession(gameSession, INPUT_NAME_VALUE, btnName, btnValue));
                    }
                }

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
            
            if (success == null) {
                out.println("<script src=\"assets/js/jquery.easing.1.3.js\"></script>\n");
                out.println("<script src=\"assets/js/jquery.waypoints.min.js\"></script>\n");
                out.println("<script src=\"assets/js/jquery.magnific-popup.min.js\"></script>\n");
                out.println("<script src=\"assets/js/salvattore.min.js\"></script>\n");
                
                // Google maps
                out.println("<script src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyC5U4UKF009_yNlDjGO9X3JEoE9QsADw9g\"></script>");
                
                String
                scriptGmaps = "<script>\n";
                    // GMaps
                    scriptGmaps += "var " + GMAPS + ";";
                    
                    // Load map
                    scriptGmaps += "(function() {";
                        // Init gmaps
                        scriptGmaps += "var initGmaps = function() {\n";
                            scriptGmaps += "var mapOptions = {\n";
                                scriptGmaps += "zoom: " + DEFAULT_ZOOM + ",\n";
                                scriptGmaps += "center: {\n";
                                    scriptGmaps += "lat: " + DEFAULT_LOCATION.getLatitude() + ",\n";
                                    scriptGmaps += "lng: " + DEFAULT_LOCATION.getLongitude() + "\n";
                                scriptGmaps += "}\n";
                            scriptGmaps += "};\n";
                            
                            // Create gmaps
                            scriptGmaps += GMAPS + " = new google.maps.Map($(\"#gmaps\")[0], mapOptions);\n";
                        scriptGmaps += "};\n";
                        
                        scriptGmaps += "initGmaps();\n"; 
                    scriptGmaps += "})();\n";
                
                scriptGmaps += "</script>";
                out.println(scriptGmaps);
                
                // Add markers for each sessions on map
                String
                scriptMarkers = "<script>\n";
                    scriptMarkers += "$(document).ready(function() {\n";
                        for (GameSession gameSession : gameSessions) {
                            Location location = GoogleMapsUtils.geocode(gameSession.getAddress() + " " + gameSession.getPostCode());
                            if (location != null) {
                                // Marker
                                String makerVar = "marker" + gameSession.getIdSession();
                                scriptMarkers += "var " + makerVar + " = new google.maps.Marker({\n";
                                    scriptMarkers += "position: " + location.toJson() + ",\n";
                                    scriptMarkers += "map: " + GMAPS + ",\n";
                                    
                                    // Normal case
                                    String icon = IMG_MARKER_OTHERS_GS;
                                    
                                    // Created game session
                                    if (gameSession.getRoot().getUsername().equals(username)) {
                                        icon = IMG_MARKER_CREATED_GS;
                                    }
                                    else {
                                        List<Player> players = gameSession.getPlayers();
                                        for (Player player : players) {
                                            // Joined game session
                                            if (player.getUsername().equals(username)) {
                                                icon = IMG_MARKER_JOINED_GS;
                                                break;
                                            }
                                        }
                                    }
                                    
                                    scriptMarkers += "icon: " + "'" + icon + "',\n";
                                    scriptMarkers += "title: '" + gameSession.getLabel() + "'\n";
                                scriptMarkers +="});\n";
                                
                                // Tooltip content
                                String tootltipContentVar = "tooltipContent" + gameSession.getIdSession();
                                scriptMarkers += "var " + tootltipContentVar + " = '" + HTMLBuilder.createPanelGameSession(gameSession).replace("\n", "") + "';\n";
                                
                                // Tooltip
                                String tootltipVar = "tooltipVar" + gameSession.getIdSession();
                                scriptMarkers += "var " + tootltipVar + " = new google.maps.InfoWindow( {\n";
                                        scriptMarkers += "content: " + tootltipContentVar + "\n";
                                scriptMarkers += "} );\n";
    
                                // Add open tooltip when clicking on marker
                                scriptMarkers += "google.maps.event.addListener(" + makerVar + ", \"click\", function() {\n";
                                    scriptMarkers += tootltipVar + ".open(" + GMAPS + ", " + makerVar + ");\n";
                                scriptMarkers += "});\n";
                            }
                        }
                    scriptMarkers += "});\n";
                scriptMarkers += "</script>";
                out.println(scriptMarkers);
            }
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
                // Only allow delete the current user is the root
               
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
                String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
                // Only allow delete the current user is the root
               
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

}
