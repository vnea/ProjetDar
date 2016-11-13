package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.Predicate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Player;
import models.PlayerDao;

import org.apache.commons.codec.digest.DigestUtils;

import utils.HTMLBuilder;
import utils.StringUtils;
import dao.PlayerDaoImpl;
import enums.PageTitle;
import enums.SessionData;

/**
 * Servlet implementation class User
 */
public class Profil extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String PARAM_USER = "user";
	
	private static final String INPUT_NAME_LOGIN = "login";
    private static final String INPUT_NAME_FIRSTNAME = "firstname";
    private static final String INPUT_NAME_LASTNAME = "lastname";
    private static final String INPUT_NAME_SEX = "sex";
    private static final String INPUT_NAME_AGE = "age";
    private static final String INPUT_NAME_ADDRESS = "address";
    private static final String INPUT_NAME_POSTCODE = "postcode";
    private static final String INPUT_NAME_PHONENUMBER = "phonenumber";
    private static final String INPUT_NAME_EMAIL = "email";
    private static final String INPUT_NAME_PASSWORD = "password";
    private static final String INPUT_NAME_CONFRIM_PASSWORD = "confirm-password";
    
    private static final String BTN_NAME_MODIFY = "btn-modify";
    private static final String BTN_NAME_ADD_FRIEND = "btn-add-friend";
    private static final String BTN_NAME_DELETE_FRIEND = "btn-delete-friend";


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
    
    private PlayerDao playerDao;
    
    private String errorMessage;
    private Boolean success;
    
    private String username;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Profil() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
        playerDao = new PlayerDaoImpl();
        username = null;
        resetStatus();
        
        super.init();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false) == null) {
            response.sendRedirect(".");
            return;
        }
        
        processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getSession(false) == null) {
            response.sendRedirect(".");
            return;
        }
	    
        // Modify button pressed
        if (request.getParameter(BTN_NAME_MODIFY) != null) {
            String currentUsername = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
            // Verify that the user is the owner of the profil
            if (currentUsername.equals(username)) {
                performModify(request, response);
            }
            else {
                errorMessage = "Vous ne pouvez pas modifier les données du profil.";
            }
        }
        // Add friend
        else if (request.getParameter(BTN_NAME_ADD_FRIEND) != null) {
            performAddFriend(request, response);
        }
        // Delete friend
        else if (request.getParameter(BTN_NAME_DELETE_FRIEND) != null) {
            performDeleteFriend(request, response);
        }
        
        username = null;
        
	    processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");
	    response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // DOCTYPE + html + head
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"fr\">");
        out.println(HTMLBuilder.createHeadTag(PageTitle.PROFILE));
        
        // Body
        out.println("<body>");
            // Top menus
            String currentUsername = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());            
            out.println(HTMLBuilder.createTopMenu());
            out.println(HTMLBuilder.createTabsMenu(currentUsername));
            
            username = request.getParameter(PARAM_USER);
            out.println("<div class=\"container well\">");
                // User name not set
                if (username == null) {
                    out.println("<div class=\"col-xs-8 well\">");
                        out.println("<p class=\"errorMessage\">Le nom d'utilisateur n'a pas été spécifié.</p>");
                    out.println("</div>");
                }
                // User name set
                else {
                    username = username.trim();
                    Player player = playerDao.getPlayer(username);               
                    
                    // Allow authenticated user to modify their profil
                    if (currentUsername.equals(username)) {
                        out.println("<form method=\"post\">");
                            if (success != null) {
                                out.println(success ? "<p class=\"successMessage\">Les modifications ont bien été enregistrées.</p>"
                                                    : "<p class=\"errorMessage\">" + errorMessage + "</p>");
                                resetStatus();
                            }
                        
                            // Login
                            out.println(HTMLBuilder.createLabel(
                                    INPUT_NAME_LOGIN,
                                    "Nom d'utilisateur : " + username
                            ));
                            out.println("<br>");
                            
                            String firstname = StringUtils.getStr(request.getParameter(INPUT_NAME_FIRSTNAME));
                            firstname = firstname.isEmpty() ? player.getFirstname() : firstname;
                            // First name
                            out.println(HTMLBuilder.createLabelInput(
                                    INPUT_NAME_FIRSTNAME,
                                    INPUT_NAME_FIRSTNAME,
                                    "Prénom",
                                    firstname,
                                    "Entrez votre prénom"
                            ));
                            out.println("<br>");
                            
                            String lastname = StringUtils.getStr(request.getParameter(INPUT_NAME_LASTNAME));
                            lastname = lastname.isEmpty() ? player.getLastname() : lastname;
                            // Last name
                            out.println(HTMLBuilder.createLabelInput(
                                    INPUT_NAME_LASTNAME,
                                    INPUT_NAME_LASTNAME,
                                    "Nom",
                                    lastname,
                                    "Entrez votre nom"
                            ));
                            out.println("<br>");
    
                            String sex = StringUtils.getStr(request.getParameter(INPUT_NAME_SEX));
                            sex = sex.isEmpty() ? player.getSex().toString() : sex;
                            // Sex
                            out.println(HTMLBuilder.createLabel(
                                    INPUT_NAME_SEX,
                                    "Sexe : "
                            ));
                            // Man
                            out.println("<label class=\"radio-inline\"><input class=\"inputGender\" type=\"radio\"" +
                                                                                                        " name=\"" + INPUT_NAME_SEX + "\"" + 
                                                                                                        " value=\""+ Player.Sex.H + "\"" +
                                                                                                        (sex == null || Player.Sex.H.toString().equals(sex) ? " checked" : "") +
                                                                                                        ">H</label>");
                            
                            // Woman
                            out.println("<label class=\"radio-inline\"><input class=\"inputGender\" type=\"radio\"" +
                                                                                                        " name=\"" + INPUT_NAME_SEX + "\"" + 
                                                                                                        " value=\""+ Player.Sex.F + "\"" +
                                                                                                         (Player.Sex.F.toString().equals(sex) ? " checked" : "") +
                                                                                                        ">F</label>");
                            out.println("<br>");
    
                            
                            String age = StringUtils.getStr(request.getParameter(INPUT_NAME_AGE));
                            // Age
                            out.println(HTMLBuilder.createLabel(
                                    INPUT_NAME_AGE,
                                    "Age"
                            ));
                            out.println("<br>");
                            out.println("<input class=\"form-control\" type=\"number\" min=\"12\" name=\"" + INPUT_NAME_AGE + "\"" +
                                                                                                " value=\"" + (age.isEmpty() ? player.getAge() : age)+ "\"" +
                                                                                                " placeholder=\"Entreez votre âge\">");
                            out.println("<br>");
                            
                            String address = StringUtils.getStr(request.getParameter(INPUT_NAME_ADDRESS));
                            address = address.isEmpty() && player.getAddress() != null ? player.getAddress() : address;
                            // Address
                            out.println(HTMLBuilder.createLabelInput(
                                    INPUT_NAME_ADDRESS,
                                    INPUT_NAME_ADDRESS,
                                    "Adresse",
                                    address,
                                    "Entrez votre adresse avec votre ville (ex: 4 place Jussieu Paris)"
                            ));
                            out.println("<br>");
                            
                            String postcode = StringUtils.getStr(request.getParameter(INPUT_NAME_POSTCODE));
                            postcode = postcode.isEmpty() && player.getPostCode() != null ? player.getPostCode() : postcode;
                            // Post code
                            out.println(HTMLBuilder.createLabelInput(
                                    INPUT_NAME_POSTCODE,
                                    INPUT_NAME_POSTCODE,
                                    "Code postal",
                                    postcode,
                                    "Entrez votre code postal"
                            ));
                            out.println("<br>");
                            
                            // Phone
                            String phonenumber = StringUtils.getStr(request.getParameter(INPUT_NAME_PHONENUMBER));
                            phonenumber = phonenumber.isEmpty() && player.getPhoneNumber() != null ? player.getPhoneNumber() : phonenumber;
                            out.println(HTMLBuilder.createLabel(
                                    INPUT_NAME_PHONENUMBER,
                                    "Numéro de téléphone"
                            ));
                            out.println("<br>");
                            out.println("<input class=\"form-control\" type=\"tel\" name=\"" + INPUT_NAME_PHONENUMBER + "\"" +
                                                                                  " value=\"" + phonenumber + "\"" +
                                                                                  " placeholder=\"Entrez votre n° de téléphone\">");
                            out.println("<br>");
                            
                            String email = StringUtils.getStr(request.getParameter(INPUT_NAME_EMAIL));
                            email = email.isEmpty() ? player.getEmail() : email;
                            // E-mail
                            out.println(HTMLBuilder.createLabel(
                                    INPUT_NAME_EMAIL,
                                    "E-mail"
                            ));
                            out.println("<br>");
                            out.println("<input class=\"form-control\" type=\"email\" name=\"" + INPUT_NAME_EMAIL + "\"" +
                                                                                    " value=\"" + email + "\"" +
                                                                                    " placeholder=\"Entrez votre adresse e-mail\">");
                            out.println("<br>");
    
                            
                            // Password
                            out.println(HTMLBuilder.createLabel(
                                    INPUT_NAME_PASSWORD,
                                    "Mot de passe"
                            ));
                            out.println("<br>");
                            out.println("<input class=\"form-control\" type=\"password\" name=\"" + INPUT_NAME_PASSWORD + "\" placeholder=\"Entrez un mot de passe\">");
    
                            // Confirm password
                            out.println(HTMLBuilder.createLabel(
                                    INPUT_NAME_CONFRIM_PASSWORD,
                                    "Confrimation du mot de passe"
                            ));
                            out.println("<br>");
                            out.println("<input type=\"password\" class=\"last-input form-control\" name=\"" + INPUT_NAME_CONFRIM_PASSWORD + "\" placeholder=\"Confrimez le mot de passe\">");
                            out.println("<br>");

                            // Create account
                            out.println("<input class=\"form-control\" type=\"submit\" name=\"" + BTN_NAME_MODIFY + "\" value=\"Modifier\">");
                        out.println("</form>");
                    }
                    // Other profil
                    else {
                        out.println("<div>");
                            if (success != null) {
                                out.println(success ? "<p class=\"successMessage\">" + errorMessage + "</p>"
                                                    : "<p class=\"errorMessage\">" + errorMessage + "</p>");
                                resetStatus();
                            }
                        
                            // Login
                            out.println(HTMLBuilder.createLabel(
                                    INPUT_NAME_LOGIN,
                                    "Nom d'utilisateur : " + username
                            ));
                            out.println("<br>");
                            
                            // First name
                            out.println(HTMLBuilder.createLabelInputDisabled(
                                    INPUT_NAME_FIRSTNAME,
                                    INPUT_NAME_FIRSTNAME,
                                    "Prénom",
                                    player.getFirstname()
                            ));
                            out.println("<br>");
                            
                            // Last name
                            out.println(HTMLBuilder.createLabelInputDisabled(
                                    INPUT_NAME_LASTNAME,
                                    INPUT_NAME_LASTNAME,
                                    "Nom",
                                    player.getLastname()
                            ));
                            out.println("<br>");
    

                            // Sex
                            out.println(HTMLBuilder.createLabelInputDisabled(
                                    INPUT_NAME_SEX,
                                    INPUT_NAME_SEX,
                                    "Nom",
                                    player.getSex().toString()
                            ));
                            out.println("<br>");
                            
                            // Age
                            out.println(HTMLBuilder.createLabel(
                                    INPUT_NAME_AGE,
                                    "Age"
                            ));
                            out.println("<br>");
                            out.println("<input disabled class=\"form-control\" type=\"number\" min=\"12\" name=\"" + INPUT_NAME_AGE + "\"" +
                                                                                                " value=\"" + player.getAge() + "\"" +
                                                                                                " placeholder=\"Entreez votre âge\">");
                            out.println("<br>");
                            
                            String address = StringUtils.getStr(player.getAddress());
                            // Address
                            out.println(HTMLBuilder.createLabelInputDisabled(
                                    INPUT_NAME_ADDRESS,
                                    INPUT_NAME_ADDRESS,
                                    "Adresse",
                                    address
                            ));
                            out.println("<br>");
                            
                            String postcode = StringUtils.getStr(player.getPostCode());
                            // Post code
                            out.println(HTMLBuilder.createLabelInputDisabled(
                                    INPUT_NAME_POSTCODE,
                                    INPUT_NAME_POSTCODE,
                                    "Code postal",
                                    postcode
                            ));
                            out.println("<br>");
                            
                            // Phone
                            String phonenumber = StringUtils.getStr(player.getPhoneNumber());
                            out.println(HTMLBuilder.createLabel(
                                    INPUT_NAME_PHONENUMBER,
                                    "Numéro de téléphone"
                            ));
                            out.println("<br>");
                            out.println("<input disabled class=\"form-control\" type=\"tel\" name=\"" + INPUT_NAME_PHONENUMBER + "\"" +
                                                                                  " value=\"" + phonenumber + "\">");
                            out.println("<br>");
                            
                            // E-mail
                            out.println(HTMLBuilder.createLabel(
                                    INPUT_NAME_EMAIL,
                                    "E-mail"
                            ));
                            out.println("<br>");
                            out.println("<input disabled class=\"form-control\" type=\"email\" name=\"" + INPUT_NAME_EMAIL + "\"" +
                                                                                    " value=\"" + player.getEmail() + "\">");
                            out.println("<br>");
    
                            Player currentAuthenticatedPlayer = playerDao.getPlayer(currentUsername);
                            // Already friends
                            List<Player> friends = currentAuthenticatedPlayer.getFriends();
                            boolean areFriends = false;
                            for (Player friend : friends) {
                                if (friend.getUsername().equals(username)) {
                                    areFriends = true;
                                    break;
                                }
                            }
                            
                            out.println("<form method=\"post\">");
                            if (areFriends) {
                                // Delete friend
                                out.println("<input class=\"form-control\" type=\"submit\" name=\"" + BTN_NAME_DELETE_FRIEND + "\" value=\"Supprimer de ma liste d'amis\">");
                            }
                            else {
                                // Add friend
                                out.println("<input class=\"form-control\" type=\"submit\" name=\"" + BTN_NAME_ADD_FRIEND + "\" value=\"Ajouter à ma liste d'amis\">");
                            }
                            out.println("</form>");


                        out.println("</div>");
                    }
                }
            out.println("</div>");
                                    
            // Scripts
            out.println(HTMLBuilder.createScriptsTags());
        out.println("</body>");
        out.print("</html>");   
	}
	
   private boolean isFirstnameValid(String firstname) {
        // Check if first name is not empty
       if (firstname.isEmpty()) {
            errorMessage = "Vous devez spécifier votre prénom.";
            return false;
        }
    
       // Check if first name is valid
        if (!firstname.matches(NAME_REGEX)) {
            errorMessage = "Le prénom est invalide.";
            return false;
        }
        
        return true;
    }
    
   private boolean isLastnameValid(String lastname) {
       // Check if last name is not empty
       if (lastname.isEmpty()) {
            errorMessage = "Vous devez spécifier votre nom.";
            return false;
        }
       
       // Check if last name is valid
       if (!lastname.matches(NAME_REGEX)) {
           errorMessage = "Le nom invalide.";
           return false;
       }
        
        return true;
    }
   
   private boolean isSexValid(String sex) {
       // Check is sex value is valid
       if (!Player.Sex.H.toString().equals(sex) && !Player.Sex.F.toString().equals(sex)) {
            errorMessage = "Le sexe est invalide.";
            return false;
        }
        
        return true;
    }
    
   private boolean isAgeValid(String age) {
       try {
           // Check min age
           if (Integer.parseInt(age) < MIN_AGE) {
               errorMessage = "Vous devez au moins avoir " + MIN_AGE + " pour vous inscrire.";
               return false;
           }

           return true;
       }
       // Age if invalid if someone tried to send a text (and not a number)
       catch (NumberFormatException ex) {
           errorMessage = "L'âge spécifié est invalide.";
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
           errorMessage = "Le code postal est invalide.";
           return false;
       }
       
       return true;
   }
   
   private boolean isPhonenumberValid(String phonenumber) {
       // Check if phone number is valid (the phone number can be empty)
       if (!phonenumber.isEmpty() && !phonenumber.matches(PHONENUMBER_REGEX)) {
           errorMessage = "Le numéroe de téléphone est invalide.";
           return false;
       }
       
       return true;
   }
   
   private boolean isEmailValid(String email) {
        // Check if email is not empty
        if (email.isEmpty()) {
            errorMessage = "Vous devez spécifier une adresse email.";
            return false;
        }
        
        // Check if email is valid
        if (!email.matches(EMAIL_REGEX)) {
            errorMessage = "L'adresse email est invalide.";
            return false;
        }
        
        return true;
    }
   
   private boolean isPasswordValid(String password, String confirmPassword) {
       // Check length
       if (password.length() < MIN_CHAR_PASSWORD) {
           errorMessage = "Le mot de passe doit contenir au moins " + MIN_CHAR_PASSWORD + " caractères.";
           return false;
       }
       
       // Check equality
       if (!password.equals(confirmPassword)) {
           errorMessage = "Les deux mots de passe sont différents.";
           return false;
       }
       
       return true;
   }
   
   private void performModify(HttpServletRequest request, HttpServletResponse response) throws IOException {
       String firstname = StringUtils.getStr(request.getParameter(INPUT_NAME_FIRSTNAME));
       String lastname = StringUtils.getStr(request.getParameter(INPUT_NAME_LASTNAME));
       
       String sex = StringUtils.getStr(request.getParameter(INPUT_NAME_SEX));
       String age = StringUtils.getStr(request.getParameter(INPUT_NAME_AGE));
       
       String address = StringUtils.getStr(request.getParameter(INPUT_NAME_ADDRESS));
       String postcode = StringUtils.getStr(request.getParameter(INPUT_NAME_POSTCODE));
       
       String phonenumber = StringUtils.getStr(request.getParameter(INPUT_NAME_PHONENUMBER));
       String email = StringUtils.getStr(request.getParameter(INPUT_NAME_EMAIL));
       String password = StringUtils.getStr(request.getParameter(INPUT_NAME_PASSWORD));
       String confirmPassword = StringUtils.getStr(request.getParameter(INPUT_NAME_CONFRIM_PASSWORD));

       // Check if sent values are OK
       if (success = 
           isFirstnameValid(firstname) && isLastnameValid(lastname) &&
           isSexValid(sex) && isAgeValid(age) && 
           isAddressValid(address) && isPostCodeValid(postcode) &&
           isPhonenumberValid(phonenumber) && isEmailValid(email)) {
           
           Player player = playerDao.getPlayer(username);
           
           // Only modify password if it has a value
           if (!password.isEmpty() || !confirmPassword.isEmpty()) {
               // Password is ok
               if (success =
                   isPasswordValid(password, confirmPassword)) {
                   player.setPassword(DigestUtils.sha1Hex(password));
               }
               else {
                   return;
               }
           }
           
           // Create new Player
           player.setFirstname(firstname);
           player.setLastname(lastname);
           
           player.setSex(Player.Sex.valueOf(sex));
           player.setAge(Integer.parseInt(age));
           
           player.setAddress(address);
           player.setPostCode(postcode);
           player.setPhoneNumber(phonenumber);
           
           player.setEmail(email);
           // Update player
           playerDao.update(player);
       }
   }

   private void performAddFriend(HttpServletRequest request, HttpServletResponse response) throws IOException {
       String currentUsername = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
       Player currentAuthenticatedPlayer = playerDao.getPlayer(currentUsername);
       
       if (currentUsername.equals(username)) {
           success = false;
           errorMessage = "Vous ne pouvez pas vous ajoutez à votre liste d'amis.";
       }
       else {
           List<Player> friends = currentAuthenticatedPlayer.getFriends();
           boolean areFriends = false;
           for (Player friend : friends) {
               if (friend.getUsername().equals(username)) {
                   areFriends = true;
                   break;
               }
           }
           
           // Already friends
           if (areFriends) {
               success = false;
               errorMessage = "Vous êtes déjà ami avec " + username + ".";
           }
           else {
               Player otherPlayer = playerDao.getPlayer(username);
               friends.add(otherPlayer);
               otherPlayer.getFriends().add(currentAuthenticatedPlayer);
               
               playerDao.update(currentAuthenticatedPlayer);
               playerDao.update(otherPlayer);
               
               success = true;
               errorMessage = "Vous êtes désormais ami avec " + username + ".";
           }
       }
   }
   
   private void performDeleteFriend(HttpServletRequest request, HttpServletResponse response) throws IOException {
       final String currentUsername = (String) request.getSession().getAttribute(SessionData.PLAYER_USERNAME.toString());
       Player currentAuthenticatedPlayer = playerDao.getPlayer(currentUsername);
       
       if (currentUsername.equals(username)) {
           success = false;
           errorMessage = "Vous ne pouvez pas vous supprimez de votre liste d'amis.";
       }
       else {
           List<Player> friends = currentAuthenticatedPlayer.getFriends();
           boolean areFriends = false;
           for (Player friend : friends) {
               if (friend.getUsername().equals(username)) {
                   areFriends = true;
                   break;
               }
           }
           
           // Are friends so we can perform delete
           if (areFriends) {
               Player otherPlayer = playerDao.getPlayer(username);
               
               // Need to change this ugly code...
               Predicate<Player> pred1 = new Predicate<Player>() {
                   @Override
                   public boolean test(Player arg0) {
                       return currentUsername.equals(arg0.getUsername());
                   }
               };
               otherPlayer.getFriends().removeIf(pred1);
               
               Predicate<Player> pred2 = new Predicate<Player>() {
                   @Override
                   public boolean test(Player arg0) {
                       return username.equals(arg0.getUsername());
                   }
               };
               friends.removeIf(pred2);
               // End of ugly code
               
               playerDao.update(currentAuthenticatedPlayer);
               playerDao.update(otherPlayer);

               success = true;
               errorMessage = "Vous n'êtes plus ami avec " + username + ".";
           }
           else {
               success = false;
               errorMessage = "Vous n'êtes pas ami avec" + username + ".";
           }
       }
   }
   
   private void resetStatus() {
       errorMessage = null;
       success = null;
   }

}
