package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Player;
import models.PlayerDao;
import utils.GiantBombUtils;
import utils.HTMLBuilder;
import dao.PlayerDaoImpl;
import enums.PageTitle;
import enums.SessionData;


//////////////////////////// Page pour afficher les genres, jeux, caractéristiques d'une console //////////////////

/**
 * Servlet implementation class Platform
 */
public class Platform extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private PlayerDao playerDao;
	private Player player;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Platform() {
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
		// If session doesn't exists redirect to SigninSignup page
        HttpSession session = request.getSession(false);
        String playerUsername;
        String platformName = null;
        
        if (session == null) {
            response.sendRedirect(".");
            return;
        }
        else {
        	playerUsername = (String) session.getAttribute(SessionData.PLAYER_USERNAME.toString());
        	player = this.playerDao.getPlayer(playerUsername);
        }
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"fr\">");
        out.println(HTMLBuilder.createHeadTag(PageTitle.PLATFORM));
        
        // Body
        out.println("<body>");
            // Menu connection
            out.println(HTMLBuilder.createTopMenu());
            out.println(HTMLBuilder.createTabsMenu());
            
	        if (request.getParameter("platform") == null) {
		        out.println("error");
		    } else {
		    	platformName = request.getParameter("platform");
		    }
            
            out.println("<div class=\"row\">");
            
	            out.println("<section class=\"col-xs-2 well\">");
		            out.println("<div id=\"wrapper\">");
						out.println("<div id=\"sidebar-wrapper\">");
							out.println("<ul class=\"sidebar-nav\">");
								out.println("<h4 class=\"sidebar-brand\">");
									out.println("Parties pour cette platforme");
		                        out.println("</h4>");
		                        // A completer
							out.println("</ul>");
						out.println("</div>");
	    			out.println("</div>");
				out.println("</section>");
				
				out.println("<section class=\"col-xs-8 well\">");
					Map<String, String> platformInfos = GiantBombUtils.getPlatformInfos(platformName);
					out.println("<div class=\"row\">");
			            out.println("<div class=\"col-xs-5 text-center\">");
			            	out.println("<img src="+platformInfos.get("image")+" width=\"250px\" height=\"200px\" alt=\"?\"/>");
			            out.println("</div>");
			            out.println("<div class=\"col-xs-7\">");
			            out.println("<div class=\"row\">");
			            	out.println("<B>Nom : </B>");
				            	if(platformInfos.get("name") != null  &&  platformInfos.get("name") != "null")
				            		out.println(platformInfos.get("name"));
		            		out.println("</div>");
				            out.println("<div class=\"row\">");
				            	out.println("<B>Compagnie : </B>");
				            	if(platformInfos.get("company") != null  &&  platformInfos.get("company") != "null")
				            		out.println(platformInfos.get("company"));
		            		out.println("</div>");
			            	out.println("<div class=\"row\">");
			            		out.println("<B>Description : </B>");
			            		if(platformInfos.get("deck") != null  &&  platformInfos.get("deck") != "null")
			            			out.println(platformInfos.get("deck"));
		            		out.println("</div>");
		            		out.println("<div class=\"row\">");
			            		out.println("<B>Original release date : </B>");
			            		if(platformInfos.get("release_date") != null  &&  platformInfos.get("release_date") != "null")
			            			out.println(platformInfos.get("release_date"));
			            	out.println("</div>");
			            out.println("</div>");  
					out.println("</div>");
					
					out.println("</br><div class=\"row\">");
		            	out.println("<div class=\"col-xs-12 text-center\">");
	            			out.println("<button type=\"button\" class=\"btn btn-primary\">Ajouter à mes consoles</button>");
		            	out.println("</div>");
	            	out.println("</div></br>");
            	
	            	out.println("<div class=\"row\">");
		            	out.println("<div class=\"col-xs-12\">");
		            		out.println("<B>Description : </B>");
		            		if(platformInfos.get("description") != null  &&  platformInfos.get("description") != "null")
		            			out.println(platformInfos.get("description"));
		            	out.println("</div>");
	            	out.println("</div>");
	            	
				out.println("</section>");
				
				out.println("<div class=\"row\">");
	            out.println("<section class=\"col-xs-2 well\">");
		            out.println("<div id=\"wrapper\">");
						out.println("<div id=\"sidebar-wrapper\">");
							out.println("<ul class=\"sidebar-nav\">");
								out.println("<h4 class=\"sidebar-brand\">");
									out.println("Plateformes");
		                        out.println("</h4>");
		                        List<String> platforms = GiantBombUtils.getPlatformsList();
								for(int i=0; i<platforms.size(); i++){
									out.println("<li>");
										out.println("<a href=\"Platform?platform="+platforms.get(i)+"\">");
											out.println(platforms.get(i));
										out.println("</a>");
									out.println("</li>");
								}
							out.println("</ul>");
						out.println("</div>");
	    			out.println("</div>");
				out.println("</section>");
									
			out.println("</div>");
			// Scripts
            out.println(HTMLBuilder.createScriptsTags());
        out.println("</body>");
        out.print("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
