package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Player;
import models.PlayerDao;
import utils.HTMLBuilder;
import dao.PlayerDaoImpl;
import enums.PageTitle;
import enums.SessionData;

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
        out.println(HTMLBuilder.createHeadTag(PageTitle.MY_FRIENDS));

        // Body
        out.println("<body>");
            // Menu connection
            String username = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
            player = this.playerDao.getPlayer(username);
            out.println(HTMLBuilder.createTopMenu());
            out.println(HTMLBuilder.createTabsMenu(username));
            
            out.println("<div class=\"container well\">");
                out.println("<h1 class=\"center\">" + PageTitle.MY_FRIENDS + "</h1>");

                out.println("<div class=\"row\">");
                    List<Player> friends = player.getFriends();
                    for (Player friend : friends) {
                        out.println("<a href=\"Profile?user=" + friend.getUsername() + "\">" + friend.getUsername() + "<a><br>\n");
                    }
                out.println("</div>");
            out.println("</div>");
        
            // Scripts
            out.println(HTMLBuilder.createScriptsTags());
        out.println("</body>");
        out.print("</html>");
	}

}
