package test;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.PlayerDaoImpl;
import model.Player;
import model.PlayerDao;
 

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
		
//		this.playerDao.CreatePlayer("jamesBond@mail.com", "007", "toto", "Bond", "James", 35, Player.Sex.H, "0707070707", 
//									"adresseSecrete", 70007, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>()); 
		
		Player agentSecret = this.playerDao.getPlayer("007","toto");
		
		PrintWriter out = response.getWriter();
        out.println("<h1>" + agentSecret.getFirstname() + "tests</h1>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
