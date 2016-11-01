package test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import dao.PlayerDaoImpl;
import dao.GameSessionDaoImpl;
import model.GameSession;
import model.Player;
import model.PlayerDao;
import model.GameSessionDao;
 

/**
 * Servlet implementation class SignIn
 */
public class SignIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignIn() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    private PlayerDao playerDao;
    private GameSessionDao sessionDao;
    
    /** 
     * @see HttpServlet#doInit
     */
    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.playerDao = new PlayerDaoImpl();
        this.sessionDao = new GameSessionDaoImpl();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/html");
		response.setCharacterEncoding( "UTF-8" );
		PrintWriter out = response.getWriter();
	
/*		// Creation d'un Player
		Player p = new Player();
        p.setEmail("jamesBond@mail.com");
        p.setUsername("007");
        p.setPassword("toto");
        p.setLastname("Bond");
        p.setFirstname("James");
        p.setAge(35);
        p.setSex(Player.Sex.H);
        p.setPhoneNumber("0707070707");
        p.setAddress("adresseSecrete");
        p.setPostCode(70007);
        p.setGames(new ArrayList<String>());
        p.setGamesType(new ArrayList<String>());
        p.setPlatforms(new ArrayList<String>());
  */    
        // Insertion dans la base de données 
//        this.playerDao.insertPlayer(p);
        // Selection d'un Player par username + pass
//		Player agentSecret = this.playerDao.getPlayer("007","toto");
		// Ecriture du resultat
//        out.println("<h1>" + agentSecret.getFirstname() + "testsPlayer</h1>");
        // Update
//     	agentSecret.setAge(50);
//        this.playerDao.updatePlayer(agentSecret);
//        Player agentSecretUpdate = this.playerDao.getPlayer("007","toto");
//        out.println("<h1>" + agentSecretUpdate.getFirstname() + "testsPlayer</h1>");
        
        // Delete
//        this.playerDao.deletePlayer(agentSecret);
        		
/*		GameSession session = new GameSession();
        session.setRoot(agentSecret);
        session.setAddress("address session");
        session.setDate(new Date(System.currentTimeMillis( )));
    */    
//        this.sessionDao.insertGameSession(session);
//        List<GameSession> sess = new ArrayList<GameSession>(this.sessionDao.getGameSessionByRoot(agentSecret));
//        this.sessionDao.deleteGameSession(sess);
		
		
		
		/////////// Exemple requete giantBomb ////////////:
		String url="http://www.giantbomb.com/api/genres/?format=xml&api_key=784568466662eacd7cf5ba81d73976e4aa9291e3&field_list=name";
	    HttpGet get=new HttpGet(url);
	    HttpClient httpClient = HttpClients.createDefault();
	    StringBuffer result = new StringBuffer();

	    CloseableHttpResponse internResponse = (CloseableHttpResponse) httpClient.execute(get);

	    int internResponseStatus = internResponse.getStatusLine().getStatusCode();
	    
	    result.append(internResponseStatus);

	    if(200 == internResponseStatus)
        {
	    	BufferedReader rd = new BufferedReader(new InputStreamReader(internResponse.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            result.append("bien");
        }
	    else{
	    	result.append("error");
	    }

        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public void createTopBar (PrintWriter out){
		out.println("<nav class=\"navbar navbar-inverse navbar-fixed-top\">");
			out.println("<div class=\"container\">");
			    out.println("<div class=\"navbar-header\">");
			    	out.println("<button type=\"button\" class=\"navbar-toggle collapsed\"" +  
			    					"data-toggle=\"collapse\" data-target=\"#navbar\" aria-expanded=\"false\"" +
			    					"aria-controls=\"navbar\">");
		    			out.println("<span class=\"sr-only\">Toggle navigation</span> ");
		    			out.println("<span class=\"icon-bar\"></span> <span class=\"icon-bar\"></span> ");
		    			out.println("<span class=\"icon-bar\"></span>");
			    	out.println("</button>");
			    	out.println("<a class=\"navbar-brand\" href=\"#\">PlayersToPlayers</a>");
			    out.println("</div>");
			    out.println("<div id=\"navbar\" class=\"collapse navbar-collapse\">");
			    	out.println("<ul class=\"nav navbar-nav\">");
			    		out.println("<li><a>Héberger</a></li>");
			    		out.println("<li><a>Rechercher</a></li>");
			    		out.println("<li><a>Déconnexion</a></li>");
			    		out.println("<li>");
							
			    		out.println("</li>");
			    	out.println("</ul>");
			    out.println("</div>");
		    out.println("</div>");
	    out.println("</nav>");
	}

}
