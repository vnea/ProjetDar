package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import utils.HTMLBuilder;

/**
 * Servlet implementation class MyFriends
 */
public class MyFriends extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private PlayerDao playerDao;
	private Player player;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyFriends() {
        super();
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
        
        if (session == null) {
            response.sendRedirect(".");
            return;
        }
        else {
        	playerUsername = (String) session.getAttribute(SessionData.PLAYER_USERNAME.toString());
        	player = this.playerDao.getPlayer(playerUsername);
        }
		
	    response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // DOCTYPE + html + head
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"fr\">");
        out.println(HTMLBuilder.createHeadTag(PageTitle.MY_FRIENDS));

        // Body
        out.println("<body>");
            // Menu connection
            out.println(HTMLBuilder.createTopMenu());
            out.println(HTMLBuilder.createTabsMenu());
            
            out.println("<div class=\"container-fluid\">");
    			out.println("<form class=\"form\" method=\"post\">");
	    			out.println("<div class=\"row\">");
	    			
		                List<Player> friends = player.getFriends();
	                
		                int j = friends.size() % 4;
		                int k = 0;
		                out.println("<div class=\"col-xs-2 col-xs-offset-1\">");
		                for(int i=0; i<friends.size(); i++){
		                	if( (i % (friends.size()/4) == k) && (i!= 0) ){
		                		if(j > 0){
		                			j--;
		                			k++;
			                		i++;
			                		out.println(checkbox(friends.get(i).getUsername()));
		                		}
								out.println("</div>");
								out.println("<div class=\"col-xs-2 col-xs-offset-1\">");
		                	}
		                	out.println(checkbox(friends.get(i).getUsername()));
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
    		String friendsName[]= request.getParameterValues("friend");
    		List <Player> friends = new ArrayList <Player>();
    		for(int i = 0; i < friendsName.length; i++){
    			friends.add(this.playerDao.getPlayer(friendsName[i]));
    		}
    		player.setFriends(friends);
    		this.playerDao.update(player);
    		response.sendRedirect("MyFriends");
    	}
	}
	
	/**
	 * @param buttonName
	 * @return
	 */
	private String checkbox (String buttonName) {
		String
		
		res = "<label class=\"checkbox white\"><input class=\"inputGender\" type=\"checkbox\" checked=\"checked\"" +
				"name=\"friend\"" + " value=\""+buttonName+"\">\n";
			res += "<a href=\"OtherUser?user="+buttonName+"\">\n";
				res += buttonName;
			res += "</a>\n";
		res += "</label>";
			     
        return res;
	}

}
