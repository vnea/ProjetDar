package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.GameSession;
import models.GameSessionDao;
import models.Player;
import models.PlayerDao;
import utils.HTMLBuilder;
import utils.StringUtils;
import dao.GameSessionDaoImpl;
import dao.PlayerDaoImpl;
import enums.PageTitle;
import enums.SessionData;

/**
 * Servlet implementation class CreateGameSession
 */
public class CreateGameSession extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
    
    private static final String INPUT_NAME_LABEL = "input-label";
    private static final String INPUT_NAME_DESC = "input-desc";
    
    private static final String INPUT_NAME_SEARCH_PLATFORMS = "input-search-platforms";
    private static final String INPUT_NAME_PLATFORMS = "input-list-platforms";
    private static final String LIST_ID_PLATFORMS = "list-platforms";
    
    private static final String INPUT_NAME_SEARCH_GAMES = "input-search-games";
    private static final String INPUT_NAME_GAMES = "input-list-games";
    private static final String LIST_ID_GAMES = "list-games";
    
    private static final String INPUT_NAME_ADDRESS = "input-address";
    private static final String INPUT_NAME_POSTCODE = "input-postcode";
    private static final String INPUT_NAME_DATE = "input-date";
    
    private static final String BTN_NAME_CREATE_SESSION = "btn-create-session";

    // Label criteria
    private static final int MIN_CHAR_LABEL = 3;
    private static final String LABEl_REGEX = "^[\\p{Alnum}][\\p{Alnum}\\s]+$";
    
    // Post code criteria
    private static final String POSTCODE_REGEX = "\\p{Digit}{5}";

    // Date criteria
    private final static String DATE_REGEX = "dd-MM-yyyy HH:mm";
    private final static SimpleDateFormat SDF = new SimpleDateFormat(DATE_REGEX, Locale.FRANCE);
    
    private GameSessionDao gameSessionDao;
    private PlayerDao playerDao;

    private String errorMessage;
    private boolean success;

    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateGameSession() {
        super();
    }

    @Override
    public void init() throws ServletException {
        resetStatus();
        
        gameSessionDao = new GameSessionDaoImpl();
        playerDao = new PlayerDaoImpl();
        
        super.init();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);        
        if (session == null) {
            response.sendRedirect(".");
        }
	    
	    resetStatus();
	    processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);        
        if (session == null) {
            response.sendRedirect(".");
        }
	    
	    resetStatus();
	    
	    // Btn create session pressed
	    if (request.getParameter(BTN_NAME_CREATE_SESSION) != null) {
	        performCreateSession(request, response);
	    }
	    
	    processRequest(request, response);
		doGet(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // DOCTYPE + html + head
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"fr\">");
        out.println(HTMLBuilder.createHeadTag(PageTitle.CREATE_SESSION));
        
        // Body
        out.println("<body>");
            // Top menus
            out.println(HTMLBuilder.createTopMenu());
            out.println(HTMLBuilder.createTabsMenu());
            
            out.println("<div class=\"container well\">");
                out.println("<h1 class=\"center\">" + PageTitle.CREATE_SESSION + "</h1>");
                
                if (success) {
                    out.println("<a href=\"CreateGameSession\">&#8592; Retour</a>");
                    out.println("<p class=\"successMessage\">La partie à bien été créée.</p>");
                }
                else {
                 // Form
                    out.println("<form method=\"post\">");
                        // Error message
                        if (errorMessage != null) {
                            out.println("<p class=\"errorMessage\">" + errorMessage + "</p>");
                        }

                        // Label
                        out.println(HTMLBuilder.createLabelInput(
                                INPUT_NAME_LABEL,
                                INPUT_NAME_LABEL,
                                "Label",
                                StringUtils.getStr(request.getParameter(INPUT_NAME_LABEL)), "Entrez un label")
                        );
                        
                        // Description
                        out.println("<br>");
                        out.println("<div>");
                            out.println("<label for=\"" + INPUT_NAME_DESC + "\">Description</label>");
                            out.println("<textarea class=\"form-control\" name=\"" + INPUT_NAME_DESC +
                                                                        "\" id=\"" + INPUT_NAME_DESC +
                                                                        "\" placeholder=\"Entrez une description\">");
                            String desc = StringUtils.getStr(request.getParameter(INPUT_NAME_DESC));
                            if (!desc.isEmpty()) {
                                out.println(desc);
                            }
                            out.println("</textarea>");
                        out.println("</div>");

                        // Platforms
                        out.println("<br>");
                        // Label
                        out.println(HTMLBuilder.createLabel(INPUT_NAME_SEARCH_PLATFORMS, "Plateformes"));
                        out.println("<div class=\"input-group\">");
                            // Input search
                            out.println(HTMLBuilder.createInput(INPUT_NAME_SEARCH_PLATFORMS, INPUT_NAME_SEARCH_PLATFORMS, "", "Cherchez une plateforme"));
                            // Search button
                            out.println("<span id=\"btn-search-platforms\" class=\"input-group-addon cursor-pointer\">");
                                out.println("<i class=\"glyphicon glyphicon-search\"></i>");
                            out.println("</span>");
                            // Add game
                            out.println("<span id=\"btn-add-platforms\" class=\"input-group-addon cursor-pointer\">");
                                out.println("<i class=\"glyphicon glyphicon-plus\"></i>");
                            out.println("</span>");
                        out.println("</div>");
                        out.println("<div>");
                            // Result in select
                            out.println("<select class=\"max-width\" id=\"select-search-platforms\"></select>");
                            // Status
                            out.println("<span id=\"result-search-platforms\"></span>");
                            // Platforms added
                            out.println("<div id=\"" + LIST_ID_PLATFORMS + "\">");
                            String[] platforms = request.getParameterValues(INPUT_NAME_PLATFORMS);
                            if (platforms != null) {
                                for (String platform : platforms) {
                                    out.println("<div class=\"input-group\">");
                                        // Platform input
                                        out.println("<input readonly=\"readonly\" name=\"" + INPUT_NAME_PLATFORMS +
                                                                               "\"value=\"" + platform + "\" >");
                                        // Remove platform
                                        out.println("<span class=\"resource cursor-pointer\"><i class=\"glyphicon glyphicon-minus\"></i></span>");
                                    out.println("</div>");
                                }
                            }
                            out.println("</div>");
                        out.println("</div>");
                        
                        // Games
                        out.println("<br>");
                        // Label
                        out.println(HTMLBuilder.createLabel(INPUT_NAME_SEARCH_GAMES, "Jeux"));
                        out.println("<div class=\"input-group\">");
                            // Input search
                            out.println(HTMLBuilder.createInput(INPUT_NAME_SEARCH_GAMES, INPUT_NAME_SEARCH_GAMES, "", "Cherchez un jeu"));
                            // Search button
                            out.println("<span id=\"btn-search-games\" class=\"input-group-addon cursor-pointer\">");
                                out.println("<i class=\"glyphicon glyphicon-search\"></i>");
                            out.println("</span>");
                            // Add game
                            out.println("<span id=\"btn-add-games\" class=\"input-group-addon cursor-pointer\">");
                                out.println("<i class=\"glyphicon glyphicon-plus\"></i>");
                            out.println("</span>");
                        out.println("</div>");
                        out.println("<div>");
                            // Result in select
                            out.println("<select class=\"max-width\" id=\"select-search-games\"></select>");
                            // Status
                            out.println("<span id=\"result-search-games\"></span>");
                            // Games added
                            out.println("<div id=\"" + LIST_ID_GAMES + "\">");
                            String[] games = request.getParameterValues(INPUT_NAME_GAMES);
                            if (games != null) {
                                for (String game : games) {
                                    out.println("<div class=\"input-group\">");
                                        // Game input
                                        out.println("<input readonly=\"readonly\" name=\"" + INPUT_NAME_GAMES +
                                                                               "\"value=\"" + game + "\" >");
                                        // Remove game
                                        out.println("<span class=\"resource cursor-pointer\"><i class=\"glyphicon glyphicon-minus\"></i></span>");
                                    out.println("</div>");
                                }
                            }
                            out.println("</div>");
                        out.println("</div>");
                        
                        // Address
                        out.println("<br>");
                        out.println(HTMLBuilder.createLabelInput(
                                INPUT_NAME_ADDRESS,
                                INPUT_NAME_ADDRESS,
                                "Adresse",
                                StringUtils.getStr(request.getParameter(INPUT_NAME_ADDRESS)),
                                "Entrez une adresse")
                        );
                        
                        // Post code
                        out.println("<br>");
                        out.println(HTMLBuilder.createLabel(INPUT_NAME_POSTCODE, "Code postal"));
                        out.println("<input class=\"form-control\" maxlength=\"5\" type=\"text\" name=\"" + INPUT_NAME_POSTCODE +
                                                                                               "\" id=\"" + INPUT_NAME_POSTCODE +
                                                                                               "\" value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_POSTCODE)) +
                                                                                               "\" placeholder=\"Entrez un code postal\">");
                        
                        // Date
                        out.println("<br>");
                        out.println("<div class=\"input-group date \" id=\"datetimepicker1\">");
                            out.println(HTMLBuilder.createLabelInput(
                                    INPUT_NAME_DATE, INPUT_NAME_DATE,
                                    "Date",
                                    StringUtils.getStr(request.getParameter(INPUT_NAME_DATE)),
                                    "Entrez une date au format " + DATE_REGEX)
                            );
                            out.println("<span class=\"input-group-addon\">");
                                out.println("<i class=\"glyphicon glyphicon-calendar\"></i>");
                            out.println("</span>");
                        out.println("</div>");
                                            
                        // Create button
                        out.println("<br>");
                        out.println("<button type=\"submit\" name=\"" + BTN_NAME_CREATE_SESSION + "\" class=\"btn btn-primary btn-lg btn-block\">Créer partie</button>");
                        
                    out.println("</form>");
                }
                
            out.println("</div>");
                                    
            // Scripts
            out.println(HTMLBuilder.createScriptsTags());
            out.println("<script src=\"assets/js/giantbomb.js\"></script>\n");
            out.println("<script src=\"assets/js/create-game-session.js\"></script>\n");
        out.println("</body>");
        out.print("</html>");   
	}
	
    private void performCreateSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String label = StringUtils.getStr(request.getParameter(INPUT_NAME_LABEL)).trim();
        String[] platforms = request.getParameterValues(INPUT_NAME_PLATFORMS);
        String address = StringUtils.getStr(request.getParameter(INPUT_NAME_ADDRESS)).trim();
        String postcode = StringUtils.getStr(request.getParameter(INPUT_NAME_POSTCODE).trim());
        String date = StringUtils.getStr(request.getParameter(INPUT_NAME_DATE).trim());
        
        Date timestamp = new Date();
        
        // No need to check description field as it can contains any values
        // No need to check games as we can only specify platforms if we want
        if (success =
                isLabelValid(label) && arePlatformsValid(platforms) &&
                isAddressValid(address, postcode) && isDateValid(date, timestamp)) {
            GameSession gameSession = new GameSession();
            gameSession.setLabel(label);
            gameSession.setDesc(StringUtils.getStr(request.getParameter(INPUT_NAME_DESC)).trim());
            gameSession.setAddress(address);
            gameSession.setPostCode(postcode);
            gameSession.setTimestamp(timestamp);
            
            // Should not throw an exception as date has alreayd been checked
            try {
                gameSession.setDate(SDF.parse(date));
            }
            catch (ParseException e) {
            }
            
            // Already checked that platform is not null before
            gameSession.setPlatforms(Arrays.asList(platforms));

            
            // No check done on games so we need to check
            String[] games = request.getParameterValues(INPUT_NAME_GAMES);
            if (games != null) {
                gameSession.setGames(Arrays.asList(games));
            }
            
            // Set root
            String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
            Player root = playerDao.getPlayer(username);
            gameSession.setRoot(root);
            
            // Insert game session
            gameSessionDao.insert(gameSession);
        }
    }
    
	private boolean isLabelValid(String label) {
	    // Check length
	    if (label.length() < MIN_CHAR_LABEL) {
	        errorMessage = "Le label doit au moins contenir " + MIN_CHAR_LABEL + " caractères.";
	        return false;
	    }
	    
	    // Check format
	    if (!label.matches(LABEl_REGEX)) {
	        errorMessage = "Le label ne peut contenir que des caractères alphanumériques.";
	        return false;
	    }
	    
	    return true;
	}
	
	private boolean arePlatformsValid(String[] platforms) {
	    // Check if we at least have one platform
	    if (platforms == null || platforms.length < 1) {
            errorMessage = "Vous devez au moins ajouter une plateforme.";
            return false;
	    }
	    
	    return true;
	}
	
    private boolean isAddressValid(String address, String postcode) {
        // Check if an address is defined
        if (address.isEmpty()) {
            errorMessage = "Vous devez spécifier une adresse.";
            return false;
        }
        
        // Check if post code is valid
        if (postcode.isEmpty() || !postcode.matches(POSTCODE_REGEX)) {
            errorMessage = "Le code postal est invalide.";
            return false;
        }
        // NORMALLY NEED TO CHECK ADDRESSE VALIDITY
	       
        return true;
    }
    
    private boolean isDateValid(String date, Date timestamp) {
        Date meetingDate;
        try {
            // Get date
            meetingDate = SDF.parse(date);
        }
        // Date format is invalid
        catch (ParseException e) {
            errorMessage = "La date doit être au format " + DATE_REGEX + ".";
            return false;
        }
        
        // Check if the session will be set after timestamp
        if (meetingDate.before(timestamp)) {
            errorMessage = "La date ne peut pas être antérieur à la date d'aujourd'hui (attention à vérifiez l'heure).";
            return false;
        }
        
        return true;
    }
	
    private void resetStatus() {
        errorMessage = null;
        success = false;
    }

}
