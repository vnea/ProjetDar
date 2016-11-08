package test;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;

import utils.JsonUtils;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class TestJDBC {
    /* La liste qui contiendra tous les résultats de nos essais */
    private List<String> messages = new ArrayList<String>();

    public List<String> executerTests( HttpServletRequest request ) {
    	try {
            messages.add( "Chargement du driver..." );
            Class.forName( "com.mysql.jdbc.Driver" );
            messages.add( "Driver chargé !" );
        } catch ( ClassNotFoundException e ) {
            messages.add( "Erreur lors du chargement : le driver n'a pas été trouvé dans le classpath ! <br/>"
                    + e.getMessage() );
        }
    	messages.add( "Driver chargé !" );
    	Connection connexion = null;
    	Statement statement = null;
        ResultSet resultat = null;
        
        // USE THIS CODE FOR THE FINAL DEPLOY
//    	String dbName = System.getProperty("RDS_DB_NAME");
//      String userName = System.getProperty("RDS_USERNAME");
//      String password = System.getProperty("RDS_PASSWORD");
//      String hostname = System.getProperty("RDS_HOSTNAME");
//      String port = System.getProperty("RDS_PORT");
        
        // Temporary code until final deploy (BEGIN)
        JsonObject jsonObjectConfig = JsonUtils.loadJsonObject("database-config.json");

        String dbName = jsonObjectConfig.getString("dbName");
        String userName = jsonObjectConfig.getString("userName");
        String password = jsonObjectConfig.getString("password");
        String hostname = jsonObjectConfig.getString("hostname");
        String port = jsonObjectConfig.getString("port");
        // Temporary code until final deploy (END)
        
        String jdbcUrl = "jdbc:mysql://aaq4zua6v6ccvz.cortfl8tkfmv.us-west-2.rds.amazonaws.com:3306/ebdb?user=root&password=dar-2016-aws";
/*        try {
			connexion = (Connection) DriverManager.getConnection(jdbcUrl);
			
			
	        statement = (Statement) ((java.sql.Connection) connexion).createStatement();
	        messages.add( "Objet requête créé !" );
	        
	        String createTable = "CREATE TABLE `Profiles` (`idProfile` int(10) unsigned NOT NULL AUTO_INCREMENT,`email` varchar(100) NOT NULL,`login` varchar(30) NOT NULL,`lastname` varchar(50) NOT NULL,`firstname` varchar(45) NOT NULL,`age` int(11) NOT NULL,`address` varchar(120) DEFAULT NULL,`postcode` int(11) DEFAULT NULL,`phoneNumber` int(11) DEFAULT NULL,`password` varchar(45) NOT NULL,`sex` enum('H','F') NOT NULL,PRIMARY KEY (`idProfile`),UNIQUE KEY `idProfiles_UNIQUE` (`idProfile`),UNIQUE KEY `email_UNIQUE` (`email`),UNIQUE KEY `login_UNIQUE` (`login`));";
	        String insert = "INSERT INTO Profiles VALUES (1, 'morvanlassauzay@msn.com', 'morvan', 'Lassauzay', 'morvan', '22', 'mon adresse', '75000', '0909090909', 'morvan', 'H');";
	        
	        statement.addBatch(insert);
	     
	        statement.executeBatch();
	        statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        finally {
            System.out.println("Closing the connection.");
            if (connexion != null) try {  connexion.close(); } catch (SQLException ignore) {}
          }*/
        
        Statement readStatement = null;
        ResultSet resultSet = null;
        String results = "";
        int numresults = 0;

        try {
            connexion = (Connection) DriverManager.getConnection(jdbcUrl);
            
            readStatement = (Statement) ((java.sql.Connection) connexion).createStatement();
            resultSet = readStatement.executeQuery("SELECT * FROM Profiles;");
            			// readStatement.executeUpdate pour INSERT, UPDATE, DELETE, etc
            
            messages.add(results);

          } catch (SQLException ex) {
            // Handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
          } finally {
        	    if ( resultSet != null ) {
        	        try {
        	            /* On commence par fermer le ResultSet */
        	            resultat.close();
        	        } catch ( SQLException ignore ) {
        	        }
        	    }
        	    if ( readStatement != null ) {
        	        try {
        	            /* Puis on ferme le Statement */
        	            statement.close();
        	        } catch ( SQLException ignore ) {
        	        }
        	    }
        	    if ( connexion != null ) {
        	        try {
        	            /* Et enfin on ferme la connexion */
        	            connexion.close();
        	        } catch ( SQLException ignore ) {
        	        }
        	    }
          }
        

        return messages;
    }
}