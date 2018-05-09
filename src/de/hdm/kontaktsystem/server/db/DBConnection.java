package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.appengine.api.rdbms.AppEngineDriver;

public class DBConnection {
	
	private static Connection con = null;
	
	//private static String databaseTyp = "google";
	private static String databaseTyp = "mySQL";
	
	
	//TODO: URL mit deren Hilfe die DB angesprochen wird
		
	// private static String url = ""; Google URL
	
	private static String url = "jdbc:mysql://85.183.140.53:8170/ITProjekt?user=ITProjekt&password=ITProjekt";
	
	
	public static Connection connection() {
		// falls noch keine Verbindung zur DB hergestellt wurde
		if ( con == null ) {
			try {
				// Laden des Treibers
				if(databaseTyp == "google"){
					DriverManager.registerDriver(new AppEngineDriver());
				}else{
					Class.forName("com.mysql.jdbc.Driver");
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

