package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.PlayerDao;
import utils.HTMLBuilder;
import utils.StringUtils;
import dao.PlayerDaoImpl;
import enums.PageTitle;

/**
 * Servlet implementation class Home
 */
public class SigninSignup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private PlayerDao playerDao;

	
	/**
	 *  Signin
	 */
	// Names for HTML tags
    private static final String INPUT_NAME_SIGNIN_LOGIN = "login-signin";
    private static final String INPUT_NAME_SIGNIN_PASSWORD = "password-signin";
    private static final String BTN_NAME_SIGNIN = "btn-signin";

	
	/**
	 *  Singup
	 */
    
    // Names for HTML tags
	private static final String INPUT_NAME_SIGNUP_LOGIN = "login-signup";
    private static final String INPUT_NAME_SIGNUP_EMAIL = "email-signup";
    private static final String INPUT_NAME_SIGNUP_ADDRESS = "address-signup";
    private static final String INPUT_NAME_SIGNUP_AGE = "age-signup";
    private static final String INPUT_NAME_SIGNUP_PASSWORD = "password-signup";
    private static final String INPUT_NAME_SIGNUP_CONFRIM_PASSWORD = "confirm-password-signup";
    private static final String BTN_NAME_SIGNUP = "btn-signup";
    
    // Login criteria
    private static int MIN_CHAR_LOGIN = 3;
    private static String LOGIN_PATTERN = "^[\\p{Alnum}]{" + MIN_CHAR_LOGIN + ",}$"; // Only alphanum char

    private String errorMessageSignup = null;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SigninSignup() {
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
        processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Reset error message
	    errorMessageSignup = null;
	    
	    // Login button pressed
	    if (request.getParameter(BTN_NAME_SIGNIN) != null) {
	    }
	    
	    // Signup button pressed
	    else if (request.getParameter(BTN_NAME_SIGNUP) != null) {
	        String login = request.getParameter(INPUT_NAME_SIGNUP_LOGIN).trim();
//	        String email = request.getParameter(INPUT_NAME_SIGNUP_EMAIL).trim();
//	        Integer age = Integer.parseInt(request.getParameter(INPUT_NAME_SIGNUP_AGE).trim());
//	        String password = request.getParameter(INPUT_NAME_SIGNUP_PASSWORD).trim();
//	        
	        if (isLoginValid(login)) {
//	            // Create new Player + session
//	            Player p = new Player();
//	            p.setUsername(login);
//	            p.setEmail(email);
//	            p.setPassword(password);
//	            p.setLastname("Bond");
//	            p.setFirstname("James");
//	            p.setAge(age);
//	            p.setSex(Player.Sex.H);
//	            p.setPhoneNumber("0707070707");
//	            p.setAddress("adresseSecrete");
//	            p.setPostCode(70007);
//	            p.setGames(new ArrayList<String>());
//	            p.setGamesType(new ArrayList<String>());
//	            p.setPlatforms(new ArrayList<String>());
//   
	        }
	    }
	    
	    processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // DOCTYPE + html + head
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"fr\">");
        out.println(HTMLBuilder.createHeadTag(PageTitle.HOME));

        // Body
        out.println("<body>");
            // Menu connection
            out.println(HTMLBuilder.createTopMenuConnection(INPUT_NAME_SIGNIN_LOGIN, INPUT_NAME_SIGNIN_PASSWORD, BTN_NAME_SIGNIN));

            /**
             * BEGIN OF MAIN CONTENT
             */
            out.println("<div class=\"container\">");
                out.println("<div id=\"home-content\">");
                    // Presentation
                    out.println("<div id=\"container-presentation\">");
                        out.println("<div class=\"icon-line\"><img src=\"assets/images/friendship-icon.png\" alt=\"friendship\"></div>");
                        out.println("<p>PTP est une communauté de joueurs d'Ile de France, pour organiser des sessions de jeux vidéos !</p>");
                        out.println("<br>");
                        
                        out.println("<div class=\"icon-line\"><img src=\"assets/images/home-icon.png\" alt=\"home\"></div>");
                        out.println("<p>Héberge une partie chez toi avec tes amis, ou rejoins une partie en IDF et fais toi de nouveaux amis !</p>");
                        out.println("<br>");
                        
                        out.println("<div class=\"icon-line\"><img src=\"assets/images/controller-icon.png\" alt=\"controller\"></div>");
                        out.println("<p>Ne joue plus jamais seul à tes jeux de sport ou de combat favoris et passe des soirées super fun !");
                        out.println("<br>");
                    out.println("</div>");
                    
                    // Sign up form
                    out.println("<div id=\"container-subscription-form\">");
                        // Display error message
                        if (errorMessageSignup != null) {
                            out.print("<p class=\"invalidField\">" + errorMessageSignup + "</p>");
                        }                    
                        out.println("<form method=\"post\">");
                            // Nickname
                            out.println("<input type=\"text\" name=\"" + INPUT_NAME_SIGNUP_LOGIN + "\"" +
                                                            " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_LOGIN)) + "\"" +
                                                            " placeholder=\"Pseudo\">");
                            // Email
                            out.println("<input type=\"email\" name=\"" + INPUT_NAME_SIGNUP_EMAIL + "\"" +
                                                             " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_EMAIL)) + "\"" +
                                                             " placeholder=\"Email\">");
                            out.println("<br>");
                            
                            // Address
                            out.println("<input type=\"text\" name=\"" + INPUT_NAME_SIGNUP_ADDRESS + "\"" +
                                                            " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_ADDRESS)) +"\">");
                            out.println("<br>");

                            // Age
                            out.println("<input type=\"number\" min=\"12\" max=\"99\" name=\"" + INPUT_NAME_SIGNUP_AGE + "\"" +
                                                                                    " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_AGE)) + "\"" +
                                                                                    " placeholder=\"Age\">");
                            out.println("<br>");
                            
                            // Password
                            out.println("<input type=\"password\" name=\"" + INPUT_NAME_SIGNUP_PASSWORD + "\" placeholder=\"Mot de passe\">");
                            out.println("<br>");

                            // Confirm password
                            out.println("<input type=\"password\" class=\"last-input\" name=\"" + INPUT_NAME_SIGNUP_CONFRIM_PASSWORD + "\" placeholder=\"Confrimation du mot de passe\">");
                            out.println("<br>");
                            
                            // Create account
                            out.println("<input type=\"submit\" name=\"" + BTN_NAME_SIGNUP + "\" value=\"Créer un compte\">");
                        out.println("</form>");
                    out.println("</div>");
                out.println("</div>");
            out.println("</div>");
            /**
             * END OF MAIN CONTENT
             */
            
            // Scripts
            out.println(HTMLBuilder.createScriptsTags());
        out.println("</body>");
        out.print("</html>");
	}
	
	private boolean isLoginValid(String login) {
        // Check length
        if (login == null || login.length() < MIN_CHAR_LOGIN) {
            errorMessageSignup = "Le pseudo doit contenir au moins " + MIN_CHAR_LOGIN + " caractères.";
            return false;
        }
        
        // Check format
        if (!login.matches(LOGIN_PATTERN)) {
            errorMessageSignup = "Le pseudo doit ne peut contenir que des caractères alphanumériques.";
            return false;
        }
        
        // Check if login already exists in DB
        if (playerDao.getPlayer(login) != null) {
            errorMessageSignup = "Le pseudo " + login + " existe déjà.";
            return false;
        }
	    
	    return true;
	}
}
