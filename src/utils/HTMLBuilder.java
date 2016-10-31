package utils;

import enums.PageTitle;

public class HTMLBuilder {

    public static String createHeadTag(PageTitle pageTitle) {
        String head;
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
    
    public static String createTopMenuConnexion() {
        String menu;
        menu = "<nav class=\"navbar navbar-inverse navbar-fixed-top\">\n";
            menu += "<div class=\"container\">\n";
                menu += "<div class=\"navbar-header\">\n";
                    menu += "<button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" data-target=\"#navbar\" aria-expanded=\"false\" aria-controls=\"navbar\">\n";
                        menu += "<span class=\"sr-only\">Toggle navigation</span>\n";
                        menu += "<span class=\"icon-bar\"></span>\n";
                        menu += "<span class=\"icon-bar\"></span>\n";
                        menu += "<span class=\"icon-bar\"></span>\n";
                    menu += "</button>\n";
                    menu += "<a class=\"navbar-brand\" href=\"#\">PlayersToPlayers</a>\n";
                menu += "</div>\n\n";
                
                menu += "<div id=\"navbar\" class=\"collapse navbar-collapse\">\n";
                    // Create login form
                    menu += "<form class=\"navbar-form navbar-right\">\n";
                        menu += "<div class=\"form-group\">\n";
                            menu += "<input type=\"text\" placeholder=\"Pseudo\" class=\"form-control\">\n";
                        menu += "</div>\n";
                        menu += "<div class=\"form-group\">\n";
                            menu += "<input type=\"password\" placeholder=\"Mot de passe\" class=\"form-control\">\n";
                        menu += "</div>\n";
                        menu += "<button type=\"submit\" class=\"btn btn-success\">Connexion</button>\n";
                    menu += "</form>\n";
                menu += "</div>\n";
            menu += "</div>\n";
            
        menu += "</nav>";
        
        return menu;
    }
    
    public static String createScriptsTags() {
        String scripts;
        
        // Bootstrap core JavaScript
        scripts = "<script src=\"assets/js/jquery-3.1.1.min.js\"></script>\n";
        scripts += "<script src=\"assets/js/bootstrap.min.js\"></script>\n\n";
        
        // IE10 viewport hack for Surface/desktop Windows 8 bug
        scripts += "<script src=\"assets/js/ie10-viewport-bug-workaround.js\"></script>\n";
        scripts += "<script src=\"assets/js/script.js\"></script>\n";
        
        return scripts;
    }
}
