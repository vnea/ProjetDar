import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
    	String dbName = System.getProperty("RDS_DB_NAME");
        String userName = System.getProperty("RDS_USERNAME");
        String password = System.getProperty("RDS_PASSWORD");
        String hostname = System.getProperty("RDS_HOSTNAME");
        String port = System.getProperty("RDS_PORT");
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
        /*try {
			connexion = (Connection) DriverManager.getConnection(jdbcUrl);
			
			 Création de l'objet gérant les requêtes 
	        statement = (Statement) ((java.sql.Connection) connexion).createStatement();
	        messages.add( "Objet requête créé !" );
	        
	        String createTable = "CREATE TABLE Beanstalk (Resource char(50));";
	        String insertRow1 = "INSERT INTO Beanstalk (Resource) VALUES ('EC2 Instance');";
	        String insertRow2 = "INSERT INTO Beanstalk (Resource) VALUES ('RDS Instance');";
	        
	        statement.addBatch(createTable);
	        statement.addBatch(insertRow1);
	        statement.addBatch(insertRow2);
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
            resultSet = readStatement.executeQuery("SELECT * FROM Beanstalk;");

            resultSet.first();
            results = resultSet.getString("Resource");
            resultSet.next();
            results += ", " + resultSet.getString("Resource");
            
            messages.add(results);
            resultSet.close();
            readStatement.close();
            connexion.close();

          } catch (SQLException ex) {
            // Handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
          } finally {
               System.out.println("Closing the connection.");
              if (connexion != null) try { connexion.close(); } catch (SQLException ignore) {}
          }
        

        return messages;
    }
}