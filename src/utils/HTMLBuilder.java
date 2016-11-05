package utils;

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
            head += "<link href=\"assets/css/bootstrap.css\" rel=\"stylesheet\">\n";
            head += "<link href=\"assets/css/ie10-viewport-bug-workaround.css\" rel=\"stylesheet\">\n";
            head += "<link href=\"assets/css/style.css\" rel=\"stylesheet\">\n\n";
            
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
        scripts += "<script src=\"assets/js/bootstrap.min.js\"></script>\n\n";
        
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
	                menu += "<a class=\"navbar-brand\" href=\"MainPage\">PlayersToPlayers</a>\n";
	            menu += "</div>\n\n";
	            
	            menu += "<div id=\"navbar-collapse-target\" class=\"collapse navbar-collapse\">\n";
	                menu += content;
	            menu += "</div>\n";
            menu += "</div>\n";
        menu += "</nav>";
        return menu;
    }
    
    public static String createTopMenuConnection(String inputNameLogin, String inputNamePassword, String btnName) {
        String
        formConnection = "<form class=\"navbar-form navbar-right\" method=\"post\">\n";
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
            menu += "<li>\n";
            	menu += "<div class=\"navbar-btn\">\n";
	                menu += "<a href=\"Disconnection\" class=\"btn btn-success\">\n";
	                    	menu += "Déconnexion";
	                menu += "</a>\n";
                menu += "</div>\n";
            menu += "</li>\n";
        menu += "</div>\n";
        
        return HTMLBuilder.createTopMenu(menu);
    }
    
    public static String createTabsMenu() {
        String
        menu = "<div class=\"row\">\n";
	        menu += "<ul class=\"nav nav-pills\">\n";
	            menu += "<li><a href=\"MyGameSessions\">Mes parties</a></li>\n";
	            menu += "<li><a href=\"MyGames\">Mes jeux</a></li>\n";
	            menu += "<li><a href=\"MyPlatforms\">Mes platformes</a></li>\n";
	            menu += "<li><a href=\"MyGameTypes\">Mes genres</a></li>\n";
	            menu += "<li><a href=\"MyFriends\">Mes amis</a></li>\n";
	            menu += "<form class=\"navbar-form navbar-right inline-form\">\n";
	                menu += "<div class=\"form-group\">\n";
	                    menu += "<input type=\"search\" class=\"input-sm form-control\" placeholder=\"jeux\">\n";
	                    menu += "<button type=\"submit\" class=\"btn btn-primary btn-sm\">\n";
	                        menu += "<span class=\"glyphicon glyphicon-eye-open\"></span>\n";
	                        menu += "Rechercher";
	                    menu += "</button>\n";
	                menu += "</div>\n";
	            menu += "</form>\n";
	        menu += "</ul>\n";
        menu += "</div>\n";
        return menu;
    }
}
