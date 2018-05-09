package de.hdm.kontaktsystem.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.appengine.api.rdbms.AppEngineDriver;

public class DBConnection {
	
	private static Connection con = null;
	
	//TODO: URL mit deren Hilfe die DB angesprochen wird
		
	private static String url = "";
	
	
	public static Connection connection() {
		// falls noch keine Verbindung zur DB hergestellt wurde
		if ( con == null ) {
			try {
				// Laden des Treibers
				DriverManager.registerDriver(new AppEngineDriver());
				// Treiber Manager baut Verbindung mit Informationen aus url auf
				con = DriverManager.getConnection(url);
			} 
			catch (SQLException e1) {
				con = null;
				e1.printStackTrace();
			}
		}
		
		return con;
	}

}

