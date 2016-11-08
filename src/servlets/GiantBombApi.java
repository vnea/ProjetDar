package servlets;

import java.io.IOException;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import utils.GiantBombUtils;
import utils.JsonUtils;
import utils.StringUtils;
import enums.GiantBombResource;

/**
 * Servlet implementation class GiantBombApi
 */
public class GiantBombApi extends HttpServlet {
    
	private static final long serialVersionUID = 1L;
	
	private static final String PARAM_RESOURCE = "resource";
	private static final String PARAM_GAME = "game";
	private static final String PARAM_PLATFORMS = "platform";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GiantBombApi() {
        super();
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
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json");
        request.setCharacterEncoding("UTF-8");
	    
        GiantBombResource resource = null;
        try {
            resource = GiantBombResource.valueOf(request.getParameter(PARAM_RESOURCE).toUpperCase());
            
            // Games
            if (resource == GiantBombResource.GAMES) {
                handleResourceGames(request, response);
            }
            // Platforms
            else if (resource == GiantBombResource.PLATFORMS) {
                handleResourcePlatforms(request, response);
            }
        }
        // Need to specify a resource
        catch (NullPointerException e) {
            response.sendError(HttpStatus.SC_BAD_REQUEST, "La ressource n'a pas été spécifiée.");            
        }
        // Resource does not exist
        catch (IllegalArgumentException e) {
            response.sendError(HttpStatus.SC_BAD_REQUEST, "La ressource " + resource + " n'existe pas.");            
        }
	}
	
	private void handleResourceGames(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String game = request.getParameter(PARAM_GAME);
        
        // The client send a null or empty value
        if (StringUtils.isNullOrEmpty(game)) {
            response.sendError(HttpStatus.SC_NO_CONTENT, "Aucun jeu n'a été trouvé.");
        }
        
        // Call GiantBombApi to get games
        String result = GiantBombUtils.getGamesAsJsonString(game);
        
        JsonObject JOGamesTypes = JsonUtils.JsonObjectFromString(result);
        JsonArray JAGamesTypes = JOGamesTypes.getJsonArray("results");
        
        // Case if GiantBombApi did not found games
        if (JAGamesTypes.isEmpty()) {
            response.sendError(HttpStatus.SC_NO_CONTENT, "Aucun jeu n'a été trouvé.");
        }

        response.getWriter().print(result);
	}
	
    private void handleResourcePlatforms(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String platform = request.getParameter(PARAM_PLATFORMS);
        
        // The client send a null or empty value
        if (StringUtils.isNullOrEmpty(platform)) {
            response.sendError(HttpStatus.SC_NO_CONTENT, "Aucune plateforme n'a été trouvée.");
        }
        
        // Call GiantBombApi to get platforms
        String result = GiantBombUtils.getPlatformInfosAsJsonString(platform);
        
        JsonObject JOReponse = JsonUtils.JsonObjectFromString(result);
        JsonArray JAPlatformInfos = JOReponse.getJsonArray("results");
        // Case if GiantBombApi did not found platforms
        if (JAPlatformInfos.isEmpty()) {
            response.sendError(HttpStatus.SC_NO_CONTENT, "Aucune plateforme n'a été trouvée.");
        }
        
        response.getWriter().print(result);
    }

}
