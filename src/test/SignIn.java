package test;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.GameSessionDao;
import model.PlayerDao;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import dao.GameSessionDaoImpl;
import dao.PlayerDaoImpl;
 

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
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/html");
		response.setCharacterEncoding( "UTF-8" );
	
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
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
		    // Json format specified
		    String url = "http://www.giantbomb.com/api/genres/?format=json&api_key=784568466662eacd7cf5ba81d73976e4aa9291e3&field_list=name";
		    
		    
            HttpGet httpget = new HttpGet(url);
            // NEED TO SET A CUSTOM USER AGENT, IT CAN VE A RANDOM VALUE
            httpget.setHeader(HttpHeaders.USER_AGENT, "GiantBombUserAgent");
            httpget.setHeader(HttpHeaders.ACCEPT, "application/json");

            // Custom Response Handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    }
                    else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            
            String responseBody = httpclient.execute(httpget, responseHandler);
            response.getWriter().print(responseBody);
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
