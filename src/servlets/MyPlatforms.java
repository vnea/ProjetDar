package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import enums.SessionData;
import model.Player;
import model.PlayerDao;

/**
 * Servlet implementation class MyPlatforms
 */
public class MyPlatforms extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	private PlayerDao playerDao;
	private Player player;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyPlatforms() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
        }

    	playerUsername = (String) session.getAttribute(SessionData.PLAYER_USERNAME.toString());
    	player = this.playerDao.getPlayer(playerUsername);
    	
    	if( player != null){
    		request.setCharacterEncoding("UTF-8");
    		String platforms[]= request.getParameterValues("platform");
    		player.setPlatforms(new ArrayList<String>(Arrays.asList(platforms)));
    		this.playerDao.updatePlayer(player);
    		response.sendRedirect("MyPlatforms");
    	}
	}

}
