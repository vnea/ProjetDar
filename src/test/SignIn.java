package test;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.PlayerDaoImpl;
import model.Player;
import model.PlayerDao;
import model.SessionDao;
 

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
    private SessionDao sessionDao;
    
    /** 
     * @see HttpServlet#doInit
     */
    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.playerDao = new PlayerDaoImpl();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	
		// Creation d'un Player
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
      
        // Insertion dans la base de données 
 //       this.playerDao.insertPlayer(p);

		
        // Selection d'un Player par username + pass
		Player agentSecret = this.playerDao.getPlayer("007","toto");
		
		PrintWriter out = response.getWriter();
        out.println("<h1>" + agentSecret.getFirstname() + "testsPlayer</h1>");

		
		model.Session session = new model.Session();
        session.setRoot(p);
        session.setAddress("address session");
        session.setDate(new Date(233333));
        
        this.sessionDao.insertSession(session);
//        model.Session sess = this.sessionDao.getSessionByRoot(agentSecret);
//        out.println("<h1>" + sess.getRoot().getUsername() + "testsSession</h1>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
