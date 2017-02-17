package utils;

import java.util.ArrayList;
import java.util.List;

import models.GameSession;
import models.Player;

import org.apache.commons.lang3.StringEscapeUtils;

import enums.PageTitle;

public class HTMLBuilder {

    public static String createHeadTag(PageTitle pageTitle) {
        String
        head = "<head>\n";
            // The below 3 meta tags *must* come first in the head; any other head content must come *after* these tags
            head += "<meta charset=\"utf-8\">\n";
            head += "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n";
            head += "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n\n";
            
            head += "<link rel=\"icon\" href=\"assets/images/favicon.ico\">\n";
            head += "<title>" + pageTitle +"</title>\n\n";
            
            // CSS
            head += "<link href=\"assets/css/master.css\" rel=\"stylesheet\">\n\n";
            
            head += " <script src=\"assets/js/modernizr-2.6.2.min.js\"></script>\n\n";

            head += "<!--[if lt IE 9]><script src=\"assets/js/ie8-responsive-file-warning.js\"></script><![endif]-->\n";
        
            // HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries
            head += "<!--[if lt IE 9]>\n";
                head += "<script src=\"assets/js/html5shiv.min.js\"></script>\n";
                head += "<script src=\"assets/js/respond.min.js\"></script>\n";
            head += "<![endif]-->\n";
        head += "</head>";
        
        return head;
    }
    
    public static String createScriptsTags() {
        String
        
        // Bootstrap core JavaScript
        scripts = "<script src=\"assets/js/jquery-3.1.1.min.js\"></script>\n";
        scripts += "<script src=\"assets/js/bootstrap.min.js\"></script>\n";
        
        // IE10 viewport hack for Surface/desktop Windows 8 bug
        scripts += "<script src=\"assets/js/ie10-viewport-bug-workaround.js\"></script>\n";
        scripts += "<script src=\"assets/js/script.js\"></script>\n";
        
        return scripts;
    }
    
    public static String createTopMenu(String content) {
        String
        menu = "<nav class=\"navbar navbar-inverse navbar-fixed-top\">\n";
        	menu += "<div class=\"container-fluid\">\n";
	            menu += "<div class=\"navbar-header\">\n";
	                menu += "<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar\" aria-expanded=\"false\" aria-controls=\"navbar\">\n";
	                    menu += "<span class=\"sr-only\">Toggle navigation</span>\n";
	                    menu += "<span class=\"icon-bar\"></span>\n";
	                    menu += "<span class=\"icon-bar\"></span>\n";
	                    menu += "<span class=\"icon-bar\"></span>\n";
	                menu += "</button>\n";
	                menu += "<a class=\"navbar-brand\" href=\"MainPage\">";
	                menu += "<img id=\"logo-brand\" src=\"assets/images/logo-brand.png\" alt=\"logo\">";
	                menu += "PlayersToPlayers</a>\n";
	            menu += "</div>\n\n";
	            
	            menu += "<div id=\"navbar-collapse-target\" class=\"collapse navbar-collapse\">\n";
	                menu += content;
	            menu += "</div>\n";
            menu += "</div>\n";
        menu += "</nav>";
        return menu;
    }
    
    public static String createTopMenuConnection(String inputNameLogin, String inputNamePassword, String btnName, String errorMessageSignin) {
        String
        formConnection = "<form class=\"navbar-form navbar-right\" method=\"post\">\n";
            // Login error
            if (errorMessageSignin != null) {
                formConnection += "<div class=\"form-group errorMessage\">" + errorMessageSignin + "</div>";
            }
        
            // Login
            formConnection += "<div class=\"form-group\">\n";
                formConnection += "<input type=\"text\" name=\"" + inputNameLogin + "\" placeholder=\"Pseudo\" class=\"form-control\">\n";
            formConnection += "</div>\n";
            
            // Password
            formConnection += "<div class=\"form-group\">\n";
                formConnection += "<input type=\"password\" name=\"" + inputNamePassword + "\" placeholder=\"Mot de passe\" class=\"form-control\">\n";
            formConnection += "</div>\n";
            
            // Connection
            formConnection += "<button type=\"submit\" name=\"" + btnName + "\" class=\"btn btn-success\">Connexion</button>\n";
        formConnection += "</form>\n";
        
        return HTMLBuilder.createTopMenu(formConnection);
    }
    
    public static String createTopMenu() {
        String
        menu = "<div class=\"nav navbar-nav navbar-right\">\n";
            //menu += "<li>\n";
            	menu += "<div class=\"navbar-btn\">\n";
	                menu += "<a href=\"Disconnection\" class=\"btn btn-success\">\n";
	                    	menu += "Déconnexion";
	                menu += "</a>\n";
                menu += "</div>\n";
            //menu += "</li>\n";
        menu += "</div>\n";
        
        return HTMLBuilder.createTopMenu(menu);
    }
    
    public static String createTabsMenu(String username) {
        String
        menu = "<div class=\"row\">\n";
	        menu += "<ul class=\"nav nav-pills\">\n";
	            
	            menu += "<li>";
    		        menu += "<form class=\"navbar-form navbar-left inline-form\" action=\"SearchUserResult\" method=\"get\">\n";
    		            menu += "<div class=\"form-group\">\n";
    		                menu += "<input type=\"search\" class=\"input-sm form-control\" placeholder=\"Rechercher un utilisateur\" name=\"user\">\n";
    	                menu += "</div>\n";
    	                menu += "<button type=\"submit\" class=\"btn btn-primary btn-sm\">\n";
    	                    menu += "<span class=\"glyphicon glyphicon-eye-open\"></span>\n";
    	                    menu += "Rechercher";
    	                menu += "</button>\n";
    		        menu += "</form>\n";
		        menu += "</li>\n";
		        
	            menu += "<li>";
    		        menu += "<form class=\"navbar-form navbar-right inline-form\" action=\"SearchGameResult\" method=\"get\">\n";
    	                menu += "<div class=\"form-group\">\n";
    	                    menu += "<input type=\"search\" class=\"input-sm form-control\" placeholder=\"Rechercher un jeu\" name=\"game\">\n";
    	                    menu += "<button type=\"submit\" class=\"btn btn-primary btn-sm\">\n";
    	                        menu += "<span class=\"glyphicon glyphicon-eye-open\"></span>\n";
    	                        menu += "Rechercher";
    	                    menu += "</button>\n";
    	                menu += "</div>\n";
    	            menu += "</form>\n";
	            menu += "</li>\n";

	            
	            menu += "<li><a href=\"CreateGameSession\">Créer une partie</a></li>\n";
	            
		        menu += "<li class=\"dropdown navbar-right\">\n";
	        		menu += "<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">Mes Infos <span class=\"caret\"></span></a>\n";
    				menu += "<ul class=\"dropdown-menu\">\n";
                        menu += "<li><a href=\"Profile?user=" + username + "\">Mon profil</a></li>\n";
			          	menu += "<li><a href=\"MyGameSessions\">Mes parties</a></li>\n";
			            menu += "<li><a href=\"MyGames\">Mes jeux</a></li>\n";
			            menu += "<li><a href=\"MyPlatforms\">Mes platformes</a></li>\n";
			            menu += "<li><a href=\"MyGameTypes\">Mes genres</a></li>\n";
			            menu += "<li><a href=\"MyFriends\">Mes amis</a></li>\n";
		            menu += " </ul>\n";
	    		menu += "</li>\n";
	            
	        menu += "</ul>\n";
        menu += "</div>\n";
        return menu;
    }
    
    public static String createLabelInput(String id, String name, String label, String value) {
        return createLabel(id, label) + createInput(id, name, value);
    }
    
    public static String createLabelInputDisabled(String id, String name, String label, String value) {
        return createLabel(id, label) + createInputDisabled(id, name, value);
    }
    
    public static String createLabelInput(String id, String name, String label, String value, String placeholder) {
        return createLabel(id, label) + createInput(id, name, value, placeholder);
    }
    
    public static String createLabelInputDisabled(String id, String name, String label, String value, String placeholder) {
        return createLabel(id, label) + createInputDisabled(id, name, value, placeholder);
    }
    
    public static String createLabel(String id, String label) {
        return "<label for=\"" + id + "\">" + label + "</label>\n";
    }
    
    public static String createInput(String id, String name, String value) {
        return "<input class=\"form-control\" type=\"text\" name=\"" + name + "\" id=\"" + id + "\" value=\"" + value + "\">\n";
    }
    
    public static String createInputDisabled(String id, String name, String value) {
        return "<input class=\"form-control\" type=\"text\" name=\"" + name + "\" id=\"" + id + "\" value=\"" + value + "\" disabled>\n";
    }
    
    public static String createInput(String id, String name, String value, String placeholder) {
        return "<input class=\"form-control\" type=\"text\" name=\"" + name + "\" id=\"" + id + "\" value=\"" + value + "\" placeholder=\"" + placeholder + "\">\n";        
    }
    
    public static String createInputDisabled(String id, String name, String value, String placeholder) {
        return "<input class=\"form-control\" type=\"text\" name=\"" + name + "\" id=\"" + id + "\" value=\"" + value + "\" placeholder=\"" + placeholder + "\" disabled>\n";        
    }
    
    public static String createInputList(String id, String name, String list, String value, String placeholder) {
        return "<input class=\"form-control\" type=\"text\" name=\"" + name + "\" id=\"" + id + "\" list=\"" + list + "\" value=\"" + value + "\" placeholder=\"" + placeholder + "\">\n";        
    }

    public static String createPanelGameSession(GameSession gameSession) {
        String
        panel = "<div class=\"panel panel-default\">\n";
            // Head
            panel += "<div class=\"panel-heading\">\n";
                panel += gameSession.getLabel();
            panel += "</div>\n";
            
            // Content
            panel += "<div class=\"panel-body\">\n";
                panel += "<span>Hébergé par: " + gameSession.getRoot().getUsername() + "</span><br>\n";
                panel += "<span>" + gameSession.getMeetingDate() + "</span><br><br>\n";
                panel += "<button type=\"button\" class=\"btn btn-primary btn-sm\" data-toggle=\"modal\" data-target=\"#myModal" + gameSession.getIdSession() + "\">Détails</button>\n";
            panel += "</div>\n";
        panel += "</div>\n";
        
        return panel;
    }
    
    public static String createModalGameSession(String csrfToken, GameSession gameSession, String inputNameValue, String btnName, String btnValue) {
        String
        modal = "<div class=\"modal fade\" id=\"myModal" + gameSession.getIdSession() + "\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">\n";
            modal += "<div class=\"modal-dialog\" role=\"document\">\n";
                modal += "<div class=\"modal-content\">\n";
                    // Header
                    modal += "<div class=\"modal-header\">";
                        modal += "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>\n";
                        modal += "<h4 class=\"modal-title display-4\" id=\"myModalLabel\">" + gameSession.getLabel() + "</h4>\n";
                    modal += "</div>\n";
                    
                    // Body
                    modal += "<div class=\"modal-body\">\n";
                        // Author
                        modal += "<span class=\"glyphicon glyphicon-user\" aria-hidden=\"true\">&nbsp;Hébergé par : " + gameSession.getRoot().getUsername() + "</span>\n";
                        modal += "<hr>\n";
    
                        // Input description : gameSession.getDescription() == <script>alert("XSS vulnerability detected");</script>
                        modal += "<span class=\"glyphicon glyphicon-pencil\" aria-hidden=\"true\">&nbsp;Description : " + StringEscapeUtils.escapeHtml4(gameSession.getDescription()) + "</span>\n";
                        modal += "<hr>\n";
                        
                        // Platforms
                        modal += "<span class=\"fa fa-television\" aria-hidden=\"true\">&nbsp;Plateformes :";
                            modal += String.join(" - ", gameSession.getPlatforms());
                        modal += "</span>\n";
                        modal += "<hr>\n";
                        
                        // Games
                        modal += "<span class=\"fa fa-gamepad\" aria-hidden=\"true\">&nbsp;Jeux :";
                            modal += String.join(" - ", gameSession.getGames());
                        modal += "</span>\n";
                        modal += "<hr>\n";
                        
                        // Participants
                        modal += "<span class=\"fa fa-users\" aria-hidden=\"true\">&nbsp;Participants :";
                            List<Player> participants = gameSession.getPlayers();
                            List<String> usernames = new ArrayList<>(participants.size());
                            for (Player participant : participants) {
                                usernames.add(participant.getUsername());
                            }
                            modal += String.join(" - ", usernames);
                        modal += "</span>\n";
                        modal += "<hr>\n";
                        
                        // Address
                        modal += "<span class=\"fa fa-home\" aria-hidden=\"true\">&nbsp;Adresse : " +
                                    gameSession.getAddress() + " " +
                                    gameSession.getPostCode() +
                                    "</span>\n";
                        modal += "<hr>\n";
                        
                        // Date
                        modal += "<span class=\"fa fa-clock-o\" aria-hidden=\"true\">&nbsp;Date : " + gameSession.getMeetingDate() + "</span>\n";
                        modal += "<hr>\n";
                        
                    modal += "</div>\n";
                    
                    // Footer
                    modal += "<div class=\"modal-footer\">\n";
                        modal += "<form method=\"post\">";
                            modal += "<input type=\"hidden\" name=\"" + TokenUtils.CSRF_TOKEN + "\" value=\"" + csrfToken + "\">\n";
                            modal += "<input type=\"hidden\" name=\"" + inputNameValue + "\" value=\"" + gameSession.getIdSession() + "\">\n";
                            modal += "<button type=\"submit\" name=\"" + btnName + "\" class=\"btn btn-primary\">" + btnValue + "</button>\n";
                        modal += "</form>\n";
                    modal += "</div>\n";
                modal += "</div>\n";
           modal += "</div>\n";
        modal += "</div>\n";
            
        return modal;
    }

}
