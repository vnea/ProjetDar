package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Player;
import model.PlayerDao;

import org.apache.commons.codec.digest.DigestUtils;

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
    private static final String INPUT_NAME_SIGNUP_FIRSTNAME = "firstname-signup";
    private static final String INPUT_NAME_SIGNUP_LASTNAME = "lastname-signup";
    
    private static final String INPUT_NAME_SIGNUP_SEX = "sex-signup";
    private static final String INPUT_NAME_SIGNUP_AGE = "age-signup";

    private static final String INPUT_NAME_SIGNUP_ADDRESS = "address-signup";
    private static final String INPUT_NAME_SIGNUP_POSTCODE = "postcode-signup";
    
    private static final String INPUT_NAME_SIGNUP_PHONENUMBER = "phonenumber-signup";
    private static final String INPUT_NAME_SIGNUP_EMAIL = "email-signup";
    private static final String INPUT_NAME_SIGNUP_PASSWORD = "password-signup";
    private static final String INPUT_NAME_SIGNUP_CONFRIM_PASSWORD = "confirm-password-signup";
    private static final String BTN_NAME_SIGNUP = "btn-signup";
    
    // Login criteria
    private static int MIN_CHAR_LOGIN = 3;
    private static String LOGIN_REGEX = "^[\\p{Alnum}]{" + MIN_CHAR_LOGIN + ",}$"; // Only alphanum char
    
    // Age criteria
    private static final int MIN_AGE = 12;
    
    // Name criteria
    private static final String NAME_REGEX = "^[\\p{L} .'-]+$";
    
    // Phone number criteria
    private static final String PHONENUMBER_REGEX = "^0[1-9]([-. ]?[0-9]{2}){4}$";
    
    // Email criteria
    private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    // Post code criteria
    private static final String POSTCODE_REGEX = "\\p{Digit}{5}";
    
    
    // Password criteria
    private static final int MIN_CHAR_PASSWORD = 6;
    
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
	    request.setCharacterEncoding("UTF-8");
	    
	    // Login button pressed
	    if (request.getParameter(BTN_NAME_SIGNIN) != null) {
	    }
	    
	    // Signup button pressed
	    else if (request.getParameter(BTN_NAME_SIGNUP) != null) {
	        String login = request.getParameter(INPUT_NAME_SIGNUP_LOGIN).trim();
	        String firstname = request.getParameter(INPUT_NAME_SIGNUP_FIRSTNAME).trim();
	        String lastname = request.getParameter(INPUT_NAME_SIGNUP_LASTNAME).trim();
	        
	        String sex = request.getParameter(INPUT_NAME_SIGNUP_SEX).trim();
	        String age = request.getParameter(INPUT_NAME_SIGNUP_AGE).trim();
	        
	        String address = request.getParameter(INPUT_NAME_SIGNUP_ADDRESS).trim();
	        String postcode = request.getParameter(INPUT_NAME_SIGNUP_POSTCODE).trim();
	        
	        String phonenumber = request.getParameter(INPUT_NAME_SIGNUP_PHONENUMBER).trim();
	        String email = request.getParameter(INPUT_NAME_SIGNUP_EMAIL).trim();
	        String password = request.getParameter(INPUT_NAME_SIGNUP_PASSWORD).trim();
	        String confirmPassword = request.getParameter(INPUT_NAME_SIGNUP_CONFRIM_PASSWORD).trim();

	        // Check if sent values are OK
	        if (isLoginValid(login) && isFirstnameValid(firstname) && isLastnameValid(lastname) &&
	            isSexValid(sex) && isAgeValid(age) && 
	            isAddressValid(address) && isPostCodeValid(postcode) &&
	            isPhonenumberValid(phonenumber) && isEmailValid(email) && isPasswordValid(password, confirmPassword)) {
	            
	            // Create new Player
	            Player player = new Player();
	            player.setUsername(login);
	            player.setFirstname(firstname);
	            player.setLastname(lastname);
	            
	            player.setSex(Player.Sex.valueOf(sex));
	            player.setAge(Integer.parseInt(age));
	            
	            player.setAddress(address);
	            player.setPostCode(null);
	            player.setPhoneNumber(null);
	            
	            player.setEmail(email);
	            player.setPassword(DigestUtils.sha1Hex(password));

	            player.setGames(new ArrayList<String>());
	            player.setGamesType(new ArrayList<String>());
	            player.setPlatforms(new ArrayList<String>());
	            
	            // Insert in database
	            playerDao.insertPlayer(player);
	            
	            // Create session and redirect to MainPage
//	            HttpSession session = request.getSession();
//	            session.setAttribute("player", player);
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
                            
                            // First name
                            out.println("<input type=\"text\" name=\"" + INPUT_NAME_SIGNUP_FIRSTNAME + "\"" +
                                                            " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_FIRSTNAME)) + "\"" +
                                                            " placeholder=\"Prénom\">");
                            
                            // Last name
                            out.println("<input type=\"text\" name=\"" + INPUT_NAME_SIGNUP_LASTNAME + "\"" +
                                                            " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_LASTNAME)) + "\"" +
                                                            " placeholder=\"Nom\">");
                            // Sex
                            out.println("<label class=\"radio-inline white\"><input class=\"inputGender\" type=\"radio\" name=\"" + INPUT_NAME_SIGNUP_SEX + "\"" + 
                                                                                                                       " value=\""+ Player.Sex.H + "\" checked>H</label>");
                            out.println("<label class=\"radio-inline white\"><input class=\"inputGender\" type=\"radio\" name=\"" + INPUT_NAME_SIGNUP_SEX + "\"" + 
                                                                                                                       " value=\""+ Player.Sex.F + "\">F</label>");
                            
                            // Age
                            out.println("<input type=\"number\" min=\"12\" name=\"" + INPUT_NAME_SIGNUP_AGE + "\"" +
                                                                         " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_AGE)) + "\"" +
                                                                         " placeholder=\"Age\">");

                            // Address
                            out.println("<input type=\"text\" name=\"" + INPUT_NAME_SIGNUP_ADDRESS + "\"" +
                                                            " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_ADDRESS)) + "\"" +
                                                            " placeholder=\"Adresse\">");
                            
                            // Post code
                            out.println("<input type=\"text\" maxlength=\"5\" name=\"" + INPUT_NAME_SIGNUP_POSTCODE + "\"" +
                                                                            " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_POSTCODE)) + "\"" +
                                                                            " placeholder=\"Code postal\">");
                            
                            // Email
                            out.println("<input type=\"tel\" name=\"" + INPUT_NAME_SIGNUP_PHONENUMBER + "\"" +
                                                             " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_PHONENUMBER)) + "\"" +
                                                             " placeholder=\"Tél.\">");
                            // Email
                            out.println("<input type=\"email\" name=\"" + INPUT_NAME_SIGNUP_EMAIL + "\"" +
                                                             " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_EMAIL)) + "\"" +
                                                             " placeholder=\"Email\">");
                            
                            // Password
                            out.println("<input type=\"password\" name=\"" + INPUT_NAME_SIGNUP_PASSWORD + "\" placeholder=\"Mot de passe\">");

                            // Confirm password
                            out.println("<input type=\"password\" class=\"last-input\" name=\"" + INPUT_NAME_SIGNUP_CONFRIM_PASSWORD + "\" placeholder=\"Confrimation du mot de passe\">");
                            
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
        if (login.length() < MIN_CHAR_LOGIN) {
            errorMessageSignup = "Le pseudo doit contenir au moins " + MIN_CHAR_LOGIN + " caractères.";
            return false;
        }
        
        // Check format
        if (!login.matches(LOGIN_REGEX)) {
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
	
	private boolean isFirstnameValid(String firstname) {
	    // Check if first name is not empty
       if (firstname.isEmpty()) {
            errorMessageSignup = "Vous devez spécifier votre prénom.";
            return false;
        }
	
       // Check if first name is valid
	    if (!firstname.matches(NAME_REGEX)) {
            errorMessageSignup = "Le prénom est invalide.";
            return false;
	    }
        
        return true;
	}
	
   private boolean isLastnameValid(String lastname) {
       // Check if last name is not empty
       if (lastname.isEmpty()) {
            errorMessageSignup = "Vous devez spécifier votre nom.";
            return false;
        }
       
       // Check if last name is valid
       if (!lastname.matches(NAME_REGEX)) {
           errorMessageSignup = "Le nom invalide.";
           return false;
       }
        
        return true;
    }
   
   private boolean isSexValid(String sex) {
       // Check is sex value is valid
       if (!Player.Sex.H.toString().equals(sex) && !Player.Sex.F.toString().equals(sex)) {
            errorMessageSignup = "Le sexe est invalide.";
            return false;
        }
        
        return true;
    }
	
   private boolean isAgeValid(String age) {
       try {
           // Check min age
           if (Integer.parseInt(age) < MIN_AGE) {
               errorMessageSignup = "Vous devez au moins avoir " + MIN_AGE + " pour vous inscrire.";
               return false;
           }

           return true;
       }
       // Age if invalid if someone tried to send a text (and not a number)
       catch (NumberFormatException ex) {
           errorMessageSignup = "L'âge spécifié est invalide.";
           return false;
       }
   }
   
   private boolean isAddressValid(String address) {
       // NEED TO CHECK
       
       return true;
   }
   
   private boolean isPostCodeValid(String postcode) {
       // Check if post code is valid (the post code can be empty)
       if (!postcode.isEmpty() && !postcode.matches(POSTCODE_REGEX)) {
           errorMessageSignup = "Le code postal est invalide.";
           return false;
       }
       
       return true;
   }
   
   private boolean isPhonenumberValid(String phonenumber) {
       // Check if phone number is valid (the phone number can be empty)
       if (!phonenumber.isEmpty() && !phonenumber.matches(PHONENUMBER_REGEX)) {
           errorMessageSignup = "Le numéroe de téléphone est invalide.";
           return false;
       }
       
       return true;
   }
   
   private boolean isEmailValid(String email) {
        // Check if email is not empty
        if (email.isEmpty()) {
            errorMessageSignup = "Vous devez spécifier une adresse email.";
            return false;
        }
        
        // Check if email is valid
        if (!email.matches(EMAIL_REGEX)) {
            errorMessageSignup = "L'adresse email est invalide.";
            return false;
        }
        
        return true;
    }
   
   private boolean isPasswordValid(String password, String confirmPassword) {
       // Check length
       if (password.length() < MIN_CHAR_PASSWORD) {
           errorMessageSignup = "Le mot de passe doit contenir au moins " + MIN_CHAR_PASSWORD + " caractères.";
           return false;
       }
       
       // Check equality
       if (!password.equals(confirmPassword)) {
           errorMessageSignup = "Les deux mots de passe sont différents.";
           return false;
       }
       
       return true;
   }
}
