package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.PlayerDaoImpl;
import enums.PageTitle;
import enums.SessionData;
import models.Player;
import models.PlayerDao;
import utils.GiantBombUtils;
import utils.HTMLBuilder;

/**
 * Servlet implementation class MyGameTypes
 */
public class MyGameTypes extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private PlayerDao playerDao;
	private Player player;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyGameTypes() {
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
        out.println(HTMLBuilder.createHeadTag(PageTitle.MY_GAME_TYPES));

        // Body
        out.println("<body>");
            // Menu connection
            String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
            player = playerDao.getPlayer(username);
            out.println(HTMLBuilder.createTopMenu());
            out.println(HTMLBuilder.createTabsMenu(username));
            
            out.println("<div class=\"container-fluid\">");
    			out.println("<form class=\"form\" method=\"post\">");
	    			out.println("<div class=\"row\">");
	    			
		    			// get List of types of games from GiantBomb
		                List<String> gamesTypes = GiantBombUtils.getGamesTypes();
	                
		                int j = gamesTypes.size() % 4;
		                int k = 0;
		                out.println("<div class=\"col-xs-2 col-xs-offset-1\">");
		                for(int i=0; i<gamesTypes.size(); i++){
		                	if( (i % (gamesTypes.size()/4) == k) && (i!= 0) ){
		                		if(j > 0){
		                			j--;
		                			k++;
			                		i++;
			                		out.println(checkbox(gamesTypes.get(i)));
		                		}
								out.println("</div>");
								out.println("<div class=\"col-xs-2 col-xs-offset-1\">");
		                	}
		                	out.println(checkbox(gamesTypes.get(i)));
		                }
		                out.println("</div>");	
					
					out.println("</div>");
					out.println("<div class=\"row\">");
    					out.println("<div class=\"col-xs-1 col-xs-offset-6\">");
        					out.println("<input type=\"submit\" name=\"submit\" value=\"Valider\">");
    					out.println("</div>");	
        			out.println("</div>");
                out.println("</form>");
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
		// If session doesn't exists redirect to SigninSignup page
        HttpSession session = request.getSession(false);
        String playerUsername;
        
        if (session == null) {
            response.sendRedirect(".");
            return;
        }

    	playerUsername = (String) session.getAttribute(SessionData.PLAYER_USERNAME.toString());
    	player = this.playerDao.getPlayer(playerUsername);
    	
    	if( player != null){
    		request.setCharacterEncoding("UTF-8");
    		String types[]= request.getParameterValues("type");
    		player.setGamesType(new ArrayList<String>(Arrays.asList(types)));
    		this.playerDao.update(player);
    		response.sendRedirect("MyGameTypes");
    	}
	}
	
	
	/**
	 * @param buttonName
	 * @return
	 */
	private String checkbox (String buttonName) {
		String res;
		
		if(player.getGamesType().contains(buttonName)){
			res = "<label class=\"checkbox white\"><input class=\"inputGender\" type=\"checkbox\" checked=\"checked\"" +
					"name=\"type\"" + " value=\""+buttonName+"\">"+buttonName+"</label>";
		}
		else{
			res = "<label class=\"checkbox white\"><input class=\"inputGender\" type=\"checkbox\"" +
					"name=\"type\"" + " value=\""+buttonName+"\">"+buttonName+"</label>";
		}
			     
        return res;
	}

}
