package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Player;
import models.PlayerDao;

import org.apache.commons.codec.digest.DigestUtils;

import utils.HTMLBuilder;
import utils.StringUtils;
import dao.PlayerDaoImpl;
import enums.PageTitle;
import enums.SessionData;

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
    private static final String NAME_REGEX = "^[\\p{L}][\\p{L} .'-]*[\\p{L}]$";
    
    // Phone number criteria
    private static final String PHONENUMBER_REGEX = "^0[1-9]([-. ]?[0-9]{2}){4}$";
    
    // Email criteria
    private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    // Post code criteria
    private static final String POSTCODE_REGEX = "\\p{Digit}{5}";
    
    
    // Password criteria
    private static final int MIN_CHAR_PASSWORD = 6;
    
    private String errorMessageSignup;
    private String errorMessageSignin;
       
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
        resetErrorMessages();
    }
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // If session exists redirect to main page
        if (request.getSession(false) != null) {
            response.sendRedirect("MainPage");
            return;
        }
        
        resetErrorMessages();
        processRequest(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        // If session exists redirect to main page
        if (request.getSession(false) != null) {
            response.sendRedirect("MainPage");
            return;
        }
        
        resetErrorMessages();
        
        // Login button pressed
        if (request.getParameter(BTN_NAME_SIGNIN) != null) {
            performSignin(request, response);
        }
        
        // Signup button pressed
        else if (request.getParameter(BTN_NAME_SIGNUP) != null) {
            performSignup(request, response);
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
            out.println(HTMLBuilder.createTopMenuConnection(INPUT_NAME_SIGNIN_LOGIN, INPUT_NAME_SIGNIN_PASSWORD, BTN_NAME_SIGNIN, errorMessageSignin));

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
                            out.print("<p class=\"errorMessage\">" + errorMessageSignup + "</p>");
                        }              
                        out.println("<form method=\"post\">");
                            // Nickname
                            out.println("<input type=\"text\" name=\"" + INPUT_NAME_SIGNUP_LOGIN + "\"" +
                                                            " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_LOGIN)) + "\"" +
                                                            " placeholder=\"Entrez un pseudo\">");
                            
                            // First name
                            out.println("<input type=\"text\" name=\"" + INPUT_NAME_SIGNUP_FIRSTNAME + "\"" +
                                                            " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_FIRSTNAME)) + "\"" +
                                                            " placeholder=\"Entrez votre prénom\">");
                            
                            // Last name
                            out.println("<input type=\"text\" name=\"" + INPUT_NAME_SIGNUP_LASTNAME + "\"" +
                                                            " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_LASTNAME)) + "\"" +
                                                            " placeholder=\"Entrez votre nom\">");
                            
                            String sex = request.getParameter(INPUT_NAME_SIGNUP_SEX);
                            // In case if sex is different from Sex.H or Sex.F
                            sex = Player.Sex.H.toString().equals(sex) || Player.Sex.F.toString().equals(sex) ? sex : null;
                            
                            out.println("<label class=\"white\" for=\"" + INPUT_NAME_SIGNUP_SEX + "\">Sexe : </label>\n");
                            // Man
                            out.println("<label class=\"radio-inline white\"><input class=\"inputGender\" type=\"radio\"" +
                                                                                                        " name=\"" + INPUT_NAME_SIGNUP_SEX + "\"" + 
                                                                                                        " value=\""+ Player.Sex.H + "\"" +
                                                                                                        (sex == null || Player.Sex.H.toString().equals(sex) ? " checked" : "") +
                                                                                                        ">H</label>");
                            
                            // Woman
                            out.println("<label class=\"radio-inline white\"><input class=\"inputGender\" type=\"radio\"" +
                                                                                                        " name=\"" + INPUT_NAME_SIGNUP_SEX + "\"" + 
                                                                                                        " value=\""+ Player.Sex.F + "\"" +
                                                                                                         (Player.Sex.F.toString().equals(sex) ? " checked" : "") +
                                                                                                        ">F</label>");
                            
                            // Age
                            out.println("<input type=\"number\" min=\"12\" name=\"" + INPUT_NAME_SIGNUP_AGE + "\"" +
                                                                         " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_AGE)) + "\"" +
                                                                         " placeholder=\"Indiquez votre âge\">");

                            // Address
                            out.println("<input type=\"text\" name=\"" + INPUT_NAME_SIGNUP_ADDRESS + "\"" +
                                                            " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_ADDRESS)) + "\"" +
                                                            " placeholder=\"Entrez votre adresse avec votre ville (ex: 4 place Jussieu Paris)\">");
                            
                            // Post code
                            out.println("<input type=\"text\" maxlength=\"5\" name=\"" + INPUT_NAME_SIGNUP_POSTCODE + "\"" +
                                                                            " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_POSTCODE)) + "\"" +
                                                                            " placeholder=\"Entrez votre code postal\">");
                            
                            // Phone
                            out.println("<input type=\"tel\" name=\"" + INPUT_NAME_SIGNUP_PHONENUMBER + "\"" +
                                                             " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_PHONENUMBER)) + "\"" +
                                                             " placeholder=\"Entrez votre n° de téléphone\">");
                            // Email
                            out.println("<input type=\"email\" name=\"" + INPUT_NAME_SIGNUP_EMAIL + "\"" +
                                                             " value=\"" + StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_EMAIL)) + "\"" +
                                                             " placeholder=\"Entrez votre adresse e-mail\">");
                            
                            // Password
                            out.println("<input type=\"password\" name=\"" + INPUT_NAME_SIGNUP_PASSWORD + "\" placeholder=\"Entrez un mot de passe\">");

                            // Confirm password
                            out.println("<input type=\"password\" class=\"last-input\" name=\"" + INPUT_NAME_SIGNUP_CONFRIM_PASSWORD + "\" placeholder=\"Confirmer le mot de passe\">");
                            
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
   
   private void performSignup(HttpServletRequest request, HttpServletResponse response) throws IOException {
       String login = StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_LOGIN));
       String firstname = StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_FIRSTNAME));
       String lastname = StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_LASTNAME));
       
       String sex = StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_SEX));
       String age = StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_AGE));
       
       String address = StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_ADDRESS));
       String postcode = StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_POSTCODE));
       
       String phonenumber = StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_PHONENUMBER));
       String email = StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_EMAIL));
       String password = StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_PASSWORD));
       String confirmPassword = StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNUP_CONFRIM_PASSWORD));

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
           player.setPostCode(postcode);
           player.setPhoneNumber(phonenumber);
           
           player.setEmail(email);
           player.setPassword(DigestUtils.sha1Hex(password));

           player.setGames(new ArrayList<String>());
           player.setGamesType(new ArrayList<String>());
           player.setPlatforms(new ArrayList<String>());
           
           // Insert in database
           playerDao.insert(player);
           
           // Create session and redirect to MainPage
           HttpSession session = request.getSession();
           session.setAttribute(SessionData.PLAYER_USERNAME.toString(), player.getUsername());
           response.sendRedirect("MainPage");
       }
   }
   
   private void performSignin(HttpServletRequest request, HttpServletResponse response) throws IOException {
       Player player = playerDao.getPlayer(
               StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNIN_LOGIN)),
               // Don't forget to encrypt password
               DigestUtils.sha1Hex(StringUtils.getStr(request.getParameter(INPUT_NAME_SIGNIN_PASSWORD)))
       );
       
       // Login or password invalid
       if (player == null) {
           errorMessageSignin = "Le pseudo ou le mot de passe est invalide.";
       }
       // Success
       else {
           // Create session and redirect to MainPage
           HttpSession session = request.getSession();
           session.setAttribute(SessionData.PLAYER_USERNAME.toString(), player.getUsername());
           response.sendRedirect("MainPage");
       }
   }
   
   private void resetErrorMessages() {
       errorMessageSignup = null;
       errorMessageSignin = null;
   }

}
