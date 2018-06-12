package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.google.appengine.api.rdbms.AppEngineDriver;
import com.google.appengine.api.utils.SystemProperty;


public class DBConnection {
	
	
	private static Connection con = null;
	
	
	//TODO: URL mit deren Hilfe die DB angesprochen wird
	//private static String googleUrl = "jdbc:google:mysql://35.187.96.42:3306/ITProjekt?user=root&password=n6obMwAe51M9lnrxT9"; // Google URL
	private static String googleUrl =   "jdbc:google:mysql://itprojektss18-t9:europe-west3:itprojektss18-t9/ITProjekt?user=test&password=test";
	//private static String url = "jdbc:mysql://google/ITProjekt?cloudSqlInstance=ITProjekt&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=root&password=n6obMwAe51M9lnrxT9&useSSL=false"; //LocalHost
	private static String localUrl = "jdbc:mysql://85.183.140.53:8170/ITProjekt?user=ITProjekt&password=ITProjekt"; 
	private static String kimlyUrl = "jdbc:mysql://127.0.0.1:3306/itprojekt?user=root&password=";

	
	
	public static Connection connection() {
		// falls noch keine Verbindung zur DB hergestellt wurde
		if ( con == null ) {
			String url = null;
			try {
				// Laden des Treibers
				DriverManager.registerDriver(new AppEngineDriver());
				if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
					
					System.out.println("Load Google Driver");
                    // Load the class that provides the new
                    // "jdbc:google:mysql://" prefix.
                    Class.forName("com.mysql.jdbc.GoogleDriver");
                    url = googleUrl;
                } else {
                	//System.out.println("Load MySQL Driver");
                    // Local MySQL instance to use during development.
                	Class.forName("com.mysql.jdbc.Driver");
                    url = kimlyUrl;
                }
				
				// Treiber Manager baut Verbindung mit Informationen aus url auf
				con = DriverManager.getConnection(url);
			} 
			catch (SQLException e1) {
				con = null;
				e1.printStackTrace();
			}catch (ClassNotFoundException e) {
				e.printStackTrace();
			} 
		}
		
		return con;
	}

}